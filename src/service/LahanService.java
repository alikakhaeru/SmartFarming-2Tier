package service;

import model.Lahan;
import java.util.List;

public interface LahanService {
    void create(Lahan l);
    void update(Lahan l);  // <--- baru
    void delete(Lahan l);
    List<Lahan> getAll();
    Lahan getById(int id);
}
