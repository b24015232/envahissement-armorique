package com.asterix.model.place;

import com.asterix.model.character.Character;
import com.asterix.model.character.Chief;
import com.asterix.model.character.gaul.Gaul;
import com.asterix.model.character.creature.Creature;
import com.asterix.model.item.Cauldron;

/**
 * Represents a Gaul Village.
 * <p>
 * Access is restricted to Gauls and Fantastic Creatures only.
 * </p>
 */
public final class GaulVillage extends Settlement {
    private final Cauldron cauldron;

    public GaulVillage(String name, double area, Chief chief) {
        super(name, area, chief);
        this.cauldron = new Cauldron();
    }

    public Cauldron getCauldron() {
        return cauldron;
    }

    @Override
    public boolean canEnter(Character c) {
        return (c instanceof Gaul) || (c instanceof Creature);
    }
}