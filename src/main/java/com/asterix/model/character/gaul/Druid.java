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

public class Druid extends Gaul implements Worker, Fighter, Leader {

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

    public int gatherIngredients(GaulVillage village) {
        if (village == null) return 0;

        // On récupère le chaudron du village (celui qui est commun)
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

            // Si l'objet est un ingrédient valide pour la potion
            if (validIngredients.contains(item.getName())) {
                System.out.println("🌿 " + this.getName() + " ramasse : " + item.getName());

                // 1. Hop, dans le chaudron !
                villageCauldron.addIngredient(item);

                // 2. On le retire du sol
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