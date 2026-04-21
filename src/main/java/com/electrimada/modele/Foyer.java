package com.electrimada.modele;

public class Foyer {
    private String idFoyer;
    private String nom;
    private String typePriorite;
    private double consommationMoyenne;
    private int joursSansElectricite;

    // No-arg constructor
    public Foyer() {}

    // Full constructor
    public Foyer(String idFoyer, String nom, String typePriorite, double consommationMoyenne, int joursSansElectricite) {
        this.idFoyer = idFoyer;
        this.nom = nom;
        this.typePriorite = typePriorite;
        this.consommationMoyenne = consommationMoyenne;
        this.joursSansElectricite = joursSansElectricite;
    }

    // Getters and Setters
    public String getIdFoyer() {
        return idFoyer;
    }

    public void setIdFoyer(String idFoyer) {
        this.idFoyer = idFoyer;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getTypePriorite() {
        return typePriorite;
    }

    public void setTypePriorite(String typePriorite) {
        this.typePriorite = typePriorite;
    }

    public double getConsommationMoyenne() {
        return consommationMoyenne;
    }

    public void setConsommationMoyenne(double consommationMoyenne) {
        this.consommationMoyenne = consommationMoyenne;
    }

    public int getJoursSansElectricite() {
        return joursSansElectricite;
    }

    public void setJoursSansElectricite(int joursSansElectricite) {
        this.joursSansElectricite = joursSansElectricite;
    }

    @Override
    public String toString() {
        return "Foyer{" +
                "idFoyer='" + idFoyer + '\'' +
                ", nom='" + nom + '\'' +
                ", typePriorite='" + typePriorite + '\'' +
                ", consommationMoyenne=" + consommationMoyenne +
                ", joursSansElectricite=" + joursSansElectricite +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Foyer foyer = (Foyer) o;
        return idFoyer != null ? idFoyer.equals(foyer.idFoyer) : foyer.idFoyer == null;
    }

@Override
    public int hashCode() {
        return idFoyer != null ? idFoyer.hashCode() : 0;
    }

}
