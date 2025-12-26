package service.impl;

import dao.TanamanDao;          // <- ini interface
import dao.mysql.TanamanDaoMysql; // <- implementasinya
import model.Tanaman;
import service.TanamanService;

import java.util.List;

public class TanamanServiceDefault implements TanamanService {

    private TanamanDao dao = new TanamanDaoMysql(); // <- pakai interface TanamanDao

    @Override
    public void create(Tanaman t) { dao.insert(t); }

    @Override
    public void update(Tanaman t) { dao.update(t); }

    @Override
    public void delete(Tanaman t) { dao.delete(t); }

    @Override
    public List<Tanaman> getAll() { return dao.getAll(); }

    @Override
    public Tanaman getById(int tanamanId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getById'");
    }
}
