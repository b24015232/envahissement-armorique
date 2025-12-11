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
import com.asterix.model.place.GaulVillage;

import java.util.Iterator;
import java.util.List;
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
            // Metamorphosis Potion (Lycanthropy)
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

    /**
     * Serves a dose of magic potion to a fellow Gaul.
     *
     */
    public int gatherIngredients(GaulVillage village) {
        if (village == null) return 0;

        Cauldron villageCauldron = village.getCauldron();
        List<Food> groundItems = village.getFoods();

        int gatheredCount = 0;

        // Liste des noms d'ingrédients que le chaudron accepte (Basé sur votre classe Cauldron)
        // Vous pouvez aussi utiliser check sur FoodType si vous y avez accès
        List<String> validIngredients = List.of(
                "Mistletoe", "Carrot", "Salt", "Honey", "Mead",
                "Secret Ingredient", "Clover", "Fish", "Rock oil", "Beet Juice", "Lobster", "Two headed unicorn milk"
        );

        // Utilisation d'un itérateur pour pouvoir supprimer de la liste en la parcourant
        Iterator<Food> it = groundItems.iterator();
        while (it.hasNext()) {
            Food item = it.next();
            if (validIngredients.contains(item.getName())) {
                System.out.println("🌿 " + this.getName() + " ramasse : " + item.getName());
                villageCauldron.addIngredient(item);

                it.remove();

                gatheredCount++;
            }
        }
        return gatheredCount;
    }

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
}