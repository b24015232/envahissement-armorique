package com.asterix.model.place;

import com.asterix.model.character.Character;
import com.asterix.model.character.Chief;
import com.asterix.model.character.gaul.Gaul;
import com.asterix.model.character.roman.Roman;

/**
 * Represents a Gallo-Roman Hamlet (Bourgade).
 * <p>
 * Access is restricted to Gauls and Romans. Fantastic Creatures are NOT explicitly allowed
 * in this specific settlement type according to specs.
 * </p>
 */
public final class GalloRomanTown extends Settlement {

    public GalloRomanTown(String name, double area, Chief chief) {
        super(name, area, chief);
    }

    @Override
    public boolean canEnter(Character c) {
        return (c instanceof Gaul) || (c instanceof Roman);
    }
}