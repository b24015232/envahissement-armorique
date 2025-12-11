package com.asterix.model.item;

/**
 * Represents a non-perishable food item with a fixed score.
 */
public class SimpleFood extends Food {

    private final int baseScore;

    public SimpleFood(FoodType foodType) {
        super(foodType);
        this.baseScore = foodType.getBaseScore();
    }

    @Override
    public int getScore() {
        return baseScore;
    }
}