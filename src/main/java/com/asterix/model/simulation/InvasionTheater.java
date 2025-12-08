package com.asterix.model.simulation;

import com.asterix.model.character.Character;
import com.asterix.model.character.gaul.Gaul;
import com.asterix.model.character.roman.Roman;

import com.asterix.model.place.Place;
import com.asterix.model.place.Battlefield;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Représente le théâtre d'envahissement[cite: 101].
 */
public class InvasionTheater {

    private String nom; // [cite: 102]
    private List<Place> places; // Utilisation de Collections [cite: 745, 104]

    public InvasionTheater(String nom) {
        this.nom = nom;
        this.places = new ArrayList<>();
    }

    public String getNom() { return nom; }

    /**
     * Ajoute un lieu au théâtre d'envahissement.
     * Cette méthode est utilisée lors du chargement du scénario XML.
     *
     * @param place Le lieu à ajouter (ne doit pas être null).
     */
    public void addPlace(Place place) {
        if (place != null) {
            this.places.add(place);
        } else {
            System.err.println("Erreur : Tentative d'ajout d'un lieu null dans le théâtre.");
        }
    }

    /**
     * Retourne la liste des lieux (utile pour le contrôleur et l'affichage).
     * @return Une copie de la liste des lieux.
     */
    public List<Place> getPlaces() {
        return new ArrayList<>(places);
    }

    // --- Méthodes Métier [cite: 111-114] ---

    public void gererCombats() {
        // Validation pré-conditionnelle pour éviter les erreurs d'exécution sur des collections nulles
        if (this.places == null || this.places.isEmpty()) {
            System.err.println("Aucun lieu configuré dans le théâtre d'opérations.");
            return;
        }

        // Parcours de tous les lieux du théâtre d'envahissement [cite: 532]
        for (Place place : this.places) {

            // Les combats ont lieu principalement sur les champs de bataille
            if (place instanceof Battlefield) {
                Battlefield battlefield = (Battlefield) place;

                // Récupération des personnages présents (Ceci est une copie de la liste) [cite: 491]
                List<Character> combatants = battlefield.getCharacters();

                if (combatants.size() < 2) {
                    continue; // Pas assez de combattants pour un duel
                }

                // --- Séparation des camps ---
                List<Character> gaulCamp = new ArrayList<>();
                List<Character> romanCamp = new ArrayList<>();

                for (Character c : combatants) {
                    if (c.isAlive()) {
                        // Polymorphisme pour trier les camps [cite: 465]
                        if (c instanceof Gaul) {
                            gaulCamp.add(c);
                        } else if (c instanceof Roman) {
                            romanCamp.add(c);
                        }
                    }
                }

                // Mélange aléatoire pour varier les affrontements (Simulation ludique)
                Collections.shuffle(gaulCamp);
                Collections.shuffle(romanCamp);

                // --- Résolution des duels ---
                int minSize = Math.min(gaulCamp.size(), romanCamp.size());

                for (int i = 0; i < minSize; i++) {
                    Character gaul = gaulCamp.get(i);
                    Character roman = romanCamp.get(i);

                    // Le combat impacte la santé (force vs endurance) [cite: 680, 681]
                    gaul.resolveFight(roman);
                }

                // --- Gestion des morts (Nettoyage post-combat) ---
                // On utilise une liste temporaire pour stocker les morts afin d'éviter
                // les problèmes de modification concurrente lors de la suppression.
                List<Character> casualties = new ArrayList<>();

                for (Character c : combatants) {
                    if (!c.isAlive()) { // Si indicateur de santé critique
                        casualties.add(c);
                        // Affichage du message demandé
                        System.out.println("✝️ " + c.getName() + " a rendu l'âme sur le champ de bataille " + battlefield.getName() + ".");
                    } else {
                        // Logique optionnelle : Renvoyer le survivant chez lui
                        // returnCharacterToOrigin(c);
                    }
                }

                // Suppression effective des morts du Lieu réel
                for (Character dead : casualties) {
                    battlefield.removeCharacter(dead);
                }
            }
        }
    }

    public void appliquerAleas() {
        // TODO: Modifier aléatoirement l'état des personnages [cite: 112]
    }

    public void genererAliments() {
        // TODO: Faire apparaître des aliments [cite: 113]
    }

    public void vieillirAliments() {
        // TODO: Changer les aliments frais en pas frais [cite: 114]
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Théâtre d'envahissement : ").append(nom).append(" ===\n");

        if (places == null || places.isEmpty()) {
            sb.append("Aucun lieu configuré.\n");
        } else {
            for (Place place : places) {
                // Titre du lieu avec distinction si c'est un champ de bataille (lieu de combat)
                sb.append("📍 Lieu : ").append(place.getName());
                if (place instanceof Battlefield) {
                    sb.append(" [⚔️ ZONE DE COMBAT]");
                }
                sb.append("\n");

                // Récupération des personnages
                List<Character> occupants = place.getCharacters();

                if (occupants.isEmpty()) {
                    sb.append("   (Aucun personnage présent)\n");
                } else {
                    sb.append("   👥 Personnages présents (").append(occupants.size()).append(") : ");

                    // Liste des noms des personnages
                    for (int i = 0; i < occupants.size(); i++) {
                        sb.append(occupants.get(i).getName());
                        if (i < occupants.size() - 1) {
                            sb.append(", ");
                        }
                    }
                    sb.append("\n");
                }
                sb.append("--------------------------------------------------\n");
            }
        }
        return sb.toString();
    }
}