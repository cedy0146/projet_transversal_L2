package com.electrimada.modele;

import java.time.LocalDateTime;
import java.util.Objects;

public class DemandeEnergie {
    private String idDemande;
    private double quantiteKwh;
    private LocalDateTime heureSouhaitee;
    private String niveauCriticite;
    private boolean estAcceptee;
    private String idFoyer;

    // No-arg constructor
    public DemandeEnergie() {}

    // Full constructor
    public DemandeEnergie(String idDemande, double quantiteKwh, LocalDateTime heureSouhaitee, String niveauCriticite, 
                          boolean estAcceptee, String idFoyer) {
        this.idDemande = idDemande;
        this.quantiteKwh = quantiteKwh;
        this.heureSouhaitee = heureSouhaitee;
        this.niveauCriticite = niveauCriticite;
        this.estAcceptee = estAcceptee;
        this.idFoyer = idFoyer;
    }

    // Getters and Setters
    public String getIdDemande() {
        return idDemande;
    }

    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    public double getQuantiteKwh() {
        return quantiteKwh;
    }

    public void setQuantiteKwh(double quantiteKwh) {
        this.quantiteKwh = quantiteKwh;
    }

    public LocalDateTime getHeureSouhaitee() {
        return heureSouhaitee;
    }

    public void setHeureSouhaitee(LocalDateTime heureSouhaitee) {
        this.heureSouhaitee = heureSouhaitee;
    }

    public String getNiveauCriticite() {
        return niveauCriticite;
    }

    public void setNiveauCriticite(String niveauCriticite) {
        this.niveauCriticite = niveauCriticite;
    }

    public boolean isEstAcceptee() {
        return estAcceptee;
    }

    public void setEstAcceptee(boolean estAcceptee) {
        this.estAcceptee = estAcceptee;
    }

    public String getIdFoyer() {
        return idFoyer;
    }

    public void setIdFoyer(String idFoyer) {
        this.idFoyer = idFoyer;
    }

    @Override
    public String toString() {
        return "DemandeEnergie{" +
                "idDemande='" + idDemande + '\'' +
                ", quantiteKwh=" + quantiteKwh +
                ", heureSouhaitee=" + heureSouhaitee +
                ", niveauCriticite='" + niveauCriticite + '\'' +
                ", estAcceptee=" + estAcceptee +
                ", idFoyer='" + idFoyer + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DemandeEnergie that = (DemandeEnergie) o;
        return Objects.equals(idDemande, that.idDemande);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDemande);
    }
}
