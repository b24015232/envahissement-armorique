package com.asterix.model.item;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FoodTypeTest {

    @Test
    void perishableFoodTypeShouldCreatePerishableFood() {
        // when
        Food food = FoodType.WILDBOAR.create();

        // then
        assertTrue(food instanceof PerishableFood);
        assertEquals("Wildboar", food.getName());
        assertEquals("MEAT", food.getType());

        PerishableFood perishable = (PerishableFood) food;
        assertTrue(perishable.getState() instanceof FreshState);
    }

    @Test
    void nonPerishableFoodTypeShouldCreateSimpleFoodWithBaseScore() {
        // when
        Food food = FoodType.SALT.create();

        // then
        assertTrue(food instanceof SimpleFood);
        assertEquals("Salt", food.getName());
        assertEquals("CONDIMENT", food.getType());
        assertEquals(1, food.getScore());
    }

    @Test
    void foodTypeMetadataShouldMatchConfiguration() {
        // given
        FoodType type = FoodType.HONEY;

        // then
        assertEquals("Honey", type.getName());
        assertEquals("SWEET", type.getType());
        assertEquals(4, type.getBaseScore());
        assertTrue(type.isPerishable());
        assertTrue(type.isGaulCanEat());
        assertTrue(type.isRomanCanEat());
        assertTrue(type.isUsableInPotion());
    }
}
