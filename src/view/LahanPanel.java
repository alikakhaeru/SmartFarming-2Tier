package view;

import model.Lahan;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import net.miginfocom.swing.MigLayout;

public class LahanPanel extends JPanel {

    private JButton tambah = new JButton("Tambah");
    private JButton hapus = new JButton("Hapus");
    private JButton update = new JButton("Update"); // tombol update
    private JTable table;
    private LahanTableModel model = new LahanTableModel();

    // search and status
    private JTextField searchField = new JTextField(24);
    private JLabel statusLabel = new JLabel("Ready");
    private JLabel countLabel = new JLabel("0 Records");
    private JProgressBar progressBar = new JProgressBar();

    public LahanPanel() {
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
        // center panel holds progress and status centered
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

    public JButton getTambah() { return tambah; }
    public JButton getHapus() { return hapus; }
    public JButton getUpdate() { return update; }
    public JTable getTable() { return table; }
    public LahanTableModel getModel() { return model; }
    public JTextField getSearchField() { return searchField; }
    public JLabel getStatusLabel() { return statusLabel; }
    public JLabel getCountLabel() { return countLabel; }

    public void setStatus(String s) { statusLabel.setText(s); }
    public void setRecordCount(int c) { countLabel.setText(c + " Records"); }

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

    public class LahanTableModel extends AbstractTableModel {
        private List<Lahan> data = new ArrayList<>();
        private String[] cols = {"ID", "Nama", "Lokasi", "Luas", "Tanaman"};

        public void setData(List<Lahan> list) {
            data = (list == null) ? new ArrayList<>() : new ArrayList<>(list);
            fireTableDataChanged();
        }

        private Map<Integer, String> tanamanMap = new HashMap<>();

        public void setTanamanMap(Map<Integer, String> map) {
            this.tanamanMap = (map == null) ? new HashMap<>() : map;
            fireTableDataChanged();
        }

        public Lahan getAt(int row) {
            if (row < 0 || row >= data.size()) return null;
            return data.get(row);
        }

        public void updateAt(int row, Lahan l) {
            if (row < 0 || row >= data.size()) return;
            data.set(row, l);
            fireTableRowsUpdated(row, row);
        }

        public void add(Lahan l) {
            if (l == null) return;
            data.add(l);
            fireTableRowsInserted(data.size() - 1, data.size() - 1);
        }

        public void removeAt(int row) {
            if (row < 0 || row >= data.size()) return;
            data.remove(row);
            fireTableRowsDeleted(row, row);
        }

        @Override
        public int getRowCount() { return data.size(); }

        @Override
        public int getColumnCount() { return cols.length; }

        @Override
        public String getColumnName(int col) { return cols[col]; }

        @Override
        public Object getValueAt(int row, int col) {
            Lahan l = data.get(row);
            if (l == null) return null;
            switch (col) {
                case 0: return l.getId();
                case 1: return l.getNamaLahan();
                case 2: return l.getLokasi();
                case 3: return l.getLuas();
                case 4:
                    String tn = tanamanMap.get(l.getTanamanId());
                    return tn != null ? tn : l.getTanamanId();
                default: return null;
            }
        }
    }
}
