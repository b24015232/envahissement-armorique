package com.asterix.model.place;

import com.asterix.model.character.Character;

/**
 * Represents a battlefield where combats primarily occur.
 * <p>
 * A battlefield is a neutral zone without a specific leader or governance structure.
 * Unlike settlements, it imposes no restrictions on entry, allowing characters
 * of all factions (Gauls, Romans, Creatures) to gather and fight.
 * </p>
 */
public final class Battlefield extends Place {

    /**
     * Constructs a new Battlefield.
     *
     * @param name The name of the battlefield (e.g., "Plains of Gergovia").
     * @param area The surface area of the battlefield in square meters.
     */
    public Battlefield(String name, double area) {
        super(name, area);
    }

    /**
     * Determines if a character can enter this place.
     * <p>
     * Battlefields are open to everyone. There are no restrictions based on faction or type.
     * </p>
     *
     * @param c The character attempting to enter.
     * @return Always {@code true}.
     */
    @Override
    public boolean canEnter(Character c) {
        return true;
    }
}