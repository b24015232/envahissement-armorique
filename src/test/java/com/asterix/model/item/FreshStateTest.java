package com.asterix.model.item;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link FreshState} class, which represents the initial,
 * optimal condition of a perishable food item.
 * <p>
 * This suite verifies the score associated with the fresh state and
 * the transition logic to the next state upon the passage of time.
 */
class FreshStateTest {

    /**
     * Tests that the {@code FreshState} correctly returns the maximum
     * positive score (10 points) and the status label "Fresh".
     */
    @Test
    void freshStateShouldHaveMaximumPositiveScore() {
        FoodState state = new FreshState();

        assertEquals(10, state.getPoints());
        assertEquals("Fresh", state.getStatus());
    }

    /**
     * Tests that calling {@code nextState()} on a {@code FreshState}
     * instance correctly transitions the food to the {@link PartiallyFreshState},
     * reflecting the passage of time.
     */
    @Test
    void freshStateShouldBecomePartiallyFreshOverTime() {
        FoodState state = new FreshState();

        FoodState next = state.nextState();

        assertInstanceOf(PartiallyFreshState.class, next);
    }
}