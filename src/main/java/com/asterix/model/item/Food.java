package com.asterix.model.item;

/**
 * Represents a generic food item in the simulation.
 * <p>
 * A {@code Food} has a name and a type (for example "MEAT", "VEGETABLE", "MAGIC"...)
 * and provides an abstract method {@link #getScore()} that defines
 * how many points this food gives (or removes) when consumed or used.
 * It also stores information about which kinds of characters
 * are allowed to eat it (Gauls and/or Romans).
 * </p>
 */
public abstract class Food {

    /**
     * Display name of the food item.
     */
    protected final String name;

    /**
     * Logical type/category of the food (e.g. "MEAT", "DRINK", "MAGIC").
     */
    protected final String type;

    /**
     * Indicates whether this food can be eaten by Gauls.
     */
    protected final boolean gaulEdible;

    /**
     * Indicates whether this food can be eaten by Romans.
     */
    protected final boolean romanEdible;

    /**
     * Creates a new {@code Food} with the given name, type and eating permissions.
     *
     * @param name        the display name of the food
     * @param type        the logical type/category of the food
     * @param gaulEdible  {@code true} if Gauls are allowed to eat this food
     * @param romanEdible {@code true} if Romans are allowed to eat this food
     */
    protected Food(String name, String type, boolean gaulEdible, boolean romanEdible) {
        this.name = name;
        this.type = type;
        this.gaulEdible = gaulEdible;
        this.romanEdible = romanEdible;
    }

    /**
     * Returns the display name of this food item.
     *
     * @return the name of the food
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the logical type/category of this food item.
     *
     * @return the type of the food (e.g. "MEAT", "DRINK")
     */
    public String getType() {
        return type;
    }

    /**
     * Indicates whether a Gaul is allowed to eat this food.
     *
     * @return {@code true} if a Gaul can eat this food, {@code false} otherwise
     */
    public boolean canBeEatenByGaul() {
        return gaulEdible;
    }

    /**
     * Indicates whether a Roman is allowed to eat this food.
     *
     * @return {@code true} if a Roman can eat this food, {@code false} otherwise
     */
    public boolean canBeEatenByRoman() {
        return romanEdible;
    }

    /**
     * Returns the score associated with this food.
     * <p>
     * The score usually represents how beneficial or harmful the food is
     * when used in the simulation (for example, when eaten by a character
     * or used as an ingredient in a potion).
     * </p>
     *
     * @return the score value for this food
     */
    public abstract int getScore();
}
