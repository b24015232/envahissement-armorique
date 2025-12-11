package com.asterix.model.character.roman;

import com.asterix.model.ability.Leader;
import com.asterix.model.character.Gender;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link Prefect} class, verifying its inheritance,
 * leadership capabilities, and basic string representation.
 */
class PrefectTest {

    /**
     * Verifies that a {@code Prefect} instance correctly inherits
     * from {@code Roman} and implements the {@link Leader} interface.
     */
    @Test
    void prefectShouldBeRomanAndLeader() {
        Prefect prefect = new Prefect("Decimus", 38, 1.8, 12, 10, Gender.MALE);

        assertInstanceOf(Roman.class, prefect);
        assertInstanceOf(Leader.class, prefect);
    }

    /**
     * Tests the {@code command()} method (from the {@code Leader} interface),
     * ensuring its execution does not have the side effect of changing
     * the Prefect's health.
     */
    @Test
    void prefectLeadShouldExecuteWithoutChangingHealth() {
        Prefect prefect = new Prefect("Decimus", 38, 1.8, 12, 10, Gender.MALE);

        double initialHealth = prefect.getHealth();

        prefect.command();

        assertEquals(initialHealth, prefect.getHealth(), 0.0001);
    }

    /**
     * Tests that the {@code toString()} method output contains the
     * Prefect's name.
     */
    @Test
    void prefectToStringShouldContainName() {
        Prefect prefect = new Prefect("Decimus", 38, 1.8, 12, 10, Gender.MALE);

        String s = prefect.toString();
        assertTrue(s.contains("Decimus"));
    }
}