package com.asterix.model.place;

import com.asterix.model.character.Chief;

/**
 * Abstract sealed class representing a settlement managed by a Chief.
 * <p>
 * Unlike a battlefield, a settlement (Village, Camp, City) has a hierarchical structure
 * headed by a Clan Chief.
 * </p>
 *
 * @see com.asterix.model.character.Chief
 * [cite_start]Reference: [cite: 489, 506] (Chiefs are attached to a place, except for battlefields)
 */
public abstract sealed class Settlement extends Place
        permits GaulVillage, RomanCamp, RomanCity, GalloRomanTown, CreatureEnclosure {

    protected Chief chief;

    public Settlement(String name, double area) {
        super(name, area);
    }

    /**
     * Assigns a chief to this settlement.
     *
     * @param chief The chief to manage this place.
     * [cite_start]Reference: [cite: 489]
     */
    public void setChief(Chief chief) {
        this.chief = chief;
    }

    /**
     * Retrieves the chief of this settlement.
     *
     * @return The current chief.
     */
    public Chief getChief() {
        return chief;
    }

    /**
     * Allows the chief to examine the place, displaying characteristics and contents.
     * [cite_start]Reference: [cite: 511]
     */
    public void examine() {
        System.out.println("Examining settlement: " + this.name);
        System.out.println("Managed by: " + (chief != null ? chief.getName() : "No Chief"));
        System.out.println("Population: " + this.characters.size());
        // Logic to display food details could be added here
    }
}