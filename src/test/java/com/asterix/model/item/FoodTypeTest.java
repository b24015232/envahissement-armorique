package com.asterix.model.item;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link FoodType} enum class.
 * <p>
 * This suite verifies the core functionality of the enum, including
 * ensuring that {@code FoodType.create()} correctly instantiates
 * the appropriate concrete class ({@code PerishableFood} or {@code SimpleFood})
 * and that the metadata stored within each enum constant is correctly
 * reflected in the created {@code Food} object.
 */
class FoodTypeTest {

    /**
     * Tests that a {@code FoodType} configured as perishable (e.g., WILDBOAR)
     * correctly creates an instance of {@link PerishableFood} and that
     * the underlying metadata (name, type, eating permissions) is accurate.
     */
    @Test
    void perishableFoodTypeShouldCreatePerishableFood() {
        Food food = FoodType.WILDBOAR.create();

        assertInstanceOf(PerishableFood.class, food);
        assertEquals("Wildboar", food.getName());
        assertEquals("MEAT", food.getType());

        PerishableFood perishable = (PerishableFood) food;
        assertInstanceOf(FreshState.class, perishable.getState());

        assertTrue(perishable.canBeEatenByGaul());
        assertFalse(perishable.canBeEatenByRoman());
    }

    /**
     * Tests that a {@code FoodType} configured as non-perishable (e.g., SALT)
     * correctly creates an instance of {@link SimpleFood} and that
     * the underlying metadata (name, type, base score, eating permissions) is accurate.
     */
    @Test
    void nonPerishableFoodTypeShouldCreateSimpleFoodWithBaseScore() {
        Food food = FoodType.SALT.create();

        assertInstanceOf(SimpleFood.class, food);
        assertEquals("Salt", food.getName());
        assertEquals("CONDIMENT", food.getType());
        assertEquals(1, food.getScore());

        assertTrue(food.canBeEatenByGaul());
        assertTrue(food.canBeEatenByRoman());
    }

    /**
     * Tests that the metadata fields (name, type, base score, perishability,
     * eating permissions, potion usability) stored within the {@code FoodType}
     * enum constants themselves (e.g., HONEY) are correctly set.
     */
    @Test
    void foodTypeMetadataShouldMatchConfiguration() {
        FoodType type = FoodType.HONEY;

        assertEquals("Honey", type.getName());
        assertEquals("SWEET", type.getType());
        assertEquals(4, type.getBaseScore());
        assertTrue(type.isPerishable());
        assertTrue(type.isGaulCanEat());
        assertTrue(type.isRomanCanEat());
        assertTrue(type.isUsableInPotion());
    }
}