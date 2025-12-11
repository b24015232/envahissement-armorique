package com.asterix.model.item;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SimpleFoodTest {

    @Test
    void simpleFoodShouldKeepNameAndType() {
        // using a non perishable item (salt)
        SimpleFood food = new SimpleFood(FoodType.SALT);

        // Then
        assertEquals("Salt", food.getName());
        assertEquals("CONDIMENT", food.getType());
    }

    @Test
    void simpleFoodShouldReturnBaseScore() {
        // using rock oil
        SimpleFood food = new SimpleFood(FoodType.ROCK_OIL);

        // Then
        assertEquals(-10, food.getScore());
    }

    @Test
    void simpleFoodShouldExposeEdibleFlags() {
        // authorized only to romans
        SimpleFood food = new SimpleFood(FoodType.ROCK_OIL);

        // Then
        assertFalse(food.canBeEatenByGaul());
        assertTrue(food.canBeEatenByRoman());
    }
}