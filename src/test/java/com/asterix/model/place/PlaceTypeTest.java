package com.asterix.model.place;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests pour la classe enum {@link PlaceType}.
 * Vérifie l'existence des valeurs et le formatage des étiquettes (labels) via toString().
 */
class PlaceTypeTest {

    @Test
    void allEnumValuesShouldBePresent() {
        List<String> expectedNames = List.of(
                "GAUL_VILLAGE",
                "GALLO_ROMAN_TOWN",
                "ROMAN_CAMP",
                "ROMAN_VILLAGE",
                "ROMAN_CITY",
                "BATTLEFIELD",
                "CREATURE_ENCLOSURE"
        );

        List<String> actualNames = Arrays.stream(PlaceType.values())
                .map(Enum::name)
                .toList();

        assertTrue(actualNames.containsAll(expectedNames) && expectedNames.containsAll(actualNames),
                "Toutes les constantes enum attendues doivent être présentes.");
        assertEquals(expectedNames.size(), actualNames.size(),
                "Le nombre total de constantes enum doit correspondre.");
    }

    @Test
    void testToStringShouldReturnCorrectLabel() {
        assertEquals("Gaul Village", PlaceType.GAUL_VILLAGE.toString());
        assertEquals("Roman Camp", PlaceType.ROMAN_CAMP.toString());
        assertEquals("Battlefield", PlaceType.BATTLEFIELD.toString());
        assertEquals("Creature Enclosure", PlaceType.CREATURE_ENCLOSURE.toString());
    }

    @Test
    void testLabelConsistency() {
        for (PlaceType type : PlaceType.values()) {
            assertNotNull(type.toString(), "Le libellé de " + type.name() + " ne doit pas être null.");
            assertFalse(type.toString().trim().isEmpty(), "Le libellé de " + type.name() + " ne doit pas être vide.");
        }
    }
}