package com.asterix.model.item;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FreshStateTest {

    @Test
    void freshStateShouldHaveMaximumPositiveScore() {
        // given
        FoodState state = new FreshState();

        // then
        assertEquals(10, state.getPoints());
        assertEquals("Fresh", state.getStatus());
    }

    @Test
    void freshStateShouldBecomePartiallyFreshOverTime() {
        // given
        FoodState state = new FreshState();

        // when
        FoodState next = state.nextState();

        // then
        assertTrue(next instanceof PartiallyFreshState);
    }
}
