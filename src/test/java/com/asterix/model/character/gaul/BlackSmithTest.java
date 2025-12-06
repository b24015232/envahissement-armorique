package com.asterix.model.character.gaul;

import com.asterix.model.ability.Worker;
import com.asterix.model.character.Gender;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link BlackSmith} class.
 * <p>
 * This class validates the specific behavior of the Blacksmith
 * and ensures it correctly implements the {@link Worker} interface.
 * </p>
 */
public class BlackSmithTest {

    /**
     * Verifies that a BlackSmith is correctly initialized with the expected attributes.
     * Checks inheritance from the {@link Gaul} class using a custom character "Messix".
     */
    @Test
    public void testBlackSmithInitialization() {
        // Arrange : creating Messix
        String name = "Messix";
        int age = 38;
        double height = 1.70;
        double strength = 15.0; // Very strong
        double stamina = 12.0;
        Gender gender = Gender.MALE;

        // Act
        BlackSmith messix = new BlackSmith(name, age, height, strength, stamina, gender);

        // Assert : Check identity and inheritance from Character
        assertEquals("Messix", messix.getName(), "Name should be initialized correctly");
        assertEquals(100.0, messix.getHealth(), "Health should default to 100.0 (inherited from Character)");
        assertTrue(messix.toString().contains("BlackSmith"), "ToString should identify the class as BlackSmith");
    }

    /**
     * Verifies the polymorphism capability of the BlackSmith.
     * Ensures the instance is correctly recognized as a {@link Worker}.
     */
    @Test
    public void testWorkerCapability() {
        // Arrange
        BlackSmith smith = new BlackSmith("Messix", 38, 1.70, 15.0, 12.0, Gender.MALE);

        // Assert : Polymorphism check
        // This is critical for the project quality: ensuring BlackSmith IS-A Worker
        assertInstanceOf(Worker.class, smith, "BlackSmith must implement the Worker interface");
    }

    /**
     * Verifies that the {@code work()} method executes without throwing exceptions.
     */
    @Test
    public void testWorkMethod() {
        // Arrange
        BlackSmith smith = new BlackSmith("Messix", 38, 1.70, 15.0, 12.0, Gender.MALE);

        // Act & Assert
        // Since work() is void and prints to console, we ensure it runs without crashing.
        assertDoesNotThrow(() -> smith.work(), "The work method should execute without throwing exceptions");
    }
}