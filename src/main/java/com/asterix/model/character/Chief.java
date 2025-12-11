package com.asterix.model.character;
import com.asterix.model.character.gaul.Druid;
import com.asterix.model.place.Place;
import com.asterix.model.place.Battlefield;
import com.asterix.model.place.CreatureEnclosure;
import com.asterix.model.item.Food;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import static com.asterix.model.character.Character.MAX_HEALTH;
/**
 * Represents a Clan Chief attached to a specific Location.
 * <p>
 * According to the specifications, a Clan Chief can manage their location,
 * heal/feed troops, and order Druids to brew potions.
 * </p>
 * * @author Project Team
 * @version 2.0
 */
public class Chief {

    private String name;
    private String gender;
    private int age;
    /**
     * The location this chief is responsible for.
     * [cite: 636] "Les chefs de clan sont rattachés à un lieu"
     */
    private Place place;
    /**
     * Creates a new Clan Chief.
     * * @param name Name of the chief.
     * @param gender Sex of the chief.
     * @param age Age of the chief.
     * @param place The initial location they manage.
     */
    public Chief(String name, String gender, int age, Place place) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.place = place;
    }
    /**
     * Examines the location by displaying its characteristics, characters, and food.
     * [cite: 641] "examiner son lieu"
     * * @return A formatted string describing the location status.
     */
    public String examineLocation() {
        if (place == null) return "This chief has no assigned location.";
        return place.toString();
    }
    /**
     * Heals all characters present in the chief's location.
     * [cite: 643] "soigner les personnages de son lieu"
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
     * [cite: 644] "nourrir les personnages de son lieu"
     *  "en consommant des aliments qu'il contient"
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
     * [cite: 645] "demander à un druide de faire de la potion magique"
     * * @param druid The target Druid (must be in the same location ideally).
     */
    public void orderPotion(Druid druid) {
        if (druid != null) {
            druid.concoctPotion();
            System.out.println(this.name + " ordered " + druid.getName() + " to brew potion.");
        }
    }

    /**
     * Forces a specific character to drink a dose of potion.
     * [cite: 650] "faire boire de la potion magique à des personnages"
     * * @param target The character who must drink.
     */
    public void makeCharacterDrinkPotion(Character target) {
        if (target != null) {
            target.drinkPotion(1);
            System.out.println(this.name + " made " + target.getName() + " drink potion.");
        }
    }

    /**
     * Transfers a character from the current location to a Battlefield or Enclosure.
     *  "transférer un personnage... à un champ de bataille ou un enclos"
     * * @param target The character to move.
     * @param destination The destination (must be Battlefield or Enclosure).
     * @throws IllegalArgumentException if destination is invalid type.
     */
    public void transferCharacter(Character target, Place destination) {
        if (target != null && destination.canEnter(target)) {
            place.removeCharacter(target);
            destination.addCharacter(target);
        }
        if (!destination.canEnter(target)) {
            System.out.println("You can't enter " + target.getName() + " to " + destination.getName());
            return;
        } else {
            System.out.println("Character not found in current location.");
        }
    }

    /**
     * Creates a new character in the current location.
     * [cite: 642] "créer un nouveau personnage dans son lieu"
     * * @param newCharacter The character instance to add.
     */
    public void recruitCharacter(Character newCharacter) {
        if (place != null) {
            place.addCharacter(newCharacter);
        }
    }

    // --- Getters & Setters ---
    public String getName() { return name; }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public Place getLocation() { return place; }
    public void setLocation(Place location) { this.place = location; }
}