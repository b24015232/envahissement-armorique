package com.asterix.model.character.gaul;

import com.asterix.model.ability.Worker;
import com.asterix.model.character.Gender;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Innkeeper} class.
 * <p>
 * This class ensures that the Innkeeper inherits correctly from Gaul
 * and implements the {@link Worker} interface as expected.
 * </p>
 */
public class InnkeeperTest {

    /**
     * Verifies that an Innkeeper is correctly initialized with the provided attributes.
     * <p>
     * Uses a specific test case "Rullix" to verify inheritance of name, health,
     * and the correct string representation.
     * </p>
     */
    @Test
    public void testInnkeeperInitialization() {
        // Arrange : creating Rullix (keeps the inn instead the goals)
        Innkeeper innkeeper = new Innkeeper("Rullix", 33, 1.89, 12.0, 5.0, Gender.MALE);

        // Assert : basic attribute verification
        assertEquals("Rullix", innkeeper.getName());
        assertEquals(100.0, innkeeper.getHealth(), "Health should be inherited from Character and default to 100.0");

        // Assert: Identity check via toString
        assertTrue(innkeeper.toString().contains("Innkeeper"), "he toString method should identify the object as an Innkeeper");
    }

    /**
     * Verifies the {@link Worker} capability of the Innkeeper.
     * <p>
     * Checks polymorphism (Innkeeper IS-A Worker) and ensures the {@code work()}
     * method executes without runtime errors.
     * </p>
     */
    @Test
    public void testWorkerCapability() {
        // Arrange
        Innkeeper rullix = new Innkeeper("Rullix", 33, 1.89, 12.0, 5.0, Gender.MALE);

        // Assert : polymorphism check
        assertTrue(rullix instanceof Worker, "Innkeeper has to be a worker");

        // Assert : ability check, ensures the work() method runs without throwing any exceptions
        assertDoesNotThrow(() -> rullix.work());
    }
}