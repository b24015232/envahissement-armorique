package com.asterix.model.character;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe enum {@link CharacterType}.
 */
class CharacterTypeTest {

    @Test
    void allEnumValuesShouldBePresent() {
        // Définition de toutes les constantes attendues
        List<String> expectedNames = List.of(
                "GAUL_MERCHANT",
                "GAUL_DRUID",
                "GAUL_BLACKSMITH",
                "GAUL_INNKEEPER",
                "ROMAN_LEGIONNAIRE",
                "ROMAN_SOLDIER",
                "ROMAN_PREFECT",
                "ROMAN_GENERAL"
        );

        // Récupère les noms réels des constantes de l'énumération
        List<String> actualNames = Arrays.stream(CharacterType.values())
                .map(Enum::name)
                .collect(Collectors.toList());

        // Vérifie si les listes correspondent
        assertEquals(expectedNames.size(), actualNames.size(),
                "Le nombre total de constantes doit être de " + expectedNames.size() + ".");
        assertTrue(actualNames.containsAll(expectedNames),
                "Toutes les constantes enum attendues doivent être présentes.");
    }

    @Test
    void testToStringShouldReturnCorrectLabel() {
        // Teste quelques valeurs spécifiques pour s'assurer que toString() retourne le bon libellé
        assertEquals("Gaul Merchant", CharacterType.GAUL_MERCHANT.toString());
        assertEquals("Gaul Blacksmith", CharacterType.GAUL_BLACKSMITH.toString());
        assertEquals("Roman Legionnaire", CharacterType.ROMAN_LEGIONNAIRE.toString());
        assertEquals("Roman Soldier", CharacterType.ROMAN_SOLDIER.toString());
        assertEquals("Roman General", CharacterType.ROMAN_GENERAL.toString());
    }

    @Test
    void testLabelConsistency() {
        // Vérifie que chaque libellé est non-null et non-vide
        for (CharacterType type : CharacterType.values()) {
            String label = type.toString();
            assertNotNull(label, "Le libellé de " + type.name() + " ne doit pas être null.");
            assertFalse(label.trim().isEmpty(), "Le libellé de " + type.name() + " ne doit pas être vide.");
            assertFalse(label.contains("_"), "Le libellé ne doit pas contenir de underscore ('_').");
        }
    }
}