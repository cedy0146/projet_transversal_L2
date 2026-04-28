package com.electrimada.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests unitaires pour l'algorithme du Sac a Dos (Knapsack)
 * Famille : Optimisation - Programmation Dynamique
 */
public class SacADosTest {

    @Test
    public void testSacADosSimple() {
        List<SacADos.Item> items = new ArrayList<>();
        items.add(new SacADos.Item("D1", 2.5, 10.0, "Lumiere hopital"));
        items.add(new SacADos.Item("D2", 1.0, 3.0, "Charge telephone"));
        items.add(new SacADos.Item("D3", 4.0, 15.0, "Pompe a eau"));

        double capacite = 5.0;

        SacADos.Resultat resultat = SacADos.resoudre(items, capacite);

        // La solution optimale : D1 (2.5) + D2 (1.0) = 3.5 kWh, valeur = 13
        // ou D3 (4.0) = 4.0 kWh, valeur = 15
        // Le meilleur est D3 avec valeur 15
        assertTrue(resultat.valeurTotale >= 13.0, "La valeur totale doit etre au moins 13");
        assertTrue(resultat.poidsTotal <= capacite, "Le poids total ne doit pas depasser la capacite");
    }

    @Test
    public void testSacADosCapaciteNulle() {
        List<SacADos.Item> items = new ArrayList<>();
        items.add(new SacADos.Item("D1", 2.5, 10.0, "Lumiere hopital"));

        SacADos.Resultat resultat = SacADos.resoudre(items, 0.0);

        assertEquals(0.0, resultat.valeurTotale, 0.001);
        assertEquals(0.0, resultat.poidsTotal, 0.001);
        assertTrue(resultat.itemsSelectionnes.isEmpty());
    }

    @Test
    public void testBaselineFIFO() {
        List<SacADos.Item> items = new ArrayList<>();
        items.add(new SacADos.Item("D1", 3.0, 5.0, "Item 1"));
        items.add(new SacADos.Item("D2", 2.0, 8.0, "Item 2"));
        items.add(new SacADos.Item("D3", 4.0, 6.0, "Item 3"));

        double capacite = 5.0;

        SacADos.Resultat resultat = SacADos.baselineFIFO(items, capacite);

        // FIFO : D1 (3.0) + D2 (2.0) = 5.0, valeur = 13
        assertEquals(2, resultat.itemsSelectionnes.size());
        assertEquals(13.0, resultat.valeurTotale, 0.001);
    }

    @Test
    public void testComparaisonOptimiseVsBaseline() {
        List<SacADos.Item> items = new ArrayList<>();
        items.add(new SacADos.Item("D1", 2.5, 10.0, "Lumiere hopital"));
        items.add(new SacADos.Item("D2", 1.0, 3.0, "Charge telephone"));
        items.add(new SacADos.Item("D3", 4.0, 15.0, "Pompe a eau"));
        items.add(new SacADos.Item("D4", 1.5, 5.0, "Eclairage etude"));
        items.add(new SacADos.Item("D5", 0.8, 2.0, "Radio"));

        double capacite = 5.0;

        SacADos.Resultat opt = SacADos.resoudre(items, capacite);
        SacADos.Resultat baseline = SacADos.baselineFIFO(items, capacite);

        // L'optimise doit etre au moins aussi bon que la baseline
        assertTrue(opt.valeurTotale >= baseline.valeurTotale,
                "L'optimise doit avoir une valeur >= a la baseline");
    }

    @Test
    public void testSacADosListeVide() {
        List<SacADos.Item> items = new ArrayList<>();
        
        SacADos.Resultat resultat = SacADos.resoudre(items, 10.0);
        
        assertEquals(0.0, resultat.valeurTotale, 0.001);
        assertTrue(resultat.itemsSelectionnes.isEmpty());
    }
}
