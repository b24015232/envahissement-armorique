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
 * <p>
 * This class introduces specific behaviors for creatures, such as the ability
 * to enter a "feral" state, which can impact their combat statistics or behavior.
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
     * @param name     The name of the creature.
     * @param age      The age of the creature in years.
     * @param height   The height of the creature in meters.
     * @param strength The physical strength of the creature.
     * @param stamina  The stamina/endurance of the creature (resistance to damage).
     * @param gender   The gender of the creature.
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
     *
     * @return {@code true} if the creature is in a rage/feral state, {@code false} otherwise.
     */
    public boolean isFeral() {
        return feral;
    }

    /**
     * Puts the creature into a feral/raging state.
     * <p>
     * When feral, the creature may exhibit aggressive behavior or enhanced stats
     * depending on the specific implementation.
     * </p>
     */
    public void enterFeralState() {
        this.feral = true;
    }

    /**
     * Brings the creature back to a calmer state.
     * <p>
     * This deactivates the feral mode.
     * </p>
     */
    public void leaveFeralState() {
        this.feral = false;
    }

    /**
     * Returns a string representation of the creature, including its specific state.
     *
     * @return A formatted string containing the class name, name, health, strength, stamina, and feral status.
     */
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