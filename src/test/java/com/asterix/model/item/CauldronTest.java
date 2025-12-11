package com.asterix.model.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link Cauldron} class, focusing on the brewing process,
 * ingredient management, special effects activation, and dose dispensing.
 */
class CauldronTest {

    private Cauldron cauldron;

    /**
     * Sets up a fresh {@code Cauldron} instance before each test.
     */
    @BeforeEach
    void setUp() {
        cauldron = new Cauldron();
    }

    /**
     * Helper method to add all necessary ingredients for a successful magic potion
     * recipe, except for the mandatory {@code FoodType.FISH} ingredient.
     */
    private void addFullRecipeMinusFish() {
        cauldron.addIngredient(FoodType.MISTLETOE.create());
        cauldron.addIngredient(FoodType.CARROT.create());
        cauldron.addIngredient(FoodType.SALT.create());
        cauldron.addIngredient(FoodType.HONEY.create());
        cauldron.addIngredient(FoodType.MEAD.create());
        cauldron.addIngredient(FoodType.SECRET_INGREDIENT.create());
        cauldron.addIngredient(FoodType.ROCK_OIL.create());
        cauldron.addIngredient(FoodType.CLOVER.create());
    }

    /**
     * Tests that the {@code brew()} method succeeds when all required ingredients
     * (9 ingredients, including aged fish) are present.
     */
    @Test
    void brewShouldSucceedWithCorrectIngredients() {
        addFullRecipeMinusFish();

        PerishableFood fish = (PerishableFood) FoodType.FISH.create();
        fish.passTime(); // Must be aged to succeed
        cauldron.addIngredient(fish);

        assertTrue(cauldron.brew(), "Brewing should succeed with the correct recipe (9 ingredients, aged fish)");
    }


    /**
     * Tests that the {@code brew()} method fails if a mandatory ingredient
     * (e.g., Mistletoe or Carrot) is missing from the cauldron.
     */
    @Test
    void brewShouldFailWithMissingIngredient() {
        Cauldron incompleteCauldron = new Cauldron();
        // Only adding Carrot, Mistletoe is missing.
        incompleteCauldron.addIngredient(FoodType.CARROT.create());

        assertFalse(incompleteCauldron.brew(), "Brewing must fail if a mandatory ingredient is missing.");
    }


    /**
     * Tests that adding {@code FoodType.LOBSTER} to the brew activates
     * the nourishing special effect.
     */
    @Test
    void brewShouldActivateNourishingEffect() {
        Cauldron c = new Cauldron();
        c.addIngredient(FoodType.LOBSTER.create());
        c.addIngredient(FoodType.MISTLETOE.create()); // Required minimum ingredient for brew to proceed

        c.brew();
        assertTrue(c.isNourishing(), "Should be nourishing due to Lobster.");
    }

    /**
     * Tests that a specific non-standard ingredient (simulated here as
     * 'Two-headed unicorn milk') activates the duplication special effect.
     */
    @Test
    void brewShouldActivateDuplicationEffect() {
        Cauldron c = new Cauldron();
        // Simulating the special duplication ingredient
        c.addIngredient(new SimpleFood(FoodType.UNICORN_MILK) { @Override public String getName() { return "Two headed unicorn milk"; } });
        c.addIngredient(FoodType.MISTLETOE.create()); // Required minimum ingredient for brew to proceed

        c.brew();
        assertTrue(c.causesDuplication(), "Should cause duplication (Two headed unicorn milk).");
    }

    /**
     * Tests that a specific non-standard ingredient (simulated here as
     * 'Idefix's Hair') activates the lycanthropy special effect.
     */
    @Test
    void brewShouldActivateLycanthropyEffect() {
        Cauldron c = new Cauldron();
        // Simulating the special lycanthropy ingredient
        c.addIngredient(new SimpleFood(FoodType.IDEFIX_HAIR) { @Override public String getName() { return "Idefix's Hair"; } });
        c.addIngredient(FoodType.MISTLETOE.create()); // Required minimum ingredient for brew to proceed

        c.brew();
        assertTrue(c.causesLycanthropy(), "Should cause lycanthropy (Idefix's Hair).");
    }

    /**
     * Tests that no special effects are activated when only standard
     * recipe ingredients (like Carrot and Mistletoe) are present.
     */
    @Test
    void brewShouldNotActivateAnySpecialEffect() {
        Cauldron c = new Cauldron();
        c.addIngredient(FoodType.CARROT.create());
        c.addIngredient(FoodType.MISTLETOE.create());

        c.brew();
        assertFalse(c.isNourishing());
        assertFalse(c.causesDuplication());
        assertFalse(c.causesLycanthropy());
    }


    /**
     * Verifies that {@code takeLadle()} returns a full dose (1.0)
     * after a successful brew, and returns 0.0 when the potion is depleted.
     */
    @Test
    void takeLadleShouldReturnFullDoseAfterSuccessfulBrew() {
        PerishableFood fish = (PerishableFood) FoodType.FISH.create();
        fish.passTime();
        addFullRecipeMinusFish();
        cauldron.addIngredient(fish);
        cauldron.brew();

        assertEquals(1.0, cauldron.takeLadle(), 0.0001, "First ladle should return 1.0 dose.");

        for (int i = 0; i < 9; i++) {
            cauldron.takeLadle();
        }

        assertEquals(0.0, cauldron.takeLadle(), 0.0001, "Ladle should return 0.0 when potion is depleted.");
    }

    /**
     * Tests that {@code takeLadle()} returns 0.0 if the cauldron has not
     * been successfully brewed.
     */
    @Test
    void takeLadleShouldReturnZeroBeforeBrew() {
        assertEquals(0.0, cauldron.takeLadle(), 0.0001, "Ladle should return 0.0 if not brewed successfully.");
    }

    /**
     * Tests that adding a {@code null} ingredient is safely ignored
     * and does not change the internal ingredient list size.
     */
    @Test
    void addIngredientShouldNotAllowNull() {
        int initialSize = cauldron.getIngredients().size();
        cauldron.addIngredient(null);
        assertEquals(initialSize, cauldron.getIngredients().size(), "Null ingredient should be ignored.");
    }
}