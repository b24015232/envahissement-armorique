package com.asterix.utils;
import com.asterix.model.simulation.InvasionTheater;
// Importez vos classes Lieu, Village, Camp ici quand elles existeront
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.List;

public class XmlScenarioSaver {

    public static void saveTheater(InvasionTheater theater, File file) throws Exception {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // 1. Élément racine <theatre>
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("theatre");
        doc.appendChild(rootElement);

        // 2. Nom du théâtre <nom>
        Element nom = doc.createElement("nom");
        nom.appendChild(doc.createTextNode(theater.getName()));
        rootElement.appendChild(nom);

        // 3. Liste des lieux <lieux>
        Element lieuxElement = doc.createElement("lieux");
        rootElement.appendChild(lieuxElement);

        // On suppose que vous avez ajouté une méthode getLieux() dans InvasionTheater
        // List<Object> listeLieux = theater.getLieux();

        // Exemple de boucle (à adapter quand vous aurez vos vrais objets Lieu)
        /* for (Object obj : listeLieux) {
             Element lieu = doc.createElement("lieu");
             // Si c'est un village...
             lieu.setAttribute("type", "village");

             Element nomLieu = doc.createElement("nom");
             nomLieu.appendChild(doc.createTextNode("Nom du Lieu (A recuperer)"));
             lieu.appendChild(nomLieu);

             Element superficie = doc.createElement("superficie");
             superficie.appendChild(doc.createTextNode("500"));
             lieu.appendChild(superficie);

             lieuxElement.appendChild(lieu);
        }
        */

        // 4. Écriture du fichier physique
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        // Pour un joli formatage (indentation)
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(file);

        transformer.transform(source, result);
    }

}
