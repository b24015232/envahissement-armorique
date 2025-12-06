package com.asterix.model.character.gaul;

import com.asterix.model.ability.Worker;
import com.asterix.model.character.Gender;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Merchant} class.
 * <p>
 * This class validates the specific behavior of the Merchant character,
 * ensuring correct inheritance from Gaul and implementation of the Worker interface.
 * </p>
 */
public class MerchantTest {

    /**
     * Verifies that a Merchant is correctly initialized with the provided attributes.
     * <p>
     * Tests the constructor using a female merchant "Dorix" and checks
     * standard attributes like name and health.
     * </p>
     */
    @Test
    public void testMerchantInitialization() {
        // Arrange
        Merchant merchant = new Merchant("Dorix", 45, 1.80, 10.0, 10.0, Gender.FEMALE);

        // Assert
        assertEquals("Dorix", merchant.getName());
        assertEquals(100.0, merchant.getHealth());
        assertTrue(merchant.toString().contains("Merchant"));
    }

    /**
     * Verifies the {@link Worker} capability of the Merchant.
     * <p>
     * Ensures that the Merchant class correctly implements the interface
     * and that the work method executes without exceptions.
     * </p>
     */
    @Test
    public void testWorkerCapability() {
        // Arrange
        Merchant merchant = new Merchant("Dorix", 45, 1.80, 10.0, 10.0, Gender.FEMALE);

        // Assert
        assertTrue(merchant instanceof Worker, "Merchant must implement the worker interface");
        assertDoesNotThrow(() -> merchant.work());
    }
}