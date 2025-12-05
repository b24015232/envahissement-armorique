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

        // correcting : variable renamed 'casalix' to be consistent
        Gaul casalix = new Gaul(name, age, height, strength, stamina, gender);

        // assert check heritage
        assertEquals("Casalix", casalix.getName(), "Name should be initialized correctly");
        assertEquals(100.0, casalix.getHealth(), "Health should default to 100.0");
        assertNotNull(casalix.toString(), "ToString should not return null");
    }

    @Test
    public void testDrinkPotion() {
        // Arrange
        // correcting : variable renamed 'balerdix' to be consistent
        Gaul balerdix = new Gaul("Balerdix", 26, 1.87, 150.0, 200.0, Gender.MALE);
        double initialPotion = balerdix.getPotionLevel();

        // Act
        balerdix.drinkPotion(10.0);

        // Assert
        assertEquals(initialPotion + 10.0, balerdix.getPotionLevel(), "Potion level should increase correctly");
    }
}