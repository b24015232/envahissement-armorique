package com.asterix.model.character.roman;

import com.asterix.model.character.Gender;
import com.asterix.model.item.Food;
import com.asterix.model.item.FoodType;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Roman-specific behavior defined in the abstract {@link Roman} class,
 * focusing particularly on eating rules (checking {@code romanCanEat} flags),
 * potion consumption, and the detailed string representation.
 */
class RomanTest {

    /**
     * Simple concrete {@code Roman} implementation used for testing.
     * Overrides {@code toString()} for detailed output verification.
     */
    private static class TestRoman extends Roman {

        TestRoman(String name,
                  int age,
                  double height,
                  double strength,
                  double stamina,
                  Gender gender) {
            super(name, age, height, strength, stamina, gender);
        }

        /**
         * Provides a formatted string containing all character details.
         *
         * @return A detailed string representation of the Roman character.
         */
        @Override
        public String toString() {
            // NOTE: Using Locale.ROOT for formatting ensures consistency regardless of system locale.
            return String.format(Locale.ROOT,
                    "%-15s | %-6s | Age: %-3d | 📏 %.2fm | ❤️ HP: %-5.1f | 🍖 Hunger: %-5.1f | 💪 Str: %-5.1f | 🏃 Sta: %-5.1f | 🧪 Potion: %.1f",
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
    }

    /**
     * Tests that the {@code eat()} method safely ignores a {@code null}
     * food object, resulting in no change to the Roman's health or hunger.
     */
    @Test
    void romanShouldIgnoreNullFood() {
        TestRoman roman = new TestRoman("Nullius", 30, 1.8, 15, 10, Gender.MALE);
        double initialHealth = roman.getHealth();
        double initialHunger = roman.getHunger();
        // branch: if (food == null) { return; }
        roman.eat(null);
        assertEquals(initialHealth, roman.getHealth(), 0.0001);
        assertEquals(initialHunger, roman.getHunger(), 0.0001);
    }

    /**
     * Tests that the {@code eat()} method prevents the Roman from consuming
     * food explicitly forbidden for them (e.g., WILDBOAR, where
     * {@code romanCanEat} is false), resulting in no change to health or hunger.
     */
    @Test
    void romanShouldNotEatFoodForbiddenForRomans() {
        TestRoman roman = new TestRoman("Brutus", 30, 1.8, 15, 10, Gender.MALE);
        double initialHealth = roman.getHealth();
        double initialHunger = roman.getHunger();
        // WILDBOAR is configured with romanCanEat = false in FoodType
        Food boar = FoodType.WILDBOAR.create();
        // branch: if (!food.canBeEatenByRoman()) { ... return; }
        roman.eat(boar);
        // Health and hunger should not change because Romans cannot eat wildboar
        assertEquals(initialHealth, roman.getHealth(), 0.0001);
        assertEquals(initialHunger, roman.getHunger(), 0.0001);
    }

    /**
     * Tests the successful consumption of an allowed food (e.g., ROCK_OIL,
     * where {@code romanCanEat} is true), verifying that health and hunger
     * are updated according to the food's score.
     */
    @Test
    void romanShouldEatAllowedFoodAndUpdateHealthAndHunger() {
        TestRoman roman = new TestRoman("Cassius", 30, 1.8, 15, 10, Gender.MALE);
        // Simulate that the roman is hungry
        roman.setHunger(40.0);
        // ROCK_OIL: romanCanEat = true, score is negative (-10)
        Food rockOil = FoodType.ROCK_OIL.create();
        double initialHealth = roman.getHealth();
        double initialHunger = roman.getHunger();
        // normal eat() branch
        roman.eat(rockOil);
        // Health should have changed because score is negative
        assertTrue(roman.getHealth() < initialHealth);
        // Hunger should have decreased (because something was eaten)
        assertTrue(roman.getHunger() < initialHunger);
    }

    /**
     * Tests that {@code drinkPotion()} successfully increases the
     * Roman's potion level when a positive dose is supplied.
     */
    @Test
    void romanDrinkPotionShouldIncreasePotionLevelForPositiveDose() {
        TestRoman roman = new TestRoman("Julius", 35, 1.8, 18, 12, Gender.MALE);
        roman.drinkPotion(1.0);
        assertEquals(1.0, roman.getPotionLevel(), 0.0001);
        roman.drinkPotion(2.0);
        assertEquals(3.0, roman.getPotionLevel(), 0.0001);
    }

    /**
     * Tests that {@code drinkPotion()} ignores non-positive doses (zero or negative),
     * resulting in no change to the Roman's potion level.
     */
    @Test
    void romanDrinkPotionShouldIgnoreNonPositiveDose() {
        TestRoman roman = new TestRoman("Minus", 35, 1.8, 18, 12, Gender.MALE);
        roman.drinkPotion(0.0);
        roman.drinkPotion(-1.0);
        assertEquals(0.0, roman.getPotionLevel(), 0.0001);
    }

    /**
     * Tests that the custom {@code toString()} method for the Roman
     * includes all expected character attributes and labels (Name, Age,
     * Gender, Height, Health, Hunger, Strength, Stamina, Potion Level).
     */
    @Test
    void romanToStringShouldContainAllDetails() {
        String name = "Visus";
        int age = 35;
        double height = 1.8;
        double strength = 18.0;
        double stamina = 12.0;

        TestRoman roman = new TestRoman(name, age, height, strength, stamina, Gender.MALE);
        String s = roman.toString();

        // 1. Verification of fixed data
        assertTrue(s.contains(name), "Must contain the name.");
        assertTrue(s.contains(String.valueOf(age)), "Must contain the age.");
        assertTrue(s.contains("Age:"), "Must contain the label 'Age:'.");
        assertTrue(s.contains(Gender.MALE.toString()), "Must contain the gender.");

        // 2. Verification of numeric values (using Locale.ROOT format for robustness)
        String expectedHeight = String.format(Locale.ROOT, "%.2fm", height);
        assertTrue(s.contains(expectedHeight), "Must contain the formatted height ('1.80m').");

        String expectedStrengthValue = String.format(Locale.ROOT, "%.1f", strength);
        assertTrue(s.contains("Str: " + expectedStrengthValue), "Must contain the strength (18.0).");

        String expectedStaminaValue = String.format(Locale.ROOT, "%.1f", stamina);
        assertTrue(s.contains("Sta: " + expectedStaminaValue), "Must contain the stamina (12.0).");

        // 5. Verification of remaining labels
        assertTrue(s.contains("HP:"), "Must contain the 'HP:' label.");
        assertTrue(s.contains("Hunger:"), "Must contain the 'Hunger:' label.");
        assertTrue(s.contains("Potion:"), "Must contain the 'Potion:' label.");
    }
}