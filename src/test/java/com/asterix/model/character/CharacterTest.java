package com.asterix.model.character;

import com.asterix.model.ability.Fighter;
import com.asterix.model.item.Food;
import com.asterix.model.item.SimpleFood;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the core behaviour defined in {@link Character}.
 */
class CharacterTest {

    /**
     * Simple concrete implementation of Character for testing purposes.
     * Also implements Fighter so we can call fight() directly.
     */
    private static class TestFighterCharacter extends Character implements Fighter {

        TestFighterCharacter(String name,
                             int age,
                             double height,
                             double strength,
                             double stamina,
                             Gender gender,
                             double initialHunger) {
            super(name, age, height, strength, stamina, gender);
            this.hunger = initialHunger;
        }

        @Override
        public void fight(Character opponent) {
            // Delegate to the generic combat logic
            resolveFight(opponent);
        }

        @Override
        public String toString() {
            return "TestFighterCharacter{name='" + name + "'}";
        }
    }

    // =========================
    //  TESTS EXISTANTS
    // =========================

    @Test
    void charactersShouldBeAliveWithFullHealthInitially() {
        TestFighterCharacter c = new TestFighterCharacter(
                "Test", 30, 1.8, 20, 10, Gender.MALE, 0.0
        );

        assertTrue(c.isAlive());
        assertEquals(Character.MAX_HEALTH, c.getHealth(), 0.0001);
    }

    @Test
    void fightShouldReduceHealthOfBothCharacters() {
        TestFighterCharacter a = new TestFighterCharacter(
                "A", 30, 1.8, 20, 10, Gender.MALE, 0.0
        );
        TestFighterCharacter b = new TestFighterCharacter(
                "B", 25, 1.7, 18, 8, Gender.FEMALE, 0.0
        );

        a.fight(b);

        assertTrue(a.getHealth() < Character.MAX_HEALTH);
        assertTrue(b.getHealth() < Character.MAX_HEALTH);
        assertTrue(a.isAlive());
        assertTrue(b.isAlive());
    }

    @Test
    void healShouldIncreaseHealthButNotExceedMax() {
        TestFighterCharacter c = new TestFighterCharacter(
                "Healer", 30, 1.8, 10, 10, Gender.MALE, 0.0
        );

        // simulate damage
        c.health = 50.0;

        c.heal(30.0);
        assertEquals(80.0, c.getHealth(), 0.0001);

        c.heal(50.0);
        assertEquals(Character.MAX_HEALTH, c.getHealth(), 0.0001);
    }

    @Test
    void eatShouldReduceHungerByDefault() {
        TestFighterCharacter c = new TestFighterCharacter(
                "Hungry", 30, 1.8, 10, 10, Gender.MALE, 50.0
        );

        Food food = new SimpleFood("Bread", "FOOD", 2, true, true);

        c.eat(food);

        assertEquals(40.0, c.hunger, 0.0001);
    }

    @Test
    void drinkPotionShouldIncreasePotionLevel() {
        TestFighterCharacter c = new TestFighterCharacter(
                "Alchemist", 30, 1.8, 10, 10, Gender.MALE, 0.0
        );

        c.drinkPotion(2.5);
        assertEquals(2.5, c.getPotionLevel(), 0.0001);

        c.drinkPotion(1.5);
        assertEquals(4.0, c.getPotionLevel(), 0.0001);
    }

    @Test
    void dieShouldSetHealthToZeroAndMarkCharacterAsDead() {
        TestFighterCharacter c = new TestFighterCharacter(
                "Doomed", 30, 1.8, 10, 10, Gender.MALE, 0.0
        );

        c.die();

        assertEquals(0.0, c.getHealth(), 0.0001);
        assertFalse(c.isAlive());
    }

    // =========================
    //  NOUVEAUX TESTS POUR LES BRANCHES MANQUANTES
    // =========================

    // ----- resolveFight() branches -----

    @Test
    void resolveFightShouldDoNothingWhenOpponentIsNull() {
        TestFighterCharacter a = new TestFighterCharacter(
                "Solo", 30, 1.8, 20, 10, Gender.MALE, 0.0
        );

        double initialHealth = a.getHealth();

        a.fight(null); // opponent == null → early return

        assertEquals(initialHealth, a.getHealth(), 0.0001);
    }

    @Test
    void resolveFightShouldDoNothingWhenOpponentIsSelf() {
        TestFighterCharacter a = new TestFighterCharacter(
                "Self", 30, 1.8, 20, 10, Gender.MALE, 0.0
        );

        double initialHealth = a.getHealth();

        // cast en Character pour pouvoir passer le même objet
        a.fight(a); // opponent == this → early return

        assertEquals(initialHealth, a.getHealth(), 0.0001);
    }

    @Test
    void resolveFightShouldDoNothingIfThisIsDead() {
        TestFighterCharacter dead = new TestFighterCharacter(
                "Dead", 30, 1.8, 20, 10, Gender.MALE, 0.0
        );
        TestFighterCharacter alive = new TestFighterCharacter(
                "Alive", 25, 1.7, 18, 8, Gender.FEMALE, 0.0
        );

        dead.health = 0.0; // mort
        double initialAliveHealth = alive.getHealth();

        dead.fight(alive); // !this.isAlive() → early return

        assertEquals(initialAliveHealth, alive.getHealth(), 0.0001);
    }

    @Test
    void resolveFightShouldDoNothingIfOpponentIsDead() {
        TestFighterCharacter alive = new TestFighterCharacter(
                "Alive", 30, 1.8, 20, 10, Gender.MALE, 0.0
        );
        TestFighterCharacter dead = new TestFighterCharacter(
                "Dead", 25, 1.7, 18, 8, Gender.FEMALE, 0.0
        );

        dead.health = 0.0;
        double initialAliveHealth = alive.getHealth();

        alive.fight(dead); // !opponent.isAlive() → early return

        assertEquals(initialAliveHealth, alive.getHealth(), 0.0001);
    }

    @Test
    void resolveFightShouldHandleZeroDamageCase() {
        // force un cas où damageToOpponent et damageToSelf = 0
        TestFighterCharacter tank1 = new TestFighterCharacter(
                "Tank1", 30, 1.8, 1.0, 100.0, Gender.MALE, 0.0
        );
        TestFighterCharacter tank2 = new TestFighterCharacter(
                "Tank2", 30, 1.8, 1.0, 100.0, Gender.FEMALE, 0.0
        );

        double initialHealth1 = tank1.getHealth();
        double initialHealth2 = tank2.getHealth();

        tank1.fight(tank2);

        // les deux devraient avoir pris 0 dégâts (Math.max(0,...))
        assertEquals(initialHealth1, tank1.getHealth(), 0.0001);
        assertEquals(initialHealth2, tank2.getHealth(), 0.0001);
    }

    // ----- heal() branches -----

    @Test
    void healShouldDoNothingWhenAmountIsNonPositive() {
        TestFighterCharacter c = new TestFighterCharacter(
                "Healer", 30, 1.8, 10, 10, Gender.MALE, 0.0
        );

        double initialHealth = c.getHealth();

        c.heal(0.0);
        c.heal(-5.0);

        assertEquals(initialHealth, c.getHealth(), 0.0001);
    }

    @Test
    void healShouldDoNothingWhenCharacterIsDead() {
        TestFighterCharacter c = new TestFighterCharacter(
                "DeadHealer", 30, 1.8, 10, 10, Gender.MALE, 0.0
        );

        c.health = 0.0;
        c.heal(10.0);

        assertEquals(0.0, c.getHealth(), 0.0001);
    }

    // ----- eat() branches -----

    @Test
    void eatShouldDoNothingWhenFoodIsNull() {
        TestFighterCharacter c = new TestFighterCharacter(
                "Hungry", 30, 1.8, 10, 10, Gender.MALE, 50.0
        );

        double initialHunger = c.hunger;
        double initialHealth = c.getHealth();

        c.eat(null); // food == null → early return

        assertEquals(initialHunger, c.hunger, 0.0001);
        assertEquals(initialHealth, c.getHealth(), 0.0001);
    }

    @Test
    void eatShouldDoNothingWhenCharacterIsDead() {
        TestFighterCharacter c = new TestFighterCharacter(
                "DeadHungry", 30, 1.8, 10, 10, Gender.MALE, 50.0
        );

        c.health = 0.0;
        double initialHunger = c.hunger;

        Food food = new SimpleFood("Bread", "FOOD", 2, true, true);

        c.eat(food); // !isAlive() → early return

        assertEquals(initialHunger, c.hunger, 0.0001);
    }

    // ----- drinkPotion() branches -----

    @Test
    void drinkPotionShouldDoNothingWhenDoseIsNonPositive() {
        TestFighterCharacter c = new TestFighterCharacter(
                "Alchemist", 30, 1.8, 10, 10, Gender.MALE, 0.0
        );

        c.drinkPotion(0.0);
        c.drinkPotion(-1.0);

        assertEquals(0.0, c.getPotionLevel(), 0.0001);
    }

    @Test
    void drinkPotionShouldDoNothingWhenCharacterIsDead() {
        TestFighterCharacter c = new TestFighterCharacter(
                "DeadAlchemist", 30, 1.8, 10, 10, Gender.MALE, 0.0
        );

        c.health = 0.0;

        c.drinkPotion(2.0); // !isAlive() → early return

        assertEquals(0.0, c.getPotionLevel(), 0.0001);
    }

    @Test
    void resolveFightShouldCallDieWhenOpponentIsKilled() {
        // Attaquant très fort, défenseur fragile (stamina = 0)
        TestFighterCharacter attacker = new TestFighterCharacter(
                "Attacker", 30, 1.8, 200.0, 10.0, Gender.MALE, 0.0
        );
        TestFighterCharacter victim = new TestFighterCharacter(
                "Victim", 25, 1.7, 1.0, 0.0, Gender.FEMALE, 0.0
        );

        // Sanity check: les deux sont vivants avant le combat
        assertTrue(attacker.isAlive());
        assertTrue(victim.isAlive());

        attacker.fight(victim);

        // Le victim doit être mort → branche (!opponent.isAlive()) vraie,
        // donc opponent.die() a été appelée.
        assertFalse(victim.isAlive());
        assertEquals(0.0, victim.getHealth(), 0.0001);
    }

    @Test
    void resolveFightShouldCallDieWhenThisIsKilled() {
        // Attaquant fragile, défenseur très fort
        TestFighterCharacter weak = new TestFighterCharacter(
                "Weak", 30, 1.8, 1.0, 0.0, Gender.MALE, 0.0
        );
        TestFighterCharacter strong = new TestFighterCharacter(
                "Strong", 25, 1.7, 200.0, 10.0, Gender.FEMALE, 0.0
        );

        assertTrue(weak.isAlive());
        assertTrue(strong.isAlive());

        // Weak attaque Strong, mais se fait éclater en retour
        weak.fight(strong);

        // Weak doit être mort → branche (!this.isAlive()) vraie,
        // donc this.die() a été appelée.
        assertFalse(weak.isAlive());
        assertEquals(0.0, weak.getHealth(), 0.0001);
    }

}
