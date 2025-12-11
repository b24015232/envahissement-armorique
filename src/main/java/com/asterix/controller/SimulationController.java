package com.asterix.controller;

import com.asterix.model.character.CharacterFactory;
import com.asterix.model.character.CharacterType;
import com.asterix.model.character.Character;
import com.asterix.model.character.gaul.Druid;
import com.asterix.model.item.Cauldron;
import com.asterix.model.item.FoodFactory;
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
 * <p>
 * Implements the MVC pattern and multithreading management for the simulation loop.
 * This class handles the "Temporal aspect", "Clan Chief interactions",
 * "Logistics/Movement", and "Resource Gathering".
 * </p>
 *
 * @author Project Team
 * @version 2.3
 */
public class SimulationController implements Runnable {

    @FXML private TextArea outputArea;
    @FXML private Button btnStart;
    @FXML private Button btnStop;

    @FXML private Button btnFeed;
    @FXML private Button btnHeal;
    @FXML private Button btnEndTurn;
    @FXML private Label lblCurrentChief;
    @FXML private Button btnBrew;
    @FXML private Button btnGather;

    @FXML private ComboBox<Place> comboChiefDest;
    @FXML private Button btnChiefTravel;

    @FXML private TextField inputTheaterName;

    private InvasionTheater model;

    private volatile boolean isRunning = false;
    private Thread simulationThread;
    private int chiefTurnIndex = 0;
    private Chief activeChief;

    @FXML private TextField inputPlaceName;
    @FXML private TextField inputPlaceArea;
    @FXML private ComboBox<PlaceType> comboPlaceType;

    @FXML private ComboBox<Place> comboDestPlace;
    @FXML private ComboBox<CharacterType> comboCharType;
    @FXML private TextField inputCharName;
    @FXML private TextField inputCharAge;

    @FXML private ComboBox<Place> comboMoveSource;
    @FXML private ComboBox<Character> comboMoveChar;
    @FXML private ComboBox<Place> comboMoveDest;

    private final Object pauseLock = new Object();
    private volatile boolean isPausedForUser = false;
    private static final int TIME_STEP = 2000;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    public void initialize() {
        logToView("Initializing Armorique System V5...");

        setChiefControlsDisable(true);
        btnStop.setDisable(true);

        if (comboCharType != null) {
            comboCharType.setItems(FXCollections.observableArrayList(CharacterType.values()));
            comboCharType.getSelectionModel().selectFirst();
        }

        if (comboPlaceType != null) {
            comboPlaceType.setItems(FXCollections.observableArrayList(PlaceType.values()));
            comboPlaceType.getSelectionModel().selectFirst();
        }

        initMovementLogic();

        try {
            initializeModel("src/main/resources/com/asterix/data/scenarioDefaut.xml");
        } catch (Exception e) {
            logToView("Info : " + e.getMessage());
            logToView("Creating an empty default Theater.");
            this.model = new InvasionTheater("Default Armorique");
        }

        refreshPlaceList();
    }

    /**
     * Sets up listeners to dynamically update the character list when a source place is selected.
     */
    private void initMovementLogic() {
        if (comboMoveSource != null) {
            comboMoveSource.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newSource) -> {
                if (newSource != null) {
                    comboMoveChar.setItems(FXCollections.observableArrayList(newSource.getCharacters()));
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
     * Loads the simulation model from an XML file.
     *
     * @param xmlPath The path to the XML file.
     * @throws Exception If parsing fails.
     */
    public void initializeModel(String xmlPath) throws Exception {
        this.model = XmlScenarioLoader.loadTheater(xmlPath);
        logToView("Theater loaded: " + model.getName());
    }

    /**
     * Starts the simulation thread.
     */
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

    /**
     * Stops the simulation thread.
     */
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

    /**
     * Main loop of the simulation thread.
     */
    @Override
    public void run() {
        while (isRunning) {
            try {
                simulateStep();
                Platform.runLater(this::updateView);
                triggerUserTurn();

                if (model != null) model.applyDailyHunger();

                Thread.sleep(TIME_STEP);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                isRunning = false;
                logToView("Simulation thread interrupted.");
            }
        }
    }

    /**
     * Executes automatic simulation steps (fights, random events, food generation).
     */
    private void simulateStep() {
        if (model != null) {
            logToView("--- New Time Cycle ---");
            model.handleFights();
            model.generateFood();
            model.ageFood();
        }
    }

    /**
     * Pauses the simulation thread to allow user interaction via the Clan Chief.
     *
     * @throws InterruptedException If the thread is interrupted while waiting.
     */
    private void triggerUserTurn() throws InterruptedException {
        if (model == null || model.getPlaces().isEmpty()) return;

        List<Chief> availableChiefs = model.getPlaces().stream()
                .filter(place -> place instanceof Settlement)
                .map(place -> ((Settlement) place).getChief())
                .filter(chief -> chief != null)
                .collect(Collectors.toList());

        if (availableChiefs.isEmpty()) return;

        Chief currentChief = availableChiefs.get(chiefTurnIndex % availableChiefs.size());
        chiefTurnIndex++;

        synchronized (pauseLock) {
            isPausedForUser = true;

            Platform.runLater(() -> {
                if (lblCurrentChief != null) {
                    String locationName = currentChief.getLocation().getName();
                    lblCurrentChief.setText("Chef : " + currentChief.getName() + "\n📍 " + locationName);
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

    /**
     * Distributes food to all characters in the active chief's location.
     */
    @FXML
    public void handleFeed() {
        if (this.activeChief == null) return;
        this.activeChief.feedCharactersInLocation();
        logToView("Chef " + this.activeChief.getName() + " distributed food!");
        updateView();
    }

    /**
     * Heals all characters in the active chief's location.
     */
    @FXML
    public void handleHeal() {
        if (this.activeChief == null) return;
        this.activeChief.healCharactersInLocation();
        logToView("Chief " + this.activeChief.getName() + " healed the wounded!");
        updateView();
    }

    /**
     * Moves the active chief to a new location.
     * <p>
     * This action updates the chief's reference but does not automatically end the turn,
     * allowing the user to perform actions in the new location.
     * </p>
     */
    @FXML
    public void handleChiefTravel() {
        if (this.activeChief == null) {
            logToView("❌ No active chief.");
            return;
        }

        Place destination = comboChiefDest.getValue();
        if (destination == null) {
            logToView("⚠️ Select a destination.");
            return;
        }

        Place source = activeChief.getLocation();
        if (source == destination) {
            logToView("⚠️ The chief is already there.");
            return;
        }

        try {
            logToView(">>> 🏃 Chief " + activeChief.getName() + " is leaving " + source.getName() + "...");

            activeChief.setLocation(destination);

            logToView("✅ Arrival confirmed at: " + destination.getName());

            if (lblCurrentChief != null) {
                lblCurrentChief.setText("Chef : " + activeChief.getName() + "\n📍 " + destination.getName());
            }

            handleDisplayStats();

            logToView("👉 You are still in command. You can act here or end the turn.");

        } catch (Exception e) {
            logToView("⛔ Travel error: " + e.getMessage());
        }
    }

    /**
     * Instructs a Druid in the current village to brew a magic potion.
     */
    @FXML
    public void handleBrewPotion() {
        if (activeChief == null) {
            logToView("❌ No active chief.");
            return;
        }

        Place currentPlace = activeChief.getLocation();

        if (!(currentPlace instanceof GaulVillage village)) {
            logToView("⚠️ Impossible: There is no cauldron here! (Not a Gaul Village)");
            return;
        }

        Druid druid = (Druid) village.getCharacters().stream()
                .filter(c -> c instanceof Druid)
                .findFirst()
                .orElse(null);

        if (druid == null) {
            logToView("⚠️ Panic! No Druid available to brew the potion!");
            return;
        }

        logToView(">>> 🧪 The chief asks " + druid.getName() + " to brew the potion!");
        Cauldron cauldron = village.getCauldron();
        boolean success = cauldron.brew();

        if (success) {
            logToView("✅ EXCELLENT! The magic potion is ready (10 doses)!");
        } else {
            logToView("🤢 Failed... Ingredients missing.");
            logToView("   (Use 'Gather' to fill the cauldron!)");
        }
    }

    /**
     * Simulates gathering ingredients and adding them to the village cauldron.
     */
    @FXML
    public void handleGather() {
        if (activeChief == null) return;

        Place currentPlace = activeChief.getLocation();

        if (!(currentPlace instanceof GaulVillage village)) {
            logToView("⚠️ Gathering is only possible in a Gaul Village.");
            return;
        }

        Druid druid = (Druid) village.getCharacters().stream()
                .filter(c -> c instanceof Druid).findFirst().orElse(null);

        if (druid == null) {
            logToView("⚠️ No Druid here to gather ingredients!");
            return;
        }

        village.addFood(FoodFactory.createRandomPotionIngredient());
        village.addFood(FoodFactory.createRandomPotionIngredient());
        village.addFood(FoodFactory.createRandomPotionIngredient());

        logToView("🌱 Wild ingredients appeared in " + village.getName() + ".");
        logToView(">>> " + druid.getName() + " goes gathering...");

        int count = druid.gatherIngredients(village);

        if (count > 0) {
            logToView("✅ Success! " + count + " ingredients added to the cauldron.");
            logToView("   Total ingredients: " + village.getCauldron().getIngredients().size());
        } else {
            logToView("🤷 " + druid.getName() + " found nothing useful.");
        }

        handleDisplayStats();
    }

    /**
     * Ends the user's turn and resumes the simulation thread.
     */
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

    /**
     * Displays detailed statistics of all places and characters in the log area.
     */
    @FXML
    public void handleDisplayStats() {
        if (model == null) {
            logToView("No theater loaded.");
            return;
        }

        logToView("--- 📊 SITUATION REPORT ---");

        for (Place place : model.getPlaces()) {
            String header = "📍 " + place.toString();
            if (activeChief != null && activeChief.getLocation() == place) {
                header += " [👑 ACTIVE CHIEF PRESENT]";
            }
            logToView(header);

            if (place instanceof Settlement) {
                Chief resident = ((Settlement) place).getChief();
                if (resident != null) {
                    logToView("    🏛️ Resident Chief: " + resident.getName());
                }
            }

            List<Character> occupants = place.getCharacters();
            if (occupants.isEmpty()) {
                logToView("    (No inhabitants)");
            } else {
                for (Character c : occupants) {
                    logToView("    👤 " + c.toString());
                }
            }

            if (!place.getFoods().isEmpty()) {
                logToView("    🍎 Supplies: " + place.getFoods().size() + " units");
            }
            logToView("--------------------------------------------------");
        }
    }

    /**
     * Creates a new, empty Invasion Theater.
     */
    @FXML
    public void handleCreateTheater() {
        String name = inputTheaterName.getText();
        if (name == null || name.trim().isEmpty()) name = "Custom Theater";
        this.model = new InvasionTheater(name);
        logToView("New theater created: " + name);
        refreshPlaceList();
    }

    /**
     * Creates a new Place based on user input and adds it to the model.
     */
    @FXML
    public void handleCreatePlace() {
        if (this.model == null) this.model = new InvasionTheater("New Theater");

        String name = inputPlaceName.getText();
        String areaStr = inputPlaceArea.getText();
        PlaceType type = comboPlaceType.getValue();

        if (name == null || name.trim().isEmpty() || type == null) {
            logToView("❌ Name/Type missing.");
            return;
        }

        double area;
        try {
            area = Double.parseDouble(areaStr);
        } catch (NumberFormatException e) {
            return;
        }

        Place newPlace = null;
        Chief c = new Chief("Chief of " + name, "MALE", 40, null);

        if (type == PlaceType.GAUL_VILLAGE) {
            newPlace = new GaulVillage(name, area, c);
        } else if (type == PlaceType.ROMAN_CAMP) {
            newPlace = new RomanCamp(name, area, c);
        } else if (type == PlaceType.ROMAN_CITY || type == PlaceType.ROMAN_VILLAGE) {
            newPlace = new RomanCity(name, area, c);
        } else if (type == PlaceType.GALLO_ROMAN_TOWN) {
            newPlace = new GalloRomanTown(name, area, c);
        } else if (type == PlaceType.CREATURE_ENCLOSURE) {
            newPlace = new CreatureEnclosure(name, area);
        } else {
            newPlace = new Battlefield(name, area);
        }

        if (newPlace instanceof Settlement) {
            c.setLocation(newPlace);
        }

        if (newPlace != null) {
            this.model.addPlace(newPlace);
            logToView("✅ Added: " + newPlace.getName());
            inputPlaceName.clear();
            inputPlaceArea.clear();
            refreshPlaceList();
            handleDisplayStats();
        }
    }

    /**
     * Creates a new Character via Factory and adds it to a destination.
     */
    @FXML
    public void handleRecruitCharacter() {
        if (model == null) return;
        Place destination = comboDestPlace.getValue();
        CharacterType type = comboCharType.getValue();
        String name = inputCharName.getText();
        String ageStr = inputCharAge.getText();

        if (destination == null || type == null || name.isEmpty() || ageStr.isEmpty()) {
            logToView("❌ Fill fields.");
            return;
        }
        try {
            int age = Integer.parseInt(ageStr);
            Character newChar = CharacterFactory.createCharacter(type, name, age);
            destination.addCharacter(newChar);
            logToView("✅ Recruited: " + newChar.getName());
            handleDisplayStats();
            inputCharName.clear();
        } catch (Exception e) {
            logToView("⛔ Error: " + e.getMessage());
        }
    }

    /**
     * Moves a character from one place to another using transactional logic.
     */
    @FXML
    public void handleMoveTroop() {
        if (model == null) return;
        Place source = comboMoveSource.getValue();
        Character character = comboMoveChar.getValue();
        Place destination = comboMoveDest.getValue();

        if (source == null || character == null || destination == null) return;
        if (source == destination) return;

        try {
            destination.addCharacter(character);
            source.removeCharacter(character);
            logToView("🚚 Moved " + character.getName());
            handleDisplayStats();
            comboMoveChar.getSelectionModel().clearSelection();
            comboMoveChar.setItems(FXCollections.observableArrayList(source.getCharacters()));
        } catch (Exception e) {
            logToView("⛔ Transfer Failed: " + e.getMessage());
        }
    }

    /**
     * Refreshes all place-related dropdown menus with current model data.
     */
    private void refreshPlaceList() {
        if (model != null) {
            List<Place> places = model.getPlaces();
            StringConverter<Place> converter = new StringConverter<Place>() {
                @Override
                public String toString(Place p) {
                    return (p == null) ? "" : p.getName();
                }
                @Override
                public Place fromString(String s) {
                    return null;
                }
            };

            if (comboDestPlace != null) {
                comboDestPlace.setItems(FXCollections.observableArrayList(places));
                comboDestPlace.setConverter(converter);
            }
            if (comboMoveSource != null) {
                comboMoveSource.setItems(FXCollections.observableArrayList(places));
                comboMoveSource.setConverter(converter);
            }
            if (comboMoveDest != null) {
                comboMoveDest.setItems(FXCollections.observableArrayList(places));
                comboMoveDest.setConverter(converter);
            }
            if (comboChiefDest != null) {
                comboChiefDest.setItems(FXCollections.observableArrayList(places));
                comboChiefDest.setConverter(converter);
            }
        }
    }

    /**
     * Appends the model's string representation to the output area.
     */
    private void updateView() {
        if (model != null) {
            outputArea.appendText(model.toString() + "\n");
            outputArea.setScrollTop(Double.MAX_VALUE);
        }
    }

    /**
     * Logs a message to the output area on the JavaFX Application Thread.
     *
     * @param message The message to display.
     */
    private void logToView(String message) {
        Platform.runLater(() -> outputArea.appendText(message + "\n"));
    }

    /**
     * Toggles the disabled state of clan chief control buttons.
     *
     * @param disable True to disable, false to enable.
     */
    private void setChiefControlsDisable(boolean disable) {
        if (btnFeed != null) btnFeed.setDisable(disable);
        if (btnHeal != null) btnHeal.setDisable(disable);
        if (btnEndTurn != null) btnEndTurn.setDisable(disable);
        if (btnChiefTravel != null) btnChiefTravel.setDisable(disable);
        if (comboChiefDest != null) comboChiefDest.setDisable(disable);
        if (btnBrew != null) btnBrew.setDisable(disable);
        if (btnGather != null) btnGather.setDisable(disable);
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
                this.model = XmlScenarioLoader.loadTheaterFromFile(file);
                refreshPlaceList();
                logToView("Loaded: " + model.getName());
            } catch (Exception e) {
                logToView("Error: " + e.getMessage());
            }
        }
    }
}