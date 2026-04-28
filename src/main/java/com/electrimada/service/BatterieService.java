package com.electrimada.service;

import com.electrimada.dao.BatterieDAO;
import com.electrimada.modele.Batterie;

import java.sql.SQLException;
import java.util.List;

/**
 * BatterieService - Gestion du stockage et etat de charge (SoC)
 * Suivi de la batterie partagee, estimation de duree restante,
 * regles pour eviter la decharge profonde.
 */
public class BatterieService {

    private final BatterieDAO batterieDAO;
    private static final double SEUIL_CRITIQUE_DEFAUT = 20.0; // %

    public BatterieService() {
        this.batterieDAO = new BatterieDAO();
    }

    /**
     * Retourne l'etat actuel de la batterie depuis la base de donnees
     */
    public Batterie getEtatBatterie() throws SQLException {
        List<Batterie> batteries = batterieDAO.findAll();
        return batteries.isEmpty() ? null : batteries.get(0);
    }

    /**
     * Calcule le pourcentage de charge actuel
     */
    public double getPourcentageCharge(Batterie batterie) {
        if (batterie == null || batterie.getCapaciteTotale() == 0) return 0.0;
        return (batterie.getCapaciteActuelle() / batterie.getCapaciteTotale()) * 100.0;
    }

    /**
     * Estime la duree restante en heures selon la consommation moyenne
     */
    public double estimerDureeRestante(Batterie batterie, double consommationMoyenne) {
        if (batterie == null || consommationMoyenne <= 0) return 0.0;
        return batterie.getCapaciteActuelle() / consommationMoyenne;
    }

    /**
     * Verifie si la batterie est en dessous du seuil critique
     */
    public boolean estSeuilCritique(Batterie batterie) {
        if (batterie == null) return true;
        return getPourcentageCharge(batterie) <= batterie.getSeuilCritique();
    }

    /**
     * Verifie si la batterie est tres basse (mode eco force)
     */
    public boolean estModeEcoForce(Batterie batterie) {
        if (batterie == null) return true;
        return getPourcentageCharge(batterie) <= 10.0;
    }

    /**
     * Consomme de l'energie et met a jour la batterie dans la base
     */
    public boolean consommer(double kwh) throws SQLException {
        Batterie batterie = getEtatBatterie();
        if (batterie == null) return false;

        double nouvelleCapacite = batterie.getCapaciteActuelle() - kwh;
        if (nouvelleCapacite < 0) return false; // Pas assez d'energie

        batterie.setCapaciteActuelle(nouvelleCapacite);
        batterieDAO.save(batterie);
        return true;
    }

    /**
     * Recharge la batterie (apres production solaire)
     */
    public void recharger(double kwh) throws SQLException {
        Batterie batterie = getEtatBatterie();
        if (batterie == null) return;

        double nouvelleCapacite = Math.min(
                batterie.getCapaciteTotale(),
                batterie.getCapaciteActuelle() + kwh
        );
        batterie.setCapaciteActuelle(nouvelleCapacite);
        batterieDAO.save(batterie);
    }

    /**
     * Verifie qu'il reste assez pour le lendemain matin (reserve strategique)
     */
    public boolean verifierReserveMatin(Batterie batterie, double besoinEstime) {
        if (batterie == null) return false;
        return batterie.getCapaciteActuelle() >= besoinEstime;
    }

    public static void main(String[] args) {
        BatterieService service = new BatterieService();
        try {
            Batterie bat = service.getEtatBatterie();
            if (bat != null) {
                System.out.println("=== Etat Batterie ===");
                System.out.println(bat);
                System.out.println("Pourcentage : " + String.format("%.1f", service.getPourcentageCharge(bat)) + "%");
                System.out.println("Duree restante (1.5kWh/h) : " +
                        String.format("%.1f", service.estimerDureeRestante(bat, 1.5)) + "h");
                System.out.println("Seuil critique : " + service.estSeuilCritique(bat));
                System.out.println("Mode eco force : " + service.estModeEcoForce(bat));
            }
        } catch (SQLException e) {
            System.err.println("Erreur batterie : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
