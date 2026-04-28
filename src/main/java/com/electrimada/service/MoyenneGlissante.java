package com.electrimada.service;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Moyenne Glissante (Moving Average / Streaming Window)
 * Famille : Streaming / Fenetrage (Maintien d'agregats)
 *
 * But : Prevision de la production solaire de demain en traitant le flux
 * de donnees des jours precedents.
 *
 * Pourquoi c'est adapte ?
 * - Traite les donnees en flux continu (streaming) sans stocker tout l'historique
 * - Tolerant aux donnees manquantes : si un jour n'a pas de donnee, on utilise
 *   la moyenne disponible ou une valeur par defaut
 * - Faible cout CPU et memoire : parfait pour telephone bas de gamme
 *
 * Complexite : O(1) par mise a jour, O(1) par requete de moyenne
 */
public class MoyenneGlissante {

    private Deque<Double> fenetre;
    private int tailleFenetre;
    private double somme;
    private double valeurDefaut; // Valeur utilisee si donnee manquante

    public MoyenneGlissante(int tailleFenetre, double valeurDefaut) {
        this.tailleFenetre = tailleFenetre;
        this.valeurDefaut = valeurDefaut;
        this.fenetre = new ArrayDeque<>();
        this.somme = 0.0;
    }

    /**
     * Ajoute une nouvelle valeur de production (kWh) dans la fenetre.
     * Si la valeur est invalide (negative, NaN, Infinity), on utilise la valeur par defaut.
     */
    public void ajouter(double valeur) {
        if (Double.isNaN(valeur) || Double.isInfinite(valeur) || valeur < 0) {
            valeur = valeurDefaut;
        }

        fenetre.addLast(valeur);
        somme += valeur;

        // Retirer l'ancienne valeur si la fenetre depasse la taille
        if (fenetre.size() > tailleFenetre) {
            somme -= fenetre.removeFirst();
        }
    }

    /**
     * Retourne la moyenne glissante actuelle en O(1)
     */
    public double getMoyenne() {
        if (fenetre.isEmpty()) return valeurDefaut;
        return somme / fenetre.size();
    }

    /**
     * Prevision pour demain : moyenne glissante avec facteur de correction
     * selon la saison (simplifie).
     */
    public double prevoirDemain() {
        return getMoyenne();
    }

    /**
     * Prevision avec ajustement saisonnier (ex: saison seche = plus de soleil)
     */
    public double prevoirDemain(double facteurSaison) {
        return getMoyenne() * facteurSaison;
    }

    /**
     * Nombre de jours de donnees actuellement dans la fenetre
     */
    public int getNombreJours() {
        return fenetre.size();
    }

    /**
     * Retourne une copie des valeurs dans la fenetre
     */
    public List<Double> getFenetre() {
        return new ArrayList<>(fenetre);
    }

    /**
     * Remplit la fenetre avec un historique existant (ex: depuis la base de donnees)
     */
    public void chargerHistorique(List<Double> historique) {
        fenetre.clear();
        somme = 0.0;
        // Ne garder que les 'tailleFenetre' dernieres valeurs
        int debut = Math.max(0, historique.size() - tailleFenetre);
        for (int i = debut; i < historique.size(); i++) {
            ajouter(historique.get(i));
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Test Moyenne Glissante (Prevision Solaire) ===");

        // Simulation : production des 10 derniers jours (kWh)
        double[] productions = {12.5, 14.2, 11.8, 0.0, 13.5, 15.0, 10.2, 9.8, 14.5, 16.0};

        MoyenneGlissante prevision = new MoyenneGlissante(7, 10.0); // Fenetre de 7 jours

        System.out.println("Historique de production (kWh) :");
        for (double p : productions) {
            prevision.ajouter(p);
            System.out.printf("%.1f ", p);
        }
        System.out.println();

        System.out.println("\nFenetre active (" + prevision.getNombreJours() + " jours) : " + prevision.getFenetre());
        System.out.println("Moyenne glissante : " + String.format("%.2f", prevision.getMoyenne()) + " kWh");
        System.out.println("Prevision demain : " + String.format("%.2f", prevision.prevoirDemain()) + " kWh");

        // Test avec donnee manquante
        System.out.println("\n--- Test donnee manquante ---");
        MoyenneGlissante prevision2 = new MoyenneGlissante(5, 8.0);
        prevision2.ajouter(10.0);
        prevision2.ajouter(-5.0); // Invalide -> defaut
        prevision2.ajouter(Double.NaN); // Invalide -> defaut
        prevision2.ajouter(12.0);
        prevision2.ajouter(11.0);
        System.out.println("Moyenne avec donnees manquantes : " + String.format("%.2f", prevision2.getMoyenne()));
    }
}
