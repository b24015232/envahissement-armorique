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
     * [cite_start]Resolves a combat round between this character and an opponent[cite: 680, 681].
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

        double damageToOpponent = Math.max(0.0, this.strength - opponent.stamina * 0.5);
        double damageToSelf = Math.max(0.0, opponent.strength - this.stamina * 0.5);

        opponent.health = Math.max(0.0, opponent.health - damageToOpponent);
        this.health = Math.max(0.0, this.health - damageToSelf);

        if (!opponent.isAlive()) {
            opponent.die();
        }
        if (!this.isAlive()) {
            this.die();
        }
    }

    /**
     * [cite_start]Heals this character by a given amount[cite: 682].
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
     * [cite_start]Reduces hunger when the character consumes food[cite: 683].
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
     * [cite_start]Increases the potion effect level[cite: 684].
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
     * [cite_start]Sets the health to 0 and prints a death message[cite: 685].
     */
    public void die() {
        this.health = 0.0;
        System.out.println(this.name + " has died.");
    }

    /**
     * [cite_start]Returns a formatted string representation of the character's statistics [cite: 663-677].
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
}