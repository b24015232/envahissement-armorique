package com.asterix.utils;

import com.asterix.model.character.Chief;
import com.asterix.model.character.Gender;
import com.asterix.model.character.gaul.*;
import com.asterix.model.character.roman.*;
import com.asterix.model.simulation.InvasionTheater;
import com.asterix.model.place.*;
import com.asterix.model.character.Character; // Import explicite pour éviter la confusion avec java.lang.Character

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class XmlScenarioLoader {

    public static InvasionTheater loadTheater(String filePath) throws Exception {
        return loadTheaterFromFile(new File(filePath));
    }

    /**
     * Parses an XML file to construct a full InvasionTheater instance.
     * <p>
     * This method handles the creation of the theater, its places, and their inhabitants.
     * It manages the circular dependency between a Chief and their Settlement by
     * initializing the Chief first with a null location, then creating the concrete
     * Settlement (GaulVillage or RomanCamp), and finally linking the Chief to the Place.
     * </p>
     *
     * @param file The XML file containing the scenario data.
     * @return A fully populated {@link InvasionTheater} object.
     * @throws Exception If the XML structure is invalid or a parsing error occurs.
     */
    public static InvasionTheater loadTheaterFromFile(File file) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);
        doc.getDocumentElement().normalize();

        String theaterName = getTagValue("name", doc.getDocumentElement());
        InvasionTheater theater = new InvasionTheater(theaterName != null ? theaterName : "Unnamed Theater");

        NodeList placeNodes = doc.getElementsByTagName("place");

        for (int i = 0; i < placeNodes.getLength(); i++) {
            Element placeElement = (Element) placeNodes.item(i);

            String type = placeElement.getAttribute("type");
            String name = getTagValue("name", placeElement);
            double area = Double.parseDouble(getTagValue("area", placeElement));
            Place place = null;

            if ("Battlefield".equalsIgnoreCase(type)) {
                place = new Battlefield(name, area);
            } else if ("Enclos".equalsIgnoreCase(type) || "CreatureEnclosure".equalsIgnoreCase(type)) {
                place = new CreatureEnclosure(name, area);
            } else {
                Element chiefElement = (Element) placeElement.getElementsByTagName("chief").item(0);
                if (chiefElement != null) {
                    String cName = getTagValue("name", chiefElement);
                    String cSex = getTagValue("sex", chiefElement);
                    int cAge = Integer.parseInt(getTagValue("age", chiefElement));

                    Chief chief = new Chief(cName, cSex, cAge, null);

                    if (type.equalsIgnoreCase("RomanCamp")) {
                        place = new RomanCamp(name, area, chief);
                    } else if (type.equalsIgnoreCase("RomanCity") || type.equalsIgnoreCase("RomanVillage")) {
                        place = new RomanCity(name, area, chief);
                    } else if (type.equalsIgnoreCase("GalloRomanTown")) {
                        place = new GalloRomanTown(name, area, chief);
                    } else {
                        place = new GaulVillage(name, area, chief);
                    }

                    chief.setLocation(place);
                }
            }

            if (place != null) {
                NodeList charNodes = placeElement.getElementsByTagName("character");
                for (int j = 0; j < charNodes.getLength(); j++) {
                    Element charElement = (Element) charNodes.item(j);
                    Character c = createCharacter(charElement);
                    if (c != null) {
                        place.addCharacter(c);
                    }
                }
                theater.addPlace(place);
            }
        }
        return theater;
    }


    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag);
        if (nodeList != null && nodeList.getLength() > 0) {
            Node node = nodeList.item(0);
            if (node != null) {
                return node.getTextContent();
            }
        }
        return "0"; // Retourne "0" par défaut pour éviter de faire planter parseInt/parseDouble
    }

    /**
     * Factory method instantiating the correct Place subclass based on the type string.
     * Handles all place types defined in the specifications.
     *
     * @param type  The type string from XML or UI (e.g., "Gaul Village", "Battlefield").
     * @param name  The name of the place.
     * @param area  The surface area.
     * @param chief The chief to assign (can be null for Battlefields/Enclosures).
     * @return The concrete Place instance.
     */
    private static Place createPlace(String type, String name, double area, Chief chief) {
        if (type == null) return new Battlefield(name, area);

        String normalizedType = type.toLowerCase().replace(" ", "").replace("-", "");

        return switch (normalizedType) {

            case "battlefield" -> new Battlefield(name, area);
            case "creatureenclosure", "enclos" -> new CreatureEnclosure(name, area);
            case "gaulvillage" -> new GaulVillage(name, area, chief);
            case "romancamp" -> new RomanCamp(name, area, chief);
            case "romancity", "romanvillage" -> new RomanCity(name, area, chief);
            case "galloromantown", "bourgade" -> new GalloRomanTown(name, area, chief);

            default -> {
                System.out.println("⚠️ Lieu inconnu : " + type + ", création d'un Battlefield par défaut.");
                yield new Battlefield(name, area);
            }
        };
    }

    private static Character createCharacter(Element element) {
        // Parsing des attributs
        String type = element.getAttribute("type");
        String name = getTagValue("name", element);
        int id =  Integer.parseInt(getTagValue("id", element));
        int age = 0;
        double height = 0.0, strength = 0.0, stamina = 0.0;

        try {
            age = Integer.parseInt(getTagValue("age", element));
            height = Double.parseDouble(getTagValue("height", element));
            strength = Double.parseDouble(getTagValue("strength", element));
            stamina = Double.parseDouble(getTagValue("stamina", element));
        } catch (NumberFormatException e) {
            System.err.println("Erreur de lecture des stats pour le personnage : " + name);
        }

        String genderStr = getTagValue("gender", element);
        Gender gender = Gender.MALE; // Valeur par défaut
        try {
            if (genderStr != null && !genderStr.equals("0")) {
                gender = Gender.valueOf(genderStr.toUpperCase());
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Genre inconnu pour " + name + ", utilisation de MALE par défaut.");
        }

        // Factory de personnages
        if (type == null) return null;

        return switch (type.toLowerCase()) {
            // --- GAULOIS ---
            case "merchant" -> new Merchant(name, age, height, strength, stamina, gender);
            case "druid" -> new Druid(name, age, height, strength, stamina, gender);
            case "blacksmith" -> new BlackSmith(name, age, height, strength, stamina, gender);
            case "innkeeper" -> new Innkeeper(name, age, height, strength, stamina, gender);

            // --- ROMAINS ---
            case "legionnaire", "legionnary" -> new Legionnaire(name, age, height, strength, stamina, gender);
            case "general" -> new General(id, name, age, height, strength, stamina, gender);
            case "prefect" -> new Prefect(name, age, height, strength, stamina, gender);

            // --- DÉFAUT ---
            default -> {
                System.err.println("Type de personnage inconnu : " + type);
                yield null;
            }
        };
    }
}