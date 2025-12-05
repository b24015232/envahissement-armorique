package com.asterix.model.item;

/**
 * Represents a non-perishable food item with a fixed score.
 * <p>
 * A {@code SimpleFood} does not change over time and always returns the same
 * {@code baseScore} when queried for its score.
 * </p>
 */
public class SimpleFood extends Food {

    /**
     * Fixed score value for this food item.
     */
    private final int baseScore;

    /**
     * Creates a new {@code SimpleFood} with the given base score.
     *
     * @param name      the display name of the food
     * @param type      the logical type/category of the food
     * @param baseScore the fixed score value for this food
     */
    public SimpleFood(String name, String type, int baseScore) {
        super(name, type);
        this.baseScore = baseScore;
    }

    /**
     * Returns the fixed base score of this food.
     *
     * @return the {@code baseScore} value
     */
    @Override
    public int getScore() {
        return baseScore;
    }

    /**
     * Returns the internal base score of this food.
     * <p>
     * This method can be useful if you need the raw score
     * independently from any polymorphic usage.
     * </p>
     *
     * @return the base score configured for this simple food
     */
    public int getBaseScore() {
        return baseScore;
    }
}
