package com.asterix.model.character.gaul;

import com.asterix.model.ability.Fighter;
import com.asterix.model.ability.Leader;
import com.asterix.model.ability.Worker;
import com.asterix.model.character.Gender;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DruidTest {

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
    }

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