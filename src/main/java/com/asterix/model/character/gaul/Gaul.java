package com.asterix.model.character.gaul;

import com.asterix.model.character.Character;
import com.asterix.model.character.Gender;
import com.asterix.model.item.Food;

/**
 * Represents a generic Gaul character in the village.
 * <p>
 * This abstract class defines the common characteristics of all Gauls.
 * The specific trait of Gauls is their ability to drink magic potion to increase their strength temporarily.
 * </p>
 */
public abstract class Gaul extends Character {

    /**
     * Constructs a new Gaul character with the specified attributes.
     *
     * @param name     The name of the Gaul.
     * @param age      The age of the Gaul in years.
     * @param height   The height of the Gaul in meters.
     * @param strength The physical strength of the Gaul.
     * @param stamina  The stamina/endurance of the Gaul.
     * @param gender   The gender of the Gaul.
     */
    public Gaul(String name, int age, double height, double strength, double stamina, Gender gender) {
        super(name, age, height, strength, stamina, gender);
    }

    /**
     * Consumes a dose of magic potion.
     * <p>
     * This action increases the {@code potionLevel} attribute of the character.
     * Only Gauls have access to this power-up mechanism in the simulation.
     * </p>
     *
     * @param dose The quantity of potion drunk (e.g., 1.0 for a standard ladle).
     */
    public void drinkPotion(double dose) {
        this.potionLevel += dose;

        // if dose=1.0, write "dose". else write "doses"
        String unit = (dose == 1.0) ? "dose" : "doses";
        System.out.println(this.name + " drinks " + dose + " " + unit + " of magic potion!");
    }

    /**
     * Makes the Gaul eat a specific food item.
     * <p>
     * This method checks if the food is suitable for a Gaul (e.g., Boar, Fish).
     * If suitable, it restores health (capped at MAX_HEALTH) and reduces hunger.
     * </p>
     *
     * @param food The food object to consume. If null or unsuitable, no action is taken.
     */
    public void eat(Food food) {
        if (food == null) {
            return;
        }

        // Check if this food is specifically allowed for Gauls (Boar, Fish, etc.)
        // Ensure your Food class has this method corresponding to the Roman one.
        if (!food.canBeEatenByGaul()) {
            System.out.println(this.name + " cannot eat " + food.getName() + " (not suitable for Gauls, by Toutatis!).");
            return;
        }

        int score = food.getScore();

        // Update health (bounded between 0 and MAX_HEALTH inherited from Character)
        this.health = Math.max(0.0, Math.min(MAX_HEALTH, this.health + score));

        // Basic rule for hunger: eating always reduces hunger a bit
        this.hunger = Math.max(0.0, this.hunger - Math.abs(score) / 2.0);

        System.out.println(this.name + " eats " + food.getName() + " and gains " + score + " points of health.");
    }

    /**
     * Returns the current health points of the Gaul.
     *
     * @return The current health value.
     */
    @Override
    public double getHealth() {
        return this.health;
    }
}