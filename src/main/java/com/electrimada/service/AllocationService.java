package com.electrimada.service;

import com.electrimada.dao.BatterieDAO;
import com.electrimada.dao.DemandeEnergieDAO;
import com.electrimada.dao.FoyerDAO;
import com.electrimada.modele.Batterie;
import com.electrimada.modele.DemandeEnergie;
import com.electrimada.modele.Foyer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * AllocationService - Coeur metier du projet
 * Orchestration du Tas Binaire (priorisation) + Sac a Dos (optimisation)
 * pour repartir l'energie disponible aux demandes du village.
 *
 * Toutes les donnees sont recuperees depuis la base de donnees.
 */
public class AllocationService {

    private final FoyerDAO foyerDAO;
    private final DemandeEnergieDAO demandeDAO;
    private final BatterieDAO batterieDAO;

    public AllocationService() {
        this.foyerDAO = new FoyerDAO();
        this.demandeDAO = new DemandeEnergieDAO();
        this.batterieDAO = new BatterieDAO();
    }

    /**
     * Classe interne pour comparer les demandes par priorite
     * Utilisee avec le Tas Binaire
     */
    public static class DemandePriorisee implements Comparable<DemandePriorisee> {
        public DemandeEnergie demande;
        public Foyer foyer;
        public double scorePriorite;

        public DemandePriorisee(DemandeEnergie demande, Foyer foyer, double scorePriorite) {
            this.demande = demande;
            this.foyer = foyer;
            this.scorePriorite = scorePriorite;
        }

        @Override
        public int compareTo(DemandePriorisee autre) {
            return Double.compare(this.scorePriorite, autre.scorePriorite);
        }

        @Override
        public String toString() {
            return String.format("%s (%s) - %.1f kWh [score=%.1f]",
                    foyer.getNom(), demande.getNiveauCriticite(),
                    demande.getQuantiteKwh(), scorePriorite);
        }
    }

    /**
     * Calcule le score de priorite d'une demande.
     * Plus le score est eleve, plus la demande est prioritaire.
     */
    private double calculerScorePriorite(DemandeEnergie demande, Foyer foyer) {
        double score = 0.0;

        // Coefficient selon le niveau de criticite
        switch (demande.getNiveauCriticite() != null ? demande.getNiveauCriticite().toUpperCase() : "NORMALE") {
            case "CRITIQUE":
                score += 100.0;
                break;
            case "HAUTE":
                score += 50.0;
                break;
            case "NORMALE":
                score += 20.0;
                break;
            case "BASSE":
                score += 5.0;
                break;
            default:
                score += 10.0;
        }

        // Bonus pour les foyers prioritaires
        if ("Prioritaire".equalsIgnoreCase(foyer.getTypePriorite())) {
            score += 30.0;
        } else if ("Eco".equalsIgnoreCase(foyer.getTypePriorite())) {
            score += 10.0;
        }

        // Bonus pour les foyers sans electricite depuis longtemps
        score += foyer.getJoursSansElectricite() * 5.0;

        return score;
    }

    /**
     * Recupere toutes les demandes depuis la base de donnees,
     * les priorise avec le Tas Binaire, puis optimise avec le Sac a Dos.
     *
     * @return Liste des demandes acceptees avec leur allocation
     */
    public List<DemandePriorisee> allouerEnergieOptimise() throws SQLException {
        // 1. Recuperer les donnees depuis la base
        List<DemandeEnergie> demandes = demandeDAO.findAll();
        List<Batterie> batteries = batterieDAO.findAll();

        if (batteries.isEmpty()) {
            throw new SQLException("Aucune batterie trouvee dans la base de donnees");
        }

        Batterie batterie = batteries.get(0);
        double capaciteDisponible = batterie.getCapaciteActuelle();

        // 2. Construire la table de hachage des foyers pour acces rapide O(1)
        TableHachage<String, Foyer> foyersMap = new TableHachage<>();
        for (Foyer f : foyerDAO.findAll()) {
            foyersMap.put(f.getIdFoyer(), f);
        }

        // 3. Prioriser les demandes avec le Tas Binaire (Max Heap)
        TasBinaire<DemandePriorisee> tasPriorites = new TasBinaire<>();

        for (DemandeEnergie demande : demandes) {
            Foyer foyer = foyersMap.get(demande.getIdFoyer());
            if (foyer == null) continue;

            double score = calculerScorePriorite(demande, foyer);
            tasPriorites.insert(new DemandePriorisee(demande, foyer, score));
        }

        // 4. Construire les items pour le Sac a Dos (extraire dans l'ordre de priorite)
        List<SacADos.Item> items = new ArrayList<>();
        List<DemandePriorisee> demandesOrdonnees = new ArrayList<>();

        while (!tasPriorites.isEmpty()) {
            DemandePriorisee dp = tasPriorites.extractMax();
            demandesOrdonnees.add(dp);
            String desc = dp.foyer.getNom() + " - " + dp.demande.getNiveauCriticite();
            items.add(new SacADos.Item(
                    dp.demande.getIdDemande(),
                    dp.demande.getQuantiteKwh(),
                    dp.scorePriorite,
                    desc
            ));
        }

        // 5. Executer l'algorithme du Sac a Dos
        SacADos.Resultat resultatOpt = SacADos.resoudre(items, capaciteDisponible);

        // 6. Filtrer les demandes acceptees
        List<DemandePriorisee> demandesAcceptees = new ArrayList<>();
        for (SacADos.Item item : resultatOpt.itemsSelectionnes) {
            for (DemandePriorisee dp : demandesOrdonnees) {
                if (dp.demande.getIdDemande().equals(item.id)) {
                    demandesAcceptees.add(dp);
                    break;
                }
            }
        }

        // 7. Mettre a jour le statut des demandes dans la base de donnees
        for (DemandePriorisee dp : demandesOrdonnees) {
            boolean acceptee = demandesAcceptees.contains(dp);
            dp.demande.setEstAcceptee(acceptee);
            demandeDAO.save(dp.demande);
        }

        // 8. Mettre a jour la batterie
        double nouvelleCapacite = capaciteDisponible - resultatOpt.poidsTotal;
        batterie.setCapaciteActuelle(Math.max(0, nouvelleCapacite));
        batterieDAO.save(batterie);

        return demandesAcceptees;
    }

    /**
     * Version baseline FIFO pour comparaison (mesure d'optimisation)
     * Calcule sans modifier la base de donnees.
     */
    public List<DemandePriorisee> allouerEnergieBaseline(List<DemandeEnergie> demandes, 
                                                          List<Batterie> batteries,
                                                          TableHachage<String, Foyer> foyersMap) {
        if (batteries.isEmpty()) {
            return new ArrayList<>();
        }

        double capaciteDisponible = batteries.get(0).getCapaciteActuelle();

        List<SacADos.Item> items = new ArrayList<>();
        for (DemandeEnergie demande : demandes) {
            Foyer foyer = foyersMap.get(demande.getIdFoyer());
            if (foyer == null) continue;
            double score = calculerScorePriorite(demande, foyer);
            String desc = foyer.getNom() + " - " + demande.getNiveauCriticite();
            items.add(new SacADos.Item(demande.getIdDemande(), demande.getQuantiteKwh(), score, desc));
        }

        SacADos.Resultat resultatBaseline = SacADos.baselineFIFO(items, capaciteDisponible);

        List<DemandePriorisee> demandesAcceptees = new ArrayList<>();
        for (SacADos.Item item : resultatBaseline.itemsSelectionnes) {
            for (DemandeEnergie d : demandes) {
                if (d.getIdDemande().equals(item.id)) {
                    Foyer f = foyersMap.get(d.getIdFoyer());
                    demandesAcceptees.add(new DemandePriorisee(d, f, item.valeur));
                    break;
                }
            }
        }

        return demandesAcceptees;
    }

    /**
     * Version optimisee sans persistance (pour comparaison)
     */
    public List<DemandePriorisee> allouerEnergieOptimise(List<DemandeEnergie> demandes,
                                                          List<Batterie> batteries,
                                                          TableHachage<String, Foyer> foyersMap) {
        if (batteries.isEmpty()) {
            return new ArrayList<>();
        }

        double capaciteDisponible = batteries.get(0).getCapaciteActuelle();

        // Prioriser les demandes avec le Tas Binaire (Max Heap)
        TasBinaire<DemandePriorisee> tasPriorites = new TasBinaire<>();

        for (DemandeEnergie demande : demandes) {
            Foyer foyer = foyersMap.get(demande.getIdFoyer());
            if (foyer == null) continue;
            double score = calculerScorePriorite(demande, foyer);
            tasPriorites.insert(new DemandePriorisee(demande, foyer, score));
        }

        // Construire les items pour le Sac a Dos
        List<SacADos.Item> items = new ArrayList<>();
        List<DemandePriorisee> demandesOrdonnees = new ArrayList<>();

        while (!tasPriorites.isEmpty()) {
            DemandePriorisee dp = tasPriorites.extractMax();
            demandesOrdonnees.add(dp);
            String desc = dp.foyer.getNom() + " - " + dp.demande.getNiveauCriticite();
            items.add(new SacADos.Item(
                    dp.demande.getIdDemande(),
                    dp.demande.getQuantiteKwh(),
                    dp.scorePriorite,
                    desc
            ));
        }

        // Executer l'algorithme du Sac a Dos
        SacADos.Resultat resultatOpt = SacADos.resoudre(items, capaciteDisponible);

        // Filtrer les demandes acceptees
        List<DemandePriorisee> demandesAcceptees = new ArrayList<>();
        for (SacADos.Item item : resultatOpt.itemsSelectionnes) {
            for (DemandePriorisee dp : demandesOrdonnees) {
                if (dp.demande.getIdDemande().equals(item.id)) {
                    demandesAcceptees.add(dp);
                    break;
                }
            }
        }

        return demandesAcceptees;
    }

    /**
     * Comparaison mesurable entre baseline et optimise.
     * Ne modifie PAS la base de donnees - utilisation des donnees en memoire.
     * @return Un dictionnaire contenant les metrics de comparaison
     */
    public Map<String, Object> comparerAllocations() throws SQLException {
        // Charger les donnees fraiches depuis la base
        List<DemandeEnergie> demandes = demandeDAO.findAll();
        List<Batterie> batteries = batterieDAO.findAll();
        double capacite = batteries.isEmpty() ? 0 : batteries.get(0).getCapaciteActuelle();

        // Construire la map des foyers
        TableHachage<String, Foyer> foyersMap = new TableHachage<>();
        for (Foyer f : foyerDAO.findAll()) {
            foyersMap.put(f.getIdFoyer(), f);
        }

        // Test optimise
        long debut1 = System.nanoTime();
        List<DemandePriorisee> opt = allouerEnergieOptimise(demandes, batteries, foyersMap);
        long fin1 = System.nanoTime();
        double tempsOpt = (fin1 - debut1) / 1_000_000.0;

        // Test baseline (memes donnees initiales, pas de modification DB)
        long debut2 = System.nanoTime();
        List<DemandePriorisee> baseline = allouerEnergieBaseline(demandes, batteries, foyersMap);
        long fin2 = System.nanoTime();
        double tempsBase = (fin2 - debut2) / 1_000_000.0;

        double scoreOpt = opt.stream().mapToDouble(d -> d.scorePriorite).sum();
        double scoreBase = baseline.stream().mapToDouble(d -> d.scorePriorite).sum();

        Map<String, Object> metrics = new HashMap<>();
        metrics.put("capaciteBatterie", capacite);
        metrics.put("totalDemandes", demandes.size());
        
        Map<String, Object> baseResults = new HashMap<>();
        baseResults.put("count", baseline.size());
        baseResults.put("score", scoreBase);
        baseResults.put("timeMs", tempsBase);
        
        Map<String, Object> optResults = new HashMap<>();
        optResults.put("count", opt.size());
        optResults.put("score", scoreOpt);
        optResults.put("timeMs", tempsOpt);
        
        metrics.put("baseline", baseResults);
        metrics.put("optimise", optResults);
        metrics.put("gainPourcentage", ((scoreOpt - scoreBase) / Math.max(scoreBase, 1) * 100));
        
        return metrics;

        // Puis effectuer la vraie allocation avec persistance
    }

    public static void main(String[] args) {
        AllocationService service = new AllocationService();
        try {
            System.out.println("=== Allocation Optimisee ===");
            List<DemandePriorisee> acceptees = service.allouerEnergieOptimise();
            System.out.println("Demandes acceptees (" + acceptees.size() + ") :");
            for (DemandePriorisee dp : acceptees) {
                System.out.println("  -> " + dp);
            }

            service.comparerAllocations();

        } catch (SQLException e) {
            System.err.println("Erreur d'allocation : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
