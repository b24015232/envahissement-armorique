package com.asterix.model.item;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PerishableFoodTest {

    @Test
    void perishableFoodShouldHaveInitialState() {
        FoodState initial = new FreshState();

        // When : STRAWBERRY is perishable
        PerishableFood food = new PerishableFood(FoodType.STRAWBERRY, initial);
        assertEquals("Strawberry", food.getName());
        assertEquals("FRUIT", food.getType());
        assertEquals("Fresh", food.getCurrentStatus());
        assertEquals(10, food.getScore()); // FreshState score
        assertTrue(food.isFresh()); // checking the new method
    }

    @Test
    void perishableFoodShouldChangeStateOverTime() {
        // Given : fresh fish
        PerishableFood food = new PerishableFood(FoodType.FISH, new FreshState());

        // passing from fresh to mid state
        food.passTime();

        assertEquals("Average", food.getCurrentStatus());
        assertEquals(5, food.getScore());
        assertFalse(food.isFresh());

        // from mid to rotten
        food.passTime();

        assertEquals("Rotten", food.getCurrentStatus());
        assertEquals(-5, food.getScore());
    }

    @Test
    void perishableFoodStateCanBeForcedManually() {

        PerishableFood food = new PerishableFood(FoodType.WILDBOAR, new FreshState());

        // When
        food.setState(new RottenState());

        // Then
        assertEquals("Rotten", food.getCurrentStatus());
        assertEquals(-5, food.getScore());
    }
}