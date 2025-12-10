package com.asterix.model.place;

import com.asterix.model.character.Gender;
import com.asterix.model.character.creature.Lycanthrope;
import com.asterix.model.character.gaul.BlackSmith;
import com.asterix.model.character.roman.Legionnaire;
import com.asterix.model.item.Food;
import com.asterix.model.item.SimpleFood;
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
        Place place = new Battlefield("Champ", 100.0);

        assertEquals("Champ", place.getName());
        assertNotNull(place.getCharacters());
        assertTrue(place.getCharacters().isEmpty());
        assertNotNull(place.getFoods());
        assertTrue(place.getFoods().isEmpty());

        String txt = place.toString();
        assertTrue(txt.contains("Champ"));
        assertTrue(txt.contains("population"));
    }

    @Test
    void addCharacterShouldThrowOnNull() {
        Place place = new Battlefield("Champ", 100.0);

        assertThrows(IllegalArgumentException.class, () -> place.addCharacter(null));
    }

    @Test
    void addCharacterShouldUseCanEnterRules_GaulVillage() {
        Place village = new GaulVillage("Village gaulois", 50.0);

        BlackSmith gaul = new BlackSmith("Astérix", 35, 1.70, 20.0, 15.0, Gender.MALE);
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
        Place place = new Battlefield("Champ", 100.0);
        BlackSmith gaul = new BlackSmith("Astérix", 35, 1.70, 20.0, 15.0, Gender.MALE);

        place.addCharacter(gaul);

        List<com.asterix.model.character.Character> copy = place.getCharacters();
        assertEquals(1, copy.size());
        assertTrue(copy.contains(gaul));

        // Modifying the copy must not affect internal state (Test Branch)
        copy.clear();
        assertEquals(1, place.getCharacters().size());
    }

    // NOUVEAU TEST : Couvre la suppression d'un caractère non présent
    @Test
    void removeCharacterShouldHandleNonExistingCharacter() {
        Place place = new Battlefield("Champ", 100.0);
        BlackSmith gaul = new BlackSmith("Astérix", 35, 1.70, 20.0, 15.0, Gender.MALE);
        Legionnaire nonPresent = new Legionnaire("NonPresent", 30, 1.80, 18.0, 12.0, Gender.MALE);

        place.addCharacter(gaul);
        assertEquals(1, place.getCharacters().size());

        // Suppression d'un personnage non présent (devrait retourner false/ne rien changer pour List.remove)
        place.removeCharacter(nonPresent);
        assertEquals(1, place.getCharacters().size());

        // Suppression du personnage présent (Test Branch)
        place.removeCharacter(gaul);
        assertTrue(place.getCharacters().isEmpty());
    }

    @Test
    void getFoodsShouldExposeInternalList() {
        Place place = new Battlefield("Champ", 100.0);

        Food bread = new SimpleFood("Bread", "FOOD", 2, true, true);

        List<Food> foodsRef = place.getFoods();
        foodsRef.add(bread);

        // getFoods retourne la référence à la liste interne (Test Branch)
        assertEquals(1, place.getFoods().size());
        assertSame(foodsRef, place.getFoods());
    }

    // NOUVEAU TEST : Couvre les branches addFood et removeFood
    @Test
    void foodManagementShouldCoverAllBranches() {
        Place place = new Battlefield("Champ", 100.0);
        Food boar = new SimpleFood("Boar", "MEAT", 15, true, true);

        // 1. addFood(Food food) -> Branch food != null
        place.addFood(boar);
        assertEquals(1, place.getFoods().size());

        // 2. addFood(null) -> Branch food == null (doit être ignoré)
        assertDoesNotThrow(() -> place.addFood(null));
        assertEquals(1, place.getFoods().size());

        // 3. removeFood(Food food) -> Branch food present
        place.removeFood(boar);
        assertTrue(place.getFoods().isEmpty());

        // 4. removeFood(Food food) -> Branch food non present
        place.removeFood(boar);
        assertTrue(place.getFoods().isEmpty());
    }

    // NOUVEAU TEST : Couvre tous les chemins de displayCharacteristics (avec/sans éléments)
    @Test
    void displayCharacteristicsShouldCoverAllCombinations() {
        Place place = new Battlefield("Champ", 100.0);
        BlackSmith gaul = new BlackSmith("Astérix", 35, 1.70, 20.0, 15.0, Gender.MALE);
        Food boar = new SimpleFood("Boar", "MEAT", 15, true, true);

        // CAS 1: Empty (Characters: None, Foods: Empty) - Déjà couvert implicitement
        assertDoesNotThrow(place::displayCharacteristics);

        // CAS 2: With Characters, Without Food
        place.addCharacter(gaul);
        StringBuilder sb2 = place.displayCharacteristics();
        assertTrue(sb2.toString().contains("👤 "));
        assertTrue(sb2.toString().contains("Food Inventory ---\n   (Empty)"));

        // CAS 3: With Characters, With Food (Test Branch)
        place.addFood(boar);
        StringBuilder sb3 = place.displayCharacteristics();
        assertTrue(sb3.toString().contains("👤 "));
        assertTrue(sb3.toString().contains("🍎 "));

        // Cleanup
        place.removeCharacter(gaul);
        place.removeFood(boar);

        // CAS 4: Without Characters, With Food (Test Branch)
        place.addFood(boar);
        StringBuilder sb4 = place.displayCharacteristics();
        assertTrue(sb4.toString().contains("Occupants ---\n   (None)"));
        assertTrue(sb4.toString().contains("🍎 "));
    }
}