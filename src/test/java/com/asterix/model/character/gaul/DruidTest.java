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
 * </p>
 */
public class DruidTest {

    /**
     * Verifies the specific abilities of the Druid, particularly potion concoction
     * and polymorphism across multiple interfaces.
     * Uses a custom character "Nevotix" to represent the academic druid.
     */
    @Test
    public void testNevotixCapabilities() {
        // Arrange : creating Nevotix
        Druid nevotix = new Druid("Nevotix", 45, 1.70, 5.0, 10.0, Gender.MALE);

        // Act & assert 1: Specific method
        assertDoesNotThrow(() -> nevotix.concoctPotion(), "Nevotix should be able to concoct potion");

        // Act & assert 2: polymorphism checks
        // This confirms the druid is a "Swiss army knife"
        assertTrue(nevotix instanceof Worker, "Druid must be a worker");
        assertTrue(nevotix instanceof Fighter, "Druid must be a fighter");
        assertTrue(nevotix instanceof Leader, "Druid must be a leader");

        // Act & assert 3: Check toString() (MISSING PART FIX -> 100% Coverage)
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

        //simulating a fight against himself for non-null testing
        assertDoesNotThrow(() -> nevotix.fight(nevotix));
    }
}