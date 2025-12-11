package com.asterix.model.place;

import com.asterix.model.character.Chief;
import com.asterix.model.character.Gender;
import com.asterix.model.character.creature.Lycanthrope;
import com.asterix.model.character.gaul.BlackSmith;
import com.asterix.model.character.roman.Legionnaire;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link RomanCity}.
 * <p>
 * This test suite focuses on verifying the core constraints of the
 * {@code RomanCity} concerning character placement, ensuring it
 * correctly allows Romans and Creatures while strictly rejecting Gauls.
 */
class RomanCityTest {

    /**
     * A minimal test implementation of {@link Chief} used as a stub
     * for the {@code RomanCity} constructor, as a city requires a chief
     * (e.g., a Governor or high-ranking official).
     */
    private static class TestChief extends Chief {
        public TestChief(String name, Place place) {
            super(name, "UNKNOWN", 0, place);
        }
    }

    /**
     * Verifies that the {@code RomanCity} successfully accepts
     * both Roman characters (e.g., {@code Legionnaire}) and Creature
     * characters (e.g., {@code Lycanthrope}) without throwing an exception.
     */
    @Test
    void romanCityShouldAllowRomansAndCreatures() {
        TestChief chiefStub = new TestChief("Pontius Pilatus", null);

        RomanCity city = new RomanCity("Condatum", 120.0, chiefStub);

        // Link the chief to the newly created city
        chiefStub.setLocation(city);

        Legionnaire roman = new Legionnaire("Fortus", 30, 1.80, 18.0, 12.0, Gender.MALE);
        Lycanthrope creature = new Lycanthrope("Lupus", 25, 1.85, 22.0, 10.0, Gender.MALE);

        assertDoesNotThrow(() -> city.addCharacter(roman));
        assertDoesNotThrow(() -> city.addCharacter(creature));
    }

    /**
     * Verifies that the {@code RomanCity} rejects characters
     * categorized as Gauls (e.g., {@code BlackSmith}) by throwing
     * an {@code IllegalArgumentException}.
     */
    @Test
    void romanCityShouldRejectGauls() {
        TestChief chiefStub = new TestChief("Pontius Pilatus", null);

        RomanCity city = new RomanCity("Condatum", 120.0, chiefStub);

        // Link the chief to the newly created city
        chiefStub.setLocation(city);

        BlackSmith gaul = new BlackSmith("Astérix", 35, 1.70, 20.0, 15.0, Gender.MALE);

        assertThrows(IllegalArgumentException.class, () -> city.addCharacter(gaul));
    }
}