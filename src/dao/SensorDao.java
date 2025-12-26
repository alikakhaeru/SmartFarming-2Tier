package dao;

import model.Sensor;
import java.util.List;

public interface SensorDao {
    void insert(Sensor s);
    void delete(Sensor s);
    List<Sensor> getAll();
}
