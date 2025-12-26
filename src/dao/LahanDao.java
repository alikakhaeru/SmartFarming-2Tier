package dao;

import model.Lahan;
import java.util.List;

public interface LahanDao {
    void insert(Lahan l);
    void update(Lahan l);  // <--- baru
    void delete(Lahan l);
    List<Lahan> getAll();
}
