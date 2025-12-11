package com.asterix.model.item;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link SimpleFood} class.
 * <p>
 * This suite verifies the core behavior of non-perishable food items,
 * ensuring they correctly inherit properties from their {@link FoodType},
 * return their fixed base score, and expose the correct eating permission flags.
 */
class SimpleFoodTest {

    /**
     * Tests that a {@code SimpleFood} instance correctly retains the
     * name and category type from its associated {@code FoodType} (e.g., SALT).
     */
    @Test
    void simpleFoodShouldKeepNameAndType() {
        SimpleFood food = new SimpleFood(FoodType.SALT);

        assertEquals("Salt", food.getName());
        assertEquals("CONDIMENT", food.getType());
    }

    /**
     * Tests that a {@code SimpleFood} instance correctly returns the
     * fixed base score defined by its {@code FoodType} (e.g., ROCK_OIL),
     * as its score does not change over time.
     */
    @Test
    void simpleFoodShouldReturnBaseScore() {
        SimpleFood food = new SimpleFood(FoodType.ROCK_OIL);

        assertEquals(-10, food.getScore());
    }

    /**
     * Tests that the eating permission flags ({@code canBeEatenByGaul} and
     * {@code canBeEatenByRoman}) are correctly exposed based on the
     * associated {@code FoodType} metadata (e.g., ROCK_OIL, which is
     * typically only authorized for Romans).
     */
    @Test
    void simpleFoodShouldExposeEdibleFlags() {
        SimpleFood food = new SimpleFood(FoodType.ROCK_OIL);

        assertFalse(food.canBeEatenByGaul());
        assertTrue(food.canBeEatenByRoman());
    }
}