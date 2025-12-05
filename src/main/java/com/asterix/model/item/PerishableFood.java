package com.asterix.model.item;

/**
 * Represents a food item that changes over time according to a {@link FoodState}.
 * <p>
 * A {@code PerishableFood} delegates its score and status to its current
 * {@link FoodState} and can transition to the next state when time passes.
 * </p>
 */
public class PerishableFood extends Food {

    /**
     * Current state of this perishable food (e.g. fresh, average, rotten).
     */
    private FoodState state;

    /**
     * Creates a new {@code PerishableFood} with an initial state.
     *
     * @param name         the display name of the food
     * @param type         the logical type/category of the food
     * @param initialState the initial {@link FoodState} for this perishable food
     */
    public PerishableFood(String name, String type, FoodState initialState) {
        super(name, type);
        this.state = initialState;
    }

    /**
     * Advances the lifecycle of this food to its next state.
     * <p>
     * Internally, this calls {@link FoodState#nextState()} on the current state
     * and replaces it with the returned value.
     * </p>
     */
    public void passTime() {
        state = state.nextState();
    }

    /**
     * Returns the score associated with the current state of this food.
     *
     * @return the current state's points
     */
    @Override
    public int getScore() {
        return state.getPoints();
    }

    /**
     * Returns a textual description of the current state of this food.
     *
     * @return the current state's status (e.g. "Fresh", "Average", "Rotten")
     */
    public String getCurrentStatus() {
        return state.getStatus();
    }

    /**
     * Returns the current {@link FoodState} of this perishable food.
     *
     * @return the current state instance
     */
    public FoodState getState() {
        return state;
    }

    /**
     * Sets the current {@link FoodState} of this perishable food.
     * <p>
     * This can be used by the simulation engine to force a specific state
     * (for example, when applying special effects or debugging).
     * </p>
     *
     * @param state the new state to use
     */
    public void setState(FoodState state) {
        this.state = state;
    }
}
