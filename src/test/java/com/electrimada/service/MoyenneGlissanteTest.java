package com.electrimada.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la Moyenne Glissante (Moving Average)
 * Famille : Streaming / Fenetrage
 */
public class MoyenneGlissanteTest {

    @Test
    public void testMoyenneSimple() {
        MoyenneGlissante mg = new MoyenneGlissante(3, 10.0);
        
        mg.ajouter(10.0);
        mg.ajouter(20.0);
        mg.ajouter(30.0);

        assertEquals(20.0, mg.getMoyenne(), 0.001);
    }

    @Test
    public void testFenetreGlissante() {
        MoyenneGlissante mg = new MoyenneGlissante(3, 10.0);
        
        mg.ajouter(10.0);
        mg.ajouter(20.0);
        mg.ajouter(30.0);
        mg.ajouter(40.0); // Le 10.0 sort de la fenetre
        
        // Moyenne de 20, 30, 40 = 30
        assertEquals(30.0, mg.getMoyenne(), 0.001);
        assertEquals(3, mg.getNombreJours());
    }

    @Test
    public void testDonneesInvalides() {
        MoyenneGlissante mg = new MoyenneGlissante(3, 5.0);
        
        mg.ajouter(10.0);
        mg.ajouter(-5.0); // Invalide -> defaut 5.0
        mg.ajouter(Double.NaN); // Invalide -> defaut 5.0
        
        // Moyenne de 10, 5, 5 = 6.666...
        assertEquals(20.0 / 3, mg.getMoyenne(), 0.001);
    }

    @Test
    public void testFenetreVide() {
        MoyenneGlissante mg = new MoyenneGlissante(5, 8.0);
        
        assertEquals(8.0, mg.getMoyenne(), 0.001); // Valeur par defaut
        assertEquals(0, mg.getNombreJours());
    }

    @Test
    public void testPrevisionDemain() {
        MoyenneGlissante mg = new MoyenneGlissante(3, 10.0);
        
        mg.ajouter(12.0);
        mg.ajouter(15.0);
        mg.ajouter(18.0);
        
        double prevision = mg.prevoirDemain();
        assertEquals(15.0, prevision, 0.001);
        
        double previsionSaison = mg.prevoirDemain(1.2); // Saison seche
        assertEquals(18.0, previsionSaison, 0.001);
    }
}
