package com.electrimada.dao;

import com.electrimada.modele.Alertes;
import com.electrimada.modele.ConnexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlertesDAO {
    
    public List<Alertes> findAll() throws SQLException {
        List<Alertes> alertesList = new ArrayList<>();
        String sql = "SELECT * FROM Alertes";
        
        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Alertes alerte = new Alertes();
                alerte.setIdAlerte(rs.getInt("id_alerte"));
                alerte.setMessageAlerte(rs.getString("message_alerte"));
                alerte.setIdRapport(rs.getString("id_rapport"));
                alertesList.add(alerte);
            }
        }
        return alertesList;
    }
    
    public Alertes findById(int idAlerte) throws SQLException {
        String sql = "SELECT * FROM Alertes WHERE id_alerte = ?";
        
        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idAlerte);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Alertes alerte = new Alertes();
                    alerte.setIdAlerte(rs.getInt("id_alerte"));
                    alerte.setMessageAlerte(rs.getString("message_alerte"));
                    alerte.setIdRapport(rs.getString("id_rapport"));
                    return alerte;
                }
            }
        }
        return null;
    }
    
    public List<Alertes> findByRapportId(String idRapport) throws SQLException {
        List<Alertes> alertesList = new ArrayList<>();
        String sql = "SELECT * FROM Alertes WHERE id_rapport = ?";
        
        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, idRapport);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Alertes alerte = new Alertes();
                    alerte.setIdAlerte(rs.getInt("id_alerte"));
                    alerte.setMessageAlerte(rs.getString("message_alerte"));
                    alerte.setIdRapport(rs.getString("id_rapport"));
                    alertesList.add(alerte);
                }
            }
        }
        return alertesList;
    }
    
    public void save(Alertes alerte) throws SQLException {
        String sql = "INSERT INTO Alertes (message_alerte, id_rapport) VALUES (?, ?)";
        
        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, alerte.getMessageAlerte());
            pstmt.setString(2, alerte.getIdRapport());
            pstmt.executeUpdate();
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    alerte.setIdAlerte(generatedKeys.getInt(1));
                }
            }
        }
    }
    
    public void delete(int idAlerte) throws SQLException {
        String sql = "DELETE FROM Alertes WHERE id_alerte = ?";
        
        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idAlerte);
            pstmt.executeUpdate();
        }
    }
    
    // Test main
    public static void main(String[] args) {
        AlertesDAO dao = new AlertesDAO();
        try {
            System.out.println("=== All Alertes ===");
            List<Alertes> alertesList = dao.findAll();
            for (Alertes a : alertesList) {
                System.out.println(a);
            }
            
            System.out.println("\n=== Alertes for R003 ===");
            List<Alertes> rapportAlertes = dao.findByRapportId("R003");
            for (Alertes a : rapportAlertes) {
                System.out.println(a);
            }
            
        } catch (SQLException e) {
            System.err.println("DAO Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

