package com.asterix.model.character.roman;

import com.asterix.model.ability.Fighter;
import com.asterix.model.character.Gender;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LegionnaireTest {

    @Test
    void legionnaireShouldBeASoldierAndAFighter() {
        Legionnaire leg = new Legionnaire("Claudius", 25, 1.75, 18, 12, Gender.MALE);

        assertInstanceOf(Soldier.class, leg);
        assertInstanceOf(Fighter.class, leg);
    }

    @Test
    void legionnaireFightShouldReduceOpponentHealth() {
        Legionnaire attacker = new Legionnaire("Attacker", 25, 1.75, 20, 10, Gender.MALE);
        Legionnaire defender = new Legionnaire("Defender", 26, 1.8, 15, 12, Gender.MALE);

        double initialHealth = defender.getHealth();

        attacker.fight(defender);

        assertTrue(defender.getHealth() < initialHealth);
    }

    @Test
    void legionnaireFightWithNullOpponentShouldNotChangeHealth() {
        Legionnaire legionnaire = new Legionnaire("Solo", 25, 1.75, 20, 10, Gender.MALE);

        double initialHealth = legionnaire.getHealth();

        legionnaire.fight(null); // doit passer par la branche "opponent == null"

        assertEquals(initialHealth, legionnaire.getHealth(), 0.0001);
    }

    @Test
    void legionnaireToStringShouldContainName() {
        Legionnaire leg = new Legionnaire("Claudius", 25, 1.75, 18, 12, Gender.MALE);

        String s = leg.toString();
        assertTrue(s.contains("Claudius"));
    }
}
