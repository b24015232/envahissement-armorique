package com.asterix.model.character.gaul;

import com.asterix.model.ability.Worker;
import com.asterix.model.character.Gender;

public class Innkeeper extends Gaul implements Worker {

    public Innkeeper(String name, int age, double height, double strength, double stamina, Gender gender) {
        super(name, age, height, strength, stamina, gender);
    }

    @Override
    public void work() {
        System.out.println(this.getName() + " cleans the counter and brings out the mugs");
            System.out.println("\"Who wants beer and wildboar ?\"");
    }

    @Override
    public String toString() {
        return "Innkeeper " + super.toString();
    }
}