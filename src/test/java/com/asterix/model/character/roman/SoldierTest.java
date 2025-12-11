package com.asterix.model.character.roman;

import com.asterix.model.ability.Fighter;
import com.asterix.model.ability.Leader;
import com.asterix.model.character.Character;
import com.asterix.model.character.Gender;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the abstract {@link Soldier} class, verifying its implementation of
 * the {@link Fighter} interface and the core combat mechanism defined in
 * {@code resolveFight()}.
 */
class SoldierTest {

    /**
     * Test-specific concrete subclass extending {@link Soldier} to allow
     * instantiation and implementation of the abstract {@code fight} method
     * using the protected {@code resolveFight()} utility.
     */
    private static class TestSoldier extends Soldier {

        TestSoldier(String name,
                    int age,
                    double height,
                    double strength,
                    double stamina,
                    Gender gender) {
            super(name, age, height, strength, stamina, gender);
        }

        /**
         * Concrete implementation of {@code fight} that utilizes the
         * generic combat logic defined in the base {@code Soldier} class.
         *
         * @param opponent The character being fought.
         */
        @Override
        public void fight(Character opponent) {
            resolveFight(opponent);
        }

        /**
         * Provides a formatted string containing all character details for verification.
         *
         * @return A detailed string representation of the Soldier character.
         */
        @Override
        public String toString() {
            return String.format(
                    Locale.ROOT,
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

    /**
     * Verifies that the {@code Soldier} abstract class implements the
     * {@link Fighter} interface.
     */
    @Test
    void soldierShouldImplementFighterInterface() {
        TestSoldier soldier = new TestSoldier("TestSoldier", 30, 1.8, 15, 10, Gender.MALE);

        assertInstanceOf(Fighter.class, soldier);
    }

    /**
     * Tests the core combat functionality ({@code resolveFight()}),
     * ensuring that fighting successfully reduces the health of both
     * the attacker and the defender (due to implied riposte logic in
     * the base combat mechanism).
     */
    @Test
    void soldierFightShouldImpactHealthOfBothFighters() {
        TestSoldier s1 = new TestSoldier("S1", 30, 1.8, 20, 10, Gender.MALE);
        TestSoldier s2 = new TestSoldier("S2", 28, 1.75, 18, 8, Gender.FEMALE);

        double initialHealthS1 = s1.getHealth();
        double initialHealthS2 = s2.getHealth();

        s1.fight(s2);
        s2.fight(s1);

        assertTrue(s1.getHealth() < initialHealthS1, "Attacker S1's health must be reduced by the opponent's riposte.");
        assertTrue(s2.getHealth() < initialHealthS2, "Opponent S2's health must be reduced by S1's attack.");
    }

    /**
     * Tests that the custom {@code toString()} method for the Soldier
     * includes all expected character attributes and labels.
     */
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

        String expectedHeight = String.format(Locale.ROOT, "%.2fm", height);
        assertTrue(s.contains(expectedHeight), "Must contain the formatted height ('1.80m').");

        String expectedStrength = String.format(Locale.ROOT, "%.1f", strength);
        assertTrue(s.contains("Str: " + expectedStrength), "Must contain the strength (15.0).");

        String expectedStamina = String.format(Locale.ROOT, "%.1f", stamina);
        assertTrue(s.contains("Sta: " + expectedStamina), "Must contain the stamina (10.0).");

        assertTrue(s.contains("HP:"));
        assertTrue(s.contains("Hunger:"));
        assertTrue(s.contains("Potion:"));
    }
}