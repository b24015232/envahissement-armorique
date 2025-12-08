package com.asterix.model.character.creature;

import com.asterix.model.character.Character;
import com.asterix.model.character.Gender;

/**
 * Abstract base class for all non-human creatures in the simulation.
 * <p>
 * A {@code Creature} is still a {@link Character}, but it is intended
 * to represent animals, monsters, or supernatural beings
 * (werewolves, lycanthropes, etc.).
 * </p>
 */
public abstract class Creature extends Character {

    /**
     * Indicates whether the creature is currently in a feral/raging state.
     * This can be used later to modulate behaviour or combat.
     */
    protected boolean feral;

    /**
     * Creates a new creature with the given basic attributes.
     *
     * @param name     creature name
     * @param age      creature age
     * @param height   creature height
     * @param strength creature strength
     * @param stamina  creature stamina (resistance to damage)
     * @param gender   creature gender
     */
    protected Creature(String name,
                       int age,
                       double height,
                       double strength,
                       double stamina,
                       Gender gender) {
        super(name, age, height, strength, stamina, gender);
        this.feral = false;
    }

    /**
     * Returns whether the creature is currently in a feral state.
     */
    public boolean isFeral() {
        return feral;
    }

    /**
     * Puts the creature into a feral/raging state.
     */
    public void enterFeralState() {
        this.feral = true;
    }

    /**
     * Brings the creature back to a calmer state.
     */
    public void leaveFeralState() {
        this.feral = false;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()
                + " [Name=" + name
                + ", Health=" + health
                + ", Strength=" + strength
                + ", Stamina=" + stamina
                + ", Feral=" + feral
                + "]";
    }
}
