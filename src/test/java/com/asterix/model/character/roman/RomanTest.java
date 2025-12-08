package com.asterix.model.character.roman;

import com.asterix.model.character.Gender;
import com.asterix.model.item.Food;
import com.asterix.model.item.FoodType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Roman-specific behaviour (especially eating rules and potion).
 */
class RomanTest {

    /**
     * Simple concrete Roman implementation used for tests.
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

        // Helpers to access protected hunger from tests
        void setHunger(double value) {
            this.hunger = value;
        }

        double getHunger() {
            return this.hunger;
        }
    }

    @Test
    void romanShouldIgnoreNullFood() {
        TestRoman roman = new TestRoman("Nullius", 30, 1.8, 15, 10, Gender.MALE);
        double initialHealth = roman.getHealth();
        double initialHunger = roman.getHunger();
        // branche: if (food == null) { return; }
        roman.eat(null);
        assertEquals(initialHealth, roman.getHealth(), 0.0001);
        assertEquals(initialHunger, roman.getHunger(), 0.0001);
    }

    @Test
    void romanShouldNotEatFoodForbiddenForRomans() {
        TestRoman roman = new TestRoman("Brutus", 30, 1.8, 15, 10, Gender.MALE);
        double initialHealth = roman.getHealth();
        double initialHunger = roman.getHunger();
        // WILDBOAR is configured with romanCanEat = false in FoodType
        Food boar = FoodType.WILDBOAR.create();
        // branche: if (!food.canBeEatenByRoman()) { ... return; }
        roman.eat(boar);
        // Health and hunger should not change because Romans cannot eat wildboar
        assertEquals(initialHealth, roman.getHealth(), 0.0001);
        assertEquals(initialHunger, roman.getHunger(), 0.0001);
    }

    @Test
    void romanShouldEatAllowedFoodAndUpdateHealthAndHunger() {
        TestRoman roman = new TestRoman("Cassius", 30, 1.8, 15, 10, Gender.MALE);
        // Simulate that the roman is hungry
        roman.setHunger(40.0);
        // ROCK_OIL: romanCanEat = true, score is negative (-10)
        Food rockOil = FoodType.ROCK_OIL.create();
        double initialHealth = roman.getHealth();
        double initialHunger = roman.getHunger();
        // branche "normale" de eat()
        roman.eat(rockOil);
        // Health should have changed because score is negative
        assertTrue(roman.getHealth() < initialHealth);
        // Hunger should have decreased (because something was eaten)
        assertTrue(roman.getHunger() < initialHunger);
    }

    @Test
    void romanDrinkPotionShouldIncreasePotionLevelForPositiveDose() {
        TestRoman roman = new TestRoman("Julius", 35, 1.8, 18, 12, Gender.MALE);
        roman.drinkPotion(1.0);
        assertEquals(1.0, roman.getPotionLevel(), 0.0001);
        roman.drinkPotion(2.0);
        assertEquals(3.0, roman.getPotionLevel(), 0.0001);
    }

    @Test
    void romanDrinkPotionShouldIgnoreNonPositiveDose() {
        TestRoman roman = new TestRoman("Minus", 35, 1.8, 18, 12, Gender.MALE);
        roman.drinkPotion(0.0);
        roman.drinkPotion(-1.0);
        assertEquals(0.0, roman.getPotionLevel(), 0.0001);
    }

    @Test
    void romanToStringShouldContainClassNameAndName() {
        TestRoman roman = new TestRoman("Visus", 35, 1.8, 18, 12, Gender.MALE);
        String s = roman.toString();
        assertTrue(s.contains("Roman"));
        assertTrue(s.contains("Visus"));
    }
}
