package com.asterix.model.item;

/**
 * Represents a generic food item in the simulation.
 * <p>
 * A {@code Food} is defined by its {@link FoodType} which provides its base properties.
 * It provides an abstract method {@link #getScore()} and a {@link #isFresh()} check
 * crucial for potion brewing.
 * </p>
 */
public abstract class Food {

    protected final String name;
    protected final String type;
    protected final boolean gaulEdible;
    protected final boolean romanEdible;
    //correcting : adding a link to enum foodType
    protected final FoodType foodType;

    /**
     * Creates a new {@code Food} based on its Enum definition.
     * @param foodType The enum constant defining this food's properties.
     */
    protected Food(FoodType foodType) {
        this.foodType = foodType;
        this.name = foodType.getName();
        this.type = foodType.getType();
        this.gaulEdible = foodType.isGaulCanEat();
        this.romanEdible = foodType.isRomanCanEat();
    }

    public String getName() { return name; }
    public String getType() { return type; }
    public boolean canBeEatenByGaul() { return gaulEdible; }
    public boolean canBeEatenByRoman() { return romanEdible; }

    public FoodType getFoodType() { return foodType; }

    /**
     * Checks if the food is currently fresh.
     * By default, non-perishable food is always considered fresh.
     * @return true if fresh.
     */
    public boolean isFresh() {
        return true;
    }

    public abstract int getScore();
}