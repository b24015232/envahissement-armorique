package com.asterix.model.character.gaul;

import com.asterix.model.ability.Fighter;
import com.asterix.model.ability.Leader;
import com.asterix.model.ability.Worker;
import com.asterix.model.character.Character;
import com.asterix.model.character.Gender;

/**
 * Represents a Druid in the Gaulish village (e.g., Panoramix).
 * <p>
 * The Druid is a central character with multiple capabilities:
 * he works (gathers mistletoe), fights when necessary, and provides leadership.
 * Most importantly, he is the only one capable of brewing the magic potion.
 * </p>
 * Implements {@link Worker}, {@link Fighter}, and {@link Leader}.
 */
public class Druid extends Gaul implements Worker, Fighter, Leader {
    // TODO : put the recipe probabily here later

    /**
     * Constructs a new Druid character.
     *
     * @param name     The name of the druid.
     * @param age      The age of the druid.
     * @param height   The height of the druid in meters.
     * @param strength The physical strength of the druid.
     * @param stamina  The stamina/endurance of the druid.
     * @param gender   The gender of the druid.
     */
    public Druid(String name, int age, double height, double strength, double stamina, Gender gender) {
        super(name, age, height, strength, stamina, gender);
    }

    /**
     * Prepares a fresh cauldron of magic potion.
     * <p>
     * This unique ability involves mixing specific ingredients like mistletoe and rock oil.
     * It allows other Gauls to gain superhuman strength.
     * </p>
     */
    public void concoctPotion() {
        System.out.println(this.getName() + " lights a fire under the cauldron");
        System.out.println("Adding mistletoe, lobster (for the taste), and rock oil...");
        System.out.println("The magic potion is ready ! (health and strength boost available)");
    }

    //implementing interfaces

    /**
     * Implementation of the {@link Worker} interface.
     * <p>
     * For a druid, work consists primarily of going into the forest to gather
     * ingredients, specifically mistletoe, using a golden sickle.
     * </p>
     */
    @Override
    public void work() {
        //for a druid, work is mostly to prepare potion or pick mistletoe
        System.out.println(this.getName() + " goes into the forest to gather fresh mistletoe with a golden sickle");
    }

    /**
     * Implementation of the {@link Leader} interface.
     * <p>
     * As a figure of wisdom and authority, the Druid can command attention
     * or silence within the village.
     * </p>
     */
    @Override
    public void command() {
        System.out.println(this.getName() + " raises a hand for silence");
    }

    /**
     * Implementation of the {@link Fighter} interface.
     * <p>
     * Although primarily a scholar, the Druid can defend himself using his staff.
     * </p>
     *
     * @param opponent The character being fought.
     */
    @Override
    public void fight(Character opponent) {
        // Simple implementation to upgrade later when the game will be more complete
        System.out.println(this.getName() + " hits " + opponent.getName() + " with his staff !");
        // TODO: Implement damage logic : damage = (this.strength / opponent.stamina)
    }

    /**
     * Returns a string representation of the Druid.
     *
     * @return A string containing the class name "Druid" followed by the Gaul details.
     */
    @Override
    public String toString() {
        return "Druid " + super.toString();
    }
}