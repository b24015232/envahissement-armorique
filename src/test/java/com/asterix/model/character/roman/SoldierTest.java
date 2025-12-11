package com.asterix.model.character.roman;

import com.asterix.model.ability.Fighter;
import com.asterix.model.ability.Leader;
import com.asterix.model.character.Character;
import com.asterix.model.character.Gender;
import org.junit.jupiter.api.Test;

import java.util.Locale;

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

        @Override
        public String toString() {
            return String.format(
                    "%-25s | %-6s | Age: %-3d | 📏 %.2fm | ❤️ HP: %-5.1f | 🍖 Hunger: %-5.1f | 💪 Str: %-5.1f | 🏃 Sta: %-5.1f | 🧪 Potion: %.1f",
                    this.getClass().getSimpleName() + " (" + this.getName() + ")",
                    this.getGender().toString(),
                    this.getAge(),
                    this.getHeight(),
                    this.getHealth(),
                    this.getHunger(),
                    this.getStrength(),
                    this.getStamina(),
                    this.getPotionLevel()
            );
        }
    }

    @Test
    void soldierShouldImplementFighterInterface() {
        TestSoldier soldier = new TestSoldier("TestSoldier", 30, 1.8, 15, 10, Gender.MALE);

        assertInstanceOf(Fighter.class, soldier);
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
    void soldierToStringShouldContainAllDetails() {
        String name = "Marcus";
        int age = 30;
        double height = 1.80;
        double strength = 15.0;
        double stamina = 10.0;
        Gender gender = Gender.MALE;

        TestSoldier soldier = new TestSoldier(name, age, height, strength, stamina, gender);
        String s = soldier.toString();

        assertTrue(s.contains("TestSoldier"), "Must contain the name of the dummy class.");
        assertTrue(s.contains(name), "Must contain the name 'Marcus'.");

        assertTrue(s.contains(String.valueOf(age)), "Must contain the age.");
        assertTrue(s.contains("Age:"), "Must contain the label 'Age:'.");
        assertTrue(s.contains(gender.toString()), "Must contain the gender.");

        String expectedHeight = String.format("%.2fm", height);
        assertTrue(s.contains(expectedHeight), "Must contain the formatted height ('1,80m' or '1.80m').");

        String expectedStrength = String.format("%.1f", strength);
        assertTrue(s.contains("Str: " + expectedStrength), "Must contain the strength (15.0).");

        String expectedStamina = String.format("%.1f", stamina);
        assertTrue(s.contains("Sta: " + expectedStamina), "Must contain the stamina (10.0).");

        assertTrue(s.contains("HP:"));
        assertTrue(s.contains("Hunger:"));
        assertTrue(s.contains("Potion:"));
    }
}