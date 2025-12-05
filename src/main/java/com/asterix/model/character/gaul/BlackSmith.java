package com.asterix.model.character.gaul;

import com.asterix.model.ability.Worker;
import com.asterix.model.character.Gender;

/**
 * Blacksmith is a gaul who crafts weapons and tools
 * Specifications : Must be able to work
 */
public class BlackSmith extends Gaul implements Worker {

    /**
     * Constructor for blackSmith using the parent Gaul constructor
     */
    public BlackSmith(String name, int age, double height, double strength, double stamina, Gender gender) {
        super(name, age, height, strength, stamina, gender);
    }

    /**
     * Implementation of the Worker interface
     * The Blacksmith uses his strength to hammer metal
     */
    @Override
    public void work() {
        // Using internal attributes to make the text dynamic
        System.out.println(this.getName() + " raises his hammer with his strength of " + this.strength + "...");
        System.out.println("A new sword is being forged for the glory of the village");
    }

    @Override
    public String toString() {
        return "BlackSmith " + super.toString();
    }
}