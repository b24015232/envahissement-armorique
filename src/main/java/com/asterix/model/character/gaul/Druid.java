package com.asterix.model.character.gaul;

import com.asterix.model.ability.Fighter;
import com.asterix.model.ability.Leader;
import com.asterix.model.ability.Worker;
import com.asterix.model.character.Character;
import com.asterix.model.character.Gender;

public class Druid extends Gaul implements Worker, Fighter, Leader {
    // TODO : put the recipe probabily here later

    public Druid(String name, int age, double height, double strength, double stamina, Gender gender) {
        super(name, age, height, strength, stamina, gender);
    }

    public void concoctPotion() {
        System.out.println(this.getName() + " lights a fire under the cauldron");
        System.out.println("Adding mistletoe, lobster (for the taste), and rock oil...");
        System.out.println("The magic potion is ready ! (health and strength boost available)");
    }

    //implementing interfaces

    @Override
    public void work() {
        //for a druid, work is mostly to prepare potion or pick mistletoe
        System.out.println(this.getName() + " goes into the forest to gather fresh mistletoe with a golden sickle");
    }

    @Override
    public void command() {
        System.out.println(this.getName() + " raises a hand for silence");
    }

    @Override
    public void fight(Character opponent) {
        // Simple implementation to upgrade later when the game will be more complete
        System.out.println(this.getName() + " hits " + opponent.getName() + " with his staff !");
        // TODO: Implement damage logic : damage = (this.strength / opponent.stamina)
    }

    @Override
    public String toString() {
        return "Druid " + super.toString();
    }
}