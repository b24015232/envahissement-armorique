package com.asterix.model.character.gaul;

import com.asterix.model.character.Character;
import com.asterix.model.character.Gender;

/**
 * Represents a generic Gaul character in the village.
 * <p>
 * This abstract class defines the common characteristics of all Gauls.
 * The specific trait of Gauls is their ability to drink magic potion to increase their strength temporarily.
 * </p>
 */
public abstract class Gaul extends Character {

    /**
     * Constructs a new Gaul character.
     *
     * @param name     The name of the Gaul.
     * @param age      The age of the Gaul.
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
     * Returns a string representation of the Gaul.
     *
     * @return A string containing the name, health, and strength of the character.
     */
    @Override
    public String toString() {
        return "Gaul [Name=" + name + ", Health=" + health + ", Strength=" + strength + "]";
    }
}