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

    public List<Place> getPlaces() {
        return new ArrayList<>(places);
    }
    /**
     * Manages combat logic between characters in Battlefields.
     * Rename of 'gererCombats' to comply with English naming convention.
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
     * [cite_start]Randomly modifies the state of characters (Hunger, Potion, Health)[cite: 112].
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
     * Generates food randomly in compatible locations at each turn.
     * * Logic:
     * 1. Iterates through all places.
     * 2. Skips Battlefields (Constraint ).
     * 3. Uses a probability check to decide if food spawns.
     */
    public void generateFood() {
        if (this.places == null) return;

        // Message de debug pour vérifier que la méthode est appelée
        System.out.println("[SIMULATION] Checking food generation...");

        for (Place place : this.places) {

            // CONSTRAINT: Food cannot spawn on Battlefields
            if (place instanceof Battlefield) {
                continue;
            }

            // Probability: 40% chance per location per turn
            if (random.nextDouble() < 0.40) {

                // 1. Create random food using the Factory
                Food newFood = FoodFactory.createRandomFood();

                // 2. Add it to the place using the method we just added
                place.addFood(newFood);

                System.out.println("   -> 🍎 A " + newFood.getName() + " appeared in " + place.getName());
            }
        }
    }

    /**
     * [cite_start]Ages food items, turning fresh food into stale food[cite: 114].
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