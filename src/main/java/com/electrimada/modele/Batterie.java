package com.electrimada.modele;

public class Batterie {
    private int idBatterie;
    private double capaciteTotale;
    private double capaciteActuelle;
    private double seuilCritique;

    // No-arg constructor
    public Batterie() {}

    // Full constructor
    public Batterie(int idBatterie, double capaciteTotale, double capaciteActuelle, double seuilCritique) {
        this.idBatterie = idBatterie;
        this.capaciteTotale = capaciteTotale;
        this.capaciteActuelle = capaciteActuelle;
        this.seuilCritique = seuilCritique;
    }

    // Getters and Setters
    public int getIdBatterie() {
        return idBatterie;
    }

    public void setIdBatterie(int idBatterie) {
        this.idBatterie = idBatterie;
    }

    public double getCapaciteTotale() {
        return capaciteTotale;
    }

    public void setCapaciteTotale(double capaciteTotale) {
        this.capaciteTotale = capaciteTotale;
    }

    public double getCapaciteActuelle() {
        return capaciteActuelle;
    }

    public void setCapaciteActuelle(double capaciteActuelle) {
        this.capaciteActuelle = capaciteActuelle;
    }

    public double getSeuilCritique() {
        return seuilCritique;
    }

    public void setSeuilCritique(double seuilCritique) {
        this.seuilCritique = seuilCritique;
    }

    @Override
    public String toString() {
        return "Batterie{" +
                "idBatterie=" + idBatterie +
                ", capaciteTotale=" + capaciteTotale +
                ", capaciteActuelle=" + capaciteActuelle +
                ", seuilCritique=" + seuilCritique +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Batterie batterie = (Batterie) o;
        return idBatterie == batterie.idBatterie;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(idBatterie);
    }
}
