package com.asterix.model.character.gaul;

import com.asterix.model.character.Character;
import com.asterix.model.character.Gender;

/**
 * Represents a gaul character
 * Specificity : can drink magic potion
 */
public class Gaul extends Character {

    public Gaul(String name, int age, double height, double strength, double stamina, Gender gender) {
        super(name, age, height, strength, stamina, gender);
    }

    /**
     * Consumes a dose of magic potion
     * Increases the potion level attribute
     * @param dose Quantity of potion drunk
     */
    public void drinkPotion(double dose) {
        this.potionLevel += dose;

        // if dose=1.0, write "dose". else write "doses"
        String unit = (dose == 1.0) ? "dose" : "doses";
        System.out.println(this.name + " drinks " + dose + " " + unit + " of magic potion!");
    }

    @Override
    public String toString() {
        return "Gaul [Name=" + name + ", Health=" + health + ", Strength=" + strength + "]";
    }
}