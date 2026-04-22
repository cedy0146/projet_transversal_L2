package com.electrimada.dao;

import com.electrimada.modele.DemandeEnergie;
import com.electrimada.modele.ConnexionDB;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DemandeEnergieDAO {
    
    public List<DemandeEnergie> findAll() throws SQLException {
        List<DemandeEnergie> demandes = new ArrayList<>();
        String sql = "SELECT * FROM DemandeEnergie";
        
        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                DemandeEnergie demande = new DemandeEnergie();
                demande.setIdDemande(rs.getString("id_demande"));
                demande.setQuantiteKwh(rs.getDouble("quantite_kwh"));
                Timestamp ts = rs.getTimestamp("heure_souhaitee");
                demande.setHeureSouhaitee(ts != null ? ts.toLocalDateTime() : null);
                demande.setNiveauCriticite(rs.getString("niveau_criticite"));
                demande.setEstAcceptee(rs.getBoolean("est_acceptee"));
                demande.setIdFoyer(rs.getString("id_foyer"));
                demandes.add(demande);
            }
        }
        return demandes;
    }
    
    public DemandeEnergie findById(String idDemande) throws SQLException {
        String sql = "SELECT * FROM DemandeEnergie WHERE id_demande = ?";
        
        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, idDemande);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    DemandeEnergie demande = new DemandeEnergie();
                    demande.setIdDemande(rs.getString("id_demande"));
                    demande.setQuantiteKwh(rs.getDouble("quantite_kwh"));
                    Timestamp ts = rs.getTimestamp("heure_souhaitee");
                    demande.setHeureSouhaitee(ts != null ? ts.toLocalDateTime() : null);
                    demande.setNiveauCriticite(rs.getString("niveau_criticite"));
                    demande.setEstAcceptee(rs.getBoolean("est_acceptee"));
                    demande.setIdFoyer(rs.getString("id_foyer"));
                    return demande;
                }
            }
        }
        return null;
    }
    
    public void save(DemandeEnergie demande) throws SQLException {
        String sql = "INSERT INTO DemandeEnergie (id_demande, quantite_kwh, heure_souhaitee, niveau_criticite, est_acceptee, id_foyer) VALUES (?, ?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE quantite_kwh=VALUES(quantite_kwh), heure_souhaitee=VALUES(heure_souhaitee), " +
                     "niveau_criticite=VALUES(niveau_criticite), est_acceptee=VALUES(est_acceptee), id_foyer=VALUES(id_foyer)";
        
        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, demande.getIdDemande());
            pstmt.setDouble(2, demande.getQuantiteKwh());
            pstmt.setTimestamp(3, demande.getHeureSouhaitee() != null ? Timestamp.valueOf(demande.getHeureSouhaitee()) : null);
            pstmt.setString(4, demande.getNiveauCriticite());
            pstmt.setBoolean(5, demande.isEstAcceptee());
            pstmt.setString(6, demande.getIdFoyer());
            pstmt.executeUpdate();
        }
    }
    
    public void delete(String idDemande) throws SQLException {
        String sql = "DELETE FROM DemandeEnergie WHERE id_demande = ?";
        
        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, idDemande);
            pstmt.executeUpdate();
        }
    }
    
    // Test main
    public static void main(String[] args) {
        DemandeEnergieDAO dao = new DemandeEnergieDAO();
        try {
            System.out.println("=== All Demandes ===");
            List<DemandeEnergie> demandes = dao.findAll();
            for (DemandeEnergie d : demandes) {
                System.out.println(d);
            }
            
            System.out.println("\n=== Demande D001 ===");
            DemandeEnergie demande = dao.findById("D001");
            System.out.println(demande != null ? demande : "Not found");
            
        } catch (SQLException e) {
            System.err.println("DAO Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
