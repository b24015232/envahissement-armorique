package com.asterix.model.character.gaul;

import com.asterix.model.ability.Worker;
import com.asterix.model.character.Gender;

/**
 * Represents a Blacksmith in the Gaulish village.
 * <p>
 * The Blacksmith is a specialized Gaul who crafts weapons and tools.
 * This class implements the {@link Worker} interface to define the specific working behavior.
 * </p>
 */
public class BlackSmith extends Gaul implements Worker {

    /**
     * Constructs a new BlackSmith character with the specified attributes.
     * Uses the parent {@link Gaul} constructor.
     *
     * @param name     The name of the blacksmith.
     * @param age      The age of the blacksmith.
     * @param height   The height of the blacksmith in meters.
     * @param strength The physical strength of the blacksmith (used during work).
     * @param stamina  The stamina/endurance of the blacksmith.
     * @param gender   The gender of the blacksmith.
     */
    public BlackSmith(String name, int age, double height, double strength, double stamina, Gender gender) {
        super(name, age, height, strength, stamina, gender);
    }

    /**
     * Implementation of the {@link Worker} interface.
     * <p>
     * The Blacksmith uses his physical strength to hammer metal and forge weapons.
     * </p>
     */
    @Override
    public void work() {
        // Using internal attributes to make the text dynamic
        System.out.println(this.getName() + " raises his hammer with his strength of " + this.strength + "...");
        System.out.println("A new sword is being forged for the glory of the village");
    }

    /**
     * Returns a string representation of the BlackSmith.
     *
     * @return A string containing the class name "BlackSmith" followed by the Gaul details.
     */
    @Override
    public String toString() {
        return "BlackSmith " + super.toString();
    }

    /**
     * Returns the current health points of the blacksmith.
     *
     * @return The current health value.
     */
    @Override
    public double getHealth() {
        return this.health;
    }
}