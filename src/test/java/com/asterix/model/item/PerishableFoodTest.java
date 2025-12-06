package com.asterix.model.item;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PerishableFoodTest {

    @Test
    void perishableFoodShouldHaveInitialState() {
        // given
        FoodState initial = new FreshState();

        // when
        PerishableFood food = new PerishableFood(
                "Strawberry",
                "FRUIT",
                initial,
                true,   // Gaul can eat
                true    // Roman can eat
        );

        // then
        assertEquals("Strawberry", food.getName());
        assertEquals("FRUIT", food.getType());
        assertEquals("Fresh", food.getCurrentStatus());
        assertEquals(10, food.getScore());
        assertTrue(food.getState() instanceof FreshState);
        assertTrue(food.canBeEatenByGaul());
        assertTrue(food.canBeEatenByRoman());
    }

    @Test
    void perishableFoodShouldChangeStateOverTime() {
        // given
        PerishableFood food = new PerishableFood(
                "Fish",
                "MEAT",
                new FreshState(),
                true,   // Gaul can eat
                true    // Roman can eat
        );

        // when - 1st time: Fresh -> PartiallyFresh
        food.passTime();

        // then
        assertEquals("Average", food.getCurrentStatus());
        assertEquals(5, food.getScore());
        assertTrue(food.getState() instanceof PartiallyFreshState);

        // when - 2nd time: PartiallyFresh -> Rotten
        food.passTime();

        // then
        assertEquals("Rotten", food.getCurrentStatus());
        assertEquals(-5, food.getScore());
        assertTrue(food.getState() instanceof RottenState);
    }

    @Test
    void perishableFoodStateCanBeForcedManually() {
        // given
        PerishableFood food = new PerishableFood(
                "Honey",
                "SWEET",
                new FreshState(),
                true,   // Gaul can eat
                true    // Roman can eat
        );

        // when
        food.setState(new RottenState());

        // then
        assertEquals("Rotten", food.getCurrentStatus());
        assertEquals(-5, food.getScore());
        assertTrue(food.getState() instanceof RottenState);
    }
}
