package com.electrimada.service;

import com.electrimada.modele.Batterie;
import com.electrimada.modele.DemandeEnergie;
import com.electrimada.modele.Foyer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests unitaires pour l'AllocationService
 * Teste la logique de comparaison baseline vs optimise sans base de donnees.
 */
public class AllocationServiceTest {

    /**
     * Cree des donnees de test en memoire
     */
    private List<DemandeEnergie> creerDemandesTest() {
        List<DemandeEnergie> demandes = new ArrayList<>();
        
        DemandeEnergie d1 = new DemandeEnergie();
        d1.setIdDemande("D1");
        d1.setQuantiteKwh(2.5);
        d1.setNiveauCriticite("CRITIQUE");
        d1.setIdFoyer("F1");
        
        DemandeEnergie d2 = new DemandeEnergie();
        d2.setIdDemande("D2");
        d2.setQuantiteKwh(1.0);
        d2.setNiveauCriticite("BASSE");
        d2.setIdFoyer("F2");
        
        DemandeEnergie d3 = new DemandeEnergie();
        d3.setIdDemande("D3");
        d3.setQuantiteKwh(4.0);
        d3.setNiveauCriticite("HAUTE");
        d3.setIdFoyer("F3");
        
        demandes.add(d1);
        demandes.add(d2);
        demandes.add(d3);
        
        return demandes;
    }

    private List<Batterie> creerBatteriesTest() {
        List<Batterie> batteries = new ArrayList<>();
        Batterie b = new Batterie();
        b.setIdBatterie(1);
        b.setCapaciteTotale(100.0);
        b.setCapaciteActuelle(5.0);
        b.setSeuilCritique(20.0);
        batteries.add(b);
        return batteries;
    }

    private TableHachage<String, Foyer> creerFoyersTest() {
        TableHachage<String, Foyer> foyers = new TableHachage<>();
        
        Foyer f1 = new Foyer();
        f1.setIdFoyer("F1");
        f1.setNom("Hopital");
        f1.setTypePriorite("Prioritaire");
        f1.setConsommationMoyenne(5.0);
        f1.setJoursSansElectricite(0);
        
        Foyer f2 = new Foyer();
        f2.setIdFoyer("F2");
        f2.setNom("Maison Rakoto");
        f2.setTypePriorite("Standard");
        f2.setConsommationMoyenne(3.0);
        f2.setJoursSansElectricite(1);
        
        Foyer f3 = new Foyer();
        f3.setIdFoyer("F3");
        f3.setNom("Ecole");
        f3.setTypePriorite("Prioritaire");
        f3.setConsommationMoyenne(4.0);
        f3.setJoursSansElectricite(0);
        
        foyers.put("F1", f1);
        foyers.put("F2", f2);
        foyers.put("F3", f3);
        
        return foyers;
    }

    @Test
    public void testAllocationOptimisee() {
        AllocationService service = new AllocationService();
        
        List<DemandeEnergie> demandes = creerDemandesTest();
        List<Batterie> batteries = creerBatteriesTest();
        TableHachage<String, Foyer> foyers = creerFoyersTest();
        
        List<AllocationService.DemandePriorisee> resultat = 
            service.allouerEnergieOptimise(demandes, batteries, foyers);
        
        assertNotNull(resultat);
        assertTrue(resultat.size() > 0, "Au moins une demande doit etre acceptee");
        
        // Verifier que la capacite n'est pas depassee
        double totalKwh = 0;
        for (AllocationService.DemandePriorisee dp : resultat) {
            totalKwh += dp.demande.getQuantiteKwh();
        }
        assertTrue(totalKwh <= 5.0, "La capacite de 5 kWh ne doit pas etre depassee");
    }

    @Test
    public void testAllocationBaseline() {
        AllocationService service = new AllocationService();
        
        List<DemandeEnergie> demandes = creerDemandesTest();
        List<Batterie> batteries = creerBatteriesTest();
        TableHachage<String, Foyer> foyers = creerFoyersTest();
        
        List<AllocationService.DemandePriorisee> resultat = 
            service.allouerEnergieBaseline(demandes, batteries, foyers);
        
        assertNotNull(resultat);
        
        // Verifier que la capacite n'est pas depassee
        double totalKwh = 0;
        for (AllocationService.DemandePriorisee dp : resultat) {
            totalKwh += dp.demande.getQuantiteKwh();
        }
        assertTrue(totalKwh <= 5.0, "La capacite de 5 kWh ne doit pas etre depassee");
    }

    @Test
    public void testOptimiseMeilleurQueBaseline() {
        AllocationService service = new AllocationService();
        
        List<DemandeEnergie> demandes = creerDemandesTest();
        List<Batterie> batteries = creerBatteriesTest();
        TableHachage<String, Foyer> foyers = creerFoyersTest();
        
        List<AllocationService.DemandePriorisee> opt = 
            service.allouerEnergieOptimise(demandes, batteries, foyers);
        List<AllocationService.DemandePriorisee> base = 
            service.allouerEnergieBaseline(demandes, batteries, foyers);
        
        double scoreOpt = opt.stream().mapToDouble(d -> d.scorePriorite).sum();
        double scoreBase = base.stream().mapToDouble(d -> d.scorePriorite).sum();
        
        assertTrue(scoreOpt >= scoreBase, 
            "L'optimise doit avoir un score >= a la baseline");
    }

    @Test
    public void testPrioriteHospital() {
        AllocationService service = new AllocationService();
        
        List<DemandeEnergie> demandes = creerDemandesTest();
        List<Batterie> batteries = creerBatteriesTest();
        TableHachage<String, Foyer> foyers = creerFoyersTest();
        
        List<AllocationService.DemandePriorisee> resultat = 
            service.allouerEnergieOptimise(demandes, batteries, foyers);
        
        // L'hopital (CRITIQUE, Prioritaire) doit etre accepte
        boolean hopitalAccepte = resultat.stream()
            .anyMatch(dp -> "Hopital".equals(dp.foyer.getNom()));
        
        assertTrue(hopitalAccepte, "L'hopital doit etre prioritaire et accepte");
    }

    @Test
    public void testBatterieVide() {
        AllocationService service = new AllocationService();
        
        List<DemandeEnergie> demandes = creerDemandesTest();
        List<Batterie> batteries = new ArrayList<>();
        TableHachage<String, Foyer> foyers = creerFoyersTest();
        
        List<AllocationService.DemandePriorisee> resultat = 
            service.allouerEnergieOptimise(demandes, batteries, foyers);
        
        assertTrue(resultat.isEmpty(), "Aucune allocation si batterie vide");
    }
}
