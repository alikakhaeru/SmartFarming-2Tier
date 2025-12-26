package service.impl;

import java.sql.*;

import config.DatabaseConnection;
import service.UserService;

public class UserServiceDefault implements UserService {

    @Override
    public boolean login(String username, String password) {
        // Query SQL untuk mencari user yang cocok
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        
        // Menggunakan try-with-resources agar koneksi otomatis tertutup
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);
            ps.setString(2, password);
            
            try (ResultSet rs = ps.executeQuery()) {
                // Jika rs.next() bernilai true, berarti user ditemukan
                return rs.next();
            }
            
        } catch (SQLException e) {
            System.err.println("Gagal Login: " + e.getMessage());
            return false;
        }
    }
}