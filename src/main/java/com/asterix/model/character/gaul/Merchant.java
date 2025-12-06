package com.asterix.model.character.gaul;

import com.asterix.model.ability.Worker;
import com.asterix.model.character.Gender;

public class Merchant extends Gaul implements Worker {

    public Merchant(String name, int age, double height, double strength, double stamina, Gender gender) {
        super(name, age, height, strength, stamina, gender);
    }

    @Override
    public void work() {
        System.out.println(this.getName() + " sets up his stall at the village market");
        System.out.println("\"Fresh fish ! My fish is fucking fresh ! Fresh from the Vieux-Port !\"");
    }

    @Override
    public String toString() {
        return "Merchant " + super.toString();
    }
}