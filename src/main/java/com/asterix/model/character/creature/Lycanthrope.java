package com.asterix.model.character.creature;

import com.asterix.model.ability.Fighter;
import com.asterix.model.character.Character;
import com.asterix.model.character.Gender;

/**
 * Represents a lycanthrope (human-wolf hybrid).
 * <p>
 * A {@code Lycanthrope} can switch between a human-like form
 * and a wolf-like form. In wolf form, strength and stamina can
 * be increased to model the transformation.
 * </p>
 */
public class Lycanthrope extends Creature implements Fighter {

    /**
     * Indicates whether the lycanthrope is currently in wolf form.
     */
    private boolean inWolfForm;

    // We keep base stats to restore them when going back to human form
    private final double baseStrength;
    private final double baseStamina;

    /**
     * Creates a new lycanthrope with given basic attributes.
     *
     * @param name     The character's name.
     * @param age      The character's age.
     * @param height   The character's height.
     * @param strength The base strength (in human form).
     * @param stamina  The base stamina (in human form).
     * @param gender   The character's gender.
     */
    public Lycanthrope(String name,
                       int age,
                       double height,
                       double strength,
                       double stamina,
                       Gender gender) {
        super(name, age, height, strength, stamina, gender);
        this.inWolfForm = false;
        this.baseStrength = strength;
        this.baseStamina = stamina;
    }

    /**
     * Returns whether the lycanthrope is currently in wolf form.
     *
     * @return {@code true} if transformed into a wolf, {@code false} otherwise.
     */
    public boolean isInWolfForm() {
        return inWolfForm;
    }

    /**
     * Transforms the lycanthrope into wolf form.
     * <p>
     * In this simple model, we just boost strength and stamina.
     * You can later call this method from a "full moon" mechanic.
     * </p>
     */
    public void transformToWolfForm() {
        if (inWolfForm) {
            return;
        }
        this.inWolfForm = true;
        this.feral = true;

        this.strength = baseStrength * 2.0;
        this.stamina = baseStamina * 1.5;
    }

    /**
     * Reverts the lycanthrope back to human form,
     * restoring its original stats.
     */
    public void revertToHumanForm() {
        if (!inWolfForm) {
            return;
        }
        this.inWolfForm = false;
        this.feral = false;

        this.strength = baseStrength;
        this.stamina = baseStamina;
    }

    /**
     * Engages in combat with another character.
     * <p>
     * For now we delegate combat resolution to the generic
     * {@link Character#resolveFight(Character)} method.
     * You can later add special rules (extra damage in wolf form, etc.).
     * </p>
     *
     * @param opponent The character to fight against.
     */
    @Override
    public void fight(Character opponent) {
        if (opponent == null) {
            System.out.println(name + " (Lycanthrope) growls at the moon, but there is no opponent.");
            return;
        }

        System.out.println(name + " (Lycanthrope in "
                + (inWolfForm ? "wolf" : "human")
                + " form) attacks " + opponent.getName() + "!");

        resolveFight(opponent);
    }

    /**
     * Returns a string representation of the lycanthrope.
     *
     * @return A formatted string with stats and transformation status.
     */
    @Override
    public String toString() {
        return "Lycanthrope [Name=" + name
                + ", Health=" + health
                + ", Strength=" + strength
                + ", Stamina=" + stamina
                + ", WolfForm=" + inWolfForm
                + ", Feral=" + feral
                + "]";
    }

    /**
     * Returns the current health points of the lycanthrope.
     *
     * @return The current health value.
     */
    @Override
    public double getHealth() {
        return this.health;
    }
}