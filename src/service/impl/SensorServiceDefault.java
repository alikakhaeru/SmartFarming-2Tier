package service.impl;

import service.SensorService;
import dao.SensorDao;
import dao.mysql.SensorDaoMySql;
import model.Sensor;

import java.util.List;

public class SensorServiceDefault implements SensorService {

    private SensorDao dao = new SensorDaoMySql();

    public void create(Sensor s) { dao.insert(s); }

    public void delete(Sensor s) { dao.delete(s); }

    public List<Sensor> getAll() { return dao.getAll(); }
}
