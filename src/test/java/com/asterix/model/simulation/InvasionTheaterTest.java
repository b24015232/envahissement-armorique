package com.asterix.model.simulation;

import com.asterix.model.character.Character;
import com.asterix.model.character.Gender;
import com.asterix.model.character.gaul.BlackSmith;
import com.asterix.model.character.roman.Legionnaire;
import com.asterix.model.place.Battlefield;
import com.asterix.model.place.GaulVillage;
import com.asterix.model.place.Place;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link InvasionTheater} class.
 */
class InvasionTheaterTest {

    @Test
    void constructorShouldInitializeWithNameAndEmptyPlaces() {
        InvasionTheater theater = new InvasionTheater("Armorique");

        assertEquals("Armorique", theater.getName());
        assertNotNull(theater.getPlaces());
        assertTrue(theater.getPlaces().isEmpty());
    }

    @Test
    void addPlaceShouldIgnoreNullAndAddNonNullPlaces() {
        InvasionTheater theater = new InvasionTheater("Armorique");
        Battlefield battlefield = new Battlefield("Champ de bataille", 100.0);

        assertEquals(0, theater.getPlaces().size());

        theater.addPlace(null); // should do nothing (just log)
        assertEquals(0, theater.getPlaces().size());

        theater.addPlace(battlefield);
        assertEquals(1, theater.getPlaces().size());
        assertSame(battlefield, theater.getPlaces().get(0));
    }

    @Test
    void getPlacesShouldReturnCopyNotInternalList() {
        InvasionTheater theater = new InvasionTheater("Armorique");
        Battlefield battlefield = new Battlefield("Champ de bataille", 100.0);
        theater.addPlace(battlefield);

        List<Place> copy = theater.getPlaces();
        assertEquals(1, copy.size());
        assertSame(battlefield, copy.get(0));

        copy.clear();
        assertEquals(1, theater.getPlaces().size());
    }

    @Test
    void gererCombatsShouldDoNothingWhenNoPlaces() {
        InvasionTheater theater = new InvasionTheater("Armorique");
        theater.handleFights();

        assertTrue(theater.getPlaces().isEmpty());
    }

    @Test
    void gererCombatsShouldHandleNullPlacesListGracefully() throws Exception {
        InvasionTheater theater = new InvasionTheater("Armorique");

        Field f = InvasionTheater.class.getDeclaredField("places");
        f.setAccessible(true);
        f.set(theater, null);

        assertDoesNotThrow(theater::handleFights);
    }

    @Test
    void gererCombatsShouldIgnoreNonBattlefieldPlaces() {
        InvasionTheater theater = new InvasionTheater("Armorique");

        GaulVillage village = new GaulVillage("Village gaulois", 50.0);
        theater.addPlace(village);

        theater.handleFights();

        assertEquals(1, theater.getPlaces().size());
        assertSame(village, theater.getPlaces().get(0));
    }

    @Test
    void gererCombatsShouldDoNothingOnEmptyBattlefield() {
        InvasionTheater theater = new InvasionTheater("Armorique");
        Battlefield battlefield = new Battlefield("Champ de bataille", 100.0);
        theater.addPlace(battlefield);

        assertTrue(battlefield.getCharacters().isEmpty());

        assertDoesNotThrow(theater::handleFights);
        assertTrue(battlefield.getCharacters().isEmpty());
    }

    @Test
    void gererCombatsShouldIgnoreAlreadyDeadCharactersWhenSplittingCamps() {
        InvasionTheater theater = new InvasionTheater("Armorique");
        Battlefield battlefield = new Battlefield("Champ de bataille", 100.0);
        theater.addPlace(battlefield);

        BlackSmith deadGaul = new BlackSmith("Mortix", 30, 1.70, 5.0, 0.0, Gender.MALE);
        deadGaul.die();
        Legionnaire roman = new Legionnaire("Fortus", 30, 1.80, 50.0, 30.0, Gender.MALE);

        battlefield.addCharacter(deadGaul);
        battlefield.addCharacter(roman);

        theater.handleFights();
        List<Character> remaining = battlefield.getCharacters();
        assertEquals(1, remaining.size());
        assertSame(roman, remaining.get(0));
        assertTrue(roman.isAlive());
    }

    @Test
    void gererCombatsShouldUseRomanBranchWhenBuildingCamps() {
        InvasionTheater theater = new InvasionTheater("Armorique");
        Battlefield battlefield = new Battlefield("Champ de bataille", 100.0);
        theater.addPlace(battlefield);

        Legionnaire roman1 = new Legionnaire("R1", 30, 1.80, 10.0, 10.0, Gender.MALE);
        Legionnaire roman2 = new Legionnaire("R2", 32, 1.82, 12.0, 12.0, Gender.MALE);

        battlefield.addCharacter(roman1);
        battlefield.addCharacter(roman2);


        assertDoesNotThrow(theater::handleFights);


        assertEquals(2, battlefield.getCharacters().size());
    }

    @Test
    void gererCombatsShouldPairGaulsAndRomansAndRemoveDeadFromBattlefield() {
        InvasionTheater theater = new InvasionTheater("Armorique");
        Battlefield battlefield = new Battlefield("Champ de bataille", 100.0);
        theater.addPlace(battlefield);

        BlackSmith weakGaul = new BlackSmith("Faibleix", 30, 1.70, 5.0, 0.0, Gender.MALE);
        Legionnaire strongRoman = new Legionnaire("Fortus", 30, 1.80, 200.0, 100.0, Gender.MALE);

        battlefield.addCharacter(weakGaul);
        battlefield.addCharacter(strongRoman);

        assertEquals(2, battlefield.getCharacters().size());

        theater.handleFights();

        List<Character> remaining = battlefield.getCharacters();
        assertEquals(1, remaining.size());
        assertSame(strongRoman, remaining.get(0));
        assertTrue(strongRoman.isAlive());
    }

    @Test
    void gererCombatsShouldNotCrashIfOnlyOneCampPresent() {
        InvasionTheater theater = new InvasionTheater("Armorique");
        Battlefield battlefield = new Battlefield("Champ de bataille", 100.0);
        theater.addPlace(battlefield);

        BlackSmith lonelyGaul = new BlackSmith("Soloix", 30, 1.70, 10.0, 10.0, Gender.MALE);
        battlefield.addCharacter(lonelyGaul);

        theater.handleFights();

        assertEquals(1, battlefield.getCharacters().size());
        assertSame(lonelyGaul, battlefield.getCharacters().get(0));
    }

    @Test
    void gererCombatsShouldIgnoreAliveCharactersThatAreNeitherGaulNorRoman() {
        InvasionTheater theater = new InvasionTheater("Armorique");
        Battlefield battlefield = new Battlefield("Champ de bataille", 100.0);
        theater.addPlace(battlefield);

        BlackSmith gaul = new BlackSmith("Astérix", 35, 1.70, 20.0, 15.0, Gender.MALE);
        com.asterix.model.character.creature.Lycanthrope creature =
                new com.asterix.model.character.creature.Lycanthrope(
                        "Lupus", 25, 1.85, 22.0, 10.0, Gender.MALE
                );

        battlefield.addCharacter(gaul);
        battlefield.addCharacter(creature);
        theater.handleFights();
        boolean stillThere = battlefield.getCharacters().stream()
                .anyMatch(c -> "Lupus".equals(c.getName()));
        assertTrue(stillThere);
    }


    @Test
    void appliquerAleasGenererAlimentsVieillirAlimentsShouldBeCallable() {
        InvasionTheater theater = new InvasionTheater("Armorique");
        assertDoesNotThrow(theater::applyRandomEvents);
        assertDoesNotThrow(theater::generateFood);
        assertDoesNotThrow(theater::ageFood);
    }

    @Test
    void toStringShouldHandleNoPlaces() {
        InvasionTheater theater = new InvasionTheater("Armorique");

        String text = theater.toString();

        assertTrue(text.contains("Armorique"));
        assertTrue(text.contains("Aucun lieu configuré"));
    }

    @Test
    void toStringShouldHandleNullPlacesListGracefully() throws Exception {
        InvasionTheater theater = new InvasionTheater("Armorique");

        // On force places à null pour couvrir la partie (places == null) dans toString()
        Field f = InvasionTheater.class.getDeclaredField("places");
        f.setAccessible(true);
        f.set(theater, null);

        String text = theater.toString();

        assertTrue(text.contains("Armorique"));
        assertTrue(text.contains("Aucun lieu configuré"));
    }

    @Test
    void toStringShouldShowNonBattlefieldPlaceWithNoCharacters() {
        InvasionTheater theater = new InvasionTheater("Armorique");
        GaulVillage village = new GaulVillage("Village gaulois", 50.0);
        theater.addPlace(village);

        String text = theater.toString();

        assertTrue(text.contains("Village gaulois"));
        assertTrue(text.contains("(Aucun personnage présent)"));
        // Pas de [⚔️ ZONE DE COMBAT] car ce n'est pas un Battlefield
        assertFalse(text.contains("ZONE DE COMBAT"));
    }

    @Test
    void toStringShouldListMultipleCharactersSeparatedByCommas() {
        InvasionTheater theater = new InvasionTheater("Armorique");
        Battlefield battlefield = new Battlefield("Champ de bataille", 100.0);
        theater.addPlace(battlefield);

        BlackSmith gaul1 = new BlackSmith("Astérix", 35, 1.70, 20.0, 15.0, Gender.MALE);
        BlackSmith gaul2 = new BlackSmith("Obélix", 35, 1.80, 30.0, 20.0, Gender.MALE);

        battlefield.addCharacter(gaul1);
        battlefield.addCharacter(gaul2);

        String text = theater.toString();
        assertTrue(text.contains("Astérix"));
        assertTrue(text.contains("Obélix"));
        assertTrue(text.contains("Astérix, Obélix"));
    }

    @Test
    void toStringShouldContainTheaterNamePlacesAndCharacters() {
        InvasionTheater theater = new InvasionTheater("Armorique");
        Battlefield battlefield = new Battlefield("Champ de bataille", 100.0);
        theater.addPlace(battlefield);

        BlackSmith gaul = new BlackSmith("Astérix", 35, 1.70, 20.0, 15.0, Gender.MALE);
        battlefield.addCharacter(gaul);

        String text = theater.toString();

        assertTrue(text.contains("Armorique"));
        assertTrue(text.contains("Champ de bataille"));
        assertTrue(text.contains("Astérix"));
        assertTrue(text.contains("ZONE DE COMBAT"));
    }
}
