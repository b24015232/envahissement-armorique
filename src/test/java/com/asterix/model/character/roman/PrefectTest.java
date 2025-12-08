package com.asterix.model.character.roman;

import com.asterix.model.ability.Leader;
import com.asterix.model.character.Gender;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrefectTest {

    @Test
    void prefectShouldBeRomanAndLeader() {
        Prefect prefect = new Prefect("Decimus", 38, 1.8, 12, 10, Gender.MALE);

        assertTrue(prefect instanceof Roman);
        assertTrue(prefect instanceof Leader);
    }

    @Test
    void prefectLeadShouldExecuteWithoutChangingHealth() {
        Prefect prefect = new Prefect("Decimus", 38, 1.8, 12, 10, Gender.MALE);

        double initialHealth = prefect.getHealth();

        prefect.command();

        assertEquals(initialHealth, prefect.getHealth(), 0.0001);
    }

    @Test
    void prefectToStringShouldContainName() {
        Prefect prefect = new Prefect("Decimus", 38, 1.8, 12, 10, Gender.MALE);

        String s = prefect.toString();
        assertTrue(s.contains("Decimus"));
    }
}
