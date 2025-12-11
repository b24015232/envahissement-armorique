package com.asterix.model.simulation;

import com.asterix.model.character.Character;
import com.asterix.model.character.Chief;
import com.asterix.model.character.gaul.Druid;
import com.asterix.model.character.gaul.Gaul;
import com.asterix.model.character.roman.Roman;
import com.asterix.model.item.Food;
import com.asterix.model.item.FoodFactory;
import com.asterix.model.item.PerishableFood;
import com.asterix.model.place.Battlefield;
import com.asterix.model.place.Place;
import com.asterix.model.place.Settlement;
import com.asterix.model.place.GaulVillage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Represents the invasion theater where the simulation takes place.
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

    public String getName() { return name; }

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
     * Makes time pass for all entities in the theater.
     */
    public void applyTimePassage() {
        if (this.places == null) return;

        // 1. Characters age/hunger/potion decay
        for (Place place : this.places) {
            for (Character character : place.getCharacters()) {
                character.passTime();
            }
        }

        // 2. Food ages (Fresh -> Stale -> Rotten)
        this.ageFood();
    }

    /**
     * Passes control to Clan Chiefs to manage their settlements.
     * Adapted to work with your specific Chief class methods.
     */
    public void triggerChiefsLogic() {
        if (this.places == null) return;

        for (Place place : this.places) {
            // Only Settlements have chiefs logic
            if (place instanceof Settlement) {
                Settlement settlement = (Settlement) place;

                // get the cllan chief
                Chief chief = settlement.getChief();

                // if no chief, create one
                if (chief == null) {
                    chief = new Chief("Automated Chief", "MALE", 50, settlement);
                }

                // generic chief methods
                chief.healCharactersInLocation();
                chief.feedCharactersInLocation();

                // potions
                if (place instanceof GaulVillage) {
                    // check potion
                    boolean lowPotion = false;
                    for (Character c : settlement.getCharacters()) {
                        if (c instanceof Gaul && c.isAlive() && c.getPotionLevel() == 0) {
                            lowPotion = true;
                            break;
                        }
                    }

                    if (lowPotion) {
                        // find a druid to have potion
                        Druid foundDruid = null;
                        for (Character c : settlement.getCharacters()) {
                            if (c instanceof Druid) {
                                foundDruid = (Druid) c;
                                break;
                            }
                        }

                        if (foundDruid != null) {
                            System.out.println(">>> 🚨 Village Alert: Low potion! Chief orders brewing.");

                            // chiefs orders potion
                            chief.orderPotion(foundDruid);

                            // chiefs gives potions to his troups
                            for (Character c : settlement.getCharacters()) {
                                if (c instanceof Gaul && c.isAlive() && !(c instanceof Druid)) {
                                    chief.makeCharacterDrinkPotion(c);
                                }
                            }
                        }
                    }
                }
            }
        }
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

                List<Character> gaulCamp = new ArrayList<>();
                List<Character> romanCamp = new ArrayList<>();

                for (Character c : combatants) {
                    if (c.isAlive()) {
                        if (c instanceof Gaul) {
                            gaulCamp.add(c);
                        } else if (c instanceof Roman) {
                            romanCamp.add(c);
                        }
                    }
                }

                Collections.shuffle(gaulCamp);
                Collections.shuffle(romanCamp);

                int minSize = Math.min(gaulCamp.size(), romanCamp.size());

                for (int i = 0; i < minSize; i++) {
                    Character gaul = gaulCamp.get(i);
                    Character roman = romanCamp.get(i);
                    gaul.resolveFight(roman);
                }

                List<Character> casualties = new ArrayList<>();
                for (Character c : combatants) {
                    if (!c.isAlive()) {
                        casualties.add(c);
                    }
                }

                for (Character dead : casualties) {
                    System.out.println("✝️ " + dead.getName() + " has fallen at " + battlefield.getName());
                    battlefield.removeCharacter(dead);
                }
            }
        }
    }

    /**
     * Generates food in every eligible location at every turn.
     */
    public void generateFood() {
        if (this.places == null) return;

        for (Place place : this.places) {
            if (place instanceof Battlefield) {
                continue;
            }

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

    public void applyDailyHunger() {

    }
    public void applyRandomEvents() {

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Invasion Theater: ").append(name).append(" ===\n");

        if (places == null || places.isEmpty()) {
            sb.append("No location configured.\n");
        } else {
            for (Place place : places) {
                sb.append("📍 Location: ").append(place.getName());
                if (place instanceof Battlefield) {
                    sb.append(" [⚔️ BATTLEFIELD]");
                }
                sb.append("\n");

                List<Character> occupants = place.getCharacters();
                if (occupants.isEmpty()) {
                    sb.append("   (No characters present)\n");
                } else {
                    sb.append("   👥 Characters (").append(occupants.size()).append("): ");
                    for (int i = 0; i < occupants.size(); i++) {
                        sb.append(occupants.get(i).getName());
                        if (occupants.get(i).getPotionLevel() > 0) {
                            sb.append(" [⚡]");
                        }
                        if (i < occupants.size() - 1) {
                            sb.append(", ");
                        }
                    }
                    sb.append("\n");
                }

                List<Food> foodItems = place.getFoods();
                if (foodItems.isEmpty()) {
                    sb.append("   (No food items)\n");
                } else {
                    sb.append("   🍎 Food items (").append(foodItems.size()).append("): ");
                    for (int i = 0; i < foodItems.size(); i++) {
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