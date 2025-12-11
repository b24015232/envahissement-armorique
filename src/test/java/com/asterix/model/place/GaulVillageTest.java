package com.asterix.model.place;

import com.asterix.model.character.Chief;
import com.asterix.model.character.Gender;
import com.asterix.model.character.creature.Lycanthrope;
import com.asterix.model.character.gaul.BlackSmith;
import com.asterix.model.character.roman.Legionnaire;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link GaulVillage}.
 * Correction: Ajout du paramètre Chief manquant au constructeur de GaulVillage.
 */
class GaulVillageTest {


    private static class TestChief extends Chief {
        public TestChief(String name, Place place) {
            super(name, "UNKNOWN", 0, place);
        }
    }

    @Test
    void gaulVillageShouldAllowGaulsAndCreatures() {
        TestChief chiefStub = new TestChief("Abraracourcix", null);

        GaulVillage village = new GaulVillage("Village gaulois", 50.0, chiefStub);

        chiefStub.setLocation(village);

        BlackSmith gaul = new BlackSmith("Astérix", 35, 1.70, 20.0, 15.0, Gender.MALE);
        Lycanthrope creature = new Lycanthrope("Lupus", 25, 1.85, 22.0, 10.0, Gender.MALE);

        assertDoesNotThrow(() -> village.addCharacter(gaul));
        assertDoesNotThrow(() -> village.addCharacter(creature));
    }

    @Test
    void gaulVillageShouldRejectRomans() {
        TestChief chiefStub = new TestChief("Abraracourcix", null);

        GaulVillage village = new GaulVillage("Village gaulois", 50.0, chiefStub);

        chiefStub.setLocation(village);

        Legionnaire roman = new Legionnaire("Fortus", 30, 1.80, 18.0, 12.0, Gender.MALE);

        assertThrows(IllegalArgumentException.class, () -> village.addCharacter(roman));
    }
}