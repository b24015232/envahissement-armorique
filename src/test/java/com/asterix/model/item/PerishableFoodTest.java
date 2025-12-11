package com.asterix.model.item;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link PerishableFood} class.
 * <p>
 * This suite verifies that perishable food items correctly manage their
 * state using the State pattern, including proper initialization,
 * state transitions over time (deterioration), and manual state overriding.
 */
class PerishableFoodTest {

    /**
     * Tests that a {@code PerishableFood} object is initialized with
     * the correct base metadata from its {@code FoodType} and the
     * specified initial {@code FoodState}.
     * It also verifies the initial score and the {@code isFresh()} status.
     */
    @Test
    void perishableFoodShouldHaveInitialState() {
        FoodState initial = new FreshState();

        PerishableFood food = new PerishableFood(FoodType.STRAWBERRY, initial);
        assertEquals("Strawberry", food.getName());
        assertEquals("FRUIT", food.getType());
        assertEquals("Fresh", food.getCurrentStatus());
        assertEquals(10, food.getScore()); // FreshState score
        assertTrue(food.isFresh()); // checking the new method
    }

    /**
     * Tests the deterioration process: verifies that calling {@code passTime()}
     * correctly transitions the food state from {@code FreshState} to
     * {@code PartiallyFreshState}, and subsequently to {@code RottenState},
     * checking the score and status at each stage.
     */
    @Test
    void perishableFoodShouldChangeStateOverTime() {
        PerishableFood food = new PerishableFood(FoodType.FISH, new FreshState());

        food.passTime();

        assertEquals("Average", food.getCurrentStatus());
        assertEquals(5, food.getScore());
        assertFalse(food.isFresh());

        food.passTime();

        assertEquals("Rotten", food.getCurrentStatus());
        assertEquals(-5, food.getScore());
    }

    /**
     * Tests that the internal state of the perishable food can be
     * manually overridden using {@code setState()} and that the food's
     * properties (status, score) immediately reflect the new state.
     */
    @Test
    void perishableFoodStateCanBeForcedManually() {

        PerishableFood food = new PerishableFood(FoodType.WILDBOAR, new FreshState());

        food.setState(new RottenState());

        assertEquals("Rotten", food.getCurrentStatus());
        assertEquals(-5, food.getScore());
    }
}