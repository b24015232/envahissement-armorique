package com.asterix.model.place;

import com.asterix.model.character.Chief;
import com.asterix.model.character.Gender;
import com.asterix.model.character.creature.Lycanthrope;
import com.asterix.model.character.gaul.BlackSmith;
import com.asterix.model.character.roman.Legionnaire;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link GaulVillage}.
 * <p>
 * This test suite focuses on verifying the core constraints of the
 * {@code GaulVillage} concerning character placement, ensuring it
 * correctly allows Gauls and Creatures while rejecting Romans.
 */
class GaulVillageTest {


    /**
     * A minimal test implementation of {@link Chief} used as a stub
     * for the {@code GaulVillage} constructor, as a village must have a chief.
     */
    private static class TestChief extends Chief {
        public TestChief(String name, Place place) {
            super(name, "UNKNOWN", 0, place);
        }
    }

    /**
     * Verifies that the {@code GaulVillage} successfully accepts
     * both Gaulish characters (e.g., {@code BlackSmith}) and Creature
     * characters (e.g., {@code Lycanthrope}) without throwing an exception.
     */
    @Test
    void gaulVillageShouldAllowGaulsAndCreatures() {
        TestChief chiefStub = new TestChief("Abraracourcix", null);

        GaulVillage village = new GaulVillage("Village gaulois", 50.0, chiefStub);

        // Link the chief to the newly created village
        chiefStub.setLocation(village);

        BlackSmith gaul = new BlackSmith("Astérix", 35, 1.70, 20.0, 15.0, Gender.MALE);
        Lycanthrope creature = new Lycanthrope("Lupus", 25, 1.85, 22.0, 10.0, Gender.MALE);

        assertDoesNotThrow(() -> village.addCharacter(gaul));
        assertDoesNotThrow(() -> village.addCharacter(creature));
    }

    /**
     * Verifies that the {@code GaulVillage} rejects characters
     * categorized as Romans (e.g., {@code Legionnaire}) by throwing
     * an {@code IllegalArgumentException}.
     */
    @Test
    void gaulVillageShouldRejectRomans() {
        TestChief chiefStub = new TestChief("Abraracourcix", null);

        GaulVillage village = new GaulVillage("Village gaulois", 50.0, chiefStub);

        // Link the chief to the newly created village
        chiefStub.setLocation(village);

        Legionnaire roman = new Legionnaire("Fortus", 30, 1.80, 18.0, 12.0, Gender.MALE);

        assertThrows(IllegalArgumentException.class, () -> village.addCharacter(roman));
    }
}