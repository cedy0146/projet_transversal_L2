package com.electrimada.modele;

import java.util.Date;

public class Rapport {
    private String idRapport;
    private Date dateRapport;
    private double consommationTotale;
    private double batterieDebut;
    private double batterieFin;

    // No-arg constructor
    public Rapport() {}

    // Full constructor
    public Rapport(String idRapport, Date dateRapport, double consommationTotale, double batterieDebut, double batterieFin) {
        this.idRapport = idRapport;
        this.dateRapport = dateRapport;
        this.consommationTotale = consommationTotale;
        this.batterieDebut = batterieDebut;
        this.batterieFin = batterieFin;
    }

    // Getters and Setters
    public String getIdRapport() {
        return idRapport;
    }

    public void setIdRapport(String idRapport) {
        this.idRapport = idRapport;
    }

    public Date getDateRapport() {
        return dateRapport;
    }

    public void setDateRapport(Date dateRapport) {
        this.dateRapport = dateRapport;
    }

    public double getConsommationTotale() {
        return consommationTotale;
    }

    public void setConsommationTotale(double consommationTotale) {
        this.consommationTotale = consommationTotale;
    }

    public double getBatterieDebut() {
        return batterieDebut;
    }

    public void setBatterieDebut(double batterieDebut) {
        this.batterieDebut = batterieDebut;
    }

    public double getBatterieFin() {
        return batterieFin;
    }

    public void setBatterieFin(double batterieFin) {
        this.batterieFin = batterieFin;
    }

    @Override
    public String toString() {
        return "Rapport{" +
                "idRapport='" + idRapport + '\'' +
                ", dateRapport=" + dateRapport +
                ", consommationTotale=" + consommationTotale +
                ", batterieDebut=" + batterieDebut +
                ", batterieFin=" + batterieFin +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rapport rapport = (Rapport) o;
        return idRapport != null ? idRapport.equals(rapport.idRapport) : rapport.idRapport == null;
    }

    @Override
    public int hashCode() {
        return idRapport != null ? idRapport.hashCode() : 0;
    }
}
