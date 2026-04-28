package com.electrimada.service;

import com.electrimada.dao.RapportDAO;
import com.electrimada.modele.Rapport;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * PrevisionService - Prevision de production solaire
 * Utilise la Moyenne Glissante sur les donnees historiques de la base.
 */
public class PrevisionService {

    private final RapportDAO rapportDAO;
    private final MoyenneGlissante moyenneGlissante;

    public PrevisionService() {
        this.rapportDAO = new RapportDAO();
        this.moyenneGlissante = new MoyenneGlissante(7, 10.0); // 7 jours, defaut 10 kWh
    }

    /**
     * Charge l'historique des productions depuis la base de donnees
     * et calcule la prevision pour demain.
     */
    public double prevoirProductionDemain() throws SQLException {
        List<Rapport> rapports = rapportDAO.findAll();

        // Extraire les consommations comme proxy de production (inverse)
        // En realite, on aurait une table de production solaire
        List<Double> historique = new ArrayList<>();
        for (Rapport r : rapports) {
            // Estimation : production = capacite batterie debut - fin + consommation
            double productionEstimee = r.getBatterieDebut() - r.getBatterieFin() + r.getConsommationTotale();
            if (productionEstimee > 0) {
                historique.add(productionEstimee);
            }
        }

        moyenneGlissante.chargerHistorique(historique);
        return moyenneGlissante.prevoirDemain();
    }

    /**
     * Prevision avec facteur saisonnier
     */
    public double prevoirProductionDemain(double facteurSaison) throws SQLException {
        return prevoirProductionDemain() * facteurSaison;
    }

    /**
     * Retourne l'historique charge
     */
    public List<Double> getHistorique() {
        return moyenneGlissante.getFenetre();
    }

    public static void main(String[] args) {
        PrevisionService service = new PrevisionService();
        try {
            double prevision = service.prevoirProductionDemain();
            System.out.println("=== Prevision Solaire ===");
            System.out.println("Historique : " + service.getHistorique());
            System.out.println("Prevision demain : " + String.format("%.2f", prevision) + " kWh");

            double previsionSeche = service.prevoirProductionDemain(1.2); // Saison seche
            System.out.println("Prevision saison seche : " + String.format("%.2f", previsionSeche) + " kWh");

        } catch (SQLException e) {
            System.err.println("Erreur de prevision : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
