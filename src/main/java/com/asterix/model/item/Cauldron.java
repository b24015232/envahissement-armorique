package com.asterix.model.item;

import java.util.ArrayList;
import java.util.List;

public class Cauldron {

    private final List<Food> ingredients;
    private double doses;
    private boolean isReady;

    // base before effects
    private boolean isNourishing = false;
    private boolean causesDuplication = false;
    private boolean causesLycanthropy = false;

    public Cauldron() {
        this.ingredients = new ArrayList<>();
        this.doses = 0;
        this.isReady = false;
    }

    public void addIngredient(Food food) {
        if (isReady) {
            System.out.println("The potion is ready! No more ingredients.");
            return;
        }
        if (food != null) {
            this.ingredients.add(food);
            System.out.println(food.getName() + " falls into the cauldron.");
        }
    }

    public boolean brew() {
        if (checkRecipe()) {
            this.isReady = true;
            this.doses = 10.0;
            checkSpecialEffects();
            System.out.println("The potion is ready !");
            return true;
        } else {
            System.out.println("It's a nasty soup...");
            this.isReady = false;
            this.doses = 0;
            return false;
        }
    }

    private void checkSpecialEffects() {
        for (Food f : ingredients) {
            String name = f.getName();
            if (name.equals("Lobster") || name.equals("Strawberries") || name.equals("Beet juice")) {
                this.isNourishing = true;
            }
            if (name.equals("Two headed unicorn milk")) {
                this.causesDuplication = true;
            }
            if (name.equals("Idefix's Hair")) {
                this.causesLycanthropy = true;
            }
        }
    }

    private boolean checkRecipe() {
        for (Food f : ingredients) {
            if (f.getName().equals("Mistletoe")) return true;
        }
        return false;
    }

    public double takeLadle() {
        if (isReady && doses > 0) {
            doses -= 1.0;
            return 1.0;
        }
        return 0.0;
    }

    public List<Food> getIngredients() { return new ArrayList<>(ingredients); }

    public boolean isNourishing() {
        return isNourishing;
    }

    public boolean causesDuplication() {
        return causesDuplication;
    }

    public boolean causesLycanthropy() {
        return causesLycanthropy;
    }
}