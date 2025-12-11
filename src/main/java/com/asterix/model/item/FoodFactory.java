package com.asterix.model.item;

import java.util.Random;

/**
 * Factory class to generate Food items randomly.
 * Implements the Factory Design Pattern to decouple creation logic.
 */
public class FoodFactory {

    private static final Random random = new Random();

    /**
     * Creates a random food item.
     * @return A new instance of Food with a random type.
     */
    public static Food createRandomFood() {
        // Pick a random enum constant
        FoodType[] types = FoodType.values();
        FoodType randomType = types[random.nextInt(types.length)];

        // Delegate creation to the Enum logic
        return randomType.create();
    }
}