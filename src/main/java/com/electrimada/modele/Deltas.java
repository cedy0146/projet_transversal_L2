package com.electrimada.modele;

public class Deltas {
    private int idDelta;
    private String idRapport;
    private double valeurDelta;

    // No-arg constructor
    public Deltas() {}

    // Full constructor
    public Deltas(int idDelta, String idRapport, double valeurDelta) {
        this.idDelta = idDelta;
        this.idRapport = idRapport;
        this.valeurDelta = valeurDelta;
    }

    // Getters and Setters
    public int getIdDelta() {
        return idDelta;
    }

    public void setIdDelta(int idDelta) {
        this.idDelta = idDelta;
    }

    public String getIdRapport() {
        return idRapport;
    }

    public void setIdRapport(String idRapport) {
        this.idRapport = idRapport;
    }

    public double getValeurDelta() {
        return valeurDelta;
    }

    public void setValeurDelta(double valeurDelta) {
        this.valeurDelta = valeurDelta;
    }

    @Override
    public String toString() {
        return "Deltas{" +
                "idDelta=" + idDelta +
                ", idRapport='" + idRapport + '\'' +
                ", valeurDelta=" + valeurDelta +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Deltas deltas = (Deltas) o;
        return idDelta == deltas.idDelta;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(idDelta);
    }
}
