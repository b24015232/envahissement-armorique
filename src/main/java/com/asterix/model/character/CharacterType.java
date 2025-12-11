package com.asterix.model.character;

/**
 * Enumeration defining the specific roles and professions available for characters.
 * <p>
 * This enum is used by the Factory pattern to determine which concrete class
 * to instantiate and by the UI to populate selection dropdowns.
 * It distinguishes between Gaulish villagers (Merchant, Druid, etc.) and Roman military units.
 * </p>
 */
public enum CharacterType {

    /**
     * Represents a merchant in the Gaulish village (e.g., selling fish).
     */
    GAUL_MERCHANT("Gaul Merchant"),

    /**
     * Represents the village druid, capable of brewing magic potions.
     */
    GAUL_DRUID("Gaul Druid"),

    /**
     * Represents the village blacksmith, responsible for forging weapons.
     */
    GAUL_BLACKSMITH("Gaul Blacksmith"),

    /**
     * Represents the village innkeeper, serving food and drinks.
     */
    GAUL_INNKEEPER("Gaul Innkeeper"),

    /**
     * Represents a standard Roman legionnaire, the backbone of the army.
     */
    ROMAN_LEGIONNAIRE("Roman Legionnaire"),

    /**
     * Represents a generic Roman soldier (often synonymous with Legionnaire in basic contexts).
     */
    ROMAN_SOLDIER("Roman Soldier"),

    /**
     * Represents a Roman Prefect, an administrative leader.
     */
    ROMAN_PREFECT("Roman Prefect"),

    /**
     * Represents a high-ranking Roman General commanding the troops.
     */
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