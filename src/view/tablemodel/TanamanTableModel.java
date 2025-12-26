package view.tablemodel;

import javax.swing.table.AbstractTableModel;
import java.util.*;
import model.Tanaman;

public class TanamanTableModel extends AbstractTableModel {

    private List<Tanaman> data = new ArrayList<>();
    private String[] col = {"ID", "Nama", "Jenis"};

    public void setData(List<Tanaman> data) {
        this.data = data;
        fireTableDataChanged();
    }

    public Tanaman getAt(int row) {
        return data.get(row);
    }

    @Override
    public int getRowCount() { return data.size(); }

    @Override
    public int getColumnCount() { return col.length; }

    @Override
    public String getColumnName(int c) { return col[c]; }

    @Override
    public Object getValueAt(int r, int c) {
        Tanaman t = data.get(r);
        switch (c) {
            case 0:
                return t.getId();
            case 1:
                return t.getNama();
            case 2:
                return t.getJenis();
            default:
                return null;
        }
    }
}
