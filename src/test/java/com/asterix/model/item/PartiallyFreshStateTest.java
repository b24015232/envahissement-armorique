package com.asterix.model.item;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PartiallyFreshStateTest {

    @Test
    void partiallyFreshStateShouldHaveIntermediateScore() {
        // given
        FoodState state = new PartiallyFreshState();

        // then
        assertEquals(5, state.getPoints());
        assertEquals("Average", state.getStatus());
    }

    @Test
    void partiallyFreshStateShouldBecomeRottenOverTime() {
        // given
        FoodState state = new PartiallyFreshState();

        // when
        FoodState next = state.nextState();

        // then
        assertTrue(next instanceof RottenState);
    }
}
