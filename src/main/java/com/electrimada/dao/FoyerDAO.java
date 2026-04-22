package com.electrimada.dao;

import com.electrimada.modele.Foyer;
import com.electrimada.modele.ConnexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FoyerDAO {
    
    public List<Foyer> findAll() throws SQLException {
        List<Foyer> foyers = new ArrayList<>();
        String sql = "SELECT * FROM Foyer";
        
        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Foyer foyer = new Foyer();
                foyer.setIdFoyer(rs.getString("id_foyer"));
                foyer.setNom(rs.getString("nom"));
                foyer.setTypePriorite(rs.getString("type_priorite"));
                foyer.setConsommationMoyenne(rs.getDouble("consommation_moyenne"));
                foyer.setJoursSansElectricite(rs.getInt("jours_sans_electricite"));
                foyers.add(foyer);
            }
        }
        return foyers;
    }
    
    public Foyer findById(String idFoyer) throws SQLException {
        String sql = "SELECT * FROM Foyer WHERE id_foyer = ?";
        
        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, idFoyer);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Foyer foyer = new Foyer();
                    foyer.setIdFoyer(rs.getString("id_foyer"));
                    foyer.setNom(rs.getString("nom"));
                    foyer.setTypePriorite(rs.getString("type_priorite"));
                    foyer.setConsommationMoyenne(rs.getDouble("consommation_moyenne"));
                    foyer.setJoursSansElectricite(rs.getInt("jours_sans_electricite"));
                    return foyer;
                }
            }
        }
        return null;
    }
    
    public void save(Foyer foyer) throws SQLException {
        String sql = "INSERT INTO Foyer (id_foyer, nom, type_priorite, consommation_moyenne, jours_sans_electricite) VALUES (?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE nom=VALUES(nom), type_priorite=VALUES(type_priorite), " +
                     "consommation_moyenne=VALUES(consommation_moyenne), jours_sans_electricite=VALUES(jours_sans_electricite)";
        
        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, foyer.getIdFoyer());
            pstmt.setString(2, foyer.getNom());
            pstmt.setString(3, foyer.getTypePriorite());
            pstmt.setDouble(4, foyer.getConsommationMoyenne());
            pstmt.setInt(5, foyer.getJoursSansElectricite());
            pstmt.executeUpdate();
        }
    }
    
    public void delete(String idFoyer) throws SQLException {
        String sql = "DELETE FROM Foyer WHERE id_foyer = ?";
        
        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, idFoyer);
            pstmt.executeUpdate();
        }
    }
    
    // Test main
    public static void main(String[] args) {
        FoyerDAO dao = new FoyerDAO();
        try {
            System.out.println("=== All Foyers ===");
            List<Foyer> foyers = dao.findAll();
            for (Foyer f : foyers) {
                System.out.println(f);
            }
            
            System.out.println("\n=== Foyer F001 ===");
            Foyer foyer = dao.findById("F001");
            System.out.println(foyer != null ? foyer : "Not found");
            
        } catch (SQLException e) {
            System.err.println("DAO Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
