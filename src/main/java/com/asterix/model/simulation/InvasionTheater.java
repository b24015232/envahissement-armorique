package com.asterix.model.simulation;

import com.asterix.model.character.Character;
import com.asterix.model.character.gaul.Gaul;
import com.asterix.model.character.roman.Roman;
import com.asterix.model.place.Place;
import com.asterix.model.place.Battlefield;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the Invasion Theater.
 * Central class managing the simulation logic.
 */
public class InvasionTheater {

    private String name;
    private List<Place> places;

    public InvasionTheater(String name) {
        this.name = name;
        this.places = new ArrayList<>();
    }

    public String getName() { return name; }

    /**
     * Adds a place to the invasion theater.
     */
    public void addPlace(Place place) {
        if (place != null) {
            this.places.add(place);
        } else {
            System.err.println("Error: Attempted to add a null Place to the Theater.");
        }
    }

    /**
     * Returns a defensive copy of the places list.
     */
    public List<Place> getPlaces() {
        return new ArrayList<>(places);
    }

    public void handleFights() {
        if (this.places == null || this.places.isEmpty()) {
            System.err.println("No places configured in the theater.");
            return;
        }

        for (Place place : this.places) {
            // Fights mainly occur on Battlefields
            if (place instanceof Battlefield) {
                Battlefield battlefield = (Battlefield) place;
                List<Character> combatants = battlefield.getCharacters();

                if (combatants.size() < 2) continue;

                // Split camps
                List<Character> gauls = new ArrayList<>();
                List<Character> romans = new ArrayList<>();

                for (Character c : combatants) {
                    if (c.isAlive()) {
                        if (c instanceof Gaul) gauls.add(c);
                        else if (c instanceof Roman) romans.add(c);
                    }
                }

                Collections.shuffle(gauls);
                Collections.shuffle(romans);

                // Resolve Duels
                int minSize = Math.min(gauls.size(), romans.size());
                for (int i = 0; i < minSize; i++) {
                    gauls.get(i).resolveFight(romans.get(i));
                }

                // Handle Casualties
                List<Character> casualties = new ArrayList<>();
                for (Character c : combatants) {
                    if (!c.isAlive()) {
                        casualties.add(c);
                        System.out.println("✝️ " + c.getName() + " has fallen at " + battlefield.getName() + ".");
                    }
                }

                // Cleanup
                for (Character dead : casualties) {
                    battlefield.removeCharacter(dead);
                }
            }
        }
    }

    public void applyRandomEvents() {
        // TODO: Implement random events
        // System.out.println("[SIMULATION] Random events applied.");
    }

    public void generateFood() {
        // TODO: Spawn food
        // System.out.println("[SIMULATION] Food generated.");
    }

    public void ageFood() {
        // TODO: Age food
        // System.out.println("[SIMULATION] Food aging.");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Invasion Theater: ").append(name).append(" ===\n");

        if (places == null || places.isEmpty()) {
            sb.append("No places configured.\n");
        } else {
            for (Place place : places) {
                sb.append("📍 Place: ").append(place.getName());
                if (place instanceof Battlefield) {
                    sb.append(" [BATTLE ZONE]");
                }
                sb.append("\n");

                List<Character> occupants = place.getCharacters();
                if (occupants.isEmpty()) {
                    sb.append("   (No characters present)\n");
                } else {
                    sb.append("   👥 Characters present (").append(occupants.size()).append("): ");
                    for (int i = 0; i < occupants.size(); i++) {
                        sb.append(occupants.get(i).getName());
                        if (i < occupants.size() - 1) sb.append(", ");
                    }
                    sb.append("\n");
                }
                sb.append("--------------------------------------------------\n");
            }
        }
        return sb.toString();
    }
}