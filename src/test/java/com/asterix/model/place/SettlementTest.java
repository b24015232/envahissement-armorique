package com.asterix.model.place;

import com.asterix.model.character.Chief;
import com.asterix.model.character.Gender;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for common behaviour defined in {@link Settlement},
 * using a concrete subclass like {@link GaulVillage}.
 */
class SettlementTest {

    @Test
    void setChiefAndGetChiefShouldStoreReference() {
        Settlement village = new GaulVillage("Village gaulois", 50.0);

        assertNull(village.getChief());
        Chief chief = new Chief("Abraracourcix", "MALE", 45, village);

        village.setChief(chief);

        assertSame(chief, village.getChief());
    }

    @Test
    void examineShouldWorkWithoutChiefAndWithChief() {
        Settlement village = new GaulVillage("Village gaulois", 50.0);
        
        assertDoesNotThrow(village::examine);

        Chief chief = new Chief("Abraracourcix", "MALE", 45, village);
        village.setChief(chief);

        assertDoesNotThrow(village::examine);
    }
}
