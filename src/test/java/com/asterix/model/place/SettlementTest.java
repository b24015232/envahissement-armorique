package com.asterix.model.place;

import com.asterix.model.character.Chief;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for common behavior defined in the abstract class {@link Settlement}.
 * <p>
 * This test suite uses a concrete subclass, {@link GaulVillage}, to test
 * shared functionalities, particularly those related to the management
 * and referencing of the {@link Chief} character.
 */
class SettlementTest {

    /**
     * A minimal test implementation of {@link Chief} used as a stub
     * for the {@code Settlement} constructor.
     */
    private static class TestChief extends Chief {
        public TestChief(String name, Place place) {
            super(name, "UNKNOWN", 0, place);
        }
    }


    /**
     * Verifies that the {@code getChief()} method returns the Chief
     * passed during construction and that {@code setChief()} correctly
     * updates the reference to a new Chief instance.
     */
    @Test
    void setChiefAndGetChiefShouldStoreReference() {
        TestChief initialChief = new TestChief("Initial", null);

        // Use GaulVillage as a concrete Settlement subclass for testing
        Settlement village = new GaulVillage("Gaul Village", 50.0, initialChief);

        assertSame(initialChief, village.getChief(), "The initial Chief should be the one passed to the constructor.");

        Chief newChief = new Chief("Abraracourcix", "MALE", 45, village);

        village.setChief(newChief);

        assertSame(newChief, village.getChief(), "The new Chief should be stored.");
    }

    /**
     * Tests that the {@code examine()} method, which is often used for
     * displaying information about the location, executes successfully
     * both when a Chief is defined and when the Chief is {@code null}.
     */
    @Test
    void examineShouldWorkWithoutChiefAndWithChief() {
        TestChief chiefStub = new TestChief("Stub", null);
        Settlement village = new GaulVillage("Gaul Village", 50.0, chiefStub);
        chiefStub.setLocation(village);

        // Test with Chief present
        assertDoesNotThrow(village::examine, "examine should not throw an exception when a chief is defined.");

        // Test with Chief set to null
        village.setChief(null);

        assertDoesNotThrow(village::examine, "examine should not throw an exception when the Chief is null.");
    }
}
