package com.asterix.model.item;

/**
 * Represents a generic food item in the simulation.
 * <p>
 * A {@code Food} has a name and a type (for example "MEAT", "VEGETABLE", "MAGIC"...)
 * and provides an abstract method {@link #getScore()} that defines
 * how many points this food gives (or removes) when consumed or used.
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
     * Creates a new {@code Food} with the given name and type.
     *
     * @param name the display name of the food
     * @param type the logical type/category of the food
     */
    protected Food(String name, String type) {
        this.name = name;
        this.type = type;
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
