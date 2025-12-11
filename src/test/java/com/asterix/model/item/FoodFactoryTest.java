package com.asterix.model.item;

import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link FoodFactory} class.
 * <p>
 * This test suite verifies the factory's core functionality, including
 * generating valid, non-null food objects, ensuring randomness and
 * verifying that the factory instantiates the correct concrete class
 * ({@code SimpleFood} or {@code PerishableFood}) based on the food's
 * perishability property defined in {@link FoodType}.
 */
class FoodFactoryTest {

    /**
     * Tests that the {@code createRandomFood()} method returns a non-null
     * object and that the returned food has valid basic properties (name, type, etc.).
     */
    @Test
    void createRandomFoodShouldReturnValidNonNullObject() {
        Food food = FoodFactory.createRandomFood();

        assertNotNull(food, "Factory should never return null");
        assertNotNull(food.getName(), "Created food should have a name");
        assertNotNull(food.getType(), "Created food should have a category type");
        assertNotNull(food.getFoodType(), "Created food should be linked to a FoodType enum");
    }

    /**
     * Tests the randomness of the factory by ensuring that multiple calls
     * to {@code createRandomFood()} generate a variety of different food types.
     */
    @Test
    void createRandomFoodShouldGenerateDifferentTypesOverTime() {
        Set<FoodType> typesGenerated = new HashSet<>();
        // Running multiple times to increase the chance of generating various types
        for (int i = 0; i < 50; i++) {
            Food food = FoodFactory.createRandomFood();
            typesGenerated.add(food.getFoodType());
        }

        assertTrue(typesGenerated.size() > 1,
                "Factory should generate different food types over multiple calls");
    }

    /**
     * Tests the factory's logic for choosing the correct concrete implementation.
     * It verifies that:
     * <ul>
     * <li>If {@code FoodType.isPerishable()} is true, a {@code PerishableFood} instance is created.</li>
     * <li>If {@code FoodType.isPerishable()} is false, a {@code SimpleFood} instance is created.</li>
     * <li>Newly created perishable food is initially marked as fresh.</li>
     * </ul>
     */
    @Test
    void createRandomFoodShouldCreateCorrectInstances() {
        // Generate many items and check if Perishable types actually result in PerishableFood instances
        for (int i = 0; i < 50; i++) {
            Food food = FoodFactory.createRandomFood();

            if (food.getFoodType().isPerishable()) {
                assertInstanceOf(PerishableFood.class, food, "Perishable FoodType " + food.getName() + " should create a PerishableFood instance");
                assertTrue(food.isFresh(), "Newly created perishable food should be fresh");
            } else {
                assertInstanceOf(SimpleFood.class, food, "Non-perishable FoodType " + food.getName() + " should create a SimpleFood instance");
            }
        }
    }
}