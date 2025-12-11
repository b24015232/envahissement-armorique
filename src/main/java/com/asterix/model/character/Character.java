package com.asterix.model.character;

import com.asterix.model.item.Food;

/**
 * Abstract basic class representing any character in the simulation.
 * <p>
 * Defines common attributes shared by Gauls, Romans, and creatures.
 * This class serves as the foundation for all entities capable of interacting
 * within the simulation (fighting, eating, moving).
 * </p>
 */
public abstract class Character {

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
     * Constructs a new Character with specific physical attributes.
     *
     * @param name     The name of the character.
     * @param age      The age of the character.
     * @param height   The height of the character in meters.
     * @param strength The physical strength of the character.
     * @param stamina  The stamina/endurance of the character.
     * @param gender   The gender of the character.
     */
    public Character(String name, int age, double height, double strength, double stamina, Gender gender) {
        this.name = name;
        this.age = age;
        this.height = height;
        this.strength = strength;
        this.stamina = stamina;
        this.gender = gender;
        this.health = MAX_HEALTH;
        this.hunger = 0.0;
        this.belligerence = 0.0;
        this.potionLevel = 0.0;
    }

    /**
     * Gets the name of the character.
     *
     * @return The character's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the character.
     *
     * @param name The new name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the gender of the character.
     *
     * @return The character's gender.
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * Sets the gender of the character.
     *
     * @param gender The new gender.
     */
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    /**
     * Gets the height of the character.
     *
     * @return The height in meters.
     */
    public double getHeight() {
        return height;
    }

    /**
     * Sets the height of the character.
     *
     * @param height The new height in meters.
     */
    public void setHeight(double height) {
        this.height = height;
    }

    /**
     * Gets the age of the character.
     *
     * @return The age in years.
     */
    public int getAge() {
        return age;
    }

    /**
     * Sets the age of the character.
     *
     * @param age The new age.
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Gets the physical strength of the character.
     *
     * @return The strength value.
     */
    public double getStrength() {
        return strength;
    }

    /**
     * Sets the physical strength of the character.
     *
     * @param strength The new strength value.
     */
    public void setStrength(double strength) {
        this.strength = strength;
    }

    /**
     * Gets the stamina of the character.
     *
     * @return The stamina value.
     */
    public double getStamina() {
        return stamina;
    }

    /**
     * Sets the stamina of the character.
     *
     * @param stamina The new stamina value.
     */
    public void setStamina(double stamina) {
        this.stamina = stamina;
    }

    /**
     * Gets the current health points of the character.
     *
     * @return The health value.
     */
    public double getHealth() {
        return health;
    }

    /**
     * Sets the current health points of the character.
     *
     * @param health The new health value.
     */
    public void setHealth(double health) {
        this.health = health;
    }

    /**
     * Gets the current hunger level of the character.
     *
     * @return The hunger value.
     */
    public double getHunger() {
        return hunger;
    }

    /**
     * Sets the hunger level of the character.
     *
     * @param hunger The new hunger value.
     */
    public void setHunger(double hunger) {
        this.hunger = hunger;
    }

    /**
     * Increases the hunger of the character by a specific amount.
     *
     * @param amount The amount to add to the current hunger.
     */
    public void increaseHunger(double amount) {
        this.hunger += amount;
    }

    /**
     * Gets the belligerence level of the character.
     *
     * @return The belligerence value.
     */
    public double getBelligerence() {
        return belligerence;
    }

    /**
     * Sets the belligerence level of the character.
     *
     * @param belligerence The new belligerence value.
     */
    public void setBelligerence(double belligerence) {
        this.belligerence = belligerence;
    }

    /**
     * Gets the current level of magic potion effect.
     *
     * @return The potion level.
     */
    public double getPotionLevel() {
        return potionLevel;
    }

    /**
     * Sets the magic potion effect level.
     *
     * @param potionLevel The new potion level.
     */
    public void setPotionLevel(double potionLevel) {
        this.potionLevel = potionLevel;
    }

    /**
     * Returns whether this character is still alive.
     *
     * @return {@code true} if health is greater than 0, {@code false} otherwise.
     */
    public boolean isAlive() {
        return health > 0.0;
    }

    /**
     * Resolves a combat round between this character and an opponent.
     * <p>
     * Logic:
     * 1. Checks if both are alive and valid.
     * 2. Calculates damage based on attacker's strength minus half opponent's stamina.
     * 3. Applies damage to health.
     * 4. Triggers death if health drops to zero.
     * </p>
     *
     * @param opponent The character to fight against.
     */
    public void resolveFight(Character opponent) {
        if (opponent == null || opponent == this) {
            return;
        }
        if (!this.isAlive() || !opponent.isAlive()) {
            return;
        }

        // 1. Determine multipliers
        // If potionLevel > 0, strength is multiplied by 5
        double myMultiplier = (this.potionLevel > 0) ? 5.0 : 1.0;
        double opponentMultiplier = (opponent.potionLevel > 0) ? 5.0 : 1.0;

        // 2. Calculate Damage
        double damageToOpponent = Math.max(0.0, (this.strength * myMultiplier) - opponent.stamina * 0.5);
        double damageToSelf = Math.max(0.0, (opponent.strength * opponentMultiplier) - this.stamina * 0.5);

        // 3. Apply Invincibility
        // If potionLevel > 0, damage received is 0
        if (this.potionLevel > 0) {
            damageToSelf = 0.0;
            System.out.println(this.name + " is invincible thanks to the magic potion!");
        }
        if (opponent.potionLevel > 0) {
            damageToOpponent = 0.0;
            System.out.println(opponent.getName() + " is invincible thanks to the magic potion!");
        }

        opponent.health = Math.max(0.0, opponent.health - damageToOpponent);
        this.health = Math.max(0.0, this.health - damageToSelf);

        // Consume potion effect after fight
        this.consumePotionEffect();
        opponent.consumePotionEffect();

        if (!opponent.isAlive()) {
            opponent.die();
        }
        if (!this.isAlive()) {
            this.die();
        }
    }

    /**
     * Helper method to reduce potion level after an action.
     * Decreases the level by a small amount (e.g. 0.5) to simulate duration wearing off.
     */
    protected void consumePotionEffect() {
        if (this.potionLevel > 0) {
            this.potionLevel = Math.max(0.0, this.potionLevel - 0.5);
            if (this.potionLevel == 0) {
                System.out.println(this.name + "'s magic potion effect has worn off.");
            }
        }
    }

    /**
     * Heals this character by a given amount.
     *
     * @param amount Points of health to restore (ignored if non-positive).
     */
    public void heal(double amount) {
        if (!isAlive() || amount <= 0.0) {
            return;
        }
        this.health = Math.min(MAX_HEALTH, this.health + amount);
    }

    /**
     * Reduces hunger when the character consumes food.
     *
     * @param food The food item consumed.
     */
    public void eat(Food food) {
        if (!isAlive() || food == null) {
            return;
        }
        this.hunger = Math.max(0.0, this.hunger - 10.0);
    }

    /**
     * Increases the potion effect level.
     *
     * @param dose The quantity of potion consumed.
     */
    public void drinkPotion(double dose) {
        if (!isAlive() || dose <= 0.0) {
            return;
        }
        this.potionLevel += dose;
    }

    /**
     * Sets the health to 0 and prints a death message.
     */
    public void die() {
        this.health = 0.0;
        System.out.println(this.name + " has died.");
    }

    /**
     * Returns a formatted string representation of the character's statistics.
     *
     * @return A formatted string with stats.
     */
    @Override
    public String toString() {
        return String.format(
                "%-15s | %-6s | Age: %-3d | 📏 %.2fm | ❤️ HP: %-5.1f | 🍖 Hunger: %-5.1f | 💪 Str: %-5.1f | 🏃 Sta: %-5.1f | 🧪 Potion: %.1f",
                this.name,
                this.gender,
                this.age,
                this.height,
                this.health,
                this.hunger,
                this.strength,
                this.stamina,
                this.potionLevel
        );
    }

    public void passTime() {
        if (!isAlive()) return;

        // 1. Natural hunger increase (Time makes you hungry)
        this.increaseHunger(2.0);

        // 2. Potion wears off naturally over time (not just in combat)
        this.consumePotionEffect();
    }
}