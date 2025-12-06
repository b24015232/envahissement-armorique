package com.asterix.model.item;

/**
 * Enumerates all possible types of food available in the simulation.
 * <p>
 * Each {@code FoodType} defines its display name, logical category (type),
 * base score, whether it is perishable, and which kind of character can eat it
 * or use it in potions.
 * </p>
 * <p>
 * The {@link #create()} factory method builds the appropriate {@link Food}
 * instance ({@link PerishableFood} or {@link SimpleFood}) according to the
 * configuration of each enum constant.
 * </p>
 */
public enum FoodType {

    // ========================
    //  CONSTANTS
    // ========================
    WILDBOAR(
            "Wildboar",
            "MEAT",
            15,
            true,
            true,
            false,
            true
    ),
    MISTLETOE(
            "Mistletoe",
            "PLANT",
            5,
            true,
            true,
            false,
            true
    ),
    LOBSTER(
            "Lobster",
            "MEAT",
            10,
            true,
            true,
            true,
            true
    ),
    STRAWBERRY(
            "Strawberry",
            "FRUIT",
            3,
            true,
            true,
            true,
            false
    ),
    CARROT(
            "Carrot",
            "VEGETABLE",
            2,
            true,
            true,
            true,
            false
    ),
    SALT(
            "Salt",
            "CONDIMENT",
            1,
            false,
            true,
            true,
            true
    ),
    ROCK_OIL(
            "Rock oil",
            "LIQUID",
            -10,
            false,
            false,
            true,
            false
    ),
    BEET_JUICE(
            "Beet Juice",
            "DRINK",
            2,
            true,
            true,
            true,
            true
    ),
    HONEY(
            "Honey",
            "SWEET",
            4,
            true,
            true,
            true,
            true
    ),
    WINE(
            "Wine",
            "DRINK",
            5,
            true,
            true,
            true,
            true
    ),
    MEAD(
            "Mead",
            "DRINK",
            6,
            true,
            true,
            true,
            true
    ),
    UNICORN_MILK(
            "Unicorn milk",
            "MAGIC",
            20,
            true,
            true,
            true,
            true
    ),
    IDEFIX_HAIR(
            "Idefix hair",
            "MAGIC",
            0,
            false,
            true,
            false,
            true
    ),
    SECRET_INGREDIENT(
            "Secret Ingredient",
            "MAGIC",
            50,
            false,
            true,
            true,
            true
    ),
    FISH(
            "Fish",
            "MEAT",
            4,
            true,
            true,
            true,
            false
    ),
    CLOVER(
            "Clover",
            "PLANT",
            1,
            true,
            true,
            true,
            true
    );

    // ========================
    //  FIELDS
    // ========================

    /**
     * Human-readable name of the food (used for display).
     */
    private final String name;

    /**
     * Logical category of the food (e.g. "MEAT", "PLANT", "MAGIC").
     */
    private final String type;

    /**
     * Base score for this food when it is not perishable or in its default state.
     */
    private final int baseScore;

    /**
     * Indicates whether this food is perishable and uses the state machine.
     */
    private final boolean isPerishable;

    /**
     * {@code true} if Gauls are allowed to eat this food.
     */
    private final boolean gaulCanEat;

    /**
     * {@code true} if Romans are allowed to eat this food.
     */
    private final boolean romanCanEat;

    /**
     * {@code true} if this food can be used as an ingredient in a potion.
     */
    private final boolean usableInPotion;

    // ========================
    //  CONSTRUCTOR
    // ========================

    /**
     * Creates a new {@code FoodType} enum constant with its configuration.
     *
     * @param name           the display name of the food
     * @param type           the logical category of the food (e.g. "MEAT", "DRINK")
     * @param baseScore      the base score this food provides
     * @param isPerishable   {@code true} if the food should age over time
     * @param gaulCanEat     {@code true} if Gauls can eat this food
     * @param romanCanEat    {@code true} if Romans can eat this food
     * @param usableInPotion {@code true} if the food can be used in potions
     */
    FoodType(String name,
             String type,
             int baseScore,
             boolean isPerishable,
             boolean gaulCanEat,
             boolean romanCanEat,
             boolean usableInPotion) {
        this.name = name;
        this.type = type;
        this.baseScore = baseScore;
        this.isPerishable = isPerishable;
        this.gaulCanEat = gaulCanEat;
        this.romanCanEat = romanCanEat;
        this.usableInPotion = usableInPotion;
    }

    // ========================
    //  FACTORY : create()
    // ========================

    /**
     * Creates a concrete {@link Food} instance corresponding to this type.
     * <p>
     * If the food is marked as perishable, a {@link PerishableFood} is created
     * starting in the {@link FreshState}. Otherwise, a {@link SimpleFood} is
     * created using the {@code baseScore}.
     * </p>
     *
     * @return a new {@link Food} instance for this type
     */
    public Food create() {
        if (isPerishable) {
            return new PerishableFood(name, type, new FreshState(),gaulCanEat,romanCanEat);
        } else {
            return new SimpleFood(name, type, baseScore, gaulCanEat, romanCanEat);
        }
    }

    // ========================
    //  GETTERS
    // ========================

    /**
     * Returns the display name of this food type.
     *
     * @return the human-readable name of the food
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the logical category of this food type.
     *
     * @return the logical type (e.g. "MEAT", "DRINK")
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the base score of this food type.
     *
     * @return the base score value
     */
    public int getBaseScore() {
        return baseScore;
    }

    /**
     * Indicates whether this food is perishable.
     *
     * @return {@code true} if the food ages over time, {@code false} otherwise
     */
    public boolean isPerishable() {
        return isPerishable;
    }

    /**
     * Indicates whether a Gaul character can eat this food.
     *
     * @return {@code true} if a Gaul can eat it, {@code false} otherwise
     */
    public boolean isGaulCanEat() {
        return gaulCanEat;
    }

    /**
     * Indicates whether a Roman character can eat this food.
     *
     * @return {@code true} if a Roman can eat it, {@code false} otherwise
     */
    public boolean isRomanCanEat() {
        return romanCanEat;
    }

    /**
     * Indicates whether this food can be used as a potion ingredient.
     *
     * @return {@code true} if usable in potions, {@code false} otherwise
     */
    public boolean isUsableInPotion() {
        return usableInPotion;
    }
}
