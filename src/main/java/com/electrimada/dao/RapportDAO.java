package com.electrimada.dao;

import com.electrimada.modele.Rapport;
import com.electrimada.modele.ConnexionDB;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RapportDAO {
    
    public List<Rapport> findAll() throws SQLException {
        List<Rapport> rapports = new ArrayList<>();
        String sql = "SELECT * FROM Rapport";
        
        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Rapport rapport = new Rapport();
                rapport.setIdRapport(rs.getString("id_rapport"));
                Timestamp ts = rs.getTimestamp("date_rapport");
                rapport.setDateRapport(ts != null ? ts.toLocalDateTime() : null);
                rapport.setConsommationTotale(rs.getDouble("consommation_totale"));
                rapport.setBatterieDebut(rs.getDouble("batterie_debut"));
                rapport.setBatterieFin(rs.getDouble("batterie_fin"));
                rapports.add(rapport);
            }
        }
        return rapports;
    }
    
    public Rapport findById(String idRapport) throws SQLException {
        String sql = "SELECT * FROM Rapport WHERE id_rapport = ?";
        
        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, idRapport);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Rapport rapport = new Rapport();
                    rapport.setIdRapport(rs.getString("id_rapport"));
                    Timestamp ts = rs.getTimestamp("date_rapport");
                    rapport.setDateRapport(ts != null ? ts.toLocalDateTime() : null);
                    rapport.setConsommationTotale(rs.getDouble("consommation_totale"));
                    rapport.setBatterieDebut(rs.getDouble("batterie_debut"));
                    rapport.setBatterieFin(rs.getDouble("batterie_fin"));
                    return rapport;
                }
            }
        }
        return null;
    }
    
    public void save(Rapport rapport) throws SQLException {
        String sql = "INSERT INTO Rapport (id_rapport, date_rapport, consommation_totale, batterie_debut, batterie_fin) VALUES (?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE date_rapport=VALUES(date_rapport), consommation_totale=VALUES(consommation_totale), " +
                     "batterie_debut=VALUES(batterie_debut), batterie_fin=VALUES(batterie_fin)";
        
        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, rapport.getIdRapport());
            pstmt.setTimestamp(2, rapport.getDateRapport() != null ? Timestamp.valueOf(rapport.getDateRapport()) : null);
            pstmt.setDouble(3, rapport.getConsommationTotale());
            pstmt.setDouble(4, rapport.getBatterieDebut());
            pstmt.setDouble(5, rapport.getBatterieFin());
            pstmt.executeUpdate();
        }
    }
    
    public void delete(String idRapport) throws SQLException {
        String sql = "DELETE FROM Rapport WHERE id_rapport = ?";
        
        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, idRapport);
            pstmt.executeUpdate();
        }
    }
    
    // Test main
    public static void main(String[] args) {
        RapportDAO dao = new RapportDAO();
        try {
            System.out.println("=== All Rapports ===");
            List<Rapport> rapports = dao.findAll();
            for (Rapport r : rapports) {
                System.out.println(r);
            }
            
            System.out.println("\n=== Rapport R001 ===");
            Rapport rapport = dao.findById("R001");
            System.out.println(rapport != null ? rapport : "Not found");
            
        } catch (SQLException e) {
            System.err.println("DAO Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
