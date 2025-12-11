package com.asterix.model.character.roman;

import com.asterix.model.ability.Fighter;
import com.asterix.model.character.Gender;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link Legionnaire} class, verifying its inheritance,
 * combat capabilities, and basic string representation.
 */
class LegionnaireTest {

    /**
     * Verifies that a {@code Legionnaire} instance correctly inherits
     * from {@code Soldier} and implements the {@link Fighter} interface.
     */
    @Test
    void legionnaireShouldBeASoldierAndAFighter() {
        Legionnaire leg = new Legionnaire("Claudius", 25, 1.75, 18, 12, Gender.MALE);

        assertInstanceOf(Soldier.class, leg);
        assertInstanceOf(Fighter.class, leg);
    }

    /**
     * Tests the {@code fight()} method, ensuring that a Legionnaire
     * successfully reduces the opponent's health upon attack.
     */
    @Test
    void legionnaireFightShouldReduceOpponentHealth() {
        Legionnaire attacker = new Legionnaire("Attacker", 25, 1.75, 20, 10, Gender.MALE);
        Legionnaire defender = new Legionnaire("Defender", 26, 1.8, 15, 12, Gender.MALE);

        double initialHealth = defender.getHealth();

        attacker.fight(defender);

        assertTrue(defender.getHealth() < initialHealth);
    }

    /**
     * Tests that calling {@code fight()} with a {@code null} opponent
     * is safely handled and does not affect the Legionnaire's own health.
     */
    @Test
    void legionnaireFightWithNullOpponentShouldNotChangeHealth() {
        Legionnaire legionnaire = new Legionnaire("Solo", 25, 1.75, 20, 10, Gender.MALE);

        double initialHealth = legionnaire.getHealth();

        legionnaire.fight(null); // should go through the "opponent == null" branch

        assertEquals(initialHealth, legionnaire.getHealth(), 0.0001);
    }

    /**
     * Tests that the {@code toString()} method output contains the
     * Legionnaire's name.
     */
    @Test
    void legionnaireToStringShouldContainName() {
        Legionnaire leg = new Legionnaire("Claudius", 25, 1.75, 18, 12, Gender.MALE);

        String s = leg.toString();
        assertTrue(s.contains("Claudius"));
    }
}