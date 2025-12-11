package com.asterix.model.character.roman;

import com.asterix.model.ability.Leader;
import com.asterix.model.character.Character;
import com.asterix.model.character.Gender;

/**
 * Represents a Roman general.
 * <p>
 * A {@code General} is both a powerful fighter and a leader who can
 * command Roman troops.
 * </p>
 */
public class General extends Soldier implements Leader {

    /**
     * Internal identifier for this general.
     */
    private final int id;

    /**
     * Creates a new Roman general with the given attributes and identifier.
     *
     * @param id       internal identifier for the general
     * @param name     the character's name
     * @param age      the character's age
     * @param height   the character's height
     * @param strength the character's strength value
     * @param stamina  the character's stamina value
     * @param gender   the character's gender
     */
    public General(int id,
                   String name,
                   int age,
                   double height,
                   double strength,
                   double stamina,
                   Gender gender) {
        super(name, age, height, strength, stamina, gender);
        this.id = id;
    }

    /**
     * Returns the internal identifier of this general.
     *
     * @return the general id
     */
    public int getId() {
        return id;
    }

    /**
     * Performs a leadership action.
     * <p>
     * For now, this method just prints an informative message.
     * </p>
     */
    @Override
    public void command() {
        System.out.println(name + " (General) leads the Roman troops into battle!");
    }

    /**
     * Engages in combat with another character.
     *
     * @param opponent the character to fight against
     */
    @Override
    public void fight(Character opponent) {
        if (opponent == null) {
            System.out.println(name + " (General) prepares a strike, but there is no opponent.");
            return;
        }

        System.out.println(name + " (General) strategically attacks " + opponent.getName() + "!");
        resolveFight(opponent);
    }

    /**
     * Specific attack action for the general.
     * <p>
     * This can be triggered by higher-level game logic to represent
     * a special offensive maneuver.
     * </p>
     */
    public void attack() {
        System.out.println(name + " (General) orders a powerful offensive maneuver!");
    }
}

