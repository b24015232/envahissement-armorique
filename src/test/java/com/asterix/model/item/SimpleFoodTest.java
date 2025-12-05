package com.asterix.model.item;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimpleFoodTest {

    @Test
    void simpleFoodShouldKeepNameAndType() {
        // given
        SimpleFood food = new SimpleFood("Carrot", "VEGETABLE", 2);

        // then
        assertEquals("Carrot", food.getName());
        assertEquals("VEGETABLE", food.getType());
    }

    @Test
    void simpleFoodShouldReturnBaseScore() {
        // given
        SimpleFood food = new SimpleFood("Honey", "SWEET", 4);

        // then
        assertEquals(4, food.getScore());
        assertEquals(4, food.getBaseScore());
    }
}
