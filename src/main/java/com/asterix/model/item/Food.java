package com.asterix.model.item;

/**
 * Represents a generic food item in the simulation.
 * <p>
 * A {@code Food} is primarily defined by its {@link FoodType}, which provides its base properties
 * (name, type, edibility). Concrete subclasses (like {@link PerishableFood} or anonymous classes)
 * must implement the {@link #getScore()} method to define the nutritional value.
 * </p>
 */
public abstract class Food {

    /** The display name of the food. */
    protected final String name;
    /** The category type of the food (e.g., MEAT, VEGETABLE). */
    protected final String type;
    /** Indicates if Gauls can eat this food. */
    protected final boolean gaulEdible;
    /** Indicates if Romans can eat this food. */
    protected final boolean romanEdible;

    // correcting : adding a link to enum foodType
    /** The underlying enum constant defining the food's properties. */
    protected final FoodType foodType;

    /**
     * Creates a new {@code Food} based on its Enum definition.
     * <p>
     * This constructor initializes the food's properties (name, type, edibility)
     * from the values stored in the provided {@link FoodType}.
     * </p>
     *
     * @param foodType The enum constant defining this food's properties.
     */
    protected Food(FoodType foodType) {
        this.foodType = foodType;
        this.name = foodType.getName();
        this.type = foodType.getType();
        this.gaulEdible = foodType.isGaulCanEat();
        this.romanEdible = foodType.isRomanCanEat();
    }

    /**
     * Gets the name of the food.
     *
     * @return The food name.
     */
    public String getName() { return name; }

    /**
     * Gets the type category of the food.
     *
     * @return The food type string.
     */
    public String getType() { return type; }

    /**
     * Checks if this food can be consumed by a Gaul.
     *
     * @return {@code true} if edible by Gauls, {@code false} otherwise.
     */
    public boolean canBeEatenByGaul() { return gaulEdible; }

    /**
     * Checks if this food can be consumed by a Roman.
     *
     * @return {@code true} if edible by Romans, {@code false} otherwise.
     */
    public boolean canBeEatenByRoman() { return romanEdible; }

    /**
     * Gets the underlying {@link FoodType} enum.
     *
     * @return The food type enum constant.
     */
    public FoodType getFoodType() { return foodType; }

    /**
     * Checks if the food is currently fresh.
     * <p>
     * By default, non-perishable food is always considered fresh.
     * Subclasses (like PerishableFood) should override this to implementing aging logic.
     * </p>
     *
     * @return {@code true} if the food is fresh enough to be used in recipes.
     */
    public boolean isFresh() {
        return true;
    }

    /**
     * Returns the nutritional score of the food.
     * <p>
     * This value determines how much health is restored when consumed.
     * </p>
     *
     * @return The nutritional score.
     */
    public abstract int getScore();
}