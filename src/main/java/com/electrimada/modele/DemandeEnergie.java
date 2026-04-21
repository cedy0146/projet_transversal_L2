package com.electrimada.modele;

import java.util.Date;

public class DemandeEnergie {
    private String idDemande;
    private double quantiteKwh;
    private Date heureSouhaitee;
    private String niveauCriticite;
    private boolean estAcceptee;
    private String idFoyer;

    // No-arg constructor
    public DemandeEnergie() {}

    // Full constructor
    public DemandeEnergie(String idDemande, double quantiteKwh, Date heureSouhaitee, String niveauCriticite, 
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

    public Date getHeureSouhaitee() {
        return heureSouhaitee;
    }

    public void setHeureSouhaitee(Date heureSouhaitee) {
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
        return idDemande != null ? idDemande.equals(that.idDemande) : that.idDemande == null;
    }

    @Override
    public int hashCode() {
        return idDemande != null ? idDemande.hashCode() : 0;
    }
}
