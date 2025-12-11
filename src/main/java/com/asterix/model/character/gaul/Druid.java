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

/**
 * Represents a Druid in the Gaulish village (e.g., Panoramix).
 * <p>
 * The Druid is a central character with multiple capabilities:
 * he works (gathers mistletoe), fights when necessary, and provides leadership.
 * Most importantly, he is the only one capable of brewing the magic potion.
 * </p>
 * Implements {@link Worker}, {@link Fighter}, and {@link Leader}.
 */
public class Druid extends Gaul implements Worker, Fighter, Leader {

    /**
     * The cauldron currently being used by the Druid.
     */
    private Cauldron currentCauldron;

    private final Random random;

    /**
     * Constructs a new Druid character.
     *
     * @param name     The name of the druid.
     * @param age      The age of the druid.
     * @param height   The height of the druid in meters.
     * @param strength The physical strength of the druid.
     * @param stamina  The stamina/endurance of the druid.
     * @param gender   The gender of the druid.
     */
    public Druid(String name, int age, double height, double strength, double stamina, Gender gender) {
        super(name, age, height, strength, stamina, gender);
        this.random = new Random();
    }

    /**
     * Prepares a fresh cauldron of magic potion.
     * <p>
     * This unique ability involves mixing specific ingredients.
     * To satisfy the TD requirements, this method randomly selects a recipe variation
     * (Standard, Nourishing, Duplication, or Lycanthropy) to simulate different outcomes.
     * </p>
     */
    public void concoctPotion() {
        System.out.println(this.getName() + " lights a fire under the cauldron...");
        this.currentCauldron = new Cauldron();

        // 1. Base Ingredients (Mandatory)
        currentCauldron.addIngredient(FoodType.MISTLETOE.create());
        currentCauldron.addIngredient(FoodType.CARROT.create());

        // 2. Fish handling (Must be partially fresh)
        Food fish = FoodType.FISH.create();
        if(fish instanceof PerishableFood) ((PerishableFood) fish).passTime();
        currentCauldron.addIngredient(fish);

        // 3. Random Recipe Variation
        double roll = random.nextDouble();

        if (roll < 0.6) {
            // Standard Potion
            currentCauldron.addIngredient(FoodType.ROCK_OIL.create());
            System.out.println("Standard potion");
        }
        else if (roll < 0.75) {
            // Nourishing Potion (Lobster)
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
            // Duplication Potion (Unicorn Milk)
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
            // Metamorphosis Potion (Lycanthropy)
            currentCauldron.addIngredient(FoodType.ROCK_OIL.create());
            currentCauldron.addIngredient(new Food(FoodType.MISTLETOE) {
                @Override public String getName() {
                    return "Idefix's Hair"; // Fixed capitalization to match Cauldron check
                }

                @Override public int getScore() {
                    return 0;
                }
            });
            System.out.println("Metamorphic potion");
        }
        this.currentCauldron.brew();
    }

    /**
     * Serves a dose of magic potion to a fellow Gaul.
     *
     * @param gaul The Gaul to receive the potion.
     */
    public void servePotion(Gaul gaul) {
        if (currentCauldron != null) {
            gaul.drinkPotionFromCauldron(currentCauldron);
        }
    }

    /**
     * Getter for the current cauldron.
     *
     * @return The active cauldron instance.
     */
    public Cauldron getCauldron() {
        return this.currentCauldron;
    }

    /**
     * Implementation of the {@link Worker} interface.
     */
    @Override public void work() {
        System.out.println(this.getName() + " gathers mistletoe.");
    }

    /**
     * Implementation of the {@link Leader} interface.
     */
    @Override public void command() {
        System.out.println(this.getName() + " raises a hand.");
    }

    /**
     * Implementation of the {@link Fighter} interface.
     *
     * @param opponent The character to fight against.
     */
    @Override public void fight(Character opponent) {
        resolveFight(opponent);
    }

    /**
     * Returns the current health points of the druid.
     *
     * @return The current health value.
     */
    @Override
    public double getHealth() {
        return this.health;
    }
}