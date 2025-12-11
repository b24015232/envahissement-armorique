package com.asterix.model.simulation;

import com.asterix.model.character.Character;
import com.asterix.model.character.Chief;
import com.asterix.model.character.Gender;
import com.asterix.model.place.Battlefield;
import com.asterix.model.place.GaulVillage;
import com.asterix.model.place.Place;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class InvasionTheaterTest {

    private static class TestChief extends Chief {
        public TestChief(String name, Place place) {
            super(name, "UNKNOWN", 0, place);
        }
    }

    private static abstract class SimpleCharacter extends Character {
        protected boolean isDead;

        public SimpleCharacter(String name, boolean isDeadInitially) {
            super(name, 0, 0, 0, 0, Gender.MALE);
            this.isDead = isDeadInitially;
        }

        @Override
        public boolean isAlive() {
            return !isDead;
        }

        @Override
        public void resolveFight(Character opponent) {
            if (opponent.isAlive()) {
                this.isDead = true;
            }
        }
    }

    private static class FakeWeakGaul extends SimpleCharacter {
        public FakeWeakGaul(String name) { super(name, false); }

        /**
         * @return
         */
        @Override
        public double getHealth() {
            return 0;
        }
    }

    private static class FakeStrongRoman extends SimpleCharacter {
        public FakeStrongRoman(String name) { super(name, false); }

        /**
         * @return
         */
        @Override
        public double getHealth() {
            return 0;
        }
    }

    @Test
    void constructorShouldInitializeWithNameAndEmptyPlaces() {
        InvasionTheater theater = new InvasionTheater("Armorica");
        assertEquals("Armorica", theater.getName());
        assertTrue(theater.getPlaces().isEmpty());
    }

    @Test
    void addPlaceShouldAddPlaceToList() {
        InvasionTheater theater = new InvasionTheater("Armorica");
        Battlefield battlefield = new Battlefield("Test Field", 10.0);

        theater.addPlace(battlefield);

        List<Place> places = theater.getPlaces();
        assertEquals(1, places.size());
        assertSame(battlefield, places.get(0));
    }

    @Test
    void addPlaceShouldIgnoreNull() {
        InvasionTheater theater = new InvasionTheater("Armorica");
        int initialSize = theater.getPlaces().size();

        theater.addPlace(null);

        assertEquals(initialSize, theater.getPlaces().size());
    }


    @Test
    void toStringShouldHandleNoPlaces() {
        InvasionTheater theater = new InvasionTheater("Armorica");
        String text = theater.toString();

        assertTrue(text.contains("Armorica"));
        assertTrue(text.contains("No location configured"));
    }

    @Test
    void toStringShouldShowNonBattlefieldPlaceWithNoCharacters() {
        InvasionTheater theater = new InvasionTheater("Armorica");
        TestChief chiefStub = new TestChief("Chief", null);
        GaulVillage village = new GaulVillage("Gaul Village", 50.0, chiefStub);
        chiefStub.setLocation(village);

        theater.addPlace(village);

        String text = theater.toString();

        assertTrue(text.contains("Gaul Village"));
        assertTrue(text.contains("(No characters present)"));
        assertFalse(text.contains("BATTLEFIELD"));
    }

    @Test
    void toStringShouldContainTheaterNamePlacesAndCharacters() {
        InvasionTheater theater = new InvasionTheater("Armorica");
        Battlefield battlefield = new Battlefield("Battlefield", 100.0);
        theater.addPlace(battlefield);

        FakeWeakGaul gaul = new FakeWeakGaul("Obelix");
        battlefield.addCharacter(gaul);

        String text = theater.toString();

        assertTrue(text.contains("Armorica"));
        assertTrue(text.contains("Battlefield"));
        assertTrue(text.contains("Obelix"));
        assertTrue(text.contains("BATTLEFIELD"));
    }

}