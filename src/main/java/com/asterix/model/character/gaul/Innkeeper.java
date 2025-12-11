package com.asterix.model.character.gaul;

import com.asterix.model.ability.Worker;
import com.asterix.model.character.Gender;

/**
 * Represents an Innkeeper in the Gaulish village.
 * <p>
 * The Innkeeper is responsible for running the tavern, serving beverages (cervoise)
 * and food (wild boar) to other villagers.
 * This class implements the {@link Worker} interface to perform its daily duties.
 * </p>
 */
public class Innkeeper extends Gaul implements Worker {

    /**
     * Constructs a new Innkeeper character.
     * Uses the parent {@link Gaul} constructor.
     *
     * @param name     The name of the innkeeper.
     * @param age      The age of the innkeeper.
     * @param height   The height of the innkeeper in meters.
     * @param strength The physical strength of the innkeeper.
     * @param stamina  The stamina/endurance of the innkeeper.
     * @param gender   The gender of the innkeeper.
     */
    public Innkeeper(String name, int age, double height, double strength, double stamina, Gender gender) {
        super(name, age, height, strength, stamina, gender);
    }

    /**
     * Implementation of the {@link Worker} interface.
     * <p>
     * The Innkeeper's work involves cleaning the tavern counter and serving
     * drinks and food to customers.
     * </p>
     */
    @Override
    public void work() {
        System.out.println(this.getName() + " cleans the counter and brings out the mugs");
        System.out.println("\"Who wants beer and wildboar ?\"");
    }

    /**
     * Returns a string representation of the Innkeeper.
     *
     * @return A string containing the class name "Innkeeper" followed by the Gaul details.
     */
    @Override
    public String toString() {
        return "Innkeeper " + super.toString();
    }

    /**
     * Returns the current health points of the innkeeper.
     *
     * @return The current health value.
     */
    @Override
    public double getHealth() {
        return this.health;
    }
}