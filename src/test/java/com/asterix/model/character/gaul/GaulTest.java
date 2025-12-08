package com.asterix.model.character.gaul;

import com.asterix.model.character.Gender;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Gaul} class.
 * <p>
 * This class verifies the core logic shared by all Gaul characters,
 * including inheritance from the abstract Character class and specific Gaul behaviors
 * like drinking magic potion.
 * </p>
 */
public class GaulTest {

    /**
     * Verifies that a Gaul is correctly initialized with the provided attributes.
     * <p>
     * Since {@link Gaul} is an abstract class, we use an anonymous subclass
     * for instantiation in this test.
     * </p>
     */
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
        // Quality fix: Using an anonymous class {} because Gaul is abstract
        Gaul casalix = new Gaul(name, age, height, strength, stamina, gender) {};

        // assert check heritage
        assertEquals("Casalix", casalix.getName(), "Name should be initialized correctly");
        assertEquals(100.0, casalix.getHealth(), "Health should default to 100.0");
        assertNotNull(casalix.toString(), "ToString should not return null");
    }

    /**
     * Verifies the {@code drinkPotion} method logic.
     * Ensures that the potion level attribute increases correctly after consumption.
     * <p>
     * Covers both branches: plural doses and singular dose.
     * </p>
     */
    @Test
    public void testDrinkPotion() {
        // Arrange
        // correcting : variable renamed 'balerdix' to be consistent
        // Quality fix: Using an anonymous class {} because Gaul is abstract
        Gaul balerdix = new Gaul("Balerdix", 26, 1.87, 150.0, 200.0, Gender.MALE) {};
        double initialPotion = balerdix.getPotionLevel();

        // Act 1: Test plural (doses) -> Covers the "else" branch
        balerdix.drinkPotion(10.0);
        assertEquals(initialPotion + 10.0, balerdix.getPotionLevel(), "Potion level should increase correctly with 10 doses");

        // Act 2: Test singular (dose) -> Covers the "if (dose == 1.0)" branch for 100% Coverage
        balerdix.drinkPotion(1.0);
        assertEquals(initialPotion + 11.0, balerdix.getPotionLevel(), "Potion level should increase correctly with 1 dose");
    }
}