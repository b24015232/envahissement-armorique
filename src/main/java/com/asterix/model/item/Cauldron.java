package com.asterix.model.item;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the druid's cauldron containing the mixture of ingredients.
 * <p>
 * This class handles the magic potion recipe verification and brewing process.
 * It ensures that all required ingredients are present and in the correct state
 * (e.g., fresh clover, average fish).
 * </p>
 */
public class Cauldron {

    /**
     * The list of ingredients currently in the cauldron.
     */
    private final List<Food> ingredients;

    /**
     * The number of doses available in the cauldron.
     */
    private double doses;

    /**
     * Indicates if the potion has been successfully brewed and is ready to be served.
     */
    private boolean isReady;

    /**
     * Constructs a new empty Cauldron.
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
            System.out.println("The potion is ready ! No more ingredients can be added.");
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
     * If the recipe is correct, the cauldron becomes ready with 10 doses.
     * Otherwise, the mixture is ruined.
     * </p>
     *
     * @return {@code true} if the potion was successfully brewed, {@code false} otherwise.
     */
    public boolean brew() {
        if (checkRecipe()) {
            this.isReady = true;
            this.doses = 10.0; // A full cauldron provides multiple doses
            System.out.println("The potion is ready!");
            return true;
        } else {
            System.out.println("It's a nasty soup...");
            this.isReady = false;
            this.doses = 0;
            return false;
        }
    }

    /**
     * Checks if the current ingredients match the magic potion recipe.
     * <p>
     * Recipe rules:
     * <ul>
     * <li>Mistletoe (mandatory)</li>
     * <li>Carrot (mandatory)</li>
     * <li>Salt (mandatory)</li>
     * <li>Honey (mandatory)</li>
     * <li>Mead (mandatory)</li>
     * <li>Secret Ingredient (mandatory)</li>
     * <li>Clover (must be FRESH)</li>
     * <li>Fish (must be AVERAGE / Partially Fresh)</li>
     * <li>Rock Oil OR Beet Juice (substitution allowed)</li>
     * </ul>
     * </p>
     *
     * @return {@code true} if all conditions are met.
     */
    private boolean checkRecipe() {
        boolean hasMistletoe = false;
        boolean hasCarrot = false;
        boolean hasSalt = false;
        boolean hasFreshClover = false;
        boolean hasAverageFish = false;
        boolean hasOilOrBeet = false;
        boolean hasHoney = false;
        boolean hasMead = false;
        boolean hasSecret = false;

        for (Food f : ingredients) {
            String name = f.getName();
            boolean isFresh = false;
            boolean isAverage = false;

            if (f instanceof PerishableFood perishable) {
                if (perishable.getState() instanceof FreshState) isFresh = true;
                if (perishable.getState() instanceof PartiallyFreshState) isAverage = true;
            } else {
                isFresh = true;
            }

            // Identify ingredients by name
            if (name.equals("Mistletoe")) hasMistletoe = true;
            if (name.equals("Carrot")) hasCarrot = true;
            if (name.equals("Salt")) hasSalt = true;
            if (name.equals("Honey")) hasHoney = true;
            if (name.equals("Mead")) hasMead = true;
            if (name.equals("Secret Ingredient")) hasSecret = true;

            // State-sensitive ingredients
            if (name.equals("Clover") && isFresh) hasFreshClover = true;

            // Fish in Average/PartiallyFresh state
            if (name.equals("Fish") && isAverage) hasAverageFish = true;

            // Substitutions
            if (name.equals("Rock oil") || name.equals("Beet Juice")) hasOilOrBeet = true;
        }

        return hasMistletoe && hasCarrot && hasSalt && hasFreshClover && hasAverageFish && hasOilOrBeet && hasHoney && hasMead && hasSecret;
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
     * @return the list of ingredients.
     */
    public List<Food> getIngredients() {
        return new ArrayList<>(ingredients);
    }
}