package com.asterix.model.item;

/**
 * Represents a non-perishable food item with a fixed nutritional score.
 * <p>
 * Unlike {@link PerishableFood}, a {@code SimpleFood} does not degrade over time
 * (no state transitions). Its nutritional value remains constant as defined
 * by its {@link FoodType}. Examples include Salt, Rock Oil, or Honey.
 * </p>
 */
public class SimpleFood extends Food {

    /**
     * The fixed nutritional score of this food.
     */
    private final int baseScore;

    /**
     * Creates a new {@code SimpleFood} based on the provided type.
     * <p>
     * The score is initialized directly from the {@link FoodType}'s base score.
     * </p>
     *
     * @param foodType The enum constant defining this food's properties.
     */
    public SimpleFood(FoodType foodType) {
        super(foodType);
        this.baseScore = foodType.getBaseScore();
    }

    /**
     * Returns the fixed nutritional score of this food.
     *
     * @return The base score defined in the FoodType.
     */
    @Override
    public int getScore() {
        return baseScore;
    }
}