package com.asterix.model.item;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PerishableFoodTest {

    @Test
    void perishableFoodShouldHaveInitialState() {
        // given
        FoodState initial = new FreshState();

        // when
        PerishableFood food = new PerishableFood("Strawberry", "FRUIT", initial);

        // then
        assertEquals("Strawberry", food.getName());
        assertEquals("FRUIT", food.getType());
        assertEquals("Fresh", food.getCurrentStatus());
        assertEquals(10, food.getScore());
        assertTrue(food.getState() instanceof FreshState);
    }

    @Test
    void perishableFoodShouldChangeStateOverTime() {
        // given
        PerishableFood food = new PerishableFood("Fish", "MEAT", new FreshState());

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
        PerishableFood food = new PerishableFood("Honey", "SWEET", new FreshState());

        // when
        food.setState(new RottenState());

        // then
        assertEquals("Rotten", food.getCurrentStatus());
        assertEquals(-5, food.getScore());
        assertTrue(food.getState() instanceof RottenState);
    }
}
