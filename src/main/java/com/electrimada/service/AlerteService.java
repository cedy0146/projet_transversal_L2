package com.electrimada.service;

import com.electrimada.dao.AlertesDAO;
import com.electrimada.dao.BatterieDAO;
import com.electrimada.dao.RapportDAO;
import com.electrimada.modele.Alertes;
import com.electrimada.modele.Batterie;
import com.electrimada.modele.Rapport;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * AlerteService - Detection et gestion des anomalies
 * Identifie les ecarts (consommation anormale, production faible, batterie basse)
 * et genere des alertes stockees dans la base de donnees.
 */
public class AlerteService {

    private final AlertesDAO alertesDAO;
    private final BatterieDAO batterieDAO;
    private final RapportDAO rapportDAO;
    private final BatterieService batterieService;

    public AlerteService() {
        this.alertesDAO = new AlertesDAO();
        this.batterieDAO = new BatterieDAO();
        this.rapportDAO = new RapportDAO();
        this.batterieService = new BatterieService();
    }

    /**
     * Verifie toutes les conditions et genere les alertes necessaires
     */
    public List<Alertes> verifierEtGenererAlertes() throws SQLException {
        List<Alertes> alertesGenerees = new ArrayList<>();

        // 1. Alertes batterie
        Batterie batterie = batterieService.getEtatBatterie();
        if (batterie != null) {
            double pct = batterieService.getPourcentageCharge(batterie);

            if (batterieService.estModeEcoForce(batterie)) {
                alertesGenerees.add(creerAlerte("CRITIQUE: Batterie a " +
                        String.format("%.0f", pct) + "% - Mode Eco force active!"));
            } else if (batterieService.estSeuilCritique(batterie)) {
                alertesGenerees.add(creerAlerte("ATTENTION: Batterie a " +
                        String.format("%.0f", pct) + "% - Seuil critique atteint"));
            }
        }

        // 2. Alertes consommation anormale
        List<Rapport> rapports = rapportDAO.findAll();
        if (rapports.size() >= 2) {
            Rapport dernier = rapports.get(rapports.size() - 1);
            Rapport avantDernier = rapports.get(rapports.size() - 2);

            double delta = dernier.getConsommationTotale() - avantDernier.getConsommationTotale();
            if (Math.abs(delta) > 5.0) {
                alertesGenerees.add(creerAlerte("Consommation anormale detectee: " +
                        String.format("%+.1f", delta) + " kWh par rapport a hier"));
            }
        }

        // 3. Production faible (batterie ne se recharge pas)
        if (rapports.size() >= 2) {
            Rapport dernier = rapports.get(rapports.size() - 1);
            double recharge = dernier.getBatterieFin() - dernier.getBatterieDebut()
                    + dernier.getConsommationTotale();
            if (recharge < 2.0) {
                alertesGenerees.add(creerAlerte("Production solaire faible: " +
                        String.format("%.1f", recharge) + " kWh - Verifier les panneaux"));
            }
        }

        // Sauvegarder les alertes
        for (Alertes alerte : alertesGenerees) {
            alertesDAO.save(alerte);
        }

        return alertesGenerees;
    }

    private Alertes creerAlerte(String message) {
        Alertes alerte = new Alertes();
        alerte.setMessageAlerte(message);
        alerte.setIdRapport("R_AUTO_" + System.currentTimeMillis());
        return alerte;
    }

    /**
     * Recupere toutes les alertes depuis la base
     */
    public List<Alertes> getToutesAlertes() throws SQLException {
        return alertesDAO.findAll();
    }

    public static void main(String[] args) {
        AlerteService service = new AlerteService();
        try {
            System.out.println("=== Verification des Alertes ===");
            List<Alertes> alertes = service.verifierEtGenererAlertes();
            System.out.println(alertes.size() + " alerte(s) generee(s):");
            for (Alertes a : alertes) {
                System.out.println("  [!] " + a.getMessageAlerte());
            }
        } catch (SQLException e) {
            System.err.println("Erreur alertes : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
