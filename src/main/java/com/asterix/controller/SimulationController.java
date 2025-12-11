package com.asterix.controller;

import com.asterix.model.character.CharacterFactory;
import com.asterix.model.character.CharacterType;
import com.asterix.model.character.Character; // Import important
import com.asterix.model.place.*;
import com.asterix.model.simulation.InvasionTheater;
import com.asterix.model.character.Chief;
import com.asterix.utils.XmlScenarioLoader;
import com.asterix.utils.XmlScenarioSaver;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Main Controller managing the temporal logic of the simulation and user interactions.
 * Implements the MVC pattern and multithreading management for the simulation loop.
 * <p>
 * This controller handles the requirements regarding the "Temporal aspect",
 * "Passing the hand to a clan chief", and "Logistics/Movement".
 * </p>
 *
 * @author Project Team
 * @version 2.1
 * @see InvasionTheater
 */
public class SimulationController implements Runnable {

    // --- FXML Elements (View) ---
    @FXML private TextArea outputArea;
    @FXML private Button btnStart;
    @FXML private Button btnStop;

    // Clan Chief Interaction Controls
    @FXML private Button btnFeed;
    @FXML private Button btnHeal;
    @FXML private Button btnEndTurn;
    @FXML private Label lblCurrentChief;

    @FXML private TextField inputTheaterName;

    // --- Model ---
    private InvasionTheater model;

    // --- Thread Management & Synchronization ---
    private volatile boolean isRunning = false;
    private Thread simulationThread;
    private int chiefTurnIndex = 0;
    private Chief activeChief;

    // --- Creation Inputs ---
    @FXML private TextField inputPlaceName;
    @FXML private TextField inputPlaceArea;
    @FXML private ComboBox<PlaceType> comboPlaceType;

    // --- Recruitment Inputs ---
    @FXML private ComboBox<Place> comboDestPlace;
    @FXML private ComboBox<CharacterType> comboCharType;
    @FXML private TextField inputCharName;
    @FXML private TextField inputCharAge;

    // --- Movement / Logistics Inputs (NEW) ---
    @FXML private ComboBox<Place> comboMoveSource;
    @FXML private ComboBox<Character> comboMoveChar;
    @FXML private ComboBox<Place> comboMoveDest;

    /**
     * Lock object to synchronize the simulation thread and the UI thread.
     */
    private final Object pauseLock = new Object();

    /**
     * Flag indicating if the simulation is currently paused waiting for the user.
     */
    private volatile boolean isPausedForUser = false;

    private static final int TIME_STEP = 2000;

    /**
     * Method called automatically by JavaFX after FXML loading.
     * Initializes the UI state and loads a default model.
     */
    @FXML
    public void initialize() {
        logToView("Initializing Armorique System V5...");

        setChiefControlsDisable(true);
        btnStop.setDisable(true);

        // Initialize Enums
        if (comboCharType != null) {
            comboCharType.setItems(FXCollections.observableArrayList(CharacterType.values()));
            comboCharType.getSelectionModel().selectFirst();
        }

        if (comboPlaceType != null) {
            comboPlaceType.setItems(FXCollections.observableArrayList(PlaceType.values()));
            comboPlaceType.getSelectionModel().selectFirst();
        }

        // Initialize Logic for Dynamic Movement Dropdowns
        initMovementLogic();

        try {
            initializeModel("src/main/resources/com/asterix/data/scenarioDefault.xml");
        } catch (Exception e) {
            logToView("Info: " + e.getMessage());
            logToView("Creating an empty default Theater.");
            this.model = new InvasionTheater("Default Armorique");
        }

        // Must be called after model init
        refreshPlaceList();
    }

    /**
     * Sets up listeners for the movement logic.
     * When a source place is selected, the character list is updated automatically.
     */
    private void initMovementLogic() {
        if (comboMoveSource != null) {
            comboMoveSource.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newSource) -> {
                if (newSource != null) {
                    // Populate character list based on selected place
                    comboMoveChar.setItems(FXCollections.observableArrayList(newSource.getCharacters()));

                    // Display converter for Characters
                    comboMoveChar.setConverter(new StringConverter<Character>() {
                        @Override
                        public String toString(Character c) {
                            return (c == null) ? "" : c.getName() + " (" + c.getClass().getSimpleName() + ")";
                        }
                        @Override
                        public Character fromString(String s) { return null; }
                    });
                } else {
                    comboMoveChar.getItems().clear();
                }
            });
        }
    }

    /**
     * Initializes the model from an XML path.
     */
    public void initializeModel(String xmlPath) throws Exception {
        this.model = XmlScenarioLoader.loadTheater(xmlPath);
        logToView("Theater loaded: " + model.getName());
    }

    // --- Simulation Management (Thread) ---

    @FXML
    private void handleStart() {
        if (!isRunning) {
            isRunning = true;
            isPausedForUser = false;

            simulationThread = new Thread(this);
            simulationThread.setName("Simu-Thread");
            simulationThread.start();

            btnStart.setDisable(true);
            btnStop.setDisable(false);
            logToView(">>> Simulation started.");
        }
    }

    @FXML
    private void handleStop() {
        isRunning = false;

        synchronized (pauseLock) {
            isPausedForUser = false;
            pauseLock.notifyAll();
        }

        btnStart.setDisable(false);
        btnStop.setDisable(true);
        setChiefControlsDisable(true);
        logToView(">>> Simulation stopped.");
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                simulateStep();
                Platform.runLater(this::updateView);
                triggerUserTurn();
                model.applyDailyHunger();
                Thread.sleep(TIME_STEP);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                isRunning = false;
                logToView("Simulation thread interrupted.");
            }
        }
    }

    private void simulateStep() {
        if (model != null) {
            logToView("--- New Time Cycle ---");
            model.handleFights();
            model.applyRandomEvents();
            model.generateFood();
            model.ageFood();
        }
    }

    private void triggerUserTurn() throws InterruptedException {
        if (model == null || model.getPlaces().isEmpty()) return;

        List<Chief> availableChiefs = model.getPlaces().stream()
                .filter(place -> place instanceof Settlement)
                .map(place -> ((Settlement) place).getChief())
                .filter(chief -> chief != null)
                .collect(Collectors.toList());

        if (availableChiefs.isEmpty()) {
            return;
        }

        Chief currentChief = availableChiefs.get(chiefTurnIndex % availableChiefs.size());
        chiefTurnIndex++;

        synchronized (pauseLock) {
            isPausedForUser = true;

            Platform.runLater(() -> {
                if (lblCurrentChief != null) {
                    lblCurrentChief.setText("Action required: " + currentChief.getName());
                }
                this.activeChief = currentChief;
                logToView(">>> Your turn, Chief " + currentChief.getName() + " of " + currentChief.getLocation().getName() + "!");
                setChiefControlsDisable(false);
            });

            while (isPausedForUser && isRunning) {
                pauseLock.wait();
            }
        }
    }

    // --- Chief Actions ---

    @FXML
    public void handleFeed() {
        if (this.activeChief == null) {
            logToView("Action impossible : No active chief.");
            return;
        }
        this.activeChief.feedCharactersInLocation();
        logToView("Chief " + this.activeChief.getName() + " distributed food!");
        updateView();
    }

    @FXML
    public void handleHeal() {
        if (this.activeChief == null) {
            logToView("Action impossible : No active chief.");
            return;
        }
        this.activeChief.healCharactersInLocation();
        logToView("Chief " + this.activeChief.getName() + " healed the wounded!");
        updateView();
    }

    @FXML
    public void handleEndTurn() {
        synchronized (pauseLock) {
            isPausedForUser = false;
            setChiefControlsDisable(true);
            if (lblCurrentChief != null) {
                lblCurrentChief.setText("Simulation running...");
            }
            logToView(">>> End of user turn.");
            pauseLock.notifyAll();
        }
    }

    @FXML
    public void handleDisplayStats() {
        if (model == null) {
            logToView("No theater loaded.");
            return;
        }

        logToView("--- 📊 DISPLAYING DETAILED CHARACTERISTICS ---");

        for (Place place : model.getPlaces()) {
            logToView("📍 " + place.toString());
            List<Character> occupants = place.getCharacters();

            if (occupants.isEmpty()) {
                logToView("    (No inhabitants)");
            } else {
                for (Character c : occupants) {
                    logToView("    👤 " + c.toString());
                }
            }
            if (!place.getFoods().isEmpty()) {
                logToView("    🍎 Inventory: " + place.getFoods().size() + " items");
            }
            logToView("--------------------------------------------------");
        }
    }

    // --- Creation & Recruitment ---

    @FXML
    public void handleCreateTheater() {
        String name = inputTheaterName.getText();
        if (name == null || name.trim().isEmpty()) name = "Custom Theater";
        this.model = new InvasionTheater(name);
        logToView("New theater created: " + name);
        logToView("Add locations.");
        refreshPlaceList();
    }

    @FXML
    public void handleCreatePlace() {
        if (this.model == null) {
            this.model = new InvasionTheater("New Theater");
        }

        String name = inputPlaceName.getText();
        String areaStr = inputPlaceArea.getText();
        PlaceType type = comboPlaceType.getValue();

        if (name == null || name.trim().isEmpty() || type == null) {
            logToView("❌ Error: Name and Type required.");
            return;
        }

        double area;
        try {
            area = Double.parseDouble(areaStr);
        } catch (NumberFormatException e) {
            logToView("❌ Error: Invalid Area.");
            return;
        }

        Place newPlace = null;

        // Factory Logic (Simplified for readability, assumed correct per previous steps)
        if (type == PlaceType.GAUL_VILLAGE) {
            Chief c = new Chief("Chief of " + name, "MALE", 50, null);
            newPlace = new GaulVillage(name, area, c);
            c.setLocation(newPlace);
        } else if (type == PlaceType.ROMAN_CAMP) {
            Chief c = new Chief("Prefect of " + name, "MALE", 40, null);
            newPlace = new RomanCamp(name, area, c);
            c.setLocation(newPlace);
        } else if (type == PlaceType.BATTLEFIELD) {
            newPlace = new Battlefield(name, area);
        } else if (type == PlaceType.CREATURE_ENCLOSURE) {
            newPlace = new CreatureEnclosure(name, area);
        } else {
            // Default fallbacks for other types
            newPlace = new Battlefield(name, area);
        }

        if (newPlace != null) {
            this.model.addPlace(newPlace);
            logToView("✅ Added: " + newPlace.getName());
            inputPlaceName.clear();
            inputPlaceArea.clear();

            refreshPlaceList(); // Update ALL dropdowns
            handleDisplayStats();
        }
    }

    @FXML
    public void handleRecruitCharacter() {
        if (model == null) return;

        Place destination = comboDestPlace.getValue();
        CharacterType type = comboCharType.getValue();
        String name = inputCharName.getText();
        String ageStr = inputCharAge.getText();

        if (destination == null || type == null || name.isEmpty() || ageStr.isEmpty()) {
            logToView("❌ Fill all fields.");
            return;
        }

        try {
            int age = Integer.parseInt(ageStr);
            Character newChar = CharacterFactory.createCharacter(type, name, age);

            // Logic to add to place (canEnter check inside addCharacter)
            destination.addCharacter(newChar);

            logToView("✅ Recruited: " + newChar.getName() + " -> " + destination.getName());
            handleDisplayStats();
            inputCharName.clear();

        } catch (NumberFormatException e) {
            logToView("❌ Invalid Age.");
        } catch (Exception e) {
            logToView("⛔ Error: " + e.getMessage());
        }
    }

    // --- Movement / Logistics (NEW) ---

    /**
     * Handles moving a character from one place to another.
     * Enforces business rules via addCharacter checks.
     */
    @FXML
    public void handleMoveTroop() {
        if (model == null) return;

        Place source = comboMoveSource.getValue();
        Character character = comboMoveChar.getValue();
        Place destination = comboMoveDest.getValue();

        if (source == null || character == null || destination == null) {
            logToView("❌ Select Source, Character, and Destination.");
            return;
        }

        if (source == destination) {
            logToView("⚠️ Source and Destination are identical.");
            return;
        }

        try {
            // 1. Attempt to add to destination (Checks rules like 'Romans only')
            destination.addCharacter(character);

            // 2. If successful, remove from source
            source.removeCharacter(character);

            logToView("🚚 Moved " + character.getName() + " from " + source.getName() + " to " + destination.getName());

            // 3. Refresh UI
            handleDisplayStats();
            comboMoveChar.getSelectionModel().clearSelection();
            comboMoveChar.setItems(FXCollections.observableArrayList(source.getCharacters()));

        } catch (Exception e) {
            logToView("⛔ Transfer Failed: " + e.getMessage());
            // Rollback done implicitly as addCharacter throws before adding
        }
    }

    /**
     * Updates all Place-related dropdowns (Recruitment and Movement).
     */
    private void refreshPlaceList() {
        if (model != null) {
            List<Place> places = model.getPlaces();

            // Helper converter
            StringConverter<Place> placeConverter = new StringConverter<Place>() {
                @Override
                public String toString(Place p) {
                    return (p == null) ? "" : p.getName() + " (" + p.getClass().getSimpleName() + ")";
                }
                @Override
                public Place fromString(String s) { return null; }
            };

            // Update Recruitment
            if (comboDestPlace != null) {
                comboDestPlace.setItems(FXCollections.observableArrayList(places));
                comboDestPlace.setConverter(placeConverter);
            }

            // Update Movement
            if (comboMoveSource != null) {
                comboMoveSource.setItems(FXCollections.observableArrayList(places));
                comboMoveSource.setConverter(placeConverter);
            }
            if (comboMoveDest != null) {
                comboMoveDest.setItems(FXCollections.observableArrayList(places));
                comboMoveDest.setConverter(placeConverter);
            }
        }
    }

    // --- Utilities ---

    private void updateView() {
        if (model != null) {
            outputArea.appendText(model.toString() + "\n");
            outputArea.setScrollTop(Double.MAX_VALUE);
        }
    }

    private void logToView(String message) {
        Platform.runLater(() -> outputArea.appendText(message + "\n"));
    }

    private void setChiefControlsDisable(boolean disable) {
        if (btnFeed != null) btnFeed.setDisable(disable);
        if (btnHeal != null) btnHeal.setDisable(disable);
        if (btnEndTurn != null) btnEndTurn.setDisable(disable);
    }

    @FXML
    public void handleSaveScenario() {
        if (model == null) return;
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try {
                XmlScenarioSaver.saveTheater(model, file);
                logToView("Saved to " + file.getAbsolutePath());
            } catch (Exception e) {
                logToView("Error: " + e.getMessage());
            }
        }
    }

    @FXML
    public void handleLoadScenario() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                initializeModelFromExternal(file.getAbsolutePath());
                refreshPlaceList();
            } catch (Exception e) {
                logToView("Error: " + e.getMessage());
            }
        }
    }

    private void initializeModelFromExternal(String path) throws Exception {
        this.model = XmlScenarioLoader.loadTheaterFromFile(new File(path));
        logToView("Loaded: " + model.getName());
    }
}