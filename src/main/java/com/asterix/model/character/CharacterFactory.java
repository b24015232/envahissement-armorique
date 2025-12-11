package com.asterix.model.character;

import com.asterix.model.character.gaul.*;
import com.asterix.model.character.roman.*;
import com.asterix.model.character.creature.Lycanthrope;
import java.util.Random;

/**
 * Factory class responsible for instantiating Character objects.
 * <p>
 * It encapsulates the creation logic and assigns default attribute values
 * (strength, stamina) based on the specific character type requested.
 * This ensures that specific roles (Merchant, Soldier, etc.) are initialized consistently.
 * </p>
 */
public class CharacterFactory {

    /**
     * Random number generator used to determine gender and slight variations in height.
     */
    private static final Random random = new Random();

    /**
     * Creates a new character instance based on the provided type.
     * <p>
     * This method initializes the character with specific default stats (strength, stamina)
     * corresponding to their role. Gender and height are generated randomly to add variety.
     * </p>
     *
     * @param type The specific type of character to create (e.g., GAUL_MERCHANT, ROMAN_LEGIONNAIRE).
     * @param name The name of the new character.
     * @param age  The age of the new character.
     * @return A new instance of a {@link Character} subclass with initialized stats.
     * @throws IllegalArgumentException If the provided character type is not supported by the factory.
     */
    public static Character createCharacter(CharacterType type, String name, int age) {
        if (type == null) {
            throw new IllegalArgumentException("Character type cannot be null.");
        }
        Gender gender = random.nextBoolean() ? Gender.MALE : Gender.FEMALE;
        // Generate a random height between 1.60m and 1.90m
        double height = 1.60 + (random.nextDouble() * 0.30);
        int id = 0; // Internal ID counter for Generals (scope is local, effectively 0 every call)

        return switch (type) {
            case GAUL_MERCHANT -> new Merchant(name, age, height, 10, 10, gender);
            case GAUL_DRUID -> new Druid(name, age, height, 5, 20, gender);
            case GAUL_BLACKSMITH -> new BlackSmith(name, age, height + 0.1, 25, 20, gender);
            case GAUL_INNKEEPER -> new Innkeeper(name, age, height, 12, 12, gender);

            case ROMAN_LEGIONNAIRE -> new Legionnaire(name, age, height, 15, 15, Gender.MALE);
            case ROMAN_PREFECT -> new Prefect(name, age, height, 10, 10, Gender.MALE);
            case ROMAN_GENERAL -> new General(id++, name, age, height, 20, 15, Gender.MALE);

            default -> throw new IllegalArgumentException("Unknown character type: " + type);
        };
    }
}