package com.electrimada.modele;

public class Alertes {
    private int idAlerte;
    private String messageAlerte;
    private String idRapport;

    // No-arg constructor
    public Alertes() {}

    // Full constructor
    public Alertes(int idAlerte, String messageAlerte, String idRapport) {
        this.idAlerte = idAlerte;
        this.messageAlerte = messageAlerte;
        this.idRapport = idRapport;
    }

    // Getters and Setters
    public int getIdAlerte() {
        return idAlerte;
    }

    public void setIdAlerte(int idAlerte) {
        this.idAlerte = idAlerte;
    }

    public String getMessageAlerte() {
        return messageAlerte;
    }

    public void setMessageAlerte(String messageAlerte) {
        this.messageAlerte = messageAlerte;
    }

    public String getIdRapport() {
        return idRapport;
    }

    public void setIdRapport(String idRapport) {
        this.idRapport = idRapport;
    }

    @Override
    public String toString() {
        return "Alertes{" +
                "idAlerte=" + idAlerte +
                ", messageAlerte='" + messageAlerte + '\'' +
                ", idRapport='" + idRapport + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alertes alertes = (Alertes) o;
        return idAlerte == alertes.idAlerte;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(idAlerte);
    }
}
