package com.asterix.model.character;

import com.asterix.model.item.Cauldron;
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
    /** Maximum health points a character can have. */
    public static final double MAX_HEALTH = 100.0;
    public double hunger;
    protected double belligerence;
    protected double potionLevel;

    // --- New attributes for advanced potion effects (TD) ---

    /** Tracks the total amount of potion consumed to trigger the "Statue" effect. */
    protected double lifetimePotionDoses = 0;
    /** Indicates if the character has turned into a granite statue. */
    protected boolean isStatue = false;
    /** Indicates if the character has transformed into a lycanthrope. */
    protected boolean isLycanthrope = false;

    /**
     * Constructs a new Character with specific physical attributes.
     *
     * @param name     The name of the character.
     * @param age      The age of the character in years.
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
     * Gets the gender of the character.
     *
     * @return The character's gender.
     */
    public Gender getGender() { return gender; }

    /**
     * Increases the potion level by a specific dose.
     * <p>
     * This is a legacy/direct method to boost potion level without side effects.
     * </p>
     *
     * @param dose The amount of potion to add.
     */
    public void drinkPotion(double dose) {
        if (!isAlive()) return;
        this.potionLevel += dose;
    }

    /**
     * Drinks a ladle from a specific cauldron, triggering complex side effects.
     * <p>
     * Rules implemented:
     * <ul>
     * <li><b>Statue:</b> If lifetime consumption exceeds ~20 doses, the character becomes a statue.</li>
     * <li><b>Nourishing:</b> If the cauldron is nourishing, hunger is reset to 0.</li>
     * <li><b>Lycanthropy:</b> Triggers transformation into a werewolf.</li>
     * <li><b>Duplication:</b> Returns a signal string to create a clone.</li>
     * </ul>
     * </p>
     *
     * @param cauldron The cauldron source.
     * @return "DUPLICATE" if the duplication effect is triggered, "NONE" otherwise.
     */
    public String drinkPotionFromCauldron(Cauldron cauldron) {
        if (!isAlive() || cauldron == null) return "NONE";

        double dose = cauldron.takeLadle();
        if (dose <= 0) return "NONE";

        System.out.println(this.name + " drinks magic potion.");
        this.drinkPotion(dose);

        this.lifetimePotionDoses += dose;
        if (this.lifetimePotionDoses >= 20.0) {
            this.isStatue = true;
            this.potionLevel = 0;
            System.out.println(this.name + " turns into granite statue");
            return "NONE";
        }

        // Apply special effects based on Cauldron flags
        if (cauldron.isNourishing()) {
            this.hunger = 0.0;
            System.out.println(this.name + " is fully fed !");
        }
        if (cauldron.causesLycanthropy()) {
            this.isLycanthrope = true;
            System.out.println(this.name + " transforms into a lycanthrope !");
        }
        if (cauldron.causesDuplication()) {
            return "DUPLICATE";
        }
        return "NONE";
    }

    // --- Getters ---

    /**
     * Gets the name of the character.
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the current magic potion effect level.
     * @return The potion level.
     */
    public double getPotionLevel() {
        return potionLevel;
    }

    /**
     * Checks if the character is alive.
     * <p>
     * A character is alive if their health > 0 and they are not a statue.
     * </p>
     * @return {@code true} if alive, {@code false} otherwise.
     */
    public boolean isAlive() {
        return health > 0.0 && !isStatue;
    }

    /**
     * Checks if the character has been turned into a statue.
     * @return {@code true} if the character is a statue.
     */
    public boolean isStatue() {
        return isStatue;
    }

    /**
     * Checks if the character has been transformed into a lycanthrope.
     * @return {@code true} if the character is a lycanthrope.
     */
    public boolean isLycanthrope() {
        return isLycanthrope;
    }

    public int getAge() {
        return age;
    }

    public double getHeight() {
        return height;
    }

    public double getStrength() {
        return strength;
    }

    public double getStamina() {
        return stamina;
    }

    // --- Setters ---

    public void setHunger(double h) {
        this.hunger = h;
    }

    public double getHunger() {
        return hunger;
    }

    public double getBelligerence() {
        return belligerence;
    }

    public void setBelligerence(double b) {
        this.belligerence = b;
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
     * Simulates the passage of time for this character.
     * <p>
     * Increases hunger and decreases potion level naturally.
     * </p>
     */
    public void passTime() {
        if (!isAlive()) return;
        this.hunger += 2.0;
        if (potionLevel > 0) potionLevel = Math.max(0, potionLevel - 0.5);
    }

    /**
     * Resolves a combat round against an opponent.
     *
     * @param opponent The character to fight.
     */
    public void resolveFight(Character opponent) {
        if(isAlive() && opponent.isAlive()) {
            opponent.health -= 5;
        }
    }

    /**
     * Heals the character by a given amount.
     *
     * @param amount Health points to restore.
     */
    public void heal(double amount) {
        if(isAlive()) this.health = Math.min(MAX_HEALTH, health + amount);
    }

    /**
     * Eats a food item to reduce hunger.
     *
     * @param food The food to eat.
     */
    public void eat(Food food) {
        if(isAlive()) this.hunger = 0;
    }

    /**
     * Sets the character's health to 0.
     */
    public void die() {
        this.health = 0;
    }

    /**
     * Returns a string representation of the character.
     * Includes status tags like [STATUE] or [WOLF].
     *
     * @return Formatted string.
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