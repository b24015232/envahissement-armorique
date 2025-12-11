package com.asterix.model.character.creature;

import com.asterix.model.character.Gender;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link Creature} abstract base class.
 */
class CreatureTest {

    /**
     * Simple concrete implementation of Creature for testing purposes.
     */
    private static class TestCreature extends Creature {

        TestCreature(String name,
                     int age,
                     double height,
                     double strength,
                     double stamina,
                     Gender gender) {
            super(name, age, height, strength, stamina, gender);
        }

        @Override
        public String toString() {
            return super.toString();
        }

        /**
         * @return
         */
        @Override
        public double getHealth() {
            return 0;
        }
    }

    @Test
    void creatureShouldInitializeWithNonFeralState() {
        TestCreature creature = new TestCreature("Beast", 10, 1.5, 20.0, 10.0, Gender.MALE);

        assertFalse(creature.isFeral());
    }

    @Test
    void creatureShouldEnterAndLeaveFeralState() {
        TestCreature creature = new TestCreature("Beast", 10, 1.5, 20.0, 10.0, Gender.MALE);

        creature.enterFeralState();
        assertTrue(creature.isFeral());

        creature.leaveFeralState();
        assertFalse(creature.isFeral());
    }

    @Test
    void creatureToStringShouldContainNameAndFeralFlag() {
        TestCreature creature = new TestCreature("Beast", 10, 1.5, 20.0, 10.0, Gender.MALE);

        String s = creature.toString();

        assertTrue(s.contains("Beast"));
        assertTrue(s.contains("Feral"));
    }
}
