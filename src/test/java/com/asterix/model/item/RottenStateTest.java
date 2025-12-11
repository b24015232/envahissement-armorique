package com.asterix.model.item;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link RottenState} class.
 * <p>
 * This state represents the final, deteriorated condition of a perishable
 * food item. This test suite verifies the negative score and status associated
 * with this state, and confirms that no further state transitions occur.
 */
class RottenStateTest {

    /**
     * Tests that the {@code RottenState} correctly returns a negative
     * score (-5 points) and the status label "Rotten".
     */
    @Test
    void rottenStateShouldHaveNegativeScore() {
        FoodState state = new RottenState();

        assertEquals(-5, state.getPoints());
        assertEquals("Rotten", state.getStatus());
    }

    /**
     * Tests that calling {@code nextState()} on a {@code RottenState}
     * instance returns the same instance, ensuring that the food item
     * does not deteriorate further once it has reached the rotten state.
     */
    @Test
    void rottenStateShouldRemainRottenOverTime() {

        FoodState state = new RottenState();

        FoodState next = state.nextState();

        assertSame(state, next);
    }
}