package view.tablemodel;

import model.Lahan;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class LahanTableModel extends AbstractTableModel {

    private List<Lahan> list = new ArrayList<>();
    private final String[] columnNames = {"ID", "Nama Lahan", "Lokasi", "Luas", "ID Tanaman"};

    public void setData(List<Lahan> data) {
        this.list = (data != null) ? new ArrayList<>(data) : new ArrayList<>();
        fireTableDataChanged();
    }

    public Lahan getAt(int row) {
        if (row < 0 || row >= list.size()) return null;
        return list.get(row);
    }

    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Lahan l = list.get(rowIndex);
        switch (columnIndex) {
            case 0: return l.getId();
            case 1: return l.getNamaLahan();
            case 2: return l.getLokasi();
            case 3: return l.getLuas();
            case 4: return l.getTanamanId();
            default: return null;
        }
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }
}
