package com.asterix.model.place;

import com.asterix.model.character.Character;
import com.asterix.model.item.Food;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract sealed class representing a generic location in the simulation.
 * <p>
 * A place is characterized by a name, a surface area, and the entities it contains
 * (characters and food). It defines the base logic for adding/removing characters
 * based on specific access rules defined by subclasses.
 * </p>
 * * @author Project Team
 * @version 1.0
 * @see com.asterix.model.character.Character
 * Reference:
 */
public abstract sealed class Place permits Battlefield, Settlement {

    protected String name;
    protected double area;
    protected List<Character> characters;
    protected List<Food> foods;

    /**
     * Constructs a new Place.
     *
     * @param name The name of the location
     * @param area The surface area of the location
     */
    public Place(String name, double area) {
        this.name = name;
        this.area = area;
        this.characters = new ArrayList<>();
        this.foods = new ArrayList<>();
    }


    /**
     * Abstract method to validate if a specific character is allowed to enter this place.
     * <p>
     * This template method must be implemented by concrete classes to enforce
     * strict access rules (e.g., only Gauls in a GaulVillage).
     * </p>
     *
     * @param c The character attempting to enter
     * @return {@code true} if the character meets the entry criteria, {@code false} otherwise.
     * Reference:
     */
    protected abstract boolean canEnter(Character c);

    /**
     * Adds a character to this place if the entry rules allow it.
     *
     * @param c The character to add
     * @throws IllegalArgumentException if the character is null or not allowed in this place.
     * Reference:
     */
    public void addCharacter(Character c) {
        if (c == null) {
            throw new IllegalArgumentException("Cannot add a null character to a place.");
        }
        if (!canEnter(c)) {
            throw new IllegalArgumentException("Character " + c.getName() + " is not allowed in " + this.name);
        }
        this.characters.add(c);
    }

    /**
     * Removes a character from this place.
     *
     * @param c The character to remove
     * Reference:
     */
    public void removeCharacter(Character c) {
        this.characters.remove(c);
    }

    /**
     * Returns a defensive copy of the list of characters currently in this place.
     *
     * @return A new List containing the characters.
     * Reference:
     */
    public List<Character> getCharacters() {
        return new ArrayList<>(this.characters);
    }

    /**
     * Returns the list of food available in this place.
     *
     * @return The list of food items.
     * Reference:
     */
    public List<Food> getFoods() {
        return this.foods;
    }

    /**
     * Gets the name of the place.
     *
     * @return The name string.
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Place{" +
                "name='" + name + '\'' +
                ", area=" + area +
                ", population=" + characters.size() +
                '}';
    }
}