package com.asterix.model.character;

/**
 * Enumeration defining the specific roles and professions available for characters.
 * <p>
 * This enum is used by the Factory pattern to determine which concrete class
 * to instantiate and by the UI to populate selection dropdowns.
 * </p>
 */
public enum CharacterType {

    GAUL_MERCHANT("Gaul Merchant"),
    GAUL_DRUID("Gaul Druid"),
    GAUL_BLACKSMITH("Gaul Blacksmith"),
    GAUL_INNKEEPER("Gaul Innkeeper"),

    ROMAN_LEGIONNAIRE("Roman Legionnaire"),
    ROMAN_SOLDIER("Roman Soldier"),
    ROMAN_PREFECT("Roman Prefect"),
    ROMAN_GENERAL("Roman General");

    private final String label;

    /**
     * Constructs a CharacterType with a display label.
     *
     * @param label The human-readable name of the character type.
     */
    CharacterType(String label) {
        this.label = label;
    }

    /**
     * Returns the string representation of the character type.
     *
     * @return The label suitable for UI display.
     */
    @Override
    public String toString() {
        return label;
    }
}