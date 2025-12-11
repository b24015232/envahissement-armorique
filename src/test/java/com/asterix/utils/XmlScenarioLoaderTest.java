package com.asterix.utils;

import com.asterix.model.character.Character;
import com.asterix.model.character.Chief;
import com.asterix.model.place.*;
import com.asterix.model.simulation.InvasionTheater;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class XmlScenarioLoaderTest {

    @TempDir
    File tempDir;
    private File xmlFile;

    private static final String VALID_XML = """
            <theater>
                <name>Gallic Invasion</name>
                <place type="GaulVillage">
                    <name>Village Gaulois</name>
                    <area>50.0</area>
                    <chief>
                        <name>Abraracourcix</name>
                        <sex>MALE</sex>
                        <age>45</age>
                    </chief>
                    <character type="Druid">
                        <name>Panoramix</name>
                        <id>1</id>
                        <age>90</age>
                        <height>1.65</height>
                        <strength>5.0</strength>
                        <stamina>20.0</stamina>
                        <gender>MALE</gender>
                    </character>
                </place>
                <place type="Battlefield">
                    <name>Plaine Romaine</name>
                    <area>200.0</area>
                    <character type="Legionnaire">
                        <name>Caius</name>
                        <id>2</id>
                        <age>30</age>
                        <height>1.80</height>
                        <strength>15.0</strength>
                        <stamina>10.0</stamina>
                        <gender>MALE</gender>
                    </character>
                </place>
            </theater>
            """;

    private static final String INVALID_STATS_XML = """
            <theater>
                <name>Invalid Stats Test</name>
                <place type="Battlefield">
                    <name>Error Field</name>
                    <area>10.0</area>
                    <character type="Legionnaire">
                        <name>Error Man</name>
                        <id>3</id>
                        <age>N/A</age> 
                        <height>1.80</height>
                        <strength>15.0</strength>
                        <stamina>10.0</stamina>
                        <gender>MALE</gender>
                    </character>
                </place>
            </theater>
            """;

    @BeforeEach
    void setup() throws IOException {
        xmlFile = new File(tempDir, "scenario.xml");
    }

    private void writeXmlToFile(String content) throws IOException {
        try (FileWriter writer = new FileWriter(xmlFile)) {
            writer.write(content);
        }
    }

    @Test
    void loadTheaterFromFile_ShouldLoadTheaterAndPlacesCorrectly() throws Exception {
        writeXmlToFile(VALID_XML);

        InvasionTheater theater = XmlScenarioLoader.loadTheaterFromFile(xmlFile);

        assertNotNull(theater);
        assertEquals("Gallic Invasion", theater.getName());
        assertEquals(2, theater.getPlaces().size());

        Place village = theater.getPlaces().stream().filter(p -> p.getName().equals("Village Gaulois")).findFirst().orElse(null);
        Place battlefield = theater.getPlaces().stream().filter(p -> p.getName().equals("Plaine Romaine")).findFirst().orElse(null);

        assertNotNull(village);
        assertInstanceOf(GaulVillage.class, village);
        assertEquals(50.0, village.getArea());
        assertEquals(1, village.getCharacters().size());

        Chief chief = ((GaulVillage) village).getChief();
        assertNotNull(chief);
        assertEquals("Abraracourcix", chief.getName());
        assertSame(village, chief.getLocation());

        assertNotNull(battlefield);
        assertInstanceOf(Battlefield.class, battlefield);
        assertEquals(200.0, battlefield.getArea());
        assertEquals(1, battlefield.getCharacters().size());
    }

    @Test
    void loadTheaterFromFile_ShouldHandleMissingTagsAndUseDefaultValues() throws Exception {
        writeXmlToFile(INVALID_STATS_XML);

        InvasionTheater theater = XmlScenarioLoader.loadTheaterFromFile(xmlFile);

        assertNotNull(theater);
        assertEquals("Invalid Stats Test", theater.getName());
        Place battlefield = theater.getPlaces().get(0);

        assertEquals(1, battlefield.getCharacters().size());
    }

    @Test
    void loadTheaterFromFile_ShouldThrowExceptionOnInvalidXmlStructure() throws Exception {
        writeXmlToFile("<theater><name>Invalid XML");


        assertThrows(Exception.class, () -> XmlScenarioLoader.loadTheaterFromFile(xmlFile));
    }

    @Test
    void loadTheaterFromFile_ShouldHandleUnknownCharacterTypeAndSkipIt() throws Exception {
        String unknownCharXml = VALID_XML.replace("Druid", "UnknownType").replace("Legionnaire", "UnknownType");
        writeXmlToFile(unknownCharXml);

        InvasionTheater theater = XmlScenarioLoader.loadTheaterFromFile(xmlFile);

        assertEquals(0, theater.getPlaces().get(0).getCharacters().size());
        assertEquals(0, theater.getPlaces().get(1).getCharacters().size());
    }

    @Test
    void loadTheaterFromFile_ShouldHandleUnknownPlaceTypeAndDefaultToGaulVillageOrBattlefield() throws Exception {
        String unknownPlaceXml = VALID_XML.replace("type=\"GaulVillage\"", "type=\"UnknownVillage\"");
        writeXmlToFile(unknownPlaceXml);

        InvasionTheater theater = XmlScenarioLoader.loadTheaterFromFile(xmlFile);

        Place unknownPlace = theater.getPlaces().stream().filter(p -> p.getName().equals("Village Gaulois")).findFirst().orElse(null);
        assertNotNull(unknownPlace);
        assertInstanceOf(GaulVillage.class, unknownPlace);
    }

    @Test
    void loadTheater_ShouldHandleSimpleFilePathCall() throws Exception {
        writeXmlToFile(VALID_XML);
        InvasionTheater theater = XmlScenarioLoader.loadTheater(xmlFile.getAbsolutePath());
        assertNotNull(theater);
        assertEquals("Gallic Invasion", theater.getName());
    }
}