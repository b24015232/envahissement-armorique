package com.asterix.model.place;

import com.asterix.model.character.Character;
import com.asterix.model.character.Chief;
import com.asterix.model.character.gaul.Gaul;
import com.asterix.model.character.creature.Creature;

/**
 * Represents a Gaul Village.
 * <p>
 * Access is restricted to Gauls and Fantastic Creatures only.
 * </p>
 * [cite_start]Reference: [cite: 499]
 */
public final class GaulVillage extends Settlement {

    public GaulVillage(String name, double area, Chief chief) {
        super(name, area, chief);
    }

    @Override
    public boolean canEnter(Character c) {
        return (c instanceof Gaul) || (c instanceof Creature);
    }
}