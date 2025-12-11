package com.asterix.model.simulation;

import com.asterix.model.character.Character;
import com.asterix.model.character.Chief;
import com.asterix.model.character.gaul.Druid;
import com.asterix.model.character.gaul.Gaul;
import com.asterix.model.character.roman.Roman;
import com.asterix.model.item.Cauldron;
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
            System.err.println("Error : Attempted to add a null place to the Theater.");
        }
    }

    public List<Place> getPlaces() {
        return new ArrayList<>(places);
    }

    public void applyDailyHunger() {
    }

    public void applyRandomEvents() {
    }

    /**
     * Makes time pass for all entities in the theater.
     */
    public void applyTimePassage() {
        if (this.places == null) return;
        for (Place place : this.places) {
            for (Character character : place.getCharacters()) {
                character.passTime();
            }
        }

        this.ageFood();
    }

    /**
     * Passes control to clan chiefs to manage their settlements.
     */
    public void triggerChiefsLogic() {
        if (this.places == null) return;

        for (Place place : this.places) {
            // Only Settlements have chiefs logic
            if (place instanceof Settlement) {
                Settlement settlement = (Settlement) place;

                // get the clan chief
                Chief chief = settlement.getChief();

                // if no chief, create one
                if (chief == null) {
                    chief = new Chief("Automated Chief", "MALE", 50, settlement);
                }

                // generic chief methods
                chief.healCharactersInLocation();
                chief.feedCharactersInLocation();

                // potions logic
                if (place instanceof GaulVillage) {
                    handleVillagePotions((GaulVillage) place, chief);
                }
            }
        }
    }

    private void handleVillagePotions(GaulVillage village, Chief chief) {
        Druid druid = null;
        for (Character c : village.getCharacters()) {
            if (c instanceof Druid) {
                druid = (Druid) c;
                break;
            }
        }

        if (druid != null) {
            boolean needPotion = false;
            for (Character c : village.getCharacters()) {
                if (c instanceof Gaul && c.isAlive() && c.getPotionLevel() == 0 && !(c instanceof Druid)) {
                    needPotion = true;
                    break;
                }
            }

            if (needPotion) {
                System.out.println("Alert : Low potion ! Druid starts brewing...");
                druid.concoctPotion();
                Cauldron cauldron = druid.getCauldron();

                if (cauldron != null) {
                    List<Character> clonesToAdd = new ArrayList<>();
                    List<Character> villagers = new ArrayList<>(village.getCharacters());

                    for (Character c : villagers) {
                        if (c instanceof Gaul && c.isAlive() && !(c instanceof Druid)) {
                            String result = c.drinkPotionFromCauldron(cauldron);

                            if ("DUPLICATE".equals(result)) {
                                Character clone = createClone(c);
                                if (clone != null) {
                                    clonesToAdd.add(clone);
                                    System.out.println("Clone well created : " + clone.getName());
                                }
                            }
                        }
                    }
                    // Ajout des clones
                    for (Character clone : clonesToAdd) {
                        village.addCharacter(clone);
                    }
                }
            }
        }
    }

    /**
     * Creates a clone
     */
    private Character createClone(Character original) {
        if (original instanceof Gaul) {
            return new Gaul(original.getName() + " (Clone)", original.getAge(), original.getHeight(),
                    original.getStrength(), original.getStamina(), original.getGender()) {
            };
        } else if (original instanceof Roman) {
            return new Roman(original.getName() + " (Clone)", original.getAge(), original.getHeight(),
                    original.getStrength(), original.getStamina(), original.getGender()) {
            };
        }
        return null;
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
                        Character c = occupants.get(i);
                        sb.append(c.getName());
                        if (c.getPotionLevel() > 0) {
                            sb.append(" [⚡]");
                        }

                        if (c.isStatue()) sb.append("Statue transformation");
                        if (c.isLycanthrope()) sb.append("Lycanthrope transformation");

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