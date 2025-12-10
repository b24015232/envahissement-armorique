package com.asterix.controller;

import com.asterix.model.simulation.InvasionTheater;
import com.asterix.utils.XmlScenarioLoader;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import java.io.File;
import com.asterix.utils.XmlScenarioSaver;
import javafx.scene.control.TextField;

/**
 * Contrôleur gérant la logique temporelle de la simulation.
 * Implémente Runnable pour l'utilisation de Threads.
 */
public class SimulationController implements Runnable {

    // --- Éléments FXML ---
    @FXML private TextArea outputArea;
    @FXML private Button btnStart;
    @FXML private Button btnStop;

    // --- Modèle ---
    private InvasionTheater model;

    // --- Gestion du Thread ---
    private volatile boolean isRunning = false;
    private Thread simulationThread;
    private static final int TIME_STEP = 2000;

    /**
     * Méthode appelée automatiquement par JavaFX après le chargement du FXML.
     */
    @FXML
    public void initialize() {
        outputArea.appendText("Initialisation du système...\n");
        try {
            initializeModel("src/main/resources/com/asterix/data/scenarioDefaut.xml");
        } catch (Exception e) {
            outputArea.appendText(e.getMessage());
            outputArea.appendText("Attention: Aucun fichier XML chargé ou fichier introuvable. Création d'un théâtre vide.\n");
            this.model = new InvasionTheater("Armorique par défaut");
        }

        btnStop.setDisable(true);
    }

    public void initializeModel(String xmlPath) throws Exception {
        this.model = XmlScenarioLoader.loadTheater(xmlPath);
        logToView("Théâtre chargé : " + model.getName());
    }

    @FXML
    private void handleStart() {
        if (!isRunning) {
            isRunning = true;
            simulationThread = new Thread(this);
            simulationThread.start();

            btnStart.setDisable(true);
            btnStop.setDisable(false);
            logToView(">>> Simulation lancée.");
        }
    }

    @FXML
    private void handleStop() {
        isRunning = false;
        btnStart.setDisable(false);
        btnStop.setDisable(true);
        logToView(">>> Simulation en pause.");
    }


    @Override
    public void run() {
        while (isRunning) {
            try {

                simulateStep();
                Platform.runLater(() -> {
                    updateView();
                });

                Thread.sleep(TIME_STEP);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                isRunning = false; // Arrêt propre
            }
        }
    }

    /**
     * Executes required actions at regular intervals.
     * (Formerly simulateStep)
     */
    private void simulateStep() {
        if (model != null) {
            model.handleFights();
            model.applyRandomEvents();
            model.generateFood();
            model.ageFood();
        }
    }

    private void updateView() {
        if (model != null) {
            outputArea.appendText(model.toString() + "\n");
            outputArea.appendText("--------------------------------------------------\n");
            outputArea.setScrollTop(Double.MAX_VALUE);
        }
    }

    private void logToView(String message) {
        outputArea.appendText(message + "\n");
    }

    @FXML private TextField inputNomTheatre;
    @FXML
    public void handleCreerTheatre() {
        String nom = inputNomTheatre.getText();
        if (nom.isEmpty()) nom = "Théâtre Personnalisé";
        this.model = new InvasionTheater(nom);
        logToView("Nouveau théâtre créé : " + nom);
        logToView("Ajoutez des lieux puis sauvegardez.");
    }
    @FXML
    public void handleSaveScenario() {
        if (model == null) {
            logToView("Erreur : Aucun théâtre à sauvegarder !");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le scénario");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers XML", "*.xml"));
        fileChooser.setInitialFileName("mon_scenario.xml");

        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try {
                XmlScenarioSaver.saveTheater(model, file);
                logToView("Succès : Scénario sauvegardé dans " + file.getAbsolutePath());
            } catch (Exception e) {
                logToView("Erreur lors de la sauvegarde : " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void handleLoadScenario() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Ouvrir un scénario");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers XML", "*.xml"));

        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            try {
                initializeModelFromExternal(file.getAbsolutePath());
            } catch (Exception e) {
                logToView("Erreur chargement : " + e.getMessage());
            }
        }
    }

    private void initializeModelFromExternal(String path) throws Exception {
        this.model = XmlScenarioLoader.loadTheaterFromFile(new File(path));
        logToView("Chargé : " + model.getName());
    }

}