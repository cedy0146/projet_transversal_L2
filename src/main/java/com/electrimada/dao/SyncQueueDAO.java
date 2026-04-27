package com.electrimada.dao;

import com.electrimada.modele.SyncQueue;
import com.electrimada.modele.ConnexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SyncQueueDAO {
    
    public List<SyncQueue> findAll() throws SQLException {
        List<SyncQueue> queues = new ArrayList<>();
        String sql = "SELECT * FROM SyncQueue";
        
        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                SyncQueue queue = new SyncQueue();
                queue.setIdSync(rs.getInt("id_sync"));
                queue.setIdRapport(rs.getString("id_rapport"));
                queue.setStatutEnvoi(rs.getBoolean("statut_envoi"));
                queues.add(queue);
            }
        }
        return queues;
    }
    
    public SyncQueue findById(int idSync) throws SQLException {
        String sql = "SELECT * FROM SyncQueue WHERE id_sync = ?";
        
        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idSync);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    SyncQueue queue = new SyncQueue();
                    queue.setIdSync(rs.getInt("id_sync"));
                    queue.setIdRapport(rs.getString("id_rapport"));
                    queue.setStatutEnvoi(rs.getBoolean("statut_envoi"));
                    return queue;
                }
            }
        }
        return null;
    }
    
    public void save(SyncQueue queue) throws SQLException {
        String sql = "INSERT INTO SyncQueue (id_rapport, statut_envoi) VALUES (?, ?) ";
        
        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, queue.getIdRapport());
            pstmt.setBoolean(2, queue.isStatutEnvoi());
            pstmt.executeUpdate();
            
            // Get generated id_sync
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    queue.setIdSync(generatedKeys.getInt(1));
                }
            }
        }
    }
    
    public void update(SyncQueue queue) throws SQLException {
        String sql = "UPDATE SyncQueue SET statut_envoi = ? WHERE id_sync = ?";
        
        try (Connection conn = ConnexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setBoolean(1, queue.isStatutEnvoi());
            pstmt.setInt(2, queue.getIdSync());
            pstmt.executeUpdate();
        }
    }
    
    public void delete(int idSync) throws SQLException {
