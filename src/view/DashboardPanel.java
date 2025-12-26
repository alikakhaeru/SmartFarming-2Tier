package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import net.miginfocom.swing.MigLayout;
import model.*;
import service.*;
import worker.DataWorker;

public class DashboardPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel lblTotalLahan, lblTotalTanaman, lblTotalSensor;
    private JProgressBar progressBar = new JProgressBar();

    private LahanService lahanService;
    private TanamanService tanamanService;
    private SensorService sensorService;

    public DashboardPanel(LahanService lahanService, TanamanService tanamanService, SensorService sensorService) {
        this.lahanService = lahanService;
        this.tanamanService = tanamanService;
        this.sensorService = sensorService;

        // MigLayout: [grow] membuat kolom/baris fleksibel mengikuti ukuran frame
        setLayout(new MigLayout("fill, insets 20", "[grow]", "[pref!][grow][pref!]"));

        // --- ATAS: Cards (Responsif) ---
        JPanel summaryPanel = new JPanel(new MigLayout("fill, insets 0", "[grow][grow][grow]", "[]"));
        lblTotalLahan = createValueLabel();
        lblTotalTanaman = createValueLabel();
        lblTotalSensor = createValueLabel();

        summaryPanel.add(createCard("TOTAL LAHAN", lblTotalLahan, new Color(41, 128, 185)), "grow");
        summaryPanel.add(createCard("JENIS TANAMAN", lblTotalTanaman, new Color(39, 174, 96)), "grow");
        summaryPanel.add(createCard("LOG DATA SENSOR", lblTotalSensor, new Color(211, 84, 0)), "grow");
        add(summaryPanel, "growx, wrap");

        // --- TENGAH: Tabel (Fleksibel) ---
        String[] columns = {"Lahan", "Tanaman", "Lokasi", "Suhu", "Kelembapan", "Cahaya", "Waktu"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(30);
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createTitledBorder("Monitoring Real-time"));
        add(sp, "grow, push, wrap");

        // --- BAWAH: Progress Bar ---
        progressBar.setIndeterminate(true);
        progressBar.setVisible(false);
        add(progressBar, "growx");
    }

    public void refreshData() {
        // Panggil worker dari folder worker
        new DataWorker<Map<String, List<?>>>(
            () -> {
                // Task Paralel (Background Thread)
                Map<String, List<?>> map = new HashMap<>();
                map.put("l", lahanService.getAll());
                map.put("t", tanamanService.getAll());
                map.put("s", sensorService.getAll());
                return map;
            },
            (data) -> {
                // Update UI (Event Dispatch Thread) - Aman & Tidak Freeze
                updateContent(data);
            },
            (loading) -> {
                progressBar.setVisible(loading);
                setCursor(loading ? Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR) : Cursor.getDefaultCursor());
            }
        ).execute();
    }

    @SuppressWarnings("unchecked")
    private void updateContent(Map<String, List<?>> data) {
        List<Lahan> listLahan = (List<Lahan>) data.get("l");
        List<Tanaman> listTanaman = (List<Tanaman>) data.get("t");
        List<Sensor> listSensor = (List<Sensor>) data.get("s");

        lblTotalLahan.setText(String.valueOf(listLahan.size()));
        lblTotalTanaman.setText(String.valueOf(listTanaman.size()));
        lblTotalSensor.setText(String.valueOf(listSensor.size()));

        tableModel.setRowCount(0);
        for (Lahan l : listLahan) {
            Tanaman tMatch = listTanaman.stream().filter(t -> t.getId() == l.getTanamanId()).findFirst().orElse(null);
            Sensor sMatch = listSensor.stream().filter(s -> s.getLahanId() == l.getId()).reduce((f, s) -> s).orElse(null);

            tableModel.addRow(new Object[]{
                l.getNamaLahan(), (tMatch != null ? tMatch.getNama() : "N/A"), l.getLokasi(),
                (sMatch != null ? String.format("%.1f °C", sMatch.getSuhu()) : "-"),
                (sMatch != null ? String.format("%.1f %%", sMatch.getKelembapanTanah()) : "-"),
                (sMatch != null ? String.format("%.1f Lux", sMatch.getIntensitasCahaya()) : "-"),
                (sMatch != null ? sMatch.getWaktu() : "No Data")
            });
        }
    }

    private JLabel createValueLabel() {
        JLabel l = new JLabel("0");
        l.setFont(new Font("SansSerif", Font.BOLD, 30));
        l.setForeground(Color.WHITE);
        l.setHorizontalAlignment(0);
        return l;
    }

    private JPanel createCard(String title, JLabel val, Color bg) {
        JPanel p = new JPanel(new MigLayout("fill, insets 10"));
        p.setBackground(bg);
        JLabel t = new JLabel(title);
        t.setForeground(Color.WHITE);
        t.setFont(new Font("SansSerif", Font.PLAIN, 11));
        p.add(t, "center, wrap");
        p.add(val, "center");
        return p;
    }
}