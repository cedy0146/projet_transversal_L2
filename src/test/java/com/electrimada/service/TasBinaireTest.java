package com.electrimada.service;

import com.electrimada.modele.DemandeEnergie;
import com.electrimada.modele.Foyer;
import com.electrimada.service.AllocationService.DemandePriorisee;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour le Tas Binaire (Binary Heap)
 * Structure de donnees avancee : O(1) peek, O(log n) insert/extract
 */
public class TasBinaireTest {

    @Test
    public void testInsertionEtExtraction() {
        TasBinaire<Integer> tas = new TasBinaire<>();
        
        tas.insert(15);
        tas.insert(10);
        tas.insert(20);
        tas.insert(5);
        
        assertEquals(4, tas.size());
        assertEquals(Integer.valueOf(20), tas.peek());
        
        assertEquals(Integer.valueOf(20), tas.extractMax());
        assertEquals(Integer.valueOf(15), tas.extractMax());
        assertEquals(Integer.valueOf(10), tas.extractMax());
        assertEquals(Integer.valueOf(5), tas.extractMax());
        
        assertTrue(tas.isEmpty());
    }

    @Test
    public void testTasVide() {
        TasBinaire<Integer> tas = new TasBinaire<>();
        
        assertTrue(tas.isEmpty());
        assertNull(tas.peek());
        assertNull(tas.extractMax());
    }

    @Test
    public void testOrdreDecroissant() {
        TasBinaire<Integer> tas = new TasBinaire<>();
        int[] valeurs = {3, 1, 4, 1, 5, 9, 2, 6};
        
        for (int v : valeurs) {
            tas.insert(v);
        }
        
        int precedent = Integer.MAX_VALUE;
        while (!tas.isEmpty()) {
            int courant = tas.extractMax();
            assertTrue(courant <= precedent, "Le tas doit extraire en ordre decroissant");
            precedent = courant;
        }
    }

    @Test
    public void testAvecDemandePriorisee() {
        TasBinaire<DemandePriorisee> tas = new TasBinaire<>();
        
        Foyer f1 = new Foyer(); f1.setNom("Foyer A");
        DemandePriorisee dp1 = new DemandePriorisee(new DemandeEnergie(), f1, 10.0);
        
        Foyer f2 = new Foyer(); f2.setNom("Foyer B");
        DemandePriorisee dp2 = new DemandePriorisee(new DemandeEnergie(), f2, 50.0);
        
        tas.insert(dp1);
        tas.insert(dp2);
        
        assertEquals(2, tas.size());
        // Le score le plus élevé (50.0) doit sortir en premier
        DemandePriorisee max = tas.extractMax();
        assertEquals(50.0, max.scorePriorite, 0.001);
        assertEquals("Foyer B", max.foyer.getNom());
    }
}
