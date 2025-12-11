package com.asterix.model.character.gaul;

import com.asterix.model.ability.Fighter;
import com.asterix.model.ability.Leader;
import com.asterix.model.ability.Worker;
import com.asterix.model.character.Character;
import com.asterix.model.character.Gender;
import com.asterix.model.item.Cauldron;
import com.asterix.model.item.FoodType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Druid} class.
 */
public class DruidTest {


    private static Druid createInstantiableDruid(String name, double initialHealth) {
        return new Druid(name, 45, 1.70, 5.0, 10.0, Gender.MALE) {
            private double currentHealth = initialHealth;

            @Override
            public double getHealth() {
                return currentHealth;
            }

            @Override
            public String toString() {
                return String.format(
                        "Druid | %-15s | %-6s | Age: %-3d | 📏 %.2fm | ❤️ HP: %-5.1f | 🍖 Hunger: %-5.1f | 💪 Str: %-5.1f | 🏃 Sta: %-5.1f | 🧪 Potion: %.1f",
                        this.getName(),
                        this.getGender().toString(),
                        this.getAge(),
                        this.getHeight(),
                        this.getHealth(),
                        this.getHunger(),
                        this.getStrength(),
                        this.getStamina(),
                        this.getPotionLevel()
                );
            }
        };
    }

    private static Gaul createInstantiableGaul(String name) {
        return new Gaul(name, 35, 1.60, 15.0, 20.0, Gender.MALE) {
            @Override
            public double getHealth() { return Character.MAX_HEALTH; }
        };
    }

    /**
     * Verifies the specific abilities of the Druid, particularly potion concoction
     * and polymorphism across multiple interfaces.
     */
    @Test
    public void testDruidCapabilities() {
        Druid nevotix = createInstantiableDruid("Nevotix", 100.0);

        assertDoesNotThrow(() -> nevotix.concoctPotion(), "Nevotix should be able to concoct potion");

        assertInstanceOf(Worker.class, nevotix, "Druid must be a worker");
        assertInstanceOf(Fighter.class, nevotix, "Druid must be a fighter");
        assertInstanceOf(Leader.class, nevotix, "Druid must be a leader");

        assertTrue(nevotix.toString().contains("Druid"), "ToString should work and contain class name (if implemented).");
        assertTrue(nevotix.toString().contains("Age: 45"), "ToString must contain the age value.");
    }

    /**
     * Ensures that the potion brewing process is initiated and basic ingredients are added.
     */
    @Test
    public void testConcoctPotionInitialization() {
        Druid nevotix = createInstantiableDruid("Nevotix", 100.0);

        nevotix.concoctPotion();
        Cauldron cauldron = nevotix.getCauldron();

        assertNotNull(cauldron, "Cauldron must be initialized after concoctPotion.");

        assertTrue(cauldron.getIngredients().size() >= 4, "Cauldron must contain at least 4 ingredients (3 base + 1 random).");

        assertTrue(cauldron.getIngredients().stream()
                        .anyMatch(f -> f.getName().contains("Mistletoe")),
                "Cauldron must contain Mistletoe ingredient.");
    }


    /**
     * Ensures that all action methods (work, command, fight) execute without errors.
     */
    @Test
    public void testDruidActions() {
        Druid nevotix = createInstantiableDruid("Nevotix", 100.0);

        assertDoesNotThrow(() -> nevotix.work());
        assertDoesNotThrow(() -> nevotix.command());

        assertDoesNotThrow(() -> nevotix.fight(nevotix));
    }

    /**
     * Verifies that the Druid can successfully serve potion to another Gaul.
     */
    @Test
    public void testServePotionSuccess() {
        // Arrange
        Druid nevotix = createInstantiableDruid("Nevotix", 100.0);
        Gaul asterix = createInstantiableGaul("Asterix");

        double initialPotionLevel = asterix.getPotionLevel();

        // Act
        nevotix.concoctPotion();   // 1. Brew the potion
        nevotix.servePotion(asterix); // 2. Serve a ladle

        // Assert: If the base class Gaul works, the potion level should have increased by 1.0.
        assertEquals(initialPotionLevel + 1.0, asterix.getPotionLevel(), 0.0001,
                "The Gaul should have received exactly one dose of potion");
    }

    /**
     * Verifies that serving potion fails (does nothing) if the cauldron has not been prepared.
     */
    @Test
    public void testServePotionWithoutBrewing() {
        // Arrange
        Druid nevotix = createInstantiableDruid("Nevotix", 100.0);
        Gaul clarix = createInstantiableGaul("Clarix");

        double initialPotionLevel = clarix.getPotionLevel();

        // Act: Try to serve WITHOUT calling concoctPotion() first
        nevotix.servePotion(clarix);

        // Assert
        assertEquals(initialPotionLevel, clarix.getPotionLevel(), 0.0001,
                "Potion level should not change if the cauldron is empty or not created");
    }
}