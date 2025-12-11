package com.asterix.model.item;

/**
 * Represents a fresh state for a perishable food item.
 * <p>
 * In this state, the food is in its best condition and provides the highest
 * number of points when used in the simulation.
 * </p>
 */
public class FreshState implements FoodState {

    /**
     * Returns the number of points for a fresh food item.
     *
     * @return a positive score representing a fully fresh food (10 by default)
     */
    @Override
    public int getPoints() {
        return 10;
    }

    /**
     * Returns the textual description of this state.
     *
     * @return the string {@code "Fresh"}
     */
    @Override
    public String getStatus() {
        return "Fresh";
    }

    /**
     * Returns the next state in the lifecycle of a perishable food.
     * <p>
     * From the fresh state, the food becomes {@link PartiallyFreshState}.
     * </p>
     *
     * @return a new {@link PartiallyFreshState} instance
     */
    @Override
    public FoodState nextState() {
        return new PartiallyFreshState();
    }
}
