package com.electrimada.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour l'Arbre de Fenwick (Binary Indexed Tree)
 * Structure de donnees avancee : O(log n) pour somme et update
 */
public class ArbreFenwickTest {

    @Test
    public void testSommePrefixe() {
        double[] valeurs = {1.0, 2.0, 3.0, 4.0, 5.0};
        ArbreFenwick arbre = new ArbreFenwick(valeurs.length);
        arbre.build(valeurs);

        assertEquals(1.0, arbre.prefixSum(1), 0.001);
        assertEquals(3.0, arbre.prefixSum(2), 0.001); // 1+2
        assertEquals(6.0, arbre.prefixSum(3), 0.001); // 1+2+3
        assertEquals(15.0, arbre.prefixSum(5), 0.001); // 1+2+3+4+5
    }

    @Test
    public void testRangeSum() {
        double[] valeurs = {2.0, 4.0, 6.0, 8.0, 10.0};
        ArbreFenwick arbre = new ArbreFenwick(valeurs.length);
        arbre.build(valeurs);

        assertEquals(6.0, arbre.rangeSum(1, 2), 0.001); // 2+4
        assertEquals(18.0, arbre.rangeSum(2, 4), 0.001); // 4+6+8
        assertEquals(30.0, arbre.rangeSum(1, 5), 0.001); // total
    }

    @Test
    public void testUpdate() {
        double[] valeurs = {1.0, 2.0, 3.0, 4.0};
        ArbreFenwick arbre = new ArbreFenwick(valeurs.length);
        arbre.build(valeurs);

        arbre.update(2, 5.0); // Ajoute 5.0 a l'index 2

        assertEquals(8.0, arbre.prefixSum(2), 0.001); // 1 + (2+5) = 8
        assertEquals(15.0, arbre.prefixSum(4), 0.001); // total + 5 = 15
    }

    @Test
    public void testIntervalleInvalide() {
        double[] valeurs = {1.0, 2.0, 3.0};
        ArbreFenwick arbre = new ArbreFenwick(valeurs.length);
        arbre.build(valeurs);

        assertEquals(0.0, arbre.rangeSum(3, 1), 0.001); // left > right
        assertEquals(0.0, arbre.rangeSum(0, 2), 0.001); // left < 1
        assertEquals(0.0, arbre.rangeSum(1, 10), 0.001); // right > n
    }

    @Test
    public void testConsommationHoraire() {
        // Simulation de consommation horaire (kWh)
        double[] consommations = {2.5, 1.0, 4.0, 1.5, 0.8, 3.2, 2.1};
        ArbreFenwick arbre = new ArbreFenwick(consommations.length);
        arbre.build(consommations);

        // Consommation entre 18h (heure 1) et 22h (heure 5)
        double consommationSoir = arbre.rangeSum(1, 5);
        assertEquals(9.8, consommationSoir, 0.001); // 2.5+1.0+4.0+1.5+0.8

        // Consommation totale
        assertEquals(15.1, arbre.prefixSum(7), 0.001);
    }
}
