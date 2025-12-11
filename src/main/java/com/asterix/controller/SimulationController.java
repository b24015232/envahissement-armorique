package com.asterix.controller;

import com.asterix.model.character.creature.Creature;
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
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Main Controller managing the temporal logic of the simulation and user interactions.
 * Implements the MVC pattern and multithreading management for the simulation loop.
 * <p>
 * This controller handles the requirements regarding the "Temporal aspect" and
 * "Passing the hand to a clan chief" defined in the specifications.
 * </p>
 *
 * @author Project Team
 * @version 2.0
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

    @FXML private TextField inputPlaceName;
    @FXML private TextField inputPlaceArea;
    @FXML private ComboBox<PlaceType> comboPlaceType;

    /**
     * Lock object to synchronize the simulation thread and the UI thread.
     * Allows the simulation loop to pause (`wait`) while waiting for user input.
     */
    private final Object pauseLock = new Object();

    /**
     * Flag indicating if the simulation is currently paused waiting for the user.
     */
    private volatile boolean isPausedForUser = false;

    private static final int TIME_STEP = 2000; // 2 seconds between AI turns

    /**
     * Method called automatically by JavaFX after FXML loading.
     * Initializes the UI state and loads a default model.
     */
    @FXML
    public void initialize() {
        logToView("Initializing Armorique System V4...");

        // Disable chief actions at startup
        setChiefControlsDisable(true);
        btnStop.setDisable(true);

        try {
            // Load default scenario
            initializeModel("src/main/resources/com/asterix/data/scenarioDefault.xml");
        } catch (Exception e) {
            logToView("Info: " + e.getMessage());
            logToView("Creating an empty default Theater.");
            this.model = new InvasionTheater("Default Armorique");
        }

        if (comboPlaceType != null) {
            // Transforme l'Enum en liste pour JavaFX
            comboPlaceType.setItems(FXCollections.observableArrayList(PlaceType.values()));

            // Sélectionne le premier élément par défaut pour éviter qu'elle soit vide au début
            comboPlaceType.getSelectionModel().selectFirst();

            System.out.println("DEBUG: ComboBox remplie avec succès.");
        } else {
            System.err.println("ERREUR: comboPlaceType est NULL. Vérifiez le fx:id dans le FXML !");
        }
    }

    /**
     * Initializes the model from an XML path.
     *
     * @param xmlPath The relative or absolute path to the XML file.
     * @throws Exception If parsing fails.
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
            simulationThread.setName("Simu-Thread"); // Professional thread naming
            simulationThread.start();

            btnStart.setDisable(true);
            btnStop.setDisable(false);
            logToView(">>> Simulation started.");
        }
    }

    @FXML
    private void handleStop() {
        isRunning = false;

        // If the thread is waiting (wait()), notify it to allow a clean exit
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
     * Main Thread Loop.
     * Manages time progression and delegates control to the user periodically.
     * <p>
     * Complies with the requirement: "At regular intervals, this method must...
     * pass the hand to a clan chief".
     * </p>
     */

    /**
     * Handles the request to display detailed statistics for all places and inhabitants.
     * <p>
     * Displays the place characteristics followed by the detailed stats of every
     * character present (Health, Strength, etc.), complying with the requirement
     * "afficher ses caractéristiques".
     * </p>
     */
    @FXML
    public void handleDisplayStats() {
        if (model == null) {
            logToView("No theater loaded.");
            return;
        }

        logToView("--- 📊 DISPLAYING DETAILED CHARACTERISTICS ---");

        for (com.asterix.model.place.Place place : model.getPlaces()) {


            logToView("📍 " + place.toString());
            List<com.asterix.model.character.Character> occupants = place.getCharacters();

            if (occupants.isEmpty()) {
                logToView("    (No inhabitants)");
            } else {
                for (com.asterix.model.character.Character c : occupants) {
                    logToView("    👤 " + c.toString());
                }
            }
            if (!place.getFoods().isEmpty()) {
                logToView("    🍎 Inventory: " + place.getFoods().size() + " items");
            }

            logToView("--------------------------------------------------");
        }
    }
    @Override
    public void run() {
        while (isRunning) {
            try {
                // 1. Automatic Execution (AI, fights, food generation)
                simulateStep();

                // Update UI on JavaFX Application Thread
                Platform.runLater(this::updateView);

                // 2. User Interaction (Clan Chief's Turn)
                triggerUserTurn();

                model.applyDailyHunger();

                // Technical pause for readability/pacing
                Thread.sleep(TIME_STEP);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                isRunning = false;
                logToView("Simulation thread interrupted.");
            }
        }
    }

    /**
     * Executes required automatic actions at regular intervals.
     * Includes fights, random events, and food management.
     */
    private void simulateStep() {
        if (model != null) {
            logToView("--- New Time Cycle ---");
            model.handleFights();        // [cite: 627]
            model.applyRandomEvents();   // [cite: 628]
            model.generateFood();        // [cite: 629]
            model.ageFood();             // [cite: 630]
        }
    }

    /**
     * Pauses the simulation thread and activates the UI for the Clan Chief.
     * Uses a synchronized block to wait for a notify() from the UI.
     *
     * @throws InterruptedException if the thread is interrupted while waiting.
     */
    private void triggerUserTurn() throws InterruptedException {
        if (model == null || model.getPlaces().isEmpty()) return;

        // 1. Récupération dynamique des chefs depuis les lieux
        // On filtre pour exclure les champs de bataille qui n'ont pas de chef
        List<Chief> availableChiefs = model.getPlaces().stream()
                .filter(place -> place instanceof Settlement) // On suppose que Settlement est la classe des lieux habités
                .map(place -> ((Settlement) place).getChief()) // On récupère le chef du lieu
                .filter(chief -> chief != null) // Sécurité
                .collect(Collectors.toList());

        if (availableChiefs.isEmpty()) {
            logToView("Aucun chef disponible pour jouer ce tour.");
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

                logToView(">>> Your turn, Chief " + currentChief.getName() + " of " + currentChief.getLocation() + "!");
                setChiefControlsDisable(false); // Active les boutons
            });

            while (isPausedForUser && isRunning) {
                pauseLock.wait();
            }
        }
    }

    /**
     * Handles the "Feed" action triggered by the user.
     * Uses the currently active chief to perform the action.
     */
    @FXML
    public void handleFeed() {
        // Vérification de sécurité : un chef doit avoir été sélectionné par triggerUserTurn
        if (this.activeChief == null) {
            logToView("Action impossible : Aucun chef n'est actif pour le moment.");
            return;
        }
        this.activeChief.feedCharactersInLocation();

        logToView("Chief " + this.activeChief.getName() + " distributed food in " + this.activeChief.getLocation() + "!");
        updateView();
    }

    /**
     * Handles the "Heal" action triggered by the user.
     * Uses the currently active chief to perform the action.
     */
    @FXML
    public void handleHeal() {
        if (this.activeChief == null) {
            logToView("Action impossible : Aucun chef n'est actif pour le moment.");
            return;
        }
        this.activeChief.healCharactersInLocation();

        logToView("Chief " + this.activeChief.getName() + " healed the wounded in " + this.activeChief.getLocation() + "!");
        updateView();

    }
    /**
     * User clicks "End Turn".
     * Resumes the simulation thread.
     */
    @FXML
    public void handleEndTurn() {
        synchronized (pauseLock) {
            isPausedForUser = false;
            setChiefControlsDisable(true); // Disable buttons
            if (lblCurrentChief != null) {
                lblCurrentChief.setText("Simulation running...");
            }
            logToView(">>> End of user turn.");

            // Wake up the "run()" thread that was waiting
            pauseLock.notifyAll();
        }
    }

    // --- Utilities ---

    private void updateView() {
        if (model != null) {
            outputArea.appendText(model.toString() + "\n");
            outputArea.appendText("--------------------------------------------------\n");
            outputArea.setScrollTop(Double.MAX_VALUE);
        }
    }

    private void logToView(String message) {
        // Ensures the call is made on the JavaFX thread
        Platform.runLater(() -> outputArea.appendText(message + "\n"));
    }

    private void setChiefControlsDisable(boolean disable) {
        if (btnFeed != null) btnFeed.setDisable(disable);
        if (btnHeal != null) btnHeal.setDisable(disable);
        if (btnEndTurn != null) btnEndTurn.setDisable(disable);
    }

    // --- File Management (Save/Load) ---

    @FXML
    public void handleCreateTheater() {
        String name = inputTheaterName.getText();
        if (name == null || name.trim().isEmpty()) name = "Custom Theater";

        this.model = new InvasionTheater(name);
        logToView("New theater created: " + name);
        logToView("Add locations.");
    }

    /**
     * Handles the creation of a new place based on user selection.
     * <p>
     * Logic:
     * 1. Validates inputs (Name, Area, Type).
     * 2. Uses a Factory approach to instantiate the correct Place subclass.
     * 3. Automatically generates a default Chief for Settlements (required by model).
     * 4. Adds the place to the model and updates the view.
     * </p>
     */
    @FXML
    public void handleCreatePlace() {
        // 1. Ensure Model Exists
        if (this.model == null) {
            this.model = new InvasionTheater("New Theater");
            logToView("⚠️ No theater existed. Created a new one.");
        }

        // 2. Retrieve Inputs
        String name = inputPlaceName.getText();
        String areaStr = inputPlaceArea.getText();
        PlaceType type = comboPlaceType.getValue();

        // 3. Validation
        if (name == null || name.trim().isEmpty()) {
            logToView("❌ Error: Place name is required.");
            return;
        }
        if (type == null) {
            logToView("❌ Error: Place type must be selected.");
            return;
        }

        double area;
        try {
            area = Double.parseDouble(areaStr);
        } catch (NumberFormatException e) {
            logToView("❌ Error: Area must be a valid number.");
            return;
        }

        // 4. Creation Logic (Factory Pattern)
        Place newPlace = null;

        if (type == PlaceType.GAUL_VILLAGE) {
            Chief defaultChief = new Chief("Chief of " + name, "MALE", 40, null);
            newPlace = new GaulVillage(name, area, defaultChief);
            defaultChief.setLocation(newPlace); // Link back

        } else if (type == PlaceType.ROMAN_CAMP) {
            Chief defaultChief = new Chief("Prefect of " + name, "MALE", 35, null);
            newPlace = new RomanCamp(name, area, defaultChief);
            defaultChief.setLocation(newPlace); // Link back

        } else if (type == PlaceType.BATTLEFIELD) {
            newPlace = new Battlefield(name, area);

        } else if (type == PlaceType.ROMAN_CITY) {
            Chief defaultChief = new Chief("Chief of " + name, "MALE", 40, null);
            newPlace = new RomanCity(name, area, defaultChief);
            defaultChief.setLocation(newPlace);

        } else if (type == PlaceType.ROMAN_VILLAGE) {
            Chief defaultChief = new Chief("Prefect of " + name, "FEMALE", 35, null);
            newPlace = new RomanCity(name, area, defaultChief);
            defaultChief.setLocation(newPlace);

        } else if (type == PlaceType.GALLO_ROMAN_TOWN) {
            Chief defaultChief = new Chief("Prefect of " + name, "FEMALE", 35, null);
            newPlace = new GalloRomanTown(name, area, defaultChief);
            defaultChief.setLocation(newPlace);

        } else if (type == PlaceType.CREATURE_ENCLOSURE) {
            newPlace = new CreatureEnclosure(name, area);
        }

        // 5. Add to Model
        if (newPlace != null) {
            this.model.addPlace(newPlace);
            logToView("✅ Added new place: " + newPlace.getName() + " (" + type + ")");

            // Clear inputs for next entry
            inputPlaceName.clear();
            inputPlaceArea.clear();

            handleDisplayStats();
        }
    }


    @FXML
    public void handleSaveScenario() {
        if (model == null) {
            logToView("Error: No theater to save!");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Scenario");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        fileChooser.setInitialFileName("my_scenario.xml");

        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try {
                XmlScenarioSaver.saveTheater(model, file);
                logToView("Success: Scenario saved to " + file.getAbsolutePath());
            } catch (Exception e) {
                logToView("Error during save: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void handleLoadScenario() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Scenario");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));

        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            try {
                initializeModelFromExternal(file.getAbsolutePath());
            } catch (Exception e) {
                logToView("Load Error: " + e.getMessage());
            }
        }
    }

    private void initializeModelFromExternal(String path) throws Exception {
        this.model = XmlScenarioLoader.loadTheaterFromFile(new File(path));
        logToView("Loaded: " + model.getName());
    }
}