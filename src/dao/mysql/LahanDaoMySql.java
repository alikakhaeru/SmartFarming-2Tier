package dao.mysql;

import dao.LahanDao;
import model.Lahan;
import config.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LahanDaoMySql implements LahanDao {

    @Override
    public void insert(Lahan l) {
        String sql = "INSERT INTO lahan (nama_lahan, lokasi, luas, tanaman_id) VALUES (?,?,?,?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, l.getNamaLahan());
            ps.setString(2, l.getLokasi());
            ps.setDouble(3, l.getLuas());
            ps.setInt(4, l.getTanamanId());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    l.setId(keys.getInt(1));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Lahan l) {
        String sql = "UPDATE lahan SET nama_lahan=?, lokasi=?, luas=?, tanaman_id=? WHERE id=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, l.getNamaLahan());
            ps.setString(2, l.getLokasi());
            ps.setDouble(3, l.getLuas());
            ps.setInt(4, l.getTanamanId());
            ps.setInt(5, l.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Lahan l) {
        String sql = "DELETE FROM lahan WHERE id=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, l.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Lahan> getAll() {
        List<Lahan> list = new ArrayList<>();
        String sql = "SELECT * FROM lahan"; // pastikan nama tabel sama dengan DB
        try (Connection c = DatabaseConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Lahan l = new Lahan(
                    rs.getInt("id"),
                    rs.getString("nama_lahan"),
                    rs.getString("lokasi"),
                    rs.getDouble("luas"),
                    rs.getInt("tanaman_id")
                );
                list.add(l);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
