package com.asterix.model.character.gaul;

import com.asterix.model.ability.Fighter;
import com.asterix.model.ability.Leader;
import com.asterix.model.ability.Worker;
import com.asterix.model.character.Character;
import com.asterix.model.character.Gender;
import com.asterix.model.item.Cauldron;
import com.asterix.model.item.FoodType;
import com.asterix.model.item.PerishableFood;

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
    }

    /**
     * Prepares a fresh cauldron of magic potion.
     * <p>
     * This unique ability involves mixing specific ingredients like mistletoe,
     * fresh clover, and rock oil (or beet juice).
     * The Druid must ensure the fish is "average" (partially fresh) for the recipe to succeed.
     * </p>
     */
    public void concoctPotion() {
        System.out.println(this.getName() + " lights a fire under the cauldron and starts brewing...");

        this.currentCauldron = new Cauldron();

        // 1. Add standard ingredients
        currentCauldron.addIngredient(FoodType.MISTLETOE.create());
        currentCauldron.addIngredient(FoodType.CARROT.create());
        currentCauldron.addIngredient(FoodType.SALT.create());
        currentCauldron.addIngredient(FoodType.HONEY.create());
        currentCauldron.addIngredient(FoodType.MEAD.create());
        currentCauldron.addIngredient(FoodType.SECRET_INGREDIENT.create());

        // Substitution allowed: Rock Oil or Beet Juice
        currentCauldron.addIngredient(FoodType.ROCK_OIL.create());

        // 2. Add freshness-sensitive ingredients
        // Clover must be FRESH (default state)
        currentCauldron.addIngredient(FoodType.CLOVER.create());

        // 3. Special Handling: Fish must be "AVERAGE" (Partially Fresh)
        // We create a fresh fish, then let time pass once to degrade it.
        PerishableFood fish = (PerishableFood) FoodType.FISH.create();
        fish.passTime(); // Transitions from Fresh -> PartiallyFresh (Average)
        System.out.println("The druid waits for the fish to smell... just enough.");

        currentCauldron.addIngredient(fish);

        // Attempt to brew
        boolean success = currentCauldron.brew();

        if (success) {
            System.out.println("By Toutatis! The magic potion is ready!");
        } else {
            System.out.println("By Belenos... I messed up the recipe.");
        }
    }

    /**
     * Serves a dose of magic potion to a fellow Gaul.
     *
     * @param gaul The Gaul to receive the potion.
     */
    public void servePotion(Gaul gaul) {
        if (currentCauldron != null) {
            double dose = currentCauldron.takeLadle();
            if (dose > 0) {
                gaul.drinkPotion(dose);
            } else {
                System.out.println("The cauldron is empty!");
            }
        } else {
            System.out.println("There is no cauldron ready yet.");
        }
    }

    // --- Interface Implementations ---

    /**
     * Implementation of the {@link Worker} interface.
     */
    @Override
    public void work() {
        System.out.println(this.getName() + " goes into the forest to gather fresh mistletoe with a golden sickle");
    }

    /**
     * Implementation of the {@link Leader} interface.
     */
    @Override
    public void command() {
        System.out.println(this.getName() + " raises a hand for silence");
    }

    /**
     * Implementation of the {@link Fighter} interface.
     */
    @Override
    public void fight(Character opponent) {
        System.out.println(this.getName() + " hits " + opponent.getName() + " with his staff!");
        resolveFight(opponent);
    }

    @Override
    public String toString() {
        return "Druid " + super.toString();
    }
}