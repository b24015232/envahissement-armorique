package com.asterix.model.character.roman;

import com.asterix.model.ability.Fighter;
import com.asterix.model.character.Character;
import com.asterix.model.character.Gender;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SoldierTest {

    private static class TestSoldier extends Soldier {

        TestSoldier(String name,
                    int age,
                    double height,
                    double strength,
                    double stamina,
                    Gender gender) {
            super(name, age, height, strength, stamina, gender);
        }

        @Override
        public void fight(Character opponent) {
            resolveFight(opponent);
        }
    }

    @Test
    void soldierShouldImplementFighterInterface() {
        TestSoldier soldier = new TestSoldier("TestSoldier", 30, 1.8, 15, 10, Gender.MALE);

        assertTrue(soldier instanceof Fighter);
    }

    @Test
    void soldierFightShouldImpactHealthOfBothFighters() {
        TestSoldier s1 = new TestSoldier("S1", 30, 1.8, 20, 10, Gender.MALE);
        TestSoldier s2 = new TestSoldier("S2", 28, 1.75, 18, 8, Gender.FEMALE);

        s1.fight(s2);

        assertTrue(s1.getHealth() < Character.MAX_HEALTH);
        assertTrue(s2.getHealth() < Character.MAX_HEALTH);
    }

    @Test
    void soldierToStringShouldContainClassNameAndName() {
        TestSoldier soldier = new TestSoldier("Marcus", 30, 1.8, 15, 10, Gender.MALE);

        String s = soldier.toString();
        assertTrue(s.contains("TestSoldier"));
        assertTrue(s.contains("Marcus"));
    }
}
