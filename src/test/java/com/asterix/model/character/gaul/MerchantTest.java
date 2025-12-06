package com.asterix.model.character.gaul;

import com.asterix.model.ability.Worker;
import com.asterix.model.character.Gender;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the merchant class
 */
public class MerchantTest {

    @Test
    public void testMerchantInitialization() {
        // Arrange
        Merchant merchant = new Merchant("Dorix", 45, 1.80, 10.0, 10.0, Gender.FEMALE);

        // Assert
        assertEquals("Dorix", merchant.getName());
        assertEquals(100.0, merchant.getHealth());
        assertTrue(merchant.toString().contains("Merchant"));
    }

    @Test
    public void testWorkerCapability() {
        // Arrange
        Merchant merchant = new Merchant("Dorix", 45, 1.80, 10.0, 10.0, Gender.FEMALE);

        // Assert
        assertTrue(merchant instanceof Worker, "Merchant must implement the worker interface");
        assertDoesNotThrow(() -> merchant.work());
    }
}