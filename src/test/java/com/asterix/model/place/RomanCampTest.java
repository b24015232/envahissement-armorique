package com.asterix.model.place;

import com.asterix.model.character.Chief;
import com.asterix.model.character.Gender;
import com.asterix.model.character.creature.Lycanthrope;
import com.asterix.model.character.gaul.BlackSmith;
import com.asterix.model.character.roman.Legionnaire;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link RomanCamp}.
 * <p>
 * This test suite focuses on verifying the core constraints of the
 * {@code RomanCamp} concerning character placement, ensuring it
 * correctly allows Romans and Creatures while strictly rejecting Gauls.
 */
class RomanCampTest {

    /**
     * A minimal test implementation of {@link Chief} used as a stub
     * for the {@code RomanCamp} constructor, as a camp must have a chief (Centurion or similar).
     */
    private static class TestChief extends Chief {
        public TestChief(String name, Place place) {
            super(name, "UNKNOWN", 0, place);
        }
    }

    /**
     * Verifies that the {@code RomanCamp} successfully accepts
     * both Roman characters (e.g., {@code Legionnaire}) and Creature
     * characters (e.g., {@code Lycanthrope}) without throwing an exception.
     */
    @Test
    void romanCampShouldAllowRomansAndCreatures() {
        TestChief chiefStub = new TestChief("Caius Bonus", null);
        RomanCamp camp = new RomanCamp("Babaorum", 80.0, chiefStub);

        // Link the chief to the newly created camp
        chiefStub.setLocation(camp);

        Legionnaire roman = new Legionnaire("Fortus", 30, 1.80, 18.0, 12.0, Gender.MALE);
        Lycanthrope creature = new Lycanthrope("Lupus", 25, 1.85, 22.0, 10.0, Gender.MALE);

        assertDoesNotThrow(() -> camp.addCharacter(roman));
        assertDoesNotThrow(() -> camp.addCharacter(creature));
    }

    /**
     * Verifies that the {@code RomanCamp} rejects characters
     * categorized as Gauls (e.g., {@code BlackSmith}) by throwing
     * an {@code IllegalArgumentException}.
     */
    @Test
    void romanCampShouldRejectGauls() {
        TestChief chiefStub = new TestChief("Caius Bonus", null);

        RomanCamp camp = new RomanCamp("Babaorum", 80.0, chiefStub);

        // Link the chief to the newly created camp
        chiefStub.setLocation(camp);

        BlackSmith gaul = new BlackSmith("Astérix", 35, 1.70, 20.0, 15.0, Gender.MALE);

        assertThrows(IllegalArgumentException.class, () -> camp.addCharacter(gaul));
    }
}