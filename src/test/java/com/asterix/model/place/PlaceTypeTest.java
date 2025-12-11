package com.asterix.model.place;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link PlaceType} enum class.
 * <p>
 * This suite verifies the existence and completeness of all expected enum constants,
 * as well as the correct formatting of their labels via the {@code toString()} method.
 */
class PlaceTypeTest {

    /**
     * Verifies that all expected enum constants for place types are defined
     * in the {@code PlaceType} enumeration.
     */
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
                "All expected enum constants must be present.");
        assertEquals(expectedNames.size(), actualNames.size(),
                "The total number of enum constants must match the expectation.");
    }

    /**
     * Tests that the {@code toString()} method returns the correct, user-friendly
     * label for specific {@code PlaceType} constants.
     */
    @Test
    void testToStringShouldReturnCorrectLabel() {
        assertEquals("Gaul Village", PlaceType.GAUL_VILLAGE.toString());
        assertEquals("Roman Camp", PlaceType.ROMAN_CAMP.toString());
        assertEquals("Battlefield", PlaceType.BATTLEFIELD.toString());
        assertEquals("Creature Enclosure", PlaceType.CREATURE_ENCLOSURE.toString());
    }

    /**
     * Ensures that the label (result of {@code toString()}) for every
     * {@code PlaceType} constant is neither {@code null} nor empty
     * after trimming.
     */
    @Test
    void testLabelConsistency() {
        for (PlaceType type : PlaceType.values()) {
            assertNotNull(type.toString(), "The label for " + type.name() + " must not be null.");
            assertFalse(type.toString().trim().isEmpty(), "The label for " + type.name() + " must not be empty.");
        }
    }
}