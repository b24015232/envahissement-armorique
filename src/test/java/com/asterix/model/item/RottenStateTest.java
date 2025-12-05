package com.asterix.model.item;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RottenStateTest {

    @Test
    void rottenStateShouldHaveNegativeScore() {
        // given
        FoodState state = new RottenState();

        // then
        assertEquals(-5, state.getPoints());
        assertEquals("Rotten", state.getStatus());
    }

    @Test
    void rottenStateShouldRemainRottenOverTime() {
        // given
        FoodState state = new RottenState();

        // when
        FoodState next = state.nextState();

        // then
        assertSame(state, next);
    }
}
