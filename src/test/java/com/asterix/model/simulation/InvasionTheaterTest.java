package com.asterix.model.simulation;

import com.asterix.model.character.Character;
import com.asterix.model.character.Gender;
import com.asterix.model.character.gaul.BlackSmith;
import com.asterix.model.character.roman.Legionnaire;
import com.asterix.model.place.Battlefield;
import com.asterix.model.place.GaulVillage;
import com.asterix.model.place.Place;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class InvasionTheaterTest {

    @Test
    void constructorShouldInitializeWithNameAndEmptyPlaces() {
        InvasionTheater theater = new InvasionTheater("Armorica");
        assertEquals("Armorica", theater.getName());
        assertTrue(theater.getPlaces().isEmpty());
    }

    @Test
    void handleFightsShouldPairGaulsAndRomansAndRemoveDead() {
        InvasionTheater theater = new InvasionTheater("Armorica");
        Battlefield battlefield = new Battlefield("Battlefield", 100.0);
        theater.addPlace(battlefield);

        BlackSmith weakGaul = new BlackSmith("Weakix", 30, 1.70, 5.0, 0.0, Gender.MALE);
        Legionnaire strongRoman = new Legionnaire("Fortus", 30, 1.80, 200.0, 100.0, Gender.MALE);

        battlefield.addCharacter(weakGaul);
        battlefield.addCharacter(strongRoman);

        theater.handleFights(); // New method name

        List<Character> remaining = battlefield.getCharacters();
        assertEquals(1, remaining.size());
        assertSame(strongRoman, remaining.get(0));
    }

    @Test
    void toStringShouldHandleNoPlaces() {
        InvasionTheater theater = new InvasionTheater("Armorica");
        String text = theater.toString();

        assertTrue(text.contains("Armorica"));
        assertTrue(text.contains("No places configured"));
    }

    @Test
    void toStringShouldShowNonBattlefieldPlaceWithNoCharacters() {
        InvasionTheater theater = new InvasionTheater("Armorica");
        GaulVillage village = new GaulVillage("Gaul Village", 50.0);
        theater.addPlace(village);

        String text = theater.toString();

        assertTrue(text.contains("Gaul Village"));
        assertTrue(text.contains("(No characters present)"));
        assertFalse(text.contains("BATTLE ZONE"));
    }

    @Test
    void toStringShouldContainTheaterNamePlacesAndCharacters() {
        InvasionTheater theater = new InvasionTheater("Armorica");
        Battlefield battlefield = new Battlefield("Battlefield", 100.0);
        theater.addPlace(battlefield);

        BlackSmith gaul = new BlackSmith("Obelix", 35, 1.70, 20.0, 15.0, Gender.MALE);
        battlefield.addCharacter(gaul);

        String text = theater.toString();

        assertTrue(text.contains("Armorica"));
        assertTrue(text.contains("Battlefield"));
        assertTrue(text.contains("Obelix"));
        assertTrue(text.contains("BATTLE ZONE"));
    }
}