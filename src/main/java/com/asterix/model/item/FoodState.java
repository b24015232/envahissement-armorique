package com.asterix.model.item;

/**
 * Represents the state of a perishable food item in the simulation.
 * <p>
 * A {@code FoodState} encapsulates how many points the food is currently worth,
 * how its status should be described (e.g. "Fresh", "Average", "Rotten"),
 * and how it evolves over time through {@link #nextState()}.
 * </p>
 */
public interface FoodState {

    /**
     * Returns the number of points associated with this state.
     * <p>
     * This value is typically used when a character eats the food or when
     * the food is evaluated in the simulation.
     * </p>
     *
     * @return the score/points for this state
     */
    int getPoints();

    /**
     * Returns a human-readable label describing this state.
     * <p>
     * For example, possible values are "Fresh", "Average" or "Rotten".
     * </p>
     *
     * @return the textual status of this state
     */
    String getStatus();

    /**
     * Returns the next state of the food after time has passed.
     * <p>
     * Implementations can either return a new state (e.g. going from
     * fresh to partially fresh) or {@code this} if the state does not change
     * anymore (e.g. already rotten).
     * </p>
     *
     * @return the next {@code FoodState} in the lifecycle
     */
    FoodState nextState();
}
