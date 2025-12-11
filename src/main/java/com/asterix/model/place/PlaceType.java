package com.asterix.model.place;

/**
 * Enumeration defining the different types of places available in the simulation.
 * <p>
 * This enum is used by the UI for selection menus and by the Factory pattern
 * to instantiate the correct concrete classes (e.g., GaulVillage, RomanCamp).
 * </p>
 */
public enum PlaceType {

    GAUL_VILLAGE("Gaul Village"),
    GALLO_ROMAN_TOWN("Gallo-Roman Town"),
    ROMAN_CAMP("Roman Camp"),
    ROMAN_VILLAGE("Roman Village"),
    ROMAN_CITY("Roman City"),
    BATTLEFIELD("Battlefield"),
    CREATURE_ENCLOSURE("Creature Enclosure");

    private final String label;

    /**
     * Constructs a PlaceType with a display label.
     *
     * @param label The human-readable name of the place type.
     */
    PlaceType(String label) {
        this.label = label;
    }

    /**
     * Returns the string representation of the place type for UI display.
     *
     * @return The label.
     */
    @Override
    public String toString() {
        return label;
    }
}