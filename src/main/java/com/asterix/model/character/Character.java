package com.asterix.model.character;

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
    protected double hunger;
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
        this.health = 100.0;     // Full health
        this.hunger = 0.0;       // Not hungry yet
        this.belligerence = 0.0; // Not in war yet
        this.potionLevel = 0.0;  // No magic effect yet
    }

    //TODO : Common behaviors (will be filled later)

    public String getName() {
        return name;
    }

    public double getHealth() {
        return health;
    }

    public double getPotionLevel() {
        return potionLevel;
    }

    // Abstract method to force subclasses to implement a string representation
    @Override
    public abstract String toString();
}