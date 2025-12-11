package com.asterix.model.character.roman;

import com.asterix.model.character.Character;
import com.asterix.model.character.Gender;

/**
 * Represents a Roman legionary soldier.
 * <p>
 * A {@code Legionnaire} is a basic Roman fighter in the Roman army.
 * </p>
 */
public class Legionnaire extends Soldier {

    /**
     * Creates a new legionnaire with the given attributes.
     *
     * @param name     the character's name
     * @param age      the character's age
     * @param height   the character's height
     * @param strength the character's strength value
     * @param stamina  the character's stamina value
     * @param gender   the character's gender
     */
    public Legionnaire(String name,
                       int age,
                       double height,
                       double strength,
                       double stamina,
                       Gender gender) {
        super(name, age, height, strength, stamina, gender);
    }

    /**
     * Engages in combat with another character.
     * <p>
     * For now, this method only prints a message. Actual combat rules
     * (damage, stamina cost, etc.) are handled by the parent method.
     * </p>
     *
     * @param opponent the character to fight against
     */
    @Override
    public void fight(Character opponent) {
        if (opponent == null) {
            System.out.println(name + " (Legionnaire) swings his gladius, but there is no opponent.");
            return;
        }

        System.out.println(name + " (Legionnaire) attacks " + opponent.getName() + " in close combat!");
        resolveFight(opponent);
    }

    /**
     * Returns a string representation of the Legionnaire.
     *
     * @return A string containing the class name "Legionnaire" followed by the Soldier details.
     */
    @Override
    public String toString() {
        return "Legionnaire " + super.toString();
    }

    /**
     * Returns the current health points of the legionnaire.
     *
     * @return The current health value.
     */
    @Override
    public double getHealth() {
        return this.health;
    }
}