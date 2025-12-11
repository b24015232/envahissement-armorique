package com.asterix.model.ability;

import com.asterix.model.character.Character;

/**
 * Interface defining the capability to fight
 * Used by soldiers, druids, and creatures
 */
public interface Fighter {

    /**
     * Engages in combat with an opponent
     * @param opponent the character to fight against
     */
    void fight(Character opponent);
}