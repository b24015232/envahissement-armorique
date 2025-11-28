package com.asterix.model.character;

/**
 * Classe abstraite de base pour tous les personnages
 */
public abstract sealed class Character permits Roman, Gaul, Creature {
    private String name;
    private int health;

    // constructeur pour les classes filles
    protected Character(String name, int health) {
        this.name = name;
        this.health = health;
    }
/**
 * Classe abstraite de base pour tous les personnages
 */
public abstract sealed class Character permits Roman, Gaul, Creature {
 {
    private String name;
    private int health;

    // constructeur pour les classes filles
    protected Character(String name, int health) {
        this.name = name;
        this.health = health;
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }
}