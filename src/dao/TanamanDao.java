package dao;

import model.Tanaman;
import java.util.List;

public interface TanamanDao {
    void insert(Tanaman t);
    void update(Tanaman t);
    void delete(Tanaman t);
    List<Tanaman> getAll();
}
