package com.asterix.model.place;

import com.asterix.model.character.Character;
import com.asterix.model.character.roman.Roman;
import com.asterix.model.character.creature.Creature;

/**
 * Represents a Roman City.
 * <p>
 * Access is restricted to any Roman (Civilian or Soldier) and Fantastic Creatures.
 * </p>
 * [cite_start]Reference: [cite: 501]
 */
public final class RomanCity extends Settlement {

    public RomanCity(String name, double area) {
        super(name, area);
    }

    @Override
    public boolean canEnter(Character c) {
        return (c instanceof Roman) || (c instanceof Creature);
    }
}