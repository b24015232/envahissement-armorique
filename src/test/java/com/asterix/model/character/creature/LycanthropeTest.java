package com.asterix.model.character.creature;

import com.asterix.model.ability.Fighter;
import com.asterix.model.character.Character;
import com.asterix.model.character.Gender;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link Lycanthrope} creature.
 */
class LycanthropeTest {

    /**
     * Simple opponent implementation to test fights.
     */
    private static class DummyOpponent extends Character {

        DummyOpponent(String name) {
            super(name, 30, 1.8, 10.0, 10.0, Gender.MALE);
        }

        @Override
        public String toString() {
            return "DummyOpponent{name='" + name + "'}";
        }
    }

    @Test
    void lycanthropeShouldBeCreatureAndFighter() {
        Lycanthrope lycan = new Lycanthrope("Jacob", 25, 1.8, 15.0, 10.0, Gender.MALE);

        assertInstanceOf(Creature.class, lycan);
        assertInstanceOf(Fighter.class, lycan);
    }

    @Test
    void lycanthropeShouldStartInHumanFormNotFeral() {
        Lycanthrope lycan = new Lycanthrope("Jacob", 25, 1.8, 15.0, 10.0, Gender.MALE);

        assertFalse(lycan.isInWolfForm());
        assertFalse(lycan.isFeral());
    }

    @Test
    void transformToWolfFormShouldSetFlags() {
        Lycanthrope lycan = new Lycanthrope("Jacob", 25, 1.8, 20.0, 10.0, Gender.MALE);

        lycan.transformToWolfForm();

        assertTrue(lycan.isInWolfForm());
        assertTrue(lycan.isFeral());
    }

    @Test
    void transformToWolfFormShouldBeIdempotent() {
        Lycanthrope lycan = new Lycanthrope("Jacob", 25, 1.8, 20.0, 10.0, Gender.MALE);

        lycan.transformToWolfForm();
        lycan.transformToWolfForm();

        assertTrue(lycan.isInWolfForm());
        assertTrue(lycan.isFeral());
    }

    @Test
    void revertToHumanFormShouldResetFlags() {
        Lycanthrope lycan = new Lycanthrope("Jacob", 25, 1.8, 20.0, 10.0, Gender.MALE);
        lycan.transformToWolfForm();
        assertTrue(lycan.isInWolfForm());

        lycan.revertToHumanForm();

        assertFalse(lycan.isInWolfForm());
        assertFalse(lycan.isFeral());
    }

    @Test
    void revertToHumanFormShouldBeIdempotent() {
        Lycanthrope lycan = new Lycanthrope("Jacob", 25, 1.8, 20.0, 10.0, Gender.MALE);

        lycan.transformToWolfForm();
        lycan.revertToHumanForm();
        lycan.revertToHumanForm();

        assertFalse(lycan.isInWolfForm());
        assertFalse(lycan.isFeral());
    }

    @Test
    void fightShouldWorkInHumanAndWolfForm() {
        Lycanthrope lycan = new Lycanthrope("Jacob", 25, 1.8, 30.0, 5.0, Gender.MALE);
        DummyOpponent opponent = new DummyOpponent("Target");
        double healthBefore = opponent.getHealth();
        lycan.fight(opponent);
        double healthAfterHumanFight = opponent.getHealth();
        assertTrue(healthAfterHumanFight < healthBefore);
        lycan.transformToWolfForm();
        assertTrue(lycan.isInWolfForm());
        lycan.fight(opponent);
        double healthAfterWolfFight = opponent.getHealth();
        assertTrue(healthAfterWolfFight < healthAfterHumanFight);
    }

    @Test
    void fightWithNullOpponentShouldNotChangeLycanHealth() {
        Lycanthrope lycan = new Lycanthrope("Jacob", 25, 1.8, 30.0, 5.0, Gender.MALE);
        double initialHealth = lycan.getHealth();
        lycan.fight(null);
        assertEquals(initialHealth, lycan.getHealth(), 0.0001);
    }

    @Test
    void lycanthropeToStringShouldContainNameAndWolfFormFlag() {
        Lycanthrope lycan = new Lycanthrope("Jacob", 25, 1.8, 20.0, 10.0, Gender.MALE);
        String s = lycan.toString();
        assertTrue(s.contains("Jacob"));
        assertTrue(s.contains("WolfForm"));
    }
}
