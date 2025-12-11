package com.asterix.model.item;

import java.util.Random;

/**
 * Factory class responsible for instantiating Food objects.
 * <p>
 * This class encapsulates the logic for generating random food items,
 * distinguishing between general sustenance and specific ingredients required
 * for the magic potion recipe.
 * </p>
 */
public class FoodFactory {

    private static final Random random = new Random();

    /**
     * Pre-defined list of ingredients compatible with the magic potion recipe.
     */
    private static final FoodType[] POTION_INGREDIENTS = {
            FoodType.MISTLETOE,
            FoodType.CARROT,
            FoodType.SALT,
            FoodType.HONEY,
            FoodType.MEAD,
            FoodType.CLOVER,
            FoodType.FISH,
            FoodType.ROCK_OIL
    };

    /**
     * Generates a random food item from any available type in the system.
     *
     * @return A new Food instance with a randomly selected type.
     */
    public static Food createRandomFood() {
        FoodType[] types = FoodType.values();
        FoodType randomType = types[random.nextInt(types.length)];
        return randomType.create();
    }

    /**
     * Generates a random food item specifically suitable for brewing the magic potion.
     * <p>
     * This method selects from a restricted subset of food types (e.g., Mistletoe,
     * Rock Oil) required by the Druid's logic.
     * </p>
     *
     * @return A new Food instance representing a potion ingredient.
     */
    public static Food createRandomPotionIngredient() {
        FoodType randomType = POTION_INGREDIENTS[random.nextInt(POTION_INGREDIENTS.length)];
        return randomType.create();
    }
}