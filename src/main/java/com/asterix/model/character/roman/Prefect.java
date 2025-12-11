package com.asterix.model.character.roman;

import com.asterix.model.ability.Leader;
import com.asterix.model.character.Character;
import com.asterix.model.character.Gender;

/**
 * Represents a Roman prefect.
 * <p>
 * A {@code Prefect} is primarily an administrative or political leader.
 * Prefects must be able to lead but are not required to fight.
 * </p>
 */
public class Prefect extends Roman implements Leader {

    /**
     * Creates a new Roman prefect with the given attributes.
     *
     * @param name     the character's name
     * @param age      the character's age
     * @param height   the character's height
     * @param strength the character's strength value
     * @param stamina  the character's stamina value
     * @param gender   the character's gender
     */
    public Prefect(String name,
                   int age,
                   double height,
                   double strength,
                   double stamina,
                   Gender gender) {
        super(name, age, height, strength, stamina, gender);
    }

    /**
     * Performs a leadership action.
     * <p>
     * For now, this method just prints that the prefect is managing
     * the city and issuing civil orders.
     * </p>
     */
    @Override
    public void command() {
        System.out.println(name + " (Prefect) manages the Roman city and issues civil orders.");
    }

    /**
     * Returns a string representation of the Prefect.
     *
     * @return A string containing the class name "Prefect" followed by the Roman details.
     */
    @Override
    public String toString() {
        return "Prefect " + super.toString();
    }

    /**
     * Returns the current health points of the prefect.
     *
     * @return The current health value.
     */
    @Override
    public double getHealth() {
        return this.health;
    }
}