package com.asterix.model.character.gaul;

import com.asterix.model.ability.Worker;
import com.asterix.model.character.Gender;

/**
 * Represents a Merchant in the Gaulish village.
 * <p>
 * The Merchant is responsible for commerce, specifically selling fish.
 * The freshness of his products is often a subject of intense debate among villagers.
 * Implements the {@link Worker} interface.
 * </p>
 */
public class Merchant extends Gaul implements Worker {

    /**
     * Constructs a new Merchant character.
     *
     * @param name     The name of the merchant (e.g., Ordralfabétix).
     * @param age      The age of the merchant.
     * @param height   The height of the merchant in meters.
     * @param strength The physical strength of the merchant.
     * @param stamina  The stamina/endurance of the merchant.
     * @param gender   The gender of the merchant.
     */
    public Merchant(String name, int age, double height, double strength, double stamina, Gender gender) {
        super(name, age, height, strength, stamina, gender);
    }

    /**
     * Implementation of the {@link Worker} interface.
     * <p>
     * The merchant sets up his stall at the market and loudly proclaims
     * the freshness of his fish (with passion and local references).
     * </p>
     */
    @Override
    public void work() {
        System.out.println(this.getName() + " sets up his stall at the village market");
        System.out.println("\"Fresh fish ! My fish is fucking fresh ! Fresh from the Vieux-Port !\"");
    }

    /**
     * Returns a string representation of the Merchant.
     *
     * @return A string containing the class name "Merchant" followed by the Gaul details.
     */
    @Override
    public String toString() {
        return "Merchant " + super.toString();
    }
}