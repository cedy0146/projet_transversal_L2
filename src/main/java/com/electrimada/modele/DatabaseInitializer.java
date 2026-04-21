package com.electrimada.modele;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void main(String[] args) {
        try {
            ConnexionDB.createDatabaseIfNotExists();
            System.out.println("Initialisation de la base de données...");
            
            try (Connection conn = ConnexionDB.getConnection();
                 Statement stmt = conn.createStatement()) {
                
                // Création des tables
                String[] createTables = {
                    "CREATE TABLE IF NOT EXISTS Foyer (" +
                    "id_foyer VARCHAR(50) PRIMARY KEY," +
                    "nom VARCHAR(100) NOT NULL," +
                    "type_priorite VARCHAR(50)," +
                    "consommation_moyenne DOUBLE," +
                    "jours_sans_electricite INT DEFAULT 0" +
                    ")",
                    
                    "CREATE TABLE IF NOT EXISTS Batterie (" +
                    "id_batterie INT PRIMARY KEY," +
                    "capacite_totale DOUBLE NOT NULL," +
                    "capacite_actuelle DOUBLE NOT NULL," +
                    "seuil_critique DOUBLE NOT NULL" +
                    ")",
                    
                    "CREATE TABLE IF NOT EXISTS DemandeEnergie (" +
                    "id_demande VARCHAR(50) PRIMARY KEY," +
                    "quantite_kwh DOUBLE NOT NULL," +
                    "heure_souhaitee DATETIME," +
                    "niveau_criticite VARCHAR(20)," +
                    "est_acceptee BOOLEAN DEFAULT FALSE," +
                    "id_foyer VARCHAR(50)," +
                    "FOREIGN KEY (id_foyer) REFERENCES Foyer(id_foyer) ON DELETE CASCADE" +
                    ")",
                    
                    "CREATE TABLE IF NOT EXISTS Rapport (" +
                    "id_rapport VARCHAR(50) PRIMARY KEY," +
                    "date_rapport DATETIME NOT NULL," +
                    "consommation_totale DOUBLE," +
                    "batterie_debut DOUBLE," +
                    "batterie_fin DOUBLE" +
                    ")",
                    
                    "CREATE TABLE IF NOT EXISTS SyncQueue (" +
                    "id_sync INT AUTO_INCREMENT PRIMARY KEY," +
                    "id_rapport VARCHAR(50)," +
                    "statut_envoi BOOLEAN DEFAULT FALSE," +
                    "FOREIGN KEY (id_rapport) REFERENCES Rapport(id_rapport) ON DELETE CASCADE" +
                    ")",
                    
                    "CREATE TABLE IF NOT EXISTS Deltas (" +
                    "id_delta INT AUTO_INCREMENT PRIMARY KEY," +
                    "id_rapport VARCHAR(50)," +
                    "valeur_delta DOUBLE," +
                    "FOREIGN KEY (id_rapport) REFERENCES Rapport(id_rapport) ON DELETE CASCADE" +
                    ")",
                    
                    "CREATE TABLE IF NOT EXISTS Alertes (" +
                    "id_alerte INT AUTO_INCREMENT PRIMARY KEY," +
                    "message_alerte TEXT," +
                    "id_rapport VARCHAR(50)," +
                    "FOREIGN KEY (id_rapport) REFERENCES Rapport(id_rapport) ON DELETE CASCADE" +
                    ")"
                };

                for (String sql : createTables) {
                    stmt.execute(sql);
                }

                // Insertions de données de test
                stmt.execute("INSERT IGNORE INTO Foyer (id_foyer, nom, type_priorite, consommation_moyenne, jours_sans_electricite) VALUES " +
                    "('F001', 'Foyer Razafy', 'Prioritaire', 5.5, 0)," +
                    "('F002', 'Foyer Rakoto', 'Standard', 3.2, 1)," +
                    "('F003', 'Foyer Manou', 'Eco', 2.1, 0)");

                stmt.execute("INSERT IGNORE INTO Batterie (id_batterie, capacite_totale, capacite_actuelle, seuil_critique) VALUES " +
                    "(1, 100.0, 75.0, 20.0)");

                stmt.execute("INSERT IGNORE INTO DemandeEnergie (id_demande, quantite_kwh, heure_souhaitee, niveau_criticite, est_acceptee, id_foyer) VALUES " +
                    "('D001', 2.5, '2026-04-19 18:00:00', 'Haute', TRUE, 'F001')," +
                    "('D002', 1.0, '2026-04-19 19:30:00', 'Basse', FALSE, 'F002')," +
                    "('D003', 4.0, '2026-04-19 20:00:00', 'Critique', TRUE, 'F001')," +
                    "('D004', 1.5, '2026-04-19 21:00:00', 'Normale', TRUE, 'F003')," +
                    "('D005', 0.8, '2026-04-19 22:00:00', 'Basse', FALSE, 'F002')");

                stmt.execute("INSERT IGNORE INTO Rapport (id_rapport, date_rapport, consommation_totale, batterie_debut, batterie_fin) VALUES " +
                    "('R001', '2026-04-17 23:59:00', 10.5, 98.0, 88.0)," +
                    "('R002', '2026-04-18 23:59:00', 9.2, 88.0, 78.0)," +
                    "('R003', '2026-04-19 23:59:00', 7.5, 78.0, 75.0)");

                stmt.execute("INSERT IGNORE INTO SyncQueue (id_sync, id_rapport, statut_envoi) VALUES " +
                    "(1, 'R001', TRUE)," +
                    "(2, 'R002', TRUE)," +
                    "(3, 'R003', FALSE)");

                stmt.execute("INSERT IGNORE INTO Deltas (id_delta, id_rapport, valeur_delta) VALUES " +
                    "(1, 'R001', 0.2)," +
                    "(2, 'R002', -1.3)," +
                    "(3, 'R003', -1.7)");

                stmt.execute("INSERT IGNORE INTO Alertes (id_alerte, message_alerte, id_rapport) VALUES " +
                    "(1, 'Production solaire optimale', 'R001')," +
                    "(2, 'Consommation élevée détectée', 'R002')," +
                    "(3, 'Seuil de batterie atteint - Mode Eco activé', 'R003')");

                System.out.println("✅ Base de données initialisée avec succès ! 8 tables + données de test.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur initialisation: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
