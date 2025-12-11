package com.asterix.model.item;

import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link FoodFactory} class.
 */
class FoodFactoryTest {

    @Test
    void createRandomFoodShouldReturnValidNonNullObject() {
        // Act
        Food food = FoodFactory.createRandomFood();

        // Assert
        assertNotNull(food, "Factory should never return null");
        assertNotNull(food.getName(), "Created food should have a name");
        assertNotNull(food.getType(), "Created food should have a category type");
        assertNotNull(food.getFoodType(), "Created food should be linked to a FoodType enum");
    }

    @Test
    void createRandomFoodShouldGenerateDifferentTypesOverTime() {
        Set<FoodType> typesGenerated = new HashSet<>();
        for (int i = 0; i < 50; i++) {
            Food food = FoodFactory.createRandomFood();
            typesGenerated.add(food.getFoodType());
        }

        assertTrue(typesGenerated.size() > 1,
                "Factory should generate different food types over multiple calls");
    }

    @Test
    void createRandomFoodShouldCreateCorrectInstances() {
        // Generate many items and check if Perishable types actually result in PerishableFood instances
        for (int i = 0; i < 50; i++) {
            Food food = FoodFactory.createRandomFood();

            if (food.getFoodType().isPerishable()) {
                assertTrue(food instanceof PerishableFood,
                        "Perishable FoodType " + food.getName() + " should create a PerishableFood instance");
                assertTrue(food.isFresh(), "Newly created perishable food should be fresh");
            } else {
                assertTrue(food instanceof SimpleFood,
                        "Non-perishable FoodType " + food.getName() + " should create a SimpleFood instance");
            }
        }
    }
}