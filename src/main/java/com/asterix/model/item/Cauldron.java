package com.asterix.model.item;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the druid's cauldron containing the mixture of ingredients.
 * <p>
 * This class handles the brewing process of the magic potion. It stores ingredients,
 * validates the recipe, and determines specific effects (nourishing, duplication,
 * [cite_start]lycanthropy) based on the added ingredients[cite: 41, 48, 53].
 * </p>
 */
public class Cauldron {

    /**
     * The list of ingredients currently in the cauldron.
     */
    private final List<Food> ingredients;

    /**
     * The number of doses available in the cauldron.
     * [cite_start]A full cauldron contains several doses[cite: 50].
     */
    private double doses;

    /**
     * Indicates if the potion has been successfully brewed and is ready to be served.
     */
    private boolean isReady;

    // base before effects
    /** Flag indicating if the potion restores hunger to zero. */
    private boolean isNourishing = false;
    /** Flag indicating if the potion causes the drinker to duplicate. */
    private boolean causesDuplication = false;
    /** Flag indicating if the potion transforms the drinker into a lycanthrope. */
    private boolean causesLycanthropy = false;

    /**
     * Constructs a new empty Cauldron.
     * Initializes the ingredient list and sets the state to not ready.
     */
    public Cauldron() {
        this.ingredients = new ArrayList<>();
        this.doses = 0;
        this.isReady = false;
    }

    /**
     * Adds an ingredient to the cauldron.
     * <p>
     * Ingredients can only be added before the brewing process is complete.
     * </p>
     *
     * @param food The food item to add.
     */
    public void addIngredient(Food food) {
        if (isReady) {
            System.out.println("The potion is ready! No more ingredients.");
            return;
        }
        if (food != null) {
            this.ingredients.add(food);
            System.out.println(food.getName() + " falls into the cauldron.");
        }
    }

    /**
     * Attempts to brew the magic potion.
     * <p>
     * This method validates the recipe based on the ingredients present.
     * If the recipe is correct, the cauldron becomes ready with 10 doses
     * and special effects are calculated. Otherwise, the mixture is ruined.
     * </p>
     *
     * @return {@code true} if the potion was successfully brewed, {@code false} otherwise.
     */
    public boolean brew() {
        if (checkRecipe()) {
            this.isReady = true;
            this.doses = 10.0;
            checkSpecialEffects();
            System.out.println("The potion is ready !");
            return true;
        } else {
            System.out.println("It's a nasty soup...");
            this.isReady = false;
            this.doses = 0;
            return false;
        }
    }

    /**
     * Analyzes ingredients to determine special effects.
     * <p>
     * Rules implemented:
     * <ul>
     * [cite_start]<li>Lobster, Strawberries, or Beet juice make the potion nourishing[cite: 49].</li>
     * [cite_start]<li>Two-headed unicorn milk adds the power of duplication[cite: 53].</li>
     * [cite_start]<li>Idéfix's Hair adds the power of metamorphosis (lycanthropy)[cite: 53].</li>
     * </ul>
     * </p>
     */
    private void checkSpecialEffects() {
        for (Food f : ingredients) {
            String name = f.getName();
            if (name.equals("Lobster") || name.equals("Strawberries") || name.equals("Beet juice")) {
                this.isNourishing = true;
            }
            if (name.equals("Two headed unicorn milk")) {
                this.causesDuplication = true;
            }
            if (name.equals("Idefix's Hair")) {
                this.causesLycanthropy = true;
            }
        }
    }

    /**
     * Validates the basic recipe.
     * <p>
     * [cite_start]Currently checks for the mandatory presence of Mistletoe[cite: 48].
     * </p>
     *
     * @return {@code true} if the base recipe is valid.
     */
    private boolean checkRecipe() {
        for (Food f : ingredients) {
            if (f.getName().equals("Mistletoe")) return true;
        }
        return false;
    }

    /**
     * Serves a ladle of magic potion.
     *
     * @return 1.0 dose if available, 0.0 otherwise.
     */
    public double takeLadle() {
        if (isReady && doses > 0) {
            doses -= 1.0;
            return 1.0;
        }
        return 0.0;
    }

    /**
     * Returns a copy of the ingredients list for inspection.
     *
     * @return The list of ingredients.
     */
    public List<Food> getIngredients() { return new ArrayList<>(ingredients); }

    /**
     * Checks if the potion is nourishing.
     *
     * @return {@code true} if the potion restores full hunger.
     */
    public boolean isNourishing() {
        return isNourishing;
    }

    /**
     * Checks if the potion causes duplication.
     *
     * @return {@code true} if the potion creates a clone.
     */
    public boolean causesDuplication() {
        return causesDuplication;
    }

    /**
     * Checks if the potion causes lycanthropy.
     *
     * @return {@code true} if the potion transforms the drinker into a creature.
     */
    public boolean causesLycanthropy() {
        return causesLycanthropy;
    }
}