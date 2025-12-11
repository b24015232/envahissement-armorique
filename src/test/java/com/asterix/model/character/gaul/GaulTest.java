package com.asterix.model.character.gaul;

import com.asterix.model.character.Gender;
import com.asterix.model.item.Food;
import com.asterix.model.item.FoodType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Gaul} class.
 */
public class GaulTest {

    @Test
    public void testGaulInitialization() {
        String name = "Casalix";
        int age = 40;
        double height = 1.75;
        double strength = 69.0;
        double stamina = 100.0;
        Gender gender = Gender.MALE;

        Gaul casalix = new Gaul(name, age, height, strength, stamina, gender) {
            @Override
            public double getHealth() {
                return this.health;
            }
        };

        assertEquals("Casalix", casalix.getName());
        assertEquals(100.0, casalix.getHealth());
        assertNotNull(casalix.toString());
    }

    @Test
    public void testDrinkPotion() {
        Gaul balerdix = new Gaul("Balerdix", 26, 1.87, 150.0, 200.0, Gender.MALE) {
            @Override
            public double getHealth() {
                return this.health;
            }
        };
        double initialPotion = balerdix.getPotionLevel();

        // Testing drinkPotion
        balerdix.drinkPotion(10.0);
        assertEquals(initialPotion + 10.0, balerdix.getPotionLevel());

        balerdix.drinkPotion(1.0);
        assertEquals(initialPotion + 11.0, balerdix.getPotionLevel());
    }

    @Test
    public void testEat() {
        Gaul ricardix = new Gaul("Ricardix", 51, 2.10, 1000.0, 500.0, Gender.MALE) {
            {
                this.health = 50.0;
            } // Initialization block to set lower health

            @Override
            public double getHealth() {
                return this.health;
            }
        };

        // 1. Eat null
        ricardix.eat(null);
        assertEquals(50.0, ricardix.getHealth());

        // 2. Eat allowed food (Wildboar)
        Food boar = FoodType.WILDBOAR.create();
        ricardix.eat(boar);

        assertEquals(60.0, ricardix.getHealth(), "Eating fresh wildboar should restore health");

        // 3. Eat forbidden food (Rock Oil)
        Food forbiddenFood = FoodType.ROCK_OIL.create();
        ricardix.eat(forbiddenFood);
        assertEquals(60.0, ricardix.getHealth(), "Gauls should not gain health from Rock Oil");
    }
}