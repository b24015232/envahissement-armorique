package com.asterix.model.place;

public enum PlaceType {
    GAUL_VILLAGE("Gaul Village"),
    GALLO_ROMAN_TOWN("Gallo Roman town"),
    ROMAN_CAMP("Roman Camp"),
    ROMAN_VILLAGE("Roman Village"),
    ROMAN_CITY("Roman City"),
    BATTLEFIELD("Battlefield"),
    CREATURE_ENCLOSURE("Creature Enclosure");

    private final String label;

    PlaceType(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}