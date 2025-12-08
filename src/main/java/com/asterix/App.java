package com.asterix;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Point d'entrée de l'application de simulation d'envahissement[cite: 6, 109].
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // 1. Définition du chemin
        String fxmlPath = "/com/asterix/view/simulation.fxml";


        // 2. Vérification avant chargement (Diagnostic)
        var url = getClass().getResource(fxmlPath);

        if (url == null) {
            System.err.println("CRITIQUE : Le fichier FXML est introuvable !");
            System.err.println("Chemin cherché : " + fxmlPath);
            System.err.println("Vérifiez que le dossier 'resources' est bien marqué comme 'Resources Root' dans IntelliJ.");
            System.exit(1); // Arrêt immédiat pour voir l'erreur
        } else {
            System.out.println("SUCCÈS : Fichier trouvé à -> " + url);
        }

        // 3. Chargement normal
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Projet Armorique");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}