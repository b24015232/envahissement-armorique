package com.asterix.model.character.gaul;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GaulTest {

    @Test
    void unGauloisDoitAvoirUnNom() {
        // création d'un gaulois
        Gaul asterix = new Gaul("Astérix", 100);

        // vérification de la compatibilité avec la classe mère
        assertEquals("Astérix", asterix.getName());
    }

    @Test
    void unGauloisDoitSavoirParler() {
        // création d'un gaulois
        Gaul obelix = new Gaul("Obélix", 150);

        // vérification de la méthode
        assertEquals("bonjour", obelix.parler());
    }
}