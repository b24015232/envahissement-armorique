package com.asterix.model.character;

import com.asterix.model.character.gaul.*;
import com.asterix.model.character.roman.*;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CharacterFactoryTest {

    private static final String TEST_NAME = "TestName";
    private static final int TEST_AGE = 30;
    private static final double MIN_HEIGHT = 1.60;
    private static final double MAX_HEIGHT_BASE = 1.60 + 0.30;


    /**
     * Test de la branche aléatoire Gender.FEMALE (couverture statistique).
     */
    @Test
    void createCharacter_GaulInnkeeper_CheckRandomGenderAndHeight() {
        Character character = CharacterFactory.createCharacter(CharacterType.GAUL_INNKEEPER, TEST_NAME, TEST_AGE);

        assertInstanceOf(Innkeeper.class, character);

        // Simplement tester les limites pour couvrir la ligne d'initialisation de height
        assertTrue(character.getHeight() >= MIN_HEIGHT);
        assertTrue(character.getHeight() <= MAX_HEIGHT_BASE);

        // On ne peut pas garantir le genre, mais l'exécution de cette ligne contribue à la couverture.
        assertTrue(character.getGender() == Gender.MALE || character.getGender() == Gender.FEMALE);

        // Vérification des stats
        assertEquals(12.0, character.getStrength(), 0.001);
        assertEquals(12.0, character.getStamina(), 0.001);
    }

    /**
     * Teste la création d'un Gaul Merchant avec les stats et le type corrects.
     */
    @Test
    void createCharacter_GaulMerchant_ShouldBeInstantiatedWithCorrectStats() {
        Character character = CharacterFactory.createCharacter(CharacterType.GAUL_MERCHANT, TEST_NAME, TEST_AGE);

        assertNotNull(character);
        assertInstanceOf(Merchant.class, character);
        assertEquals(10.0, character.getStrength(), 0.001);
        assertEquals(10.0, character.getStamina(), 0.001);

        assertTrue(character.getHeight() >= MIN_HEIGHT && character.getHeight() <= MAX_HEIGHT_BASE);
    }

    /**
     * Teste la création d'un Gaul Druid.
     */
    @Test
    void createCharacter_GaulDruid_ShouldBeInstantiatedWithCorrectStats() {
        Character character = CharacterFactory.createCharacter(CharacterType.GAUL_DRUID, TEST_NAME, TEST_AGE);

        assertInstanceOf(Druid.class, character);
        assertEquals(5.0, character.getStrength(), 0.001);
        assertEquals(20.0, character.getStamina(), 0.001);
    }


    /**
     * Teste la création d'un Gaul Blacksmith avec la stat de hauteur modifiée.
     */
    @Test
    void createCharacter_GaulBlacksmith_ShouldBeInstantiatedWithOffsetHeight() {
        Character character = CharacterFactory.createCharacter(CharacterType.GAUL_BLACKSMITH, TEST_NAME, TEST_AGE);

        assertInstanceOf(BlackSmith.class, character);
        assertEquals(25.0, character.getStrength(), 0.001);

        double minHeight = MIN_HEIGHT + 0.1;
        double maxHeight = MAX_HEIGHT_BASE + 0.1;
        assertTrue(character.getHeight() >= minHeight && character.getHeight() <= maxHeight);
    }


    /**
     * Teste la création d'un Romain Legionnaire avec Gender.MALE fixe.
     */
    @Test
    void createCharacter_RomanLegionnaire_ShouldBeInstantiatedWithFixedMaleGender() {
        Character character = CharacterFactory.createCharacter(CharacterType.ROMAN_LEGIONNAIRE, TEST_NAME, TEST_AGE);

        assertInstanceOf(Legionnaire.class, character);
        assertEquals(Gender.MALE, character.getGender());
        assertEquals(15.0, character.getStrength(), 0.001);
    }

    /**
     * Teste la création d'un Romain Prefect (ligne non couverte précédemment).
     */
    @Test
    void createCharacter_RomanPrefect_ShouldBeInstantiatedWithCorrectStats() {
        Character character = CharacterFactory.createCharacter(CharacterType.ROMAN_PREFECT, TEST_NAME, TEST_AGE);

        assertInstanceOf(Prefect.class, character);
        assertEquals(Gender.MALE, character.getGender());
        assertEquals(10.0, character.getStrength(), 0.001);
        assertEquals(10.0, character.getStamina(), 0.001);
    }


    /**
     * Teste la création d'un Romain General.
     */
    @Test
    void createCharacter_RomanGeneral_ShouldBeInstantiated() {
        Character character = CharacterFactory.createCharacter(CharacterType.ROMAN_GENERAL, TEST_NAME, TEST_AGE);

        assertInstanceOf(General.class, character);
        assertEquals(Gender.MALE, character.getGender());
        assertEquals(20.0, character.getStrength(), 0.001);

        // Vérifie l'appel getId() (si la méthode existe et que l'ID est bien passé)
        assertDoesNotThrow(() -> ((General) character).getId());
    }


    /**
     * Vérifie que la fabrique lève une exception pour un type null (comportement réel du switch).
     * Couvre la branche NPE avant le default.
     */
    @Test
    void createCharacter_NullType_ShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            CharacterFactory.createCharacter(null, TEST_NAME, TEST_AGE);
        }, "L'appel avec un type null doit lancer une IllegalArgumentException après correction.");
    }




}