package service;

import java.util.List;
import model.Sensor;

public interface SensorService {
    void create(Sensor s);
    void delete(Sensor s);
    List<Sensor> getAll();
}
