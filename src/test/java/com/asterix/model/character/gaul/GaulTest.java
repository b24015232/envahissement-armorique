package com.asterix.model.character.gaul;

import com.asterix.model.character.Gender;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the gaul class
 * Also validates the logic of the abstract character class
 */
public class GaulTest {

    @Test
    public void testGaulInitialization() {
        // creating Casalix's gaul version
        String name = "Casalix";
        int age = 40;
        double height = 1.75;
        double strength = 69.0;
        double stamina = 100.0;
        Gender gender = Gender.MALE;

        Gaul asterix = new Gaul(name, age, height, strength, stamina, gender);

        // assert check heritage
        assertEquals("Casalix", asterix.getName(), "Name should be initialized correctly");
        assertEquals(100.0, asterix.getHealth(), "Health should default to 100.0");
        assertNotNull(asterix.toString(), "ToString should not return null");
    }

    @Test
    public void testDrinkPotion() {
        // Arrange
        Gaul obelix = new Gaul("Balerdix", 26, 1.87, 150.0, 200.0, Gender.MALE);

        // Act
        obelix.drinkPotion(10.0);
    }
}