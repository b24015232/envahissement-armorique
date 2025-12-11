package com.asterix.model.character.roman;

import com.asterix.model.ability.Fighter;
import com.asterix.model.character.Gender;

/**
 * Abstract base class for all Roman fighters (legionaries, generals, ...).
 * <p>
 * A {@code Soldier} is a Roman character that can engage in combat.
 * Concrete subclasses must implement the {@link #fight} method.
 * </p>
 */
public abstract class Soldier extends Roman implements Fighter {

    /**
     * Creates a new Roman soldier with the given attributes.
     *
     * @param name     the character's name
     * @param age      the character's age
     * @param height   the character's height
     * @param strength the character's strength value
     * @param stamina  the character's stamina value
     * @param gender   the character's gender
     */
    protected Soldier(String name,
                      int age,
                      double height,
                      double strength,
                      double stamina,
                      Gender gender) {
        super(name, age, height, strength, stamina, gender);
    }

    @Override
    public String toString() {
        return "Soldier " + super.toString();
    }
}
