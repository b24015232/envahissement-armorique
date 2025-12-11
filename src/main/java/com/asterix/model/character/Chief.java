package com.asterix.model.character;

import com.asterix.model.character.gaul.Druid;
import com.asterix.model.place.Place;
import com.asterix.model.item.Food;
import java.util.List;
import static com.asterix.model.character.Character.MAX_HEALTH;

/**
 * Represents a Clan Chief attached to a specific Location.
 * <p>
 * [cite_start]According to the specifications, a Clan Chief is responsible for a location[cite: 302].
 * They can manage their location (examine), heal/feed troops, and order Druids to brew potions.
 * </p>
 */
public class Chief {

    private String name;
    private String gender;
    private int age;

    /**
     * The location this chief is responsible for.
     */
    private Place place;

    /**
     * Creates a new Clan Chief.
     *
     * @param name   The name of the chief.
     * @param gender The gender of the chief.
     * @param age    The age of the chief.
     * @param place  The initial location they manage.
     */
    public Chief(String name, String gender, int age, Place place) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.place = place;
    }

    /**
     * Examines the location by displaying its characteristics, characters, and food.
     *
     * [cite_start]@return A formatted string describing the location status[cite: 307].
     */
    public String examineLocation() {
        if (place == null) return "This chief has no assigned location.";
        return place.toString();
    }

    /**
     * Heals all characters present in the chief's location.
     * <p>
     * [cite_start]This restores characters to their maximum health[cite: 309].
     * </p>
     */
    public void healCharactersInLocation() {
        if (place == null) return;

        List<Character> characters = place.getCharacters();
        int healedCount = 0;

        for (Character p : characters) {
            p.heal(MAX_HEALTH);
            healedCount++;
        }

        System.out.println(this.name + " has healed " + healedCount + " characters in " + place.getName());
    }

    /**
     * Feeds characters in the location using available food.
     * <p>
     * [cite_start]Characters consume food found in the location to reduce their hunger[cite: 310].
     * </p>
     */
    public void feedCharactersInLocation() {
        if (place == null) return;

        List<Character> hungryPeople = place.getCharacters();
        List<Food> pantry = place.getFoods();

        if (pantry.isEmpty()) {
            System.out.println("No food available in " + place.getName() + "!");
            return;
        }

        for (Character p : hungryPeople) {
            if (pantry.isEmpty()) break;

            Food food = pantry.remove(0);
            p.eat(food);
            System.out.println(p.getName() + " ate " + food.getName());
        }
    }

    /**
     * Orders a Druid to brew a magic potion.
     * <p>
     * [cite_start]The Chief asks a Druid to perform the concoction process[cite: 311].
     * </p>
     *
     * @param druid The target Druid (must be in the same location ideally).
     */
    public void orderPotion(Druid druid) {
        if (druid != null) {
            druid.concoctPotion();
            System.out.println(this.name + " ordered " + druid.getName() + " to brew potion.");
        }
    }

    /**
     * Forces a specific character to drink a dose of potion.
     * <p>
     * [cite_start]This distributes the effects of the potion to the troops[cite: 316].
     * </p>
     *
     * @param target The character who must drink.
     */
    public void makeCharacterDrinkPotion(Character target) {
        if (target != null) {
            target.drinkPotion(1);
            System.out.println(this.name + " made " + target.getName() + " drink potion.");
        }
    }

    /**
     * Transfers a character from the current location to a Battlefield or Enclosure.
     * <p>
     * [cite_start]Checks if the destination allows entry before moving the character[cite: 317].
     * </p>
     *
     * @param target      The character to move.
     * @param destination The destination (must be Battlefield or Enclosure).
     */
    public void transferCharacter(Character target, Place destination) {
        if (target == null) {
            System.out.println("Target character is null.");
            return;
        }

        if (destination.canEnter(target)) {
            place.removeCharacter(target);
            destination.addCharacter(target);
            System.out.println(this.name + " transferred " + target.getName() + " to " + destination.getName());
        } else {
            System.out.println("You can't move " + target.getName() + " to " + destination.getName());
        }
    }

    /**
     * Creates a new character in the current location.
     * <p>
     * [cite_start]Adds a newly instantiated character directly to the chief's place[cite: 308].
     * </p>
     *
     * @param newCharacter The character instance to add.
     */
    public void recruitCharacter(Character newCharacter) {
        if (place != null && newCharacter != null) {
            place.addCharacter(newCharacter);
            System.out.println(this.name + " recruited " + newCharacter.getName() + ".");
        }
    }

    // --- Getters & Setters ---

    /**
     * Gets the name of the Chief.
     *
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the age of the Chief.
     *
     * @return The age.
     */
    public int getAge() {
        return age;
    }

    /**
     * Gets the gender of the Chief.
     *
     * @return The gender string.
     */
    public String getGender() {
        return gender;
    }

    /**
     * Gets the location currently managed by the Chief.
     *
     * @return The location.
     */
    public Place getLocation() {
        return place;
    }

    /**
     * Sets the location managed by the Chief.
     *
     * @param location The new location.
     */
    public void setLocation(Place location) {
        this.place = location;
    }
}