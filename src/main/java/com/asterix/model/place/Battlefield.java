package com.asterix.model.place;

import com.asterix.model.character.Character;

/**
 * Represents a battlefield where combats mainly occur.
 * <p>
 * A battlefield is a neutral ground that can contain any type of character.
 * It is not managed by a specific chief.
 * </p>
 *
 * [cite_start]Reference: [cite: 504, 536]
 */
public final class Battlefield extends Place {

    public Battlefield(String name, double area) {
        super(name, area);
    }

    /**
     * Validates entry rules for a battlefield.
     * <p>
     * A battlefield can contain all types of characters.
     * </p>
     *
     * @param c The character.
     * @return Always {@code true}.
     * [cite_start]Reference: [cite: 504]
     */
    @Override
    protected boolean canEnter(Character c) {
        return true;
    }
}