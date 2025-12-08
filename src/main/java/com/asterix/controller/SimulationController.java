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
 * Contrôleur gérant la logique temporelle de la simulation[cite: 109, 110].
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
    private volatile boolean isRunning = false; // volatile pour la visibilité entre threads
    private Thread simulationThread;
    private static final int TIME_STEP = 2000; // 2 secondes par tour

    /**
     * Méthode appelée automatiquement par JavaFX après le chargement du FXML.
     */
    @FXML
    public void initialize() {
        outputArea.appendText("Initialisation du système...\n");
        try {
            // Chargement initial (Simulation d'un chemin pour l'exemple)
            // Idéalement, ouvrir un FileChooser ici
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
        logToView("Théâtre chargé : " + model.getNom());
    }

    // --- Gestion des Actions Utilisateur ---

    @FXML
    private void handleStart() {
        if (!isRunning) {
            isRunning = true;
            simulationThread = new Thread(this);
            simulationThread.start(); // Lancement du Thread

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

    // --- Logique du Thread ---

    @Override
    public void run() {
        while (isRunning) {
            try {
                // 1. Logique métier [cite: 110]
                simulateStep();

                // 2. Mise à jour de l'interface graphique
                // IMPORTANT : Doit être fait dans le thread JavaFX
                Platform.runLater(() -> {
                    updateView();
                });

                // 3. Pause temporelle
                Thread.sleep(TIME_STEP);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                isRunning = false; // Arrêt propre
            }
        }
    }

    /**
     * Exécute les actions requises à intervalle régulier[cite: 110].
     */
    private void simulateStep() {
        if (model != null) {
            model.gererCombats();
            System.out.println("normalement ya eu heja");// [cite: 111]
            model.appliquerAleas();     // [cite: 112]
            model.genererAliments();    // [cite: 113]
            model.vieillirAliments();   // [cite: 114]
        }
    }

    private void updateView() {
        if (model != null) {
            // Affichage simple de l'état actuel
            outputArea.appendText(model.toString() + "\n");
            outputArea.appendText("--------------------------------------------------\n");

            // Scroll automatique vers le bas
            outputArea.setScrollTop(Double.MAX_VALUE);
        }
    }

    private void logToView(String message) {
        outputArea.appendText(message + "\n");
    }



// ... Dans la classe SimulationController

    @FXML private TextField inputNomTheatre;

    // 1. Créer un nouveau théâtre en mémoire
    @FXML
    public void handleCreerTheatre() {
        String nom = inputNomTheatre.getText();
        if (nom.isEmpty()) nom = "Théâtre Personnalisé";

        this.model = new InvasionTheater(nom);
        // Ajoutez ici des lieux par défaut si vous voulez

        logToView("Nouveau théâtre créé : " + nom);
        logToView("Ajoutez des lieux puis sauvegardez.");
    }

    // 2. Sauvegarder vers un fichier XML
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

        // Ouvre la fenêtre "Enregistrer sous"
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

    // 3. Charger (modifier votre méthode existante pour utiliser FileChooser)
    @FXML
    public void handleLoadScenario() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Ouvrir un scénario");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers XML", "*.xml"));

        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            try {
                // ATTENTION : Ici on utilise loadTheater (qui prend un File)
                // et PAS loadTheaterFromResource (qui prend un String interne)
                // Vous devrez peut-être surcharger votre méthode dans le Loader.
                // Pour l'instant, faisons simple :
                initializeModelFromExternal(file.getAbsolutePath());
            } catch (Exception e) {
                logToView("Erreur chargement : " + e.getMessage());
            }
        }
    }

    // Petite méthode utilitaire pour charger depuis l'extérieur (Disque dur)
    private void initializeModelFromExternal(String path) throws Exception {
        // Vous devrez adapter XmlScenarioLoader pour accepter un chemin absolu
        // ou créer une méthode loadTheaterFromFile(File f)
        this.model = XmlScenarioLoader.loadTheaterFromFile(new File(path));
        logToView("Chargé : " + model.getNom());
    }

}