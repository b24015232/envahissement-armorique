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

        Gaul casalix = new Gaul(name, age, height, strength, stamina, gender) {};

        assertEquals("Casalix", casalix.getName());
        assertEquals(100.0, casalix.getHealth());
        assertNotNull(casalix.toString());
    }

    @Test
    public void testDrinkPotion() {
        Gaul balerdix = new Gaul("Balerdix", 26, 1.87, 150.0, 200.0, Gender.MALE) {};
        double initialPotion = balerdix.getPotionLevel();

        balerdix.drinkPotion(10.0);
        assertEquals(initialPotion + 10.0, balerdix.getPotionLevel());

        balerdix.drinkPotion(1.0);
        assertEquals(initialPotion + 11.0, balerdix.getPotionLevel());
    }

    @Test
    public void testEat() {
        Gaul ricardix = new Gaul("Ricardix", 51, 2.10, 1000.0, 500.0, Gender.MALE) {
            { this.health = 50.0; }
        };

        // 1. Eat null
        ricardix.eat(null);
        assertEquals(50.0, ricardix.getHealth());

        // 2. Eat allowed food (Wildboar)
        // PerishableFood uses FreshState score (10), not Enum score (15).
        // Health: 50 + 10 = 60.
        Food boar = FoodType.WILDBOAR.create();
        ricardix.eat(boar);
        assertEquals(60.0, ricardix.getHealth(), "Eating fresh wildboar should restore 10 health points");

        // 3. Eat forbidden food
        Food forbiddenFood = FoodType.ROCK_OIL.create();
        ricardix.eat(forbiddenFood);
        assertEquals(60.0, ricardix.getHealth());
    }
}