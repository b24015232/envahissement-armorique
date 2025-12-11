package com.asterix.model.item;

/**
 * Represents a food item that changes over time according to a {@link FoodState}.
 * <p>
 * Unlike simple food, perishable food (like Fish) degrades over time (Fresh -> Partially Fresh -> Rotten).
 * This aging process affects its nutritional score and its suitability for potion brewing.
 * </p>
 */
public class PerishableFood extends Food {

    /**
     * The current freshness state of the food.
     */
    private FoodState state;

    /**
     * Creates a new {@code PerishableFood} with an initial state.
     *
     * @param foodType     The enum definition of the food type.
     * @param initialState The starting state (usually an instance of {@link FreshState}).
     */
    public PerishableFood(FoodType foodType, FoodState initialState) {
        super(foodType);
        this.state = initialState;
    }

    /**
     * Checks if the food is currently considered fresh.
     * <p>
     * This is essential for potion validation (e.g., the Druid needs Fresh Clover).
     * </p>
     *
     * @return {@code true} if the current state is an instance of {@link FreshState}.
     */
    @Override
    public boolean isFresh() {
        return state instanceof FreshState;
    }

    /**
     * Simulates the passage of time for this food item.
     * <p>
     * Triggers the transition to the next state (e.g., Fresh -> Partially Fresh).
     * </p>
     */
    public void passTime() {
        state = state.nextState();
    }

    /**
     * Returns the nutritional score of the food based on its current state.
     *
     * @return The score provided by the current {@link FoodState}.
     */
    @Override
    public int getScore() {
        return state.getPoints();
    }

    /**
     * Gets the textual description of the current state.
     *
     * @return A string representing the status (e.g., "Fresh", "Rotten").
     */
    public String getCurrentStatus() {
        return state.getStatus();
    }

    /**
     * Gets the current state object.
     *
     * @return The current {@link FoodState}.
     */
    public FoodState getState() {
        return state;
    }

    /**
     * Manually sets the state of the food.
     *
     * @param state The new state to assign.
     */
    public void setState(FoodState state) {
        this.state = state;
    }
}