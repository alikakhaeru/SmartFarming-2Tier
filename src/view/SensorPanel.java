package view;

import model.Sensor;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import net.miginfocom.swing.MigLayout;

public class SensorPanel extends JPanel {

    private JButton tambah = new JButton("Tambah");
    private JButton hapus = new JButton("Hapus");
    private JButton update = new JButton("Update");
    private JTable table;
    private SensorTableModel model = new SensorTableModel();
    
    // search and status
    private JTextField searchField = new JTextField(24);
    private JLabel statusLabel = new JLabel("Ready");
    private JLabel countLabel = new JLabel("0 Records");
    private JProgressBar progressBar = new JProgressBar();

    public SensorPanel() {
        initUI();
    }

    private void initUI() {
        setLayout(new MigLayout("wrap 1", "[grow]", "[][grow][]"));

        JPanel top = new JPanel(new MigLayout("fill", "[grow][pref]", "[]"));

        JPanel leftSearch = new JPanel(new MigLayout("", "[][grow]"));
        leftSearch.add(new JLabel("Search:"));
        leftSearch.add(searchField, "growx");

        JPanel rightButtons = new JPanel(new MigLayout("", "[][][]"));
        rightButtons.add(tambah);
        rightButtons.add(hapus);
        rightButtons.add(update);

        top.add(leftSearch, "growx");
        top.add(rightButtons, "right");

        add(top, "growx");

        table = new JTable(model);
        table.setFillsViewportHeight(true);
        add(new JScrollPane(table), "grow, push");

        JPanel bottom = new JPanel(new MigLayout("fill", "[grow][pref]", "[]"));
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        centerPanel.setOpaque(false);
        progressBar.setVisible(false);
        progressBar.setIndeterminate(false);
        progressBar.setPreferredSize(new java.awt.Dimension(180, 16));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        centerPanel.add(progressBar);
        centerPanel.add(statusLabel);
        bottom.add(centerPanel, "growx");
        bottom.add(countLabel);
        add(bottom, "growx");
    }

    // Getters
    public JButton getTambah() { return tambah; }
    public JButton getHapus() { return hapus; }
    public JButton getUpdate() { return update; }
    public JTable getTable() { return table; }
    public SensorTableModel getModel() { return model; }
    public JTextField getSearchField() { return searchField; }
    public JLabel getStatusLabel() { return statusLabel; }
    public JLabel getCountLabel() { return countLabel; }

    // Helper methods
    public void setStatus(String s) { statusLabel.setText(s); }
    
    // Method untuk update angka di pojok kanan bawah
    public void setRecordCount(int c) { 
        countLabel.setText(c + " Records"); 
    }

    public void showLoading(boolean loading) {
        showLoading(loading, loading ? "Loading..." : "Ready");
    }

    public void showLoading(boolean loading, String message) {
        progressBar.setVisible(loading);
        progressBar.setIndeterminate(loading);
        tambah.setEnabled(!loading);
        hapus.setEnabled(!loading);
        update.setEnabled(!loading);
        setStatus(message == null ? (loading ? "Loading..." : "Ready") : message);
    }

    // ===========================================================
    // INNER CLASS: TABLE MODEL
    // ===========================================================
    public class SensorTableModel extends AbstractTableModel {
        private List<Sensor> data = new ArrayList<>();
        private String[] cols = {"ID", "Lahan", "Suhu", "Kelembapan", "Cahaya", "Waktu"};
        private Map<Integer, String> lahanMap = new HashMap<>();

        public void setData(List<Sensor> list) {
            this.data = (list == null) ? new ArrayList<>() : new ArrayList<>(list);
            fireTableDataChanged();
            // Setiap kali data diset, update label di pojok bawah
            setRecordCount(data.size());
        }

        public void setLahanMap(Map<Integer, String> map) {
            this.lahanMap = (map == null) ? new HashMap<>() : map;
            fireTableDataChanged();
        }

        public Sensor getAt(int row) {
            if (row < 0 || row >= data.size()) return null;
            return data.get(row);
        }

        @Override
        public int getRowCount() { return data.size(); }

        @Override
        public int getColumnCount() { return cols.length; }

        @Override
        public String getColumnName(int col) { return cols[col]; }

        @Override
        public Object getValueAt(int row, int col) {
            Sensor s = data.get(row);
            if (s == null) return null;
            
            switch (col) {
                case 0: return s.getId();
                case 1:
                    String ln = lahanMap.get(s.getLahanId());
                    return ln != null ? ln : s.getLahanId();
                case 2: 
                    // FORMAT: Membulatkan angka & Tambah satuan Celsius
                    return String.format("%.2f °C", s.getSuhu());
                case 3: 
                    // FORMAT: Membulatkan angka & Tambah satuan %
                    return String.format("%.2f %%", s.getKelembapanTanah());
                case 4: 
                    // FORMAT: Membulatkan angka & Tambah satuan Lux
                    return String.format("%.2f Lux", s.getIntensitasCahaya());
                case 5: 
                    return s.getWaktu();
                default: 
                    return null;
            }
        }
    }
}