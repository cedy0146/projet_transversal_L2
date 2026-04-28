package com.electrimada.service;

import java.util.ArrayList;
import java.util.List;

/**
 * Algorithme du Sac a Dos (Knapsack) - Programmation Dynamique
 * Famille : Optimisation (Obligatoire)
 *
 * But : Decider quelles demandes d'electricite accepter pour maximiser
 * l'utilite sociale du village sans depasser la capacite de la batterie.
 *
 * Modelisation :
 * - Chaque demande i a une utilite v_i (score de priorite) et un cout w_i (kWh requis)
 * - Capacite W = energie disponible dans la batterie
 * - Objectif : maximiser sum(v_i) sous contrainte sum(w_i) <= W
 *
 * Complexite : O(n * W) temporelle, O(n * W) spatiale
 */
public class SacADos {

    /**
     * Represente un item (demande d'energie) pour le sac a dos
     */
    public static class Item {
        public String id;
        public double poids;      // kWh requis
        public double valeur;     // score de priorite
        public String description;

        public Item(String id, double poids, double valeur, String description) {
            this.id = id;
            this.poids = poids;
            this.valeur = valeur;
            this.description = description;
        }

        @Override
        public String toString() {
            return String.format("%s (%.1f kWh, prio=%.1f)", description, poids, valeur);
        }
    }

    /**
     * Resultat de l'optimisation
     */
    public static class Resultat {
        public double valeurTotale;
        public double poidsTotal;
        public List<Item> itemsSelectionnes;

        public Resultat() {
            this.valeurTotale = 0;
            this.poidsTotal = 0;
            this.itemsSelectionnes = new ArrayList<>();
        }
    }

    /**
     * Algorithme du Sac a Dos 0/1 avec programmation dynamique.
     * Utilise une table DP complete pour reconstruction correcte.
     *
     * @param items Liste des demandes
     * @param capacite Capacite de la batterie (W) en kWh
     * @return Resultat avec les items selectionnes
     */
    public static Resultat resoudre(List<Item> items, double capacite) {
        int n = items.size();
        int W = (int) Math.ceil(capacite * 10); // Multiplicateur pour gerer les doubles (precision 0.1 kWh)

        // Conversion des poids en entiers (precision 0.1 kWh)
        int[] poids = new int[n];
        double[] valeurs = new double[n];
        for (int i = 0; i < n; i++) {
            poids[i] = (int) Math.ceil(items.get(i).poids * 10);
            valeurs[i] = items.get(i).valeur;
        }

        // Table DP complete pour reconstruction correcte
        double[][] dp = new double[n + 1][W + 1];

        // Remplissage de la table DP
        for (int i = 1; i <= n; i++) {
            int wi = poids[i - 1];
            double vi = valeurs[i - 1];
            for (int w = 0; w <= W; w++) {
                dp[i][w] = dp[i - 1][w]; // Ne pas prendre l'item
                if (w >= wi && dp[i - 1][w - wi] + vi > dp[i][w]) {
                    dp[i][w] = dp[i - 1][w - wi] + vi; // Prendre l'item
                }
            }
        }

        // Reconstruction de la solution
        Resultat resultat = new Resultat();
        int w = W;
        for (int i = n; i >= 1 && w > 0; i--) {
            if (Math.abs(dp[i][w] - dp[i - 1][w]) > 0.001) {
                Item item = items.get(i - 1);
                resultat.itemsSelectionnes.add(item);
                resultat.valeurTotale += item.valeur;
                resultat.poidsTotal += item.poids;
                w -= poids[i - 1];
            }
        }

        return resultat;
    }

    /**
     * Baseline naive : FIFO (First-In First-Out)
     * On sert les demandes dans l'ordre jusqu'a epuisement de la batterie.
     */
    public static Resultat baselineFIFO(List<Item> items, double capacite) {
        Resultat resultat = new Resultat();
        double reste = capacite;

        for (Item item : items) {
            if (item.poids <= reste) {
                resultat.itemsSelectionnes.add(item);
                resultat.valeurTotale += item.valeur;
                resultat.poidsTotal += item.poids;
                reste -= item.poids;
            }
        }

        return resultat;
    }

    /**
     * Baseline naive : Partage egalitaire
     * On divise l'energie par le nombre de foyers sans tenir compte des priorites.
     */
    public static Resultat baselineEgalitaire(List<Item> items, double capacite) {
        Resultat resultat = new Resultat();
        if (items.isEmpty()) return resultat;

        double part = capacite / items.size();

        for (Item item : items) {
            if (item.poids <= part) {
                resultat.itemsSelectionnes.add(item);
                resultat.valeurTotale += item.valeur;
                resultat.poidsTotal += item.poids;
            }
        }

        return resultat;
    }

    public static void main(String[] args) {
        System.out.println("=== Test Sac a Dos (Allocation Optimale) ===");

        List<Item> demandes = new ArrayList<>();
        demandes.add(new Item("D1", 2.5, 10.0, "Lumiere hopital"));
        demandes.add(new Item("D2", 1.0, 3.0, "Charge telephone"));
        demandes.add(new Item("D3", 4.0, 15.0, "Pompe a eau"));
        demandes.add(new Item("D4", 1.5, 5.0, "Eclairage etude"));
        demandes.add(new Item("D5", 0.8, 2.0, "Radio"));
        demandes.add(new Item("D6", 3.2, 8.0, "Boutique"));
        demandes.add(new Item("D7", 2.1, 6.0, "Lumiere maison"));

        double capaciteBatterie = 8.0; // kWh disponibles

        System.out.println("\n--- Baseline FIFO ---");
        Resultat fifo = baselineFIFO(demandes, capaciteBatterie);
        System.out.println("Valeur totale : " + fifo.valeurTotale);
        System.out.println("Energie utilisee : " + fifo.poidsTotal + " / " + capaciteBatterie);
        for (Item i : fifo.itemsSelectionnes) System.out.println("  -> " + i);

        System.out.println("\n--- Optimise (Knapsack DP) ---");
        Resultat opt = resoudre(demandes, capaciteBatterie);
        System.out.println("Valeur totale : " + opt.valeurTotale);
        System.out.println("Energie utilisee : " + opt.poidsTotal + " / " + capaciteBatterie);
        for (Item i : opt.itemsSelectionnes) System.out.println("  -> " + i);

        System.out.println("\n--- Amelioration ---");
        System.out.println("Gain de satisfaction : +" + (opt.valeurTotale - fifo.valeurTotale) + " points");
        System.out.println("Meilleure utilisation : " + String.format("%.1f", (opt.poidsTotal/capaciteBatterie)*100) + "%");
    }
}
