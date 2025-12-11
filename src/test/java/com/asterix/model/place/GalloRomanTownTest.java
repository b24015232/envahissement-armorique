package com.asterix.model.place;

import com.asterix.model.character.Chief;
import com.asterix.model.character.Gender;
import com.asterix.model.character.creature.Lycanthrope;
import com.asterix.model.character.gaul.BlackSmith;
import com.asterix.model.character.roman.Legionnaire;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link GalloRomanTown}.
 * <p>
 * This test suite focuses on verifying the core constraints of the
 * {@code GalloRomanTown} concerning character placement, ensuring it
 * correctly allows Gauls and Romans while rejecting Creatures.
 */
class GalloRomanTownTest {

    /**
     * A minimal test implementation of {@link Chief} used as a stub
     * for the {@code GalloRomanTown} constructor, as a town must have a chief.
     */
    private static class TestChief extends Chief {
        public TestChief(String name, Place place) {
            super(name, "UNKNOWN", 0, place);
        }
    }


    /**
     * Verifies that the {@code GalloRomanTown} successfully accepts
     * both Gaulish characters (e.g., {@code BlackSmith}) and Roman
     * characters (e.g., {@code Legionnaire}) without throwing an exception.
     */
    @Test
    void galloRomanTownShouldAllowGaulsAndRomans() {
        TestChief chiefStub = new TestChief("Abraracourcix", null);

        GalloRomanTown town = new GalloRomanTown("Lutèce", 90.0, chiefStub);

        // Link the chief to the newly created town
        chiefStub.setLocation(town);

        BlackSmith gaul = new BlackSmith("Astérix", 35, 1.70, 20.0, 15.0, Gender.MALE);
        Legionnaire roman = new Legionnaire("Fortus", 30, 1.80, 18.0, 12.0, Gender.MALE);

        assertDoesNotThrow(() -> town.addCharacter(gaul));
        assertDoesNotThrow(() -> town.addCharacter(roman));
    }

    /**
     * Verifies that the {@code GalloRomanTown} rejects characters
     * categorized as Creatures (e.g., {@code Lycanthrope}) by throwing
     * an {@code IllegalArgumentException}.
     */
    @Test
    void galloRomanTownShouldRejectCreatures() {
        TestChief chiefStub = new TestChief("Abraracourcix", null);

        GalloRomanTown town = new GalloRomanTown("Lutèce", 90.0, chiefStub);

        // Link the chief to the newly created town
        chiefStub.setLocation(town);

        Lycanthrope creature = new Lycanthrope("Lupus", 25, 1.85, 22.0, 10.0, Gender.MALE);

        assertThrows(IllegalArgumentException.class, () -> town.addCharacter(creature));
    }
}