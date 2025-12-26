package service.impl;

import service.LahanService;
import dao.LahanDao;
import dao.mysql.LahanDaoMySql;
import model.Lahan;
import java.util.List;

public class LahanServiceDefault implements LahanService {

    private LahanDao dao = new LahanDaoMySql();

    @Override
    public void create(Lahan l) { dao.insert(l); }

    @Override
    public void update(Lahan l) { dao.update(l); }

    @Override
    public void delete(Lahan l) { dao.delete(l); }

    @Override
    public List<Lahan> getAll() { return dao.getAll(); }

    @Override
    public Lahan getById(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getById'");
    }
}
