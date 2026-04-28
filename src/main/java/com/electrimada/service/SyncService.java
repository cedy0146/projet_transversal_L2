package com.electrimada.service;

import com.electrimada.dao.RapportDAO;
import com.electrimada.dao.SyncQueueDAO;
import com.electrimada.modele.Rapport;
import com.electrimada.modele.SyncQueue;

import java.sql.SQLException;
import java.util.List;

/**
 * SyncService - Mode offline-first et synchronisation opportuniste
 * Stocke localement tous les rapports, les synchronise quand une connexion est disponible.
 * Respecte la contrainte C1 : Connectivite intermittente.
 */
public class SyncService {

    private final SyncQueueDAO syncQueueDAO;
    private final RapportDAO rapportDAO;

    public SyncService() {
        this.syncQueueDAO = new SyncQueueDAO();
        this.rapportDAO = new RapportDAO();
    }

    /**
     * Ajoute un rapport a la file d'attente de synchronisation
     */
    public void queueRapport(String idRapport) throws SQLException {
        SyncQueue queue = new SyncQueue();
        queue.setIdRapport(idRapport);
        queue.setStatutEnvoi(false);
        syncQueueDAO.save(queue);
    }

    /**
     * Tente d'envoyer les rapports en attente.
     * Si succes, marque comme envoye dans la base.
     * Retourne le nombre de rapports synchronises.
     */
    public int synchroniser() throws SQLException {
        List<SyncQueue> aEnvoyer = syncQueueDAO.findUnsent();
        int succes = 0;

        for (SyncQueue item : aEnvoyer) {
            // Simulation de l'envoi (dans la vraie vie : appel API ou email)
            boolean envoiReussi = simulerEnvoi(item.getIdRapport());

            if (envoiReussi) {
                item.setStatutEnvoi(true);
                syncQueueDAO.update(item);
                succes++;
            }
        }

        return succes;
    }

    /**
     * Simulation d'envoi de rapport (fallback si pas de connexion)
     */
    private boolean simulerEnvoi(String idRapport) {
        // Dans un vrai scenario : tester la connectivite, puis envoyer
        // Ici on simule une reussite aleatoire pour la demo
        return Math.random() > 0.3; // 70% de chance de succes
    }

    /**
     * Retourne le nombre de rapports en attente
     */
    public int getNombreEnAttente() throws SQLException {
        return syncQueueDAO.findUnsent().size();
    }

    /**
     * Recupere tous les rapports non envoyes avec leurs details
     */
    public List<SyncQueue> getRapportsEnAttente() throws SQLException {
        return syncQueueDAO.findUnsent();
    }

    public static void main(String[] args) {
        SyncService service = new SyncService();
        try {
            System.out.println("=== SyncService (Offline-First) ===");

            int enAttente = service.getNombreEnAttente();
            System.out.println("Rapports en attente : " + enAttente);

            int synchronises = service.synchroniser();
            System.out.println("Rapports synchronises : " + synchronises);

            System.out.println("Reste en attente : " + service.getNombreEnAttente());

        } catch (SQLException e) {
            System.err.println("Erreur sync : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
