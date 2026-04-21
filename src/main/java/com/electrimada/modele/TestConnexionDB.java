package com.electrimada.modele;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class TestConnexionDB {
    public static void main(String[] args) {
        try {
            ConnexionDB.createDatabaseIfNotExists();
            System.out.println("Base de données ElectiMadaDB créée ou vérifiée.");

            try (Connection conn = ConnexionDB.getConnection()) {
                System.out.println("Connexion à la base de données réussie !");
                System.out.println("DB: " + conn.getCatalog());
                
                // List all tables
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SHOW TABLES")) {
                    System.out.println("\nTables dans la base de données:");
                    int count = 0;
                    while (rs.next()) {
                        System.out.println("- " + rs.getString(1));
                        count++;
                    }
                    if (count == 0) {
                        System.out.println("Aucune table trouvée. Créez-en une !");
                    } else {
                        System.out.println("Total tables: " + count);
                    }
                } catch (SQLException tableEx) {
                    System.err.println("Erreur lors de la liste des tables: " + tableEx.getMessage());
                }
            }
        } catch (SQLException e) {
            System.err.println("Échec de connexion ou création DB: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
