package com.asterix.model.character.gaul;

import com.asterix.model.character.Character;

public class Gaul extends Character {

    public Gaul(String name, int health) {
        super(name, health); // appelle le constructeur mère
    }

    // juste pour tester le workflow
    public String parler() {
        return "bonjour";
    }
}