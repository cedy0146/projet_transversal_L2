package com.electrimada.dao;

import com.electrimada.modele.Deltas;
import com.electrimada.modele.ConnexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeltasDAO {
    
    public List<Deltas> findAll() throws SQLException {
        List<Deltas> deltasList = new ArrayList<>();
        String sql = "SELECT * FROM Deltas";
        
        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Deltas delta = new Deltas();
                delta.setIdDelta(rs.getInt("id_delta"));
                delta.setIdRapport(rs.getString("id_rapport"));
                delta.setValeurDelta(rs.getDouble("valeur_delta"));
                deltasList.add(delta);
            }
        }
        return deltasList;
    }
    
    public Deltas findById(int idDelta) throws SQLException {
        String sql = "SELECT * FROM Deltas WHERE id_delta = ?";
        
        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idDelta);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Deltas delta = new Deltas();
                    delta.setIdDelta(rs.getInt("id_delta"));
                    delta.setIdRapport(rs.getString("id_rapport"));
                    delta.setValeurDelta(rs.getDouble("valeur_delta"));
                    return delta;
                }
            }
        }
        return null;
    }
    
    public List<Deltas> findByRapportId(String idRapport) throws SQLException {
        List<Deltas> deltasList = new ArrayList<>();
        String sql = "SELECT * FROM Deltas WHERE id_rapport = ?";
        
        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, idRapport);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Deltas delta = new Deltas();
                    delta.setIdDelta(rs.getInt("id_delta"));
                    delta.setIdRapport(rs.getString("id_rapport"));
                    delta.setValeurDelta(rs.getDouble("valeur_delta"));
                    deltasList.add(delta);
                }
            }
        }
        return deltasList;
    }
    
    public void save(Deltas delta) throws SQLException {
        String sql = "INSERT INTO Deltas (id_rapport, valeur_delta) VALUES (?, ?)";
        
        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, delta.getIdRapport());
            pstmt.setDouble(2, delta.getValeurDelta());
            pstmt.executeUpdate();
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    delta.setIdDelta(generatedKeys.getInt(1));
                }
            }
        }
    }
    
    public void delete(int idDelta) throws SQLException {
        String sql = "DELETE FROM Deltas WHERE id_delta = ?";
        
        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idDelta);
            pstmt.executeUpdate();
        }
    }
    
    // Test main
    public static void main(String[] args) {
        DeltasDAO dao = new DeltasDAO();
        try {
            System.out.println("=== All Deltas ===");
            List<Deltas> deltasList = dao.findAll();
            for (Deltas d : deltasList) {
                System.out.println(d);
            }
            
            System.out.println("\n=== Deltas for R001 ===");
            List<Deltas> rapportDeltas = dao.findByRapportId("R001");
            for (Deltas d : rapportDeltas) {
                System.out.println(d);
            }
            
        } catch (SQLException e) {
            System.err.println("DAO Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

