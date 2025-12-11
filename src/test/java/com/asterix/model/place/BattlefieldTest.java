package com.asterix.model.place;

import com.asterix.model.character.Gender;
import com.asterix.model.character.creature.Lycanthrope;
import com.asterix.model.character.gaul.BlackSmith;
import com.asterix.model.character.roman.Legionnaire;
import com.asterix.model.item.Food;
import com.asterix.model.item.FoodType; // Import required for the Fix
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link Battlefield} and basic {@link Place} behaviour.
 */
class BattlefieldTest {

    @Test
    void battlefieldShouldInitializeWithNameAndEmptyCollections() {
        Battlefield battlefield = new Battlefield("Field", 100.0);
        assertEquals("Field", battlefield.getName());
        assertNotNull(battlefield.getCharacters());
        assertTrue(battlefield.getCharacters().isEmpty());
        assertNotNull(battlefield.getFoods());
        assertTrue(battlefield.getFoods().isEmpty());
    }

    @Test
    void addCharacterShouldRejectNull() {
        Battlefield battlefield = new Battlefield("Field", 100.0);
        assertThrows(IllegalArgumentException.class, () -> battlefield.addCharacter(null));
    }

    @Test
    void addCharacterShouldAcceptAnyCharacterType() {
        Battlefield battlefield = new Battlefield("Field", 100.0);

        BlackSmith gaul = new BlackSmith("Asterix", 35, 1.70, 20.0, 15.0, Gender.MALE);
        Legionnaire roman = new Legionnaire("Fortus", 30, 1.80, 18.0, 12.0, Gender.MALE);
        Lycanthrope creature = new Lycanthrope("Lupus", 25, 1.85, 22.0, 10.0, Gender.MALE);

        battlefield.addCharacter(gaul);
        battlefield.addCharacter(roman);
        battlefield.addCharacter(creature);

        List<com.asterix.model.character.Character> chars = battlefield.getCharacters();
        assertEquals(3, chars.size());
        assertTrue(chars.contains(gaul));
        assertTrue(chars.contains(roman));
        assertTrue(chars.contains(creature));
    }

    @Test
    void removeCharacterShouldRemoveFromPlace() {
        Battlefield battlefield = new Battlefield("Field", 100.0);
        BlackSmith gaul = new BlackSmith("Asterix", 35, 1.70, 20.0, 15.0, Gender.MALE);

        battlefield.addCharacter(gaul);
        assertEquals(1, battlefield.getCharacters().size());

        battlefield.removeCharacter(gaul);
        assertTrue(battlefield.getCharacters().isEmpty());
    }

    @Test
    void getCharactersShouldReturnDefensiveCopy() {
        Battlefield battlefield = new Battlefield("Field", 100.0);
        BlackSmith gaul = new BlackSmith("Asterix", 35, 1.70, 20.0, 15.0, Gender.MALE);

        battlefield.addCharacter(gaul);

        List<com.asterix.model.character.Character> copy = battlefield.getCharacters();
        assertEquals(1, copy.size());

        copy.clear(); // Should not affect internal list
        assertEquals(1, battlefield.getCharacters().size());
    }

    @Test
    void getFoodsShouldExposeInternalList() {
        Battlefield battlefield = new Battlefield("Field", 100.0);

        // Fix: using the factory via FoodType instead of manual constructor
        // Honey creates a SimpleFood
        Food bread = FoodType.HONEY.create();

        List<Food> foodsRef = battlefield.getFoods();
        foodsRef.add(bread);

        // getFoods returns the same list instance
        assertEquals(1, battlefield.getFoods().size());
        assertSame(foodsRef, battlefield.getFoods());
    }

    @Test
    void toStringShouldContainNameAndPopulation() {
        Battlefield battlefield = new Battlefield("Field", 100.0);
        String text = battlefield.toString();

        assertTrue(text.contains("Field"));
        assertTrue(text.contains("population"));
    }
}