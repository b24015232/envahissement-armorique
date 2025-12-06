package com.asterix.model.character.gaul;

import com.asterix.model.ability.Worker;
import com.asterix.model.character.Gender;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InnkeeperTest {

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