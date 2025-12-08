package com.asterix.model.character.roman;

import com.asterix.model.ability.Fighter;
import com.asterix.model.ability.Leader;
import com.asterix.model.character.Gender;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GeneralTest {

    @Test
    void generalShouldBeSoldierFighterAndLeader() {
        General general = new General(1, "Caius", 40, 1.85, 25, 15, Gender.MALE);

        assertTrue(general instanceof Soldier);
        assertTrue(general instanceof Fighter);
        assertTrue(general instanceof Leader);
    }

    @Test
    void generalShouldExposeId() {
        General general = new General(42, "Caius", 40, 1.85, 25, 15, Gender.MALE);

        assertEquals(42, general.getId());
    }

    @Test
    void generalFightShouldReduceOpponentHealth() {
        General general = new General(1, "Caius", 40, 1.85, 25, 15, Gender.MALE);
        Legionnaire legionnaire = new Legionnaire("Attacked", 25, 1.75, 18, 12, Gender.MALE);

        double initialHealth = legionnaire.getHealth();

        general.fight(legionnaire);

        assertTrue(legionnaire.getHealth() < initialHealth);
    }

    @Test
    void generalFightWithNullOpponentShouldNotChangeHealth() {
        General general = new General(1, "Caius", 40, 1.85, 25, 15, Gender.MALE);

        double initialHealth = general.getHealth();

        general.fight(null); // branche opponent == null

        assertEquals(initialHealth, general.getHealth(), 0.0001);
    }

    @Test
    void generalLeadShouldExecuteWithoutChangingHealth() {
        General general = new General(1, "Caius", 40, 1.85, 25, 15, Gender.MALE);

        double initialHealth = general.getHealth();

        general.command();

        assertEquals(initialHealth, general.getHealth(), 0.0001);
    }

    @Test
    void generalAttackShouldExecuteWithoutChangingHealth() {
        General general = new General(1, "Caius", 40, 1.85, 25, 15, Gender.MALE);

        double initialHealth = general.getHealth();

        general.attack();

        assertEquals(initialHealth, general.getHealth(), 0.0001);
    }

    @Test
    void generalToStringShouldContainClassNameAndName() {
        General general = new General(1, "Caius", 40, 1.85, 25, 15, Gender.MALE);

        String s = general.toString();

        assertTrue(s.contains("General"));
        assertTrue(s.contains("Caius"));
    }
}
