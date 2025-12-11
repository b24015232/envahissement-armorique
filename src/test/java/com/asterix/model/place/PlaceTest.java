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
 * Tests for common behaviour defined in {@link Place},
 * using concrete subclasses like {@link Battlefield} and {@link GaulVillage}.
 */
class PlaceTest {

    @Test
    void placeConstructorShouldInitializeNameAreaAndCollections() {
        Place place = new Battlefield("Field", 100.0);

        assertEquals("Field", place.getName());
        assertNotNull(place.getCharacters());
        assertTrue(place.getCharacters().isEmpty());
        assertNotNull(place.getFoods());
        assertTrue(place.getFoods().isEmpty());

        String txt = place.toString();
        assertTrue(txt.contains("Field"));
        assertTrue(txt.contains("population"));
    }

    @Test
    void addCharacterShouldThrowOnNull() {
        Place place = new Battlefield("Field", 100.0);

        assertThrows(IllegalArgumentException.class, () -> place.addCharacter(null));
    }

    @Test
    void addCharacterShouldUseCanEnterRules_GaulVillage() {
        Place village = new GaulVillage("Gaulish Village", 50.0);

        BlackSmith gaul = new BlackSmith("Asterix", 35, 1.70, 20.0, 15.0, Gender.MALE);
        Lycanthrope creature = new Lycanthrope("Lupus", 25, 1.85, 22.0, 10.0, Gender.MALE);
        Legionnaire roman = new Legionnaire("Fortus", 30, 1.80, 18.0, 12.0, Gender.MALE);

        // Gaul + Creature allowed
        assertDoesNotThrow(() -> village.addCharacter(gaul));
        assertDoesNotThrow(() -> village.addCharacter(creature));

        // Roman forbidden -> IllegalArgumentException from Place.addCharacter
        assertThrows(IllegalArgumentException.class, () -> village.addCharacter(roman));
    }

    @Test
    void getCharactersShouldReturnDefensiveCopy() {
        Place place = new Battlefield("Field", 100.0);
        BlackSmith gaul = new BlackSmith("Asterix", 35, 1.70, 20.0, 15.0, Gender.MALE);

        place.addCharacter(gaul);

        List<com.asterix.model.character.Character> copy = place.getCharacters();
        assertEquals(1, copy.size());
        assertTrue(copy.contains(gaul));

        // Modifying the copy must not affect internal state (Test Branch)
        copy.clear();
        assertEquals(1, place.getCharacters().size());
    }

    // new test: Covers removing a character that is not present
    @Test
    void removeCharacterShouldHandleNonExistingCharacter() {
        Place place = new Battlefield("Field", 100.0);
        BlackSmith gaul = new BlackSmith("Asterix", 35, 1.70, 20.0, 15.0, Gender.MALE);
        Legionnaire nonPresent = new Legionnaire("NonPresent", 30, 1.80, 18.0, 12.0, Gender.MALE);

        place.addCharacter(gaul);
        assertEquals(1, place.getCharacters().size());

        // Removing a non-present character (should return false/do nothing for List.remove)
        place.removeCharacter(nonPresent);
        assertEquals(1, place.getCharacters().size());

        // Removing the present character (Test Branch)
        place.removeCharacter(gaul);
        assertTrue(place.getCharacters().isEmpty());
    }

    @Test
    void getFoodsShouldExposeInternalList() {
        Place place = new Battlefield("Field", 100.0);

        // Fix: using the factory instead of manual constructor
        Food bread = FoodType.HONEY.create(); // Honey is a SimpleFood

        List<Food> foodsRef = place.getFoods();
        foodsRef.add(bread);

        // getFoods returns the reference to the internal list (Test Branch)
        assertEquals(1, place.getFoods().size());
        assertSame(foodsRef, place.getFoods());
    }

    // new test: Covers branches for addFood and removeFood
    @Test
    void foodManagementShouldCoverAllBranches() {
        Place place = new Battlefield("Field", 100.0);

        // Fix: using the factory
        Food boar = FoodType.WILDBOAR.create();

        // 1. addFood(Food food) -> Branch food != null
        place.addFood(boar);
        assertEquals(1, place.getFoods().size());

        // 2. addFood(null) -> Branch food == null (must be ignored)
        assertDoesNotThrow(() -> place.addFood(null));
        assertEquals(1, place.getFoods().size());

        // 3. removeFood(Food food) -> Branch food present
        place.removeFood(boar);
        assertTrue(place.getFoods().isEmpty());

        // 4. removeFood(Food food) -> Branch food not present
        place.removeFood(boar);
        assertTrue(place.getFoods().isEmpty());
    }

    // new test: Covers all paths of displayCharacteristics (with/without items)
    @Test
    void displayCharacteristicsShouldCoverAllCombinations() {
        Place place = new Battlefield("Field", 100.0);
        BlackSmith gaul = new BlackSmith("Asterix", 35, 1.70, 20.0, 15.0, Gender.MALE);

        // Fix: using the factory
        Food boar = FoodType.WILDBOAR.create();

        // CASE 1: Empty (Characters: None, Foods: Empty) - Implicitly covered
        assertDoesNotThrow(place::displayCharacteristics);

        // CASE 2: With Characters, Without Food
        place.addCharacter(gaul);
        StringBuilder sb2 = place.displayCharacteristics();
        // Adjust these assertions based on your actual displayCharacteristics output format
        // Checking generic indicators (like emoji or text parts)
        assertTrue(sb2.toString().contains("Asterix"));

        // CASE 3: With Characters, With Food (Test Branch)
        place.addFood(boar);
        StringBuilder sb3 = place.displayCharacteristics();
        assertTrue(sb3.toString().contains("Asterix"));
        assertTrue(sb3.toString().contains("Wildboar"));

        // Cleanup
        place.removeCharacter(gaul);
        place.removeFood(boar);

        // CASE 4: Without Characters, With Food (Test Branch)
        place.addFood(boar);
        StringBuilder sb4 = place.displayCharacteristics();
        // Assuming your output shows "None" or similar when empty
        // Verify specifically that the food is displayed
        assertTrue(sb4.toString().contains("Wildboar"));
    }
}