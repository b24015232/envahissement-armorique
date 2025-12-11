package com.asterix.model.item;

/**
 * Represents a food item that changes over time according to a {@link FoodState}.
 */
public class PerishableFood extends Food {

    private FoodState state;

    /**
     * Creates a new {@code PerishableFood} with an initial state.
     * @param foodType The enum definition.
     * @param initialState The starting state (usually Fresh).
     */
    public PerishableFood(FoodType foodType, FoodState initialState) {
        super(foodType);
        this.state = initialState;
    }

    /**
     * Checks if the food is currently fresh.
     * Essential for Potion validation.
     */
    @Override
    public boolean isFresh() {
        return state instanceof FreshState;
    }

    public void passTime() {
        state = state.nextState();
    }

    @Override
    public int getScore() {
        return state.getPoints();
    }

    public String getCurrentStatus() {
        return state.getStatus();
    }

    public FoodState getState() { return state; }
    public void setState(FoodState state) { this.state = state; }
}