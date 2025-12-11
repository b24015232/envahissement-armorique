package com.asterix.model.item;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link PartiallyFreshState} class.
 * <p>
 * This state represents an intermediate condition of a perishable food
 * item where its quality is average. This test suite verifies the score
 * and status associated with this state, and the transition logic to
 * the next (deteriorated) state.
 */
class PartiallyFreshStateTest {

    /**
     * Tests that the {@code PartiallyFreshState} correctly returns an
     * intermediate score (5 points) and the status label "Average".
     */
    @Test
    void partiallyFreshStateShouldHaveIntermediateScore() {

        FoodState state = new PartiallyFreshState();

        assertEquals(5, state.getPoints());
        assertEquals("Average", state.getStatus());
    }

    /**
     * Tests that calling {@code nextState()} on a {@code PartiallyFreshState}
     * instance correctly transitions the food to the {@link RottenState},
     * reflecting further passage of time and deterioration.
     */
    @Test
    void partiallyFreshStateShouldBecomeRottenOverTime() {
        FoodState state = new PartiallyFreshState();

        FoodState next = state.nextState();
        assertInstanceOf(RottenState.class, next);
    }
}