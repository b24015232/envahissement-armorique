package com.asterix.model.place;

import com.asterix.model.character.Character;
import com.asterix.model.character.creature.Creature;

/**
 * Represents an enclosure for special creatures.
 * <p>
 * Access is strictly restricted to Fantastic Creatures (e.g., Lycanthropes).
 * </p>
 * [cite_start]Reference: [cite: 503, 621]
 */
public final class CreatureEnclosure extends Settlement {

    public CreatureEnclosure(String name, double area) {
        super(name, area);
    }

    @Override
    protected boolean canEnter(Character c) {
        return c instanceof Creature;
    }
}