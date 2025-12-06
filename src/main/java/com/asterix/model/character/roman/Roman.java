package com.asterix.model.character.roman;

import com.asterix.model.character.Character;
import com.asterix.model.character.Gender;
import com.asterix.model.item.Food;

/**
 * Abstract base class for all Roman characters in the simulation.
 * <p>
 * Romans share the common attributes defined in {@link Character}
 * and also have a specific behaviour regarding food and magic potion.
 * </p>
 */
public abstract class Roman extends Character {

    /**
     * Creates a new Roman character with the given attributes.
     *
     * @param name     the character's name
     * @param age      the character's age
     * @param height   the character's height
     * @param strength the character's strength value
     * @param stamina  the character's stamina value
     * @param gender   the character's gender
     */
    protected Roman(String name,
                    int age,
                    double height,
                    double strength,
                    double stamina,
                    Gender gender) {
        super(name, age, height, strength, stamina, gender);
    }

    /**
     * Roman eats a {@link Food} item.
     * <p>
     * For now, the basic behaviour is:
     * <ul>
     *     <li>increase health by the food score (clamped to {@link #MAX_HEALTH})</li>
     *     <li>slightly reduce hunger</li>
     *     <li>print a message to the console</li>
     * </ul>
     * This can be refined later according to game design rules.
     * </p>
     *
     * @param food the food consumed by this Roman
     */
    public void eat(Food food) {
        if (food == null) {
            return;
        }

        // First, check if this food is allowed for Romans
        if (!food.canBeEatenByRoman()) {
            System.out.println(this.name + " cannot eat " + food.getName()
                    + " (not suitable for Romans).");
            // Optional: you could increase hunger or apply a small penalty instead
            return;
        }

        int score = food.getScore();

        // Update health (bounded between 0 and MAX_HEALTH)
        this.health = Math.max(0.0, Math.min(MAX_HEALTH, this.health + score));

        // Basic rule for hunger: eating always reduces hunger a bit
        this.hunger = Math.max(0.0, this.hunger - Math.abs(score) / 2.0);

        System.out.println(this.name + " eats " + food.getName()
                + " and gains " + score + " points of health.");
    }


    /**
     * Consumes a dose of magic potion.
     * <p>
     * By default, drinking potion increases the internal {@code potionLevel}
     * and prints an informational message. Subclasses may extend this
     * behaviour (for example by boosting strength temporarily).
     * </p>
     *
     * @param dose quantity of potion drunk (must be positive)
     */
    public void drinkPotion(double dose) {
        if (dose <= 0.0) {
            return;
        }

        this.potionLevel += dose;

        String unit = (dose == 1.0) ? "dose" : "doses";
        System.out.println(this.name + " drinks " + dose + " " + unit + " of magic potion!");
    }

    @Override
    public String toString() {
        return "Roman [Name=" + name
                + ", Health=" + health
                + ", Strength=" + strength
                + ", Stamina=" + stamina
                + ", PotionLevel=" + potionLevel
                + "]";
    }
}
