package com.electrimada.modele;

public class SyncQueue {
    private int idSync;
    private String idRapport;
    private boolean statutEnvoi;

    // No-arg constructor
    public SyncQueue() {}

    // Full constructor
    public SyncQueue(int idSync, String idRapport, boolean statutEnvoi) {
        this.idSync = idSync;
        this.idRapport = idRapport;
        this.statutEnvoi = statutEnvoi;
    }

    // Getters and Setters
    public int getIdSync() {
        return idSync;
    }

    public void setIdSync(int idSync) {
        this.idSync = idSync;
    }

    public String getIdRapport() {
        return idRapport;
    }

    public void setIdRapport(String idRapport) {
        this.idRapport = idRapport;
    }

    public boolean isStatutEnvoi() {
        return statutEnvoi;
    }

    public void setStatutEnvoi(boolean statutEnvoi) {
        this.statutEnvoi = statutEnvoi;
    }

    @Override
    public String toString() {
        return "SyncQueue{" +
                "idSync=" + idSync +
                ", idRapport='" + idRapport + '\'' +
                ", statutEnvoi=" + statutEnvoi +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SyncQueue syncQueue = (SyncQueue) o;
        return idSync == syncQueue.idSync;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(idSync);
    }
}
