package com.asterix.model.character;

import com.asterix.model.item.Cauldron;
import com.asterix.model.item.Food;

public abstract class Character {

    protected String name;
    protected Gender gender;
    protected double height;
    protected int age;
    protected double strength;
    protected double stamina;
    protected double health;
    public static final double MAX_HEALTH = 100.0;
    public double hunger;
    protected double belligerence;
    protected double potionLevel;

    // new attributes adapted to potions
    protected double lifetimePotionDoses = 0;
    protected boolean isStatue = false;
    protected boolean isLycanthrope = false;

    public Character(String name, int age, double height, double strength, double stamina, Gender gender) {
        this.name = name;
        this.age = age;
        this.height = height;
        this.strength = strength;
        this.stamina = stamina;
        this.gender = gender;
        this.health = MAX_HEALTH;
        this.hunger = 0.0;
        this.belligerence = 0.0;
        this.potionLevel = 0.0;
    }

    //fix : adding gender getter
    public Gender getGender() { return gender; }

    // add drinkPotion
    public void drinkPotion(double dose) {
        if (!isAlive()) return;
        this.potionLevel += dose;
    }

    public String drinkPotionFromCauldron(Cauldron cauldron) {
        if (!isAlive() || cauldron == null) return "NONE";

        double dose = cauldron.takeLadle();
        if (dose <= 0) return "NONE";

        System.out.println(this.name + " drinks magic potion.");
        this.drinkPotion(dose);

        this.lifetimePotionDoses += dose;
        if (this.lifetimePotionDoses >= 20.0) {
            this.isStatue = true;
            this.potionLevel = 0;
            System.out.println(this.name + " turns into granite statue");
            return "NONE";
        }

        //using getters
        if (cauldron.isNourishing()) {
            this.hunger = 0.0;
            System.out.println(this.name + " is fully fed !");
        }
        if (cauldron.causesLycanthropy()) {
            this.isLycanthrope = true;
            System.out.println(this.name + " transforms into a lycanthrope !");
        }
        if (cauldron.causesDuplication()) {
            return "DUPLICATE";
        }
        return "NONE";
    }

    //getters

    public String getName() {
        return name;
    }

    public double getPotionLevel() {
        return potionLevel;
    }

    public boolean isAlive() {
        return health > 0.0 && !isStatue;
    }

    public boolean isStatue() {
        return isStatue;
    }

    public boolean isLycanthrope() {
        return isLycanthrope;
    }

    public int getAge() {
        return age;
    }

    public double getHeight() {
        return height;
    }

    public double getStrength() {
        return strength;
    }

    public double getStamina() {
        return stamina;
    }

    // setters
    public void setHunger(double h) {
        this.hunger = h;
    }

    public double getHunger() {
        return hunger;
    }

    public double getBelligerence() {
        return belligerence;
    }

    public void setBelligerence(double b) {
        this.belligerence = b;
    }

    /**
     * Gets the current health points of the character.
     *
     * @return The health value.
     */
    public double getHealth() {
        return health;
    }

    public void passTime() {
        if (!isAlive()) return;
        this.hunger += 2.0;
        if (potionLevel > 0) potionLevel = Math.max(0, potionLevel - 0.5);
    }

    public void resolveFight(Character opponent) {
        if(isAlive() && opponent.isAlive()) {
            opponent.health -= 5;
        }
    }

    public void heal(double amount) {
        if(isAlive()) this.health = Math.min(MAX_HEALTH, health + amount);
    }

    public void eat(Food food) {
        if(isAlive()) this.hunger = 0;
    }

    public void die() {
        this.health = 0;
    }

    @Override
    public String toString() {
        return String.format(
                "%-15s | %-6s | Age: %-3d | 📏 %.2fm | ❤️ HP: %-5.1f | 🍖 Hunger: %-5.1f | 💪 Str: %-5.1f | 🏃 Sta: %-5.1f | 🧪 Potion: %.1f",
                this.name,
                this.gender,
                this.age,
                this.height,
                this.health,
                this.hunger,
                this.strength,
                this.stamina,
                this.potionLevel
        );
    }
}