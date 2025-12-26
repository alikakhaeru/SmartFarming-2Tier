package dao.mysql;

import dao.SensorDao;
import model.Sensor;
import config.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SensorDaoMySql implements SensorDao {

    @Override
    public void insert(Sensor s) {
        String sql = "INSERT INTO sensor_data (lahan_id, suhu, kelembapan_tanah, intensitas_cahaya, waktu) VALUES (?,?,?,?,?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, s.getLahanId());
            ps.setDouble(2, s.getSuhu());
            ps.setDouble(3, s.getKelembapanTanah());
            ps.setDouble(4, s.getIntensitasCahaya());
            ps.setTimestamp(5, s.getWaktu());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) s.setId(keys.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Sensor> getAll() {
        List<Sensor> list = new ArrayList<>();
        String sql = "SELECT * FROM sensor_data";  // ✅ harus sesuai tabel
        try (Connection c = DatabaseConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Sensor(
                    rs.getInt("id"),
                    rs.getInt("lahan_id"),
                    rs.getDouble("suhu"),
                    rs.getDouble("kelembapan_tanah"),
                    rs.getDouble("intensitas_cahaya"),
                    rs.getTimestamp("waktu")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void delete(Sensor s) {
        String sql = "DELETE FROM sensor_data WHERE id=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, s.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
