package com.asterix.model.character;

import com.asterix.model.item.Food;

/**
 * Abstract basic class representing any character in the simulation
 * Defines common attributes shared by gauls, romans, and creatures
 * References TD3
 */
public abstract class Character {

    // characters attributes defined in TD3
    protected String name;
    protected Gender gender;
    protected double height;
    protected int age;
    protected double strength;
    protected double stamina;
    protected double health;
    public static final double MAX_HEALTH = 100.0;
    public double hunger;
    protected double belligerence;
    protected double potionLevel;

    /**
     * Constructor for the abstract Character class
     * Initializes dynamic attributes with default values
     */
    public Character(String name, int age, double height, double strength, double stamina, Gender gender) {
        this.name = name;
        this.age = age;
        this.height = height;
        this.strength = strength;
        this.stamina = stamina;
        this.gender = gender;

        // Initial values
        this.health = MAX_HEALTH; // Full health
        this.hunger = 0.0;        // Not hungry yet
        this.belligerence = 0.0;  // Not in war yet
        this.potionLevel = 0.0;   // No magic effect yet
    }

    // =========================
    //  BASIC STATE QUERIES
    // =========================

    public String getName() {
        return name;
    }

    public double getHealth() {
        return health;
    }

    public double getPotionLevel() {
        return potionLevel;
    }

    /**
     * Returns whether this character is still alive.
     *
     * @return {@code true} if health is greater than 0, {@code false} otherwise
     */
    public boolean isAlive() {
        return health > 0.0;
    }


    /**
     * Basic combat resolution between this character and an opponent.
     * <p>
     * This method encapsulates the common rule:
     * each participant impacts the other's health using its strength
     * and the opponent's stamina.
     * </p>
     *
     * @param opponent the character to fight against
     */
    public void resolveFight(Character opponent) {
        if (opponent == null || opponent == this) {
            return;
        }
        if (!this.isAlive() || !opponent.isAlive()) {
            return;
        }

        // Simple damage model: impact = strength - (opponent stamina * 0.5)
        double damageToOpponent = Math.max(0.0, this.strength - opponent.stamina * 0.5);
        double damageToSelf     = Math.max(0.0, opponent.strength - this.stamina * 0.5);

        opponent.health = Math.max(0.0, opponent.health - damageToOpponent);
        this.health     = Math.max(0.0, this.health - damageToSelf);

        // Optionally: check for death
        if (!opponent.isAlive()) {
            opponent.die();
        }
        if (!this.isAlive()) {
            this.die();
        }
    }

    /**
     * Heals this character by a given amount.
     *
     * @param amount points of health to restore (ignored if non-positive)
     */
    public void heal(double amount) {
        if (!isAlive() || amount <= 0.0) {
            return;
        }
        this.health = Math.min(MAX_HEALTH, this.health + amount);
    }

    /**
     * Basic eating behaviour: reduces hunger when the character eats.
     * <p>
     * Subclasses can override this method to apply additional effects
     * (e.g. Gauls or Romans using {@link Food#getScore()} differently).
     * </p>
     *
     * @param food the food consumed
     */
    public void eat(Food food) {
        if (!isAlive() || food == null) {
            return;
        }

        // Default rule: eating always reduces hunger a bit
        this.hunger = Math.max(0.0, this.hunger - 10.0);
    }

    /**
     * Basic rule for drinking magic potion: simply increases potion level.
     * <p>
     * Subclasses (e.g. Gauls, Druids) can override this to apply
     * specific effects on strength or stamina.
     * </p>
     *
     * @param dose quantity of potion drunk
     */
    public void drinkPotion(double dose) {
        if (!isAlive() || dose <= 0.0) {
            return;
        }
        this.potionLevel += dose;
    }

    /**
     * Marks this character as dead by setting health to 0.
     * <p>
     * Higher-level logic (place, theater) can later remove dead characters
     * from collections or trigger additional events.
     * </p>
     */
    public void die() {
        this.health = 0.0;
        System.out.println(this.name + " has died.");
    }

    // Abstract method to force subclasses to implement a string representation
    @Override
    public abstract String toString();
}
