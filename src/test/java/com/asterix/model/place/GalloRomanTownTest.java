package com.asterix.model.place;

import com.asterix.model.character.Gender;
import com.asterix.model.character.creature.Lycanthrope;
import com.asterix.model.character.gaul.BlackSmith;
import com.asterix.model.character.roman.Legionnaire;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link GalloRomanTown}.
 */
class GalloRomanTownTest {

    @Test
    void galloRomanTownShouldAllowGaulsAndRomans() {
        GalloRomanTown town = new GalloRomanTown("Lutèce", 90.0);

        BlackSmith gaul = new BlackSmith("Astérix", 35, 1.70, 20.0, 15.0, Gender.MALE);
        Legionnaire roman = new Legionnaire("Fortus", 30, 1.80, 18.0, 12.0, Gender.MALE);

        assertDoesNotThrow(() -> town.addCharacter(gaul));
        assertDoesNotThrow(() -> town.addCharacter(roman));
    }

    @Test
    void galloRomanTownShouldRejectCreatures() {
        GalloRomanTown town = new GalloRomanTown("Lutèce", 90.0);
        Lycanthrope creature = new Lycanthrope("Lupus", 25, 1.85, 22.0, 10.0, Gender.MALE);

        assertThrows(IllegalArgumentException.class, () -> town.addCharacter(creature));
    }
}
