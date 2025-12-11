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
     */
    @Test
    public void testDruidCapabilities() {
        // Arrange : creating Nevotix with anonymous class to handle abstract methods
        Druid nevotix = new Druid("Nevotix", 45, 1.70, 5.0, 10.0, Gender.MALE) {
            @Override
            public double getHealth() {
                return this.health;
            }
        };

        // Act & assert 1: Specific method
        assertDoesNotThrow(() -> nevotix.concoctPotion(), "Nevotix should be able to concoct potion");

        // Act & assert 2: polymorphism checks
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
        // Fix: Implement abstract method getHealth()
        Druid nevotix = new Druid("Nevotix", 45, 1.70, 5.0, 10.0, Gender.MALE) {
            @Override
            public double getHealth() {
                return this.health;
            }
        };

        // checking that methods execute without error
        assertDoesNotThrow(() -> nevotix.work());
        assertDoesNotThrow(() -> nevotix.command());

        // simulating a fight against himself for non-null testing
        assertDoesNotThrow(() -> nevotix.fight(nevotix));
    }

    /**
     * Verifies that the Druid can successfully serve potion to another Gaul.
     */
    @Test
    public void testServePotionSuccess() {
        // Arrange
        Druid nevotix = new Druid("Nevotix", 45, 1.70, 5.0, 10.0, Gender.MALE) {
            @Override
            public double getHealth() { return this.health; }
        };

        // We use an anonymous Gaul instead of Merchant
        Gaul asterix = new Gaul("Asterix", 35, 1.60, 15.0, 20.0, Gender.MALE) {
            @Override
            public double getHealth() { return this.health; }
        };

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
        Druid nevotix = new Druid("Nevotix", 45, 1.70, 5.0, 10.0, Gender.MALE) {
            @Override
            public double getHealth() { return this.health; }
        };

        Gaul clarix = new Gaul("Clarix", 35, 1.90, 100.0, 50.0, Gender.FEMALE) {
            @Override
            public double getHealth() { return this.health; }
        };

        double initialPotionLevel = clarix.getPotionLevel();

        // Act: Try to serve WITHOUT calling concoctPotion() first
        nevotix.servePotion(clarix);

        // Assert
        assertEquals(initialPotionLevel, clarix.getPotionLevel(), 0.0001,
                "Potion level should not change if the cauldron is empty or not created");
    }
}