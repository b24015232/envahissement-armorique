package com.asterix.model.item;

/**
 * Represents a rotten state for a perishable food item.
 * <p>
 * In this state, the food is in its worst condition, usually giving negative
 * points and no longer evolving to any other state.
 * </p>
 */
public class RottenState implements FoodState {

    /**
     * Returns the number of points for a rotten food item.
     *
     * @return a negative score (−5 by default) indicating a harmful food
     */
    @Override
    public int getPoints() {
        return -5;
    }

    /**
     * Returns the textual description of this state.
     *
     * @return the string {@code "Rotten"}
     */
    @Override
    public String getStatus() {
        return "Rotten";
    }

    /**
     * Returns the next state in the lifecycle of a perishable food.
     * <p>
     * Rotten food does not evolve further, so this method returns {@code this}.
     * </p>
     *
     * @return this {@link RottenState} instance
     */
    @Override
    public FoodState nextState() {
        return this;
    }
}
