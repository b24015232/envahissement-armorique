package com.asterix.model.character.gaul;

import com.asterix.model.ability.Fighter;
import com.asterix.model.ability.Leader;
import com.asterix.model.ability.Worker;
import com.asterix.model.character.Character;
import com.asterix.model.character.Gender;
import com.asterix.model.item.Cauldron;
import com.asterix.model.item.Food;
import com.asterix.model.item.FoodType;
import com.asterix.model.item.PerishableFood;
import java.util.Random;

public abstract class Druid extends Gaul implements Worker, Fighter, Leader {

    private Cauldron currentCauldron;
    private final Random random;

    public Druid(String name, int age, double height, double strength, double stamina, Gender gender) {
        super(name, age, height, strength, stamina, gender);
        this.random = new Random();
    }

    public void concoctPotion() {
        System.out.println(this.getName() + " lights a fire under the cauldron...");
        this.currentCauldron = new Cauldron();

        // basic ingredients
        currentCauldron.addIngredient(FoodType.MISTLETOE.create());
        currentCauldron.addIngredient(FoodType.CARROT.create());

        // fish
        Food fish = FoodType.FISH.create();
        if(fish instanceof PerishableFood) ((PerishableFood) fish).passTime();
        currentCauldron.addIngredient(fish);

        // random recipe
        double roll = random.nextDouble();

        if (roll < 0.6) {
            // standard potion
            currentCauldron.addIngredient(FoodType.ROCK_OIL.create());
            System.out.println("Standard potion");
        }

        else if (roll < 0.75) {
            // feeding potion
            currentCauldron.addIngredient(FoodType.ROCK_OIL.create());
            currentCauldron.addIngredient(new Food(FoodType.FISH) {
                @Override public String getName() {
                    return "Lobster";
                }

                @Override public int getScore() {
                    return 10;
                }
            });
            System.out.println("Feeding potion");
        }
        else if (roll < 0.90) {
            // duplication potion
            currentCauldron.addIngredient(FoodType.ROCK_OIL.create());
            currentCauldron.addIngredient(new Food(FoodType.MEAD) {
                @Override public String getName() {
                    return "Two headed unicorn milk";
                }
                @Override public int getScore() {
                    return 0;
                }
            });
            System.out.println("Duplication potion");
        }
        else {
            // metamorphosis into lycantrhope potion
            currentCauldron.addIngredient(FoodType.ROCK_OIL.create());
            currentCauldron.addIngredient(new Food(FoodType.MISTLETOE) {
                @Override public String getName() {
                    return "Idefix's hair";
                }

                @Override public int getScore() {
                    return 0;
                }
            });
            System.out.println("Metamorphic potion");
        }
        this.currentCauldron.brew();
    }

    public void servePotion(Gaul gaul) {
        if (currentCauldron != null) {
            gaul.drinkPotionFromCauldron(currentCauldron);
        }
    }

    public Cauldron getCauldron() {
        return this.currentCauldron;
    }

    //some narration
    @Override public void work() {
        System.out.println(this.getName() + " gathers mistletoe.");
    }

    @Override public void command() {
        System.out.println(this.getName() + " raises a hand.");
    }

    @Override public void fight(Character opponent) {
        resolveFight(opponent);
    }
}