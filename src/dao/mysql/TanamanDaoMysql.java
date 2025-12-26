package dao.mysql;

import dao.TanamanDao;
import model.Tanaman;
import config.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TanamanDaoMysql implements TanamanDao {

    @Override
    public void insert(Tanaman t) {
        String sql = "INSERT INTO tanaman(nama, jenis) VALUES (?, ?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, t.getNama());
            ps.setString(2, t.getJenis());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) t.setId(keys.getInt(1));
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void update(Tanaman t) {
        String sql = "UPDATE tanaman SET nama=?, jenis=? WHERE id=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, t.getNama());
            ps.setString(2, t.getJenis());
            ps.setInt(3, t.getId());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void delete(Tanaman t) {
        String sql = "DELETE FROM tanaman WHERE id=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, t.getId());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public List<Tanaman> getAll() {
        List<Tanaman> list = new ArrayList<>();
        String sql = "SELECT * FROM tanaman ORDER BY id ASC";
        try (Connection c = DatabaseConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Tanaman(
                    rs.getInt("id"),
                    rs.getString("nama"),
                    rs.getString("jenis")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
}
