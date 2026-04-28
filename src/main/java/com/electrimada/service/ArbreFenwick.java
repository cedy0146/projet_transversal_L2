package com.electrimada.service;

/**
 * Arbre de Fenwick (Binary Indexed Tree)
 * Structure de donnees avancee pour calculer efficacement les sommes sur des intervalles.
 *
 * Pourquoi c'est adapte ?
 * - Complexite : O(log n) pour les requetes de somme sur intervalle et les mises a jour.
 *   Contre O(n) pour une approche naive (boucle sur tous les elements).
 * - Contrainte 2035 : Rapports rapides sur l'etat de la batterie sans faire ramer
 *   l'ordinateur du village. Ex: "Combien d'energie entre 18h et 22h ?"
 */
public class ArbreFenwick {

    private double[] tree;
    private int n;

    public ArbreFenwick(int taille) {
        this.n = taille;
        this.tree = new double[n + 1]; // 1-based indexing
    }

    /**
     * Ajoute une valeur a l'index i (1-based) en O(log n)
     */
    public void update(int index, double delta) {
        while (index <= n) {
            tree[index] += delta;
            index += index & (-index); // Ajoute le bit le moins significatif
        }
    }

    /**
     * Construit l'arbre a partir d'un tableau de valeurs en O(n log n)
     */
    public void build(double[] valeurs) {
        for (int i = 0; i < valeurs.length && i < n; i++) {
            update(i + 1, valeurs[i]);
        }
    }

    /**
     * Somme prefixe de 1 a index en O(log n)
     */
    public double prefixSum(int index) {
        double sum = 0.0;
        while (index > 0) {
            sum += tree[index];
            index -= index & (-index);
        }
        return sum;
    }

    /**
     * Somme sur l'intervalle [left, right] en O(log n)
     */
    public double rangeSum(int left, int right) {
        if (left > right || left < 1 || right > n) return 0.0;
        return prefixSum(right) - prefixSum(left - 1);
    }

    public int size() {
        return n;
    }

    public static void main(String[] args) {
        System.out.println("=== Test Arbre de Fenwick ===");
        double[] consommations = {2.5, 1.0, 4.0, 1.5, 0.8, 3.2, 2.1}; // kWh par heure

        ArbreFenwick arbre = new ArbreFenwick(consommations.length);
        arbre.build(consommations);

        System.out.println("Consommations horaires : ");
        for (double c : consommations) System.out.print(c + " ");
        System.out.println();

        System.out.println("Somme totale (1-7) : " + arbre.prefixSum(7));
        System.out.println("Somme entre heure 2 et 5 : " + arbre.rangeSum(2, 5));

        // Mise a jour : la pompe a eau consomme plus
        arbre.update(3, 1.0); // +1.0 kWh a l'heure 3
        System.out.println("Nouvelle somme totale apres update : " + arbre.prefixSum(7));
    }
}
