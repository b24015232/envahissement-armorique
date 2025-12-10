package com.asterix.model.place;

import com.asterix.model.character.Character;
import com.asterix.model.character.roman.Roman; // Assuming a specific class for fighters
import com.asterix.model.character.creature.Creature;

/**
 * Represents a Roman Retrenched Camp (e.g., Babaorum).
 * <p>
 * Access is restricted to Roman Fighters (Legionaries, etc.) and Fantastic Creatures.
 * Civilians cannot enter.
 * </p>
 * [cite_start]Reference: [cite: 500]
 */
public final class RomanCamp extends Settlement {

    public RomanCamp(String name, double area) {
        super(name, area);
    }

    @Override
    public boolean canEnter(Character c) {
        // "ne peut contenir que des combattants romains"
        return (c instanceof Roman) || (c instanceof Creature);
    }
}