package com.asterix.model.character.roman;

import com.asterix.model.ability.Fighter;
import com.asterix.model.ability.Leader;
import com.asterix.model.character.Gender;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link General} class, verifying its instantiation,
 * inheritance, behavior (fight, command, attack), and the correctness
 * of its detailed string representation.
 */
class GeneralTest {

    /**
     * Test-specific concrete subclass extending {@link General} to allow
     * instantiation and provide a custom {@code toString()} method for
     * verification tests.
     */
    private static class TestGeneral extends General {

        TestGeneral(int id,
                    String name,
                    int age,
                    double height,
                    double strength,
                    double stamina,
                    Gender gender) {
            super(id, name, age, height, strength, stamina, gender);
        }

        /**
         * Provides a formatted string containing all character details.
         * Note: The formatting logic here is specifically for testing purposes
         * and ensures high coverage of the base class properties.
         *
         * @return A detailed string representation of the General.
         */
        @Override
        public String toString() {
            return String.format(
                    "General | %-15s | %-6s | Age: %-3d | 📏 %.2fm | ❤️ HP: %-5.1f | 🍖 Hunger: %-5.1f | 💪 Str: %-5.1f | 🏃 Sta: %-5.1f | 🧪 Potion: %.1f",
                    this.getName(),
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
     * Tests that the custom {@code toString()} method for the General
     * includes all expected attributes (Name, Age, Gender, Height, Health,
     * Hunger, Strength, Stamina, Potion Level).
     */
    @Test
    void generalToStringShouldContainAllDetails() {
        int id = 1;
        String name = "Caius";
        int age = 40;
        double height = 1.85;
        double strength = 25.0;
        double stamina = 15.0;
        Gender gender = Gender.MALE;

        TestGeneral general = new TestGeneral(id, name, age, height, strength, stamina, gender);
        String s = general.toString();

        assertTrue(s.contains("General"), "Must contain the class type 'General'.");
        assertTrue(s.contains(name), "Must contain the name.");

        assertTrue(s.contains(String.valueOf(age)), "Must contain the age.");
        assertTrue(s.contains("Age:"), "Must contain the label 'Age:'.");
        assertTrue(s.contains(gender.toString()), "Must contain the gender.");

        String expectedHeight = String.format(Locale.ROOT, "%.2fm", height);
        assertTrue(s.contains(expectedHeight), "Must contain the formatted height ('1.85m').");

        String expectedStrengthValue = String.format(Locale.ROOT, "%.1f", strength);
        assertTrue(s.contains("Str: " + expectedStrengthValue), "Must contain the strength (25.0).");

        String expectedStaminaValue = String.format(Locale.ROOT, "%.1f", stamina);
        assertTrue(s.contains("Sta: " + expectedStaminaValue), "Must contain the stamina (15.0).");

        assertTrue(s.contains("HP:"), "Must contain the 'HP:' label.");
        assertTrue(s.contains("Hunger:"), "Must contain the 'Hunger:' label.");
        assertTrue(s.contains("Potion:"), "Must contain the 'Potion:' label.");
    }

    /**
     * Verifies that {@code General} correctly inherits from {@code Soldier}
     * and implements the required ability interfaces: {@link Fighter} and {@link Leader}.
     */
    @Test
    void generalShouldBeSoldierFighterAndLeader() {
        TestGeneral general = new TestGeneral(1, "Caius", 40, 1.85, 25, 15, Gender.MALE);

        assertInstanceOf(Soldier.class, general);
        assertInstanceOf(Fighter.class, general);
        assertInstanceOf(Leader.class, general);
    }

    /**
     * Tests that the {@code getId()} method returns the ID passed during construction.
     */
    @Test
    void generalShouldExposeId() {
        TestGeneral general = new TestGeneral(42, "Caius", 40, 1.85, 25, 15, Gender.MALE);

        assertEquals(42, general.getId());
    }

    /**
     * Tests the {@code fight()} method, ensuring that attacking another
     * character (e.g., a {@code Legionnaire}) successfully reduces the
     * opponent's health.
     */
    @Test
    void generalFightShouldReduceOpponentHealth() {
        TestGeneral general = new TestGeneral(1, "Caius", 40, 1.85, 25, 15, Gender.MALE);
        Legionnaire legionnaire = new Legionnaire("Attacked", 25, 1.75, 18, 12, Gender.MALE);

        double initialHealth = legionnaire.getHealth();

        general.fight(legionnaire);

        assertTrue(legionnaire.getHealth() < initialHealth);
    }

    /**
     * Tests that calling {@code fight()} with a {@code null} opponent
     * is safely handled and does not affect the General's own health.
     */
    @Test
    void generalFightWithNullOpponentShouldNotChangeHealth() {
        TestGeneral general = new TestGeneral(1, "Caius", 40, 1.85, 25, 15, Gender.MALE);

        double initialHealth = general.getHealth();

        general.fight(null);

        assertEquals(initialHealth, general.getHealth(), 0.0001);
    }

    /**
     * Tests the {@code command()} method (from the {@code Leader} interface),
     * ensuring its execution does not have the side effect of changing
     * the General's health.
     */
    @Test
    void generalLeadShouldExecuteWithoutChangingHealth() {
        TestGeneral general = new TestGeneral(1, "Caius", 40, 1.85, 25, 15, Gender.MALE);

        double initialHealth = general.getHealth();

        general.command();

        assertEquals(initialHealth, general.getHealth(), 0.0001);
    }

    /**
     * Tests the {@code attack()} method, ensuring its execution does not
     * have the side effect of changing the General's health.
     */
    @Test
    void generalAttackShouldExecuteWithoutChangingHealth() {
        TestGeneral general = new TestGeneral(1, "Caius", 40, 1.85, 25, 15, Gender.MALE);

        double initialHealth = general.getHealth();

        general.attack();

        assertEquals(initialHealth, general.getHealth(), 0.0001);
    }
}