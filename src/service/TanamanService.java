package service;

import model.Tanaman;
import java.util.List;

public interface TanamanService {
    void create(Tanaman t);
    void update(Tanaman t);
    void delete(Tanaman t);
    List<Tanaman> getAll();
    Tanaman getById(int tanamanId);
}
