package com.asterix.utils;

import com.asterix.model.simulation.InvasionTheater;
import com.asterix.model.place.Place;
import com.asterix.model.place.Settlement;
import com.asterix.model.character.Character;
import com.asterix.model.character.Chief;

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

/**
 * Utility class to save the current state of an InvasionTheater to an XML file.
 * <p>
 * This class ensures that the output format is fully compatible with {@link XmlScenarioLoader}.
 * It persists the theater details, the list of places, their specific chiefs (if applicable),
 * and all characters currently present in those places.
 * </p>
 */
public class XmlScenarioSaver {

    /**
     * Saves the provided theater model to an XML file.
     *
     * @param theater The theater model to save.
     * @param file    The destination file.
     * @throws Exception If an error occurs during DOM creation or file writing.
     */
    public static void saveTheater(InvasionTheater theater, File file) throws Exception {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // 1. Root Element <InvasionTheater>
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("InvasionTheater");
        doc.appendChild(rootElement);

        // 2. Theater Name <name>
        createElementWithText(doc, rootElement, "name", theater.getName());

        // 3. Places Container <places> (Optional wrapper, but good for structure)
        // Note: The loader iterates directly on "place" tags, but having a parent is cleaner XML.
        // We will append <place> directly to root or a wrapper depending on your Loader logic.
        // Based on previous Loader: doc.getElementsByTagName("place") finds them anywhere.

        if (theater.getPlaces() != null) {
            for (Place place : theater.getPlaces()) {
                Element placeElement = doc.createElement("place");

                // Attribute: type (e.g., GaulVillage, Battlefield...)
                // We use the simple class name as the type
                placeElement.setAttribute("type", place.getClass().getSimpleName());

                // Place Properties
                createElementWithText(doc, placeElement, "name", place.getName());

                // We assume Place has a getter for area. If not, using a default or formatted string.
                // You must ensure public double getArea() exists in Place.java
                // Using 100.0 as fallback if you haven't implemented getArea() yet.
                try {
                    // Reflection or cast could be used if getArea isn't on abstract Place
                    // Assuming Place has: public double getArea();
                    createElementWithText(doc, placeElement, "area", String.valueOf(place.getArea()));
                } catch (Exception e) {
                    createElementWithText(doc, placeElement, "area", "100.0");
                }

                // 4a. Save Chief (Only for Settlements)
                if (place instanceof Settlement) {
                    Chief chief = ((Settlement) place).getChief();
                    if (chief != null) {
                        Element chiefElement = doc.createElement("chief");
                        createElementWithText(doc, chiefElement, "name", chief.getName());
                        createElementWithText(doc, chiefElement, "age", String.valueOf(chief.getAge()));
                        createElementWithText(doc, chiefElement, "sex", String.valueOf(chief.getGender()));
                        placeElement.appendChild(chiefElement);
                    }
                }

                // 4b. Save Characters <characters>
                // Although the loader looks for "character" tags inside "place", a wrapper is polite.
                // However, let's stick to simple direct children if desired, or wrapper.
                // XML standard often prefers specific lists.

                for (Character character : place.getCharacters()) {
                    Element charElement = doc.createElement("character");

                    // Attribute: type (e.g., Merchant, Legionnaire...)
                    charElement.setAttribute("type", character.getClass().getSimpleName());

                    createElementWithText(doc, charElement, "name", character.getName());
                    createElementWithText(doc, charElement, "age", String.valueOf(character.getAge()));
                    createElementWithText(doc, charElement, "height", String.valueOf(character.getHeight()));
                    createElementWithText(doc, charElement, "strength", String.valueOf(character.getStrength()));
                    createElementWithText(doc, charElement, "stamina", String.valueOf(character.getStamina()));
                    createElementWithText(doc, charElement, "gender", String.valueOf(character.getGender()));

                    placeElement.appendChild(charElement);
                }

                rootElement.appendChild(placeElement);
            }
        }

        // 5. Write to physical file
        writeXmlToFile(doc, file);
    }

    // --- Utility Methods ---

    /**
     * Helper to create a simple XML tag containing text.
     * Example: <name>Value</name>
     */
    private static void createElementWithText(Document doc, Element parent, String tagName, String textValue) {
        Element element = doc.createElement(tagName);
        element.appendChild(doc.createTextNode(textValue));
        parent.appendChild(element);
    }

    /**
     * Helper to write the DOM to a file with indentation.
     */
    private static void writeXmlToFile(Document doc, File file) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        // Configuration for Pretty Print (Indentation)
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(file);

        transformer.transform(source, result);
    }
}