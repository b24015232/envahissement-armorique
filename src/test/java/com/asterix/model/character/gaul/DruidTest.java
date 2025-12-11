package com.asterix.model.character.gaul;

import com.asterix.model.ability.Fighter;
import com.asterix.model.ability.Leader;
import com.asterix.model.ability.Worker;
import com.asterix.model.character.Gender;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Druid} class.
 * <p>
 * This class validates the complex behavior of the Druid, who is a versatile character
 * implementing three interfaces: {@link Worker}, {@link Fighter}, and {@link Leader}.
 * It also tests the unique ability to brew and serve magic potion.
 * </p>
 */
public class DruidTest {

    /**
     * Verifies the specific abilities of the Druid, particularly potion concoction
     * and polymorphism across multiple interfaces.
     * Uses a custom character "Nevotix" to represent the academic druid.
     */
    @Test
    public void testDruidCapabilities() {
        // Arrange : creating Nevotix
        Druid nevotix = new Druid("Nevotix", 45, 1.70, 5.0, 10.0, Gender.MALE);

        // Act & assert 1: Specific method
        // This validates that the brewing process (using Cauldron) runs without error
        assertDoesNotThrow(() -> nevotix.concoctPotion(), "Nevotix should be able to concoct potion");

        // Act & assert 2: polymorphism checks
        // This confirms the druid is a "Swiss army knife"
        assertTrue(nevotix instanceof Worker, "Druid must be a worker");
        assertTrue(nevotix instanceof Fighter, "Druid must be a fighter");
        assertTrue(nevotix instanceof Leader, "Druid must be a leader");

        // Act & assert 3: Check toString()
        assertTrue(nevotix.toString().contains("Druid"), "ToString should work and contain class name");
    }

    /**
     * Ensures that all action methods (work, command, fight) execute without errors.
     */
    @Test
    public void testDruidActions() {
        Druid nevotix = new Druid("Nevotix", 45, 1.70, 5.0, 10.0, Gender.MALE);

        // checking that methods execute without error
        assertDoesNotThrow(() -> nevotix.work());
        assertDoesNotThrow(() -> nevotix.command());

        // simulating a fight against himself for non-null testing
        assertDoesNotThrow(() -> nevotix.fight(nevotix));
    }

    /**
     * Verifies that the Druid can successfully serve potion to another Gaul.
     * <p>
     * Scenario:
     * 1. Nevotix brews the potion (concoctPotion).
     * 2. Nevotix serves Asterix.
     * 3. Asterix's potion level increases by exactly 1.0.
     * </p>
     */
    @Test
    public void testServePotionSuccess() {
        // Arrange
        Druid nevotix = new Druid("Nevotix", 45, 1.70, 5.0, 10.0, Gender.MALE);
        // We use a Merchant as a generic Gaul for testing the receiver
        Gaul asterix = new Merchant("Asterix", 35, 1.60, 15.0, 20.0, Gender.MALE);

        double initialPotionLevel = asterix.getPotionLevel();

        // Act
        nevotix.concoctPotion();   // 1. Brew the potion
        nevotix.servePotion(asterix); // 2. Serve a ladle

        // Assert
        assertEquals(initialPotionLevel + 1.0, asterix.getPotionLevel(), 0.0001,
                "The Gaul should have received exactly one dose of potion");
    }

    /**
     * Verifies that serving potion fails (does nothing) if the cauldron has not been prepared.
     */
    @Test
    public void testServePotionWithoutBrewing() {
        // Arrange
        Druid nevotix = new Druid("Nevotix", 45, 1.70, 5.0, 10.0, Gender.MALE);
        Gaul obelix = new Merchant("Obelix", 35, 1.90, 100.0, 50.0, Gender.MALE);

        double initialPotionLevel = obelix.getPotionLevel();

        // Act: Try to serve WITHOUT calling concoctPotion() first
        nevotix.servePotion(obelix);

        // Assert
        assertEquals(initialPotionLevel, obelix.getPotionLevel(), 0.0001,
                "Potion level should not change if the cauldron is empty or not created");
    }
}