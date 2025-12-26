package view;

import model.Tanaman;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import net.miginfocom.swing.MigLayout;

public class TanamanPanel extends JPanel {

    private JButton tambah = new JButton("Tambah");
    private JButton update = new JButton("Update");
    private JButton hapus = new JButton("Hapus");
    private JTable table;
    private TanamanTableModel model = new TanamanTableModel();
    // search and status
    private JTextField searchField = new JTextField(24);
    private JLabel statusLabel = new JLabel("Ready");
    private JLabel countLabel = new JLabel("0 Records");
    private JProgressBar progressBar = new JProgressBar();

    public TanamanPanel() { initUI(); }

    private void initUI() {
        setLayout(new MigLayout("wrap 1", "[grow]", "[][grow][]"));

        JPanel top = new JPanel(new MigLayout("fill", "[grow][pref]", "[]"));

        JPanel leftSearch = new JPanel(new MigLayout("", "[][grow]"));
        leftSearch.add(new JLabel("Search:"));
        leftSearch.add(searchField, "growx");

        JPanel rightButtons = new JPanel(new MigLayout("", "[][][]"));
        rightButtons.add(tambah);
        rightButtons.add(update);
        rightButtons.add(hapus);

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

    public JButton getTambah() { return tambah; }
    public JButton getUpdate() { return update; }
    public JButton getHapus() { return hapus; }
    public JTable getTable() { return table; }
    public TanamanTableModel getModel() { return model; }
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
        update.setEnabled(!loading);
        hapus.setEnabled(!loading);
        setStatus(message == null ? (loading ? "Loading..." : "Ready") : message);
    }

    public class TanamanTableModel extends AbstractTableModel {
        private List<Tanaman> data = new ArrayList<>();
        private String[] cols = {"ID", "Nama", "Jenis"};

        public void setData(List<Tanaman> list) {
            data = new ArrayList<>(list);
            fireTableDataChanged();
        }

        public Tanaman getAt(int row) { return data.get(row); }

        @Override
        public int getRowCount() { return data.size(); }

        @Override
        public int getColumnCount() { return cols.length; }

        @Override
        public String getColumnName(int col) { return cols[col]; }

        @Override
        public Object getValueAt(int row, int col) {
            Tanaman t = data.get(row);
            switch (col) {
                case 0: return t.getId();
                case 1: return t.getNama();
                case 2: return t.getJenis();
                default: return null;
            }
        }
    }
}
