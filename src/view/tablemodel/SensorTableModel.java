package view.tablemodel;

import model.Sensor;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class SensorTableModel extends AbstractTableModel {

    private List<Sensor> list;
    private final String[] columnNames = {
        "ID", "ID Lahan", "Suhu", "Kelembapan", "Cahaya", "Waktu"
    };

    public SensorTableModel(List<Sensor> list) {
        this.list = list;
    }

    @Override
    public int getRowCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Sensor s = list.get(rowIndex);

        switch (columnIndex) {
            case 0: return s.getId();
            case 1: return s.getLahanId();
            case 2: 
                // Membulatkan suhu ke 2 desimal + Satuan
                return String.format("%.2f °C", s.getSuhu());
            case 3: 
                // Membulatkan kelembapan + Satuan %
                return String.format("%.2f %%", s.getKelembapanTanah());
            case 4: 
                // Membulatkan cahaya + Satuan Lux
                return String.format("%.2f Lux", s.getIntensitasCahaya());
            case 5: 
                return s.getWaktu();
            default: 
                return null;
        }
    }

    public void setList(List<Sensor> list) {
        this.list = list;
        fireTableDataChanged();
    }
}