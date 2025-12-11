package com.asterix.model.item;

/**
 * Represents a partially fresh (or average) state for a perishable food item.
 * <p>
 * In this state, the food is no longer at its best but still somewhat usable,
 * giving fewer points than when it was fresh.
 * </p>
 */
public class PartiallyFreshState implements FoodState {

    /**
     * Returns the number of points for an average quality food item.
     *
     * @return a moderate positive score (5 by default)
     */
    @Override
    public int getPoints() {
        return 5;
    }

    /**
     * Returns the textual description of this state.
     *
     * @return the string {@code "Average"}
     */
    @Override
    public String getStatus() {
        return "Average";
    }

    /**
     * Returns the next state in the lifecycle of a perishable food.
     * <p>
     * From the partially fresh state, the food becomes {@link RottenState}.
     * </p>
     *
     * @return a new {@link RottenState} instance
     */
    @Override
    public FoodState nextState() {
        return new RottenState();
    }
}
