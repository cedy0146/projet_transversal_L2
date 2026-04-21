package com.electrimada.dao;

import com.electrimada.modele.Batterie;
import com.electrimada.modele.ConnexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BatterieDAO {
    
    public List<Batterie> findAll() throws SQLException {
        List<Batterie> batteries = new ArrayList<>();
        String sql = "SELECT * FROM Batterie";
        
        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Batterie batterie = new Batterie();
                batterie.setIdBatterie(rs.getInt("id_batterie"));
                batterie.setCapaciteTotale(rs.getDouble("capacite_totale"));
                batterie.setCapaciteActuelle(rs.getDouble("capacite_actuelle"));
                batterie.setSeuilCritique(rs.getDouble("seuil_critique"));
                batteries.add(batterie);
            }
        }
        return batteries;
    }
    
    public Batterie findById(int idBatterie) throws SQLException {
        String sql = "SELECT * FROM Batterie WHERE id_batterie = ?";
        
        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idBatterie);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Batterie batterie = new Batterie();
                    batterie.setIdBatterie(rs.getInt("id_batterie"));
                    batterie.setCapaciteTotale(rs.getDouble("capacite_totale"));
                    batterie.setCapaciteActuelle(rs.getDouble("capacite_actuelle"));
                    batterie.setSeuilCritique(rs.getDouble("seuil_critique"));
                    return batterie;
                }
            }
        }
        return null;
    }
    
    public void save(Batterie batterie) throws SQLException {
        String sql = "INSERT INTO Batterie (id_batterie, capacite_totale, capacite_actuelle, seuil_critique) VALUES (?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE capacite_totale=VALUES(capacite_totale), capacite_actuelle=VALUES(capacite_actuelle), " +
                     "seuil_critique=VALUES(seuil_critique)";
        
        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, batterie.getIdBatterie());
            pstmt.setDouble(2, batterie.getCapaciteTotale());
            pstmt.setDouble(3, batterie.getCapaciteActuelle());
            pstmt.setDouble(4, batterie.getSeuilCritique());
            pstmt.executeUpdate();
        }
    }
    
    public void delete(int idBatterie) throws SQLException {
        String sql = "DELETE FROM Batterie WHERE id_batterie = ?";
        
        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idBatterie);
            pstmt.executeUpdate();
        }
    }
    
    // Test main
    public static void main(String[] args) {
        BatterieDAO dao = new BatterieDAO();
        try {
            System.out.println("=== All Batteries ===");
            List<Batterie> batteries = dao.findAll();
            for (Batterie b : batteries) {
                System.out.println(b);
            }
            
            System.out.println("\n=== Batterie ID 1 ===");
            Batterie batterie = dao.findById(1);
            System.out.println(batterie != null ? batterie : "Not found");
            
        } catch (SQLException e) {
            System.err.println("DAO Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
