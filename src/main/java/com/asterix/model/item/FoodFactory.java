package com.asterix.model.item;

import java.util.Random;

/**
 * Factory class responsible for generating Food items randomly.
 * <p>
 * This class implements the Factory Design Pattern to decouple the creation logic
 * from the simulation logic. It is primarily used by the {@code InvasionTheater}
 * to spawn food in various locations during the simulation.
 * </p>
 */
public class FoodFactory {

    /**
     * Random number generator shared for all factory calls.
     */
    private static final Random random = new Random();

    /**
     * Creates a random food item selected from all available types.
     * <p>
     * This method picks a random {@link FoodType} enum constant and delegates
     * the actual object instantiation to that enum's {@code create()} method.
     * </p>
     *
     * @return A new instance of a {@link Food} subclass (either Simple or Perishable) with a random type.
     */
    public static Food createRandomFood() {
        // Pick a random enum constant
        FoodType[] types = FoodType.values();
        FoodType randomType = types[random.nextInt(types.length)];

        // Delegate creation to the Enum logic
        return randomType.create();
    }
}