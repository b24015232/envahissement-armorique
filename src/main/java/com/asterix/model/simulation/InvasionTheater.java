package com.asterix.model.simulation;

import com.asterix.model.character.Character;
import com.asterix.model.character.gaul.Gaul;
import com.asterix.model.character.roman.Roman;
import com.asterix.model.item.Food;
import com.asterix.model.item.FoodFactory;
import com.asterix.model.item.PerishableFood;
import com.asterix.model.place.Battlefield;
import com.asterix.model.place.Place;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * [cite_start]Represents the invasion theater where the simulation takes place[cite: 101].
 * Acts as the main facade for the domain model.
 */
public class InvasionTheater {

    private String name;
    private List<Place> places;
    private final Random random;

    private static final double RANDOM_EVENT_PROBABILITY = 0.30;

    public InvasionTheater(String name) {
        this.name = name;
        this.places = new ArrayList<>();
        this.random = new Random();
    }

    public String getName() { return name; } // previously getNom

    public void addPlace(Place place) {
        if (place != null) {
            this.places.add(place);
        } else {
            System.err.println("Error: Attempted to add a null Place to the Theater.");
        }
    }

    /**
     * Retrieves the list of places.
     * <p>
     * This method returns a defensive copy of the list. Modifying the returned
     * list will not affect the internal state of this object.
     *
     * @return a new List containing the Place objects.
     */
    public List<Place> getPlaces() {
        return new ArrayList<>(places);
    }

    /**
     * Systematically increases the hunger of ALL characters in ALL places.
     * <p>
     * This replaces the random event logic for hunger.
     * Every turn, every character gets hungrier.
     * </p>
     */
    public void applyDailyHunger() {
        if (this.places == null) return;

        // Stream through all places and all characters
        this.places.stream()
                .flatMap(place -> place.getCharacters().stream())
                .forEach(character -> {
                    // Increase hunger by 10 points every turn
                    // Ensure Character class has increaseHunger(int amount)
                    character.increaseHunger(10);
                });
    }
    /**
     * Manages combat logic between characters in Battlefields.
     */
    public void handleFights() {
        if (this.places == null || this.places.isEmpty()) return;

        for (Place place : this.places) {
            if (place instanceof Battlefield) {
                Battlefield battlefield = (Battlefield) place;
                List<Character> combatants = battlefield.getCharacters();

                if (combatants.size() < 2) continue;

                List<Character> gaulCamp = combatants.stream().filter(c -> c instanceof Gaul && c.isAlive()).collect(Collectors.toList());
                List<Character> romanCamp = combatants.stream().filter(c -> c instanceof Roman && c.isAlive()).collect(Collectors.toList());

                Collections.shuffle(gaulCamp);
                Collections.shuffle(romanCamp);

                int minSize = Math.min(gaulCamp.size(), romanCamp.size());

                for (int i = 0; i < minSize; i++) {
                    Character gaul = gaulCamp.get(i);
                    Character roman = romanCamp.get(i);
                    gaul.resolveFight(roman);
                }

                List<Character> casualties = combatants.stream()
                        .filter(c -> !c.isAlive())
                        .collect(Collectors.toList());

                casualties.forEach(dead -> {
                    System.out.println("✝️ " + dead.getName() + " has fallen at " + battlefield.getName());
                    battlefield.removeCharacter(dead);
                });
            }
        }
    }

    /**
     * Randomly modifies the state of characters (Hunger, Potion, Health).
     */
    public void applyRandomEvents() {
        if (places == null) return;

        places.stream()
                .flatMap(place -> place.getCharacters().stream())
                .forEach(character -> {
                    if (random.nextDouble() < RANDOM_EVENT_PROBABILITY) {
                        int eventType = random.nextInt(3);
                        switch (eventType) {
                            case 0:
                                break;
                            case 1:
                                break;
                            case 2:
                                break;
                        }
                    }
                });
    }

    /**
     * Generates food in every eligible location at every turn.
     * <p>
     * Removed the random probability check. Food now spawns guaranteed.
     * Constraint: Still excludes Battlefields.
     * </p>
     */
    public void generateFood() {
        if (this.places == null) return;

        for (Place place : this.places) {
            if (place instanceof Battlefield) {
                continue;
            }

            // SYSTEMATIC SPAWN (No random check)
            Food newFood = FoodFactory.createRandomFood();
            place.addFood(newFood);

            System.out.println("   -> 🍎 (Guaranteed) A " + newFood.getName() + " appeared in " + place.getName());
        }
    }

    /**
     * Ages food items, turning fresh food into stale food.
     */
    public void ageFood() {
        if (places == null) return;

        for (Place place : places) {
             for (Food food : place.getFoods()) {
                 if (food instanceof PerishableFood) {
                     ((PerishableFood) food).passTime();
                 }
             }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Invasion Theater: ").append(name).append(" ===\n"); // [cite: 662]

        if (places == null || places.isEmpty()) {
            sb.append("No location configured.\n");
        } else {
            for (Place place : places) {
                // 1. Display Place Name and Type
                sb.append("📍 Location: ").append(place.getName());
                if (place instanceof Battlefield) {
                    sb.append(" [⚔️ BATTLEFIELD]");
                }
                sb.append("\n");

                // 2. Display Characters [cite: 663]
                List<Character> occupants = place.getCharacters();
                if (occupants.isEmpty()) {
                    sb.append("   (No characters present)\n");
                } else {
                    sb.append("   👥 Characters (").append(occupants.size()).append("): ");
                    for (int i = 0; i < occupants.size(); i++) {
                        sb.append(occupants.get(i).getName());
                        if (i < occupants.size() - 1) {
                            sb.append(", ");
                        }
                    }
                    sb.append("\n");
                }

                // 3. Display Foods (Added Feature)
                // Assumes Place has a getter: public List<Food> getFoods();
                List<Food> foodItems = place.getFoods();
                if (foodItems.isEmpty()) {
                    sb.append("   (No food items)\n");
                } else {
                    sb.append("   🍎 Food items (").append(foodItems.size()).append("): ");
                    for (int i = 0; i < foodItems.size(); i++) {
                        // getName() will include state description (e.g., "Fish (Stale)") thanks to State Pattern
                        sb.append(foodItems.get(i).getName());
                        if (i < foodItems.size() - 1) {
                            sb.append(", ");
                        }
                    }
                    sb.append("\n");
                }

                sb.append("--------------------------------------------------\n");
            }
        }
        return sb.toString();
    }
}