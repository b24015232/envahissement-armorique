package com.asterix.model.item;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CauldronTest {

    @Test
    void brewShouldSucceedWithCorrectIngredients() {
        Cauldron cauldron = new Cauldron();

        // Add mandatory ingredients
        cauldron.addIngredient(FoodType.MISTLETOE.create());
        cauldron.addIngredient(FoodType.CARROT.create());
        cauldron.addIngredient(FoodType.SALT.create());
        cauldron.addIngredient(FoodType.HONEY.create());
        cauldron.addIngredient(FoodType.MEAD.create());
        cauldron.addIngredient(FoodType.SECRET_INGREDIENT.create());
        cauldron.addIngredient(FoodType.ROCK_OIL.create());
        cauldron.addIngredient(FoodType.CLOVER.create()); // Fresh by default

        // Add properly aged fish (Fresh -> Average)
        PerishableFood fish = (PerishableFood) FoodType.FISH.create();
        fish.passTime();
        cauldron.addIngredient(fish);

        assertTrue(cauldron.brew(), "Brewing should succeed with the correct recipe");
    }

    @Test
    void brewShouldFailWithFreshFish() {
        Cauldron cauldron = new Cauldron();
        // Add all ingredients but with FRESH fish (not average)
        cauldron.addIngredient(FoodType.MISTLETOE.create());
        cauldron.addIngredient(FoodType.CARROT.create());
        cauldron.addIngredient(FoodType.SALT.create());
        cauldron.addIngredient(FoodType.HONEY.create());
        cauldron.addIngredient(FoodType.MEAD.create());
        cauldron.addIngredient(FoodType.SECRET_INGREDIENT.create());
        cauldron.addIngredient(FoodType.ROCK_OIL.create());
        cauldron.addIngredient(FoodType.CLOVER.create());

        // Fresh fish! Error!
        cauldron.addIngredient(FoodType.FISH.create());

        assertFalse(cauldron.brew(), "Brewing should fail if the fish is too fresh");
    }
}