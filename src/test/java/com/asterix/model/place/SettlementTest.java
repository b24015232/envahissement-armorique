package com.asterix.model.place;

import com.asterix.model.character.Chief;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for common behaviour defined in {@link Settlement},
 * utilisant une sous-classe concrète comme {@link GaulVillage}.
 * Correction: Ajout du paramètre Chief manquant au constructeur de GaulVillage.
 */
class SettlementTest {

    private static class TestChief extends Chief {
        public TestChief(String name, Place place) {
            super(name, "UNKNOWN", 0, place);
        }
    }


    @Test
    void setChiefAndGetChiefShouldStoreReference() {
        TestChief initialChief = new TestChief("Initial", null);

        Settlement village = new GaulVillage("Village gaulois", 50.0, initialChief);

        assertSame(initialChief, village.getChief(), "Le Chief initial devrait être celui passé au constructeur.");

        Chief newChief = new Chief("Abraracourcix", "MALE", 45, village);

        village.setChief(newChief);

        assertSame(newChief, village.getChief(), "Le nouveau Chief devrait être stocké.");
    }

    @Test
    void examineShouldWorkWithoutChiefAndWithChief() {
        TestChief chiefStub = new TestChief("Stub", null);
        Settlement village = new GaulVillage("Village gaulois", 50.0, chiefStub);
        chiefStub.setLocation(village);

        assertDoesNotThrow(village::examine, "examine ne doit pas lever d'exception avec un chief défini.");

        village.setChief(null);

        assertDoesNotThrow(village::examine, "examine ne doit pas lever d'exception quand le Chief est null.");
    }
}