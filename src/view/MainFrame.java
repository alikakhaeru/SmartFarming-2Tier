package view;

import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import controller.LahanController;
import controller.SensorController;
import controller.TanamanController;
import service.impl.LahanServiceDefault;
import service.impl.TanamanServiceDefault;
import service.impl.SensorServiceDefault;

public class MainFrame extends JFrame {
    private LahanPanel lahanPanel;
    private SensorPanel sensorPanel;
    private TanamanPanel tanamanPanel;
    private DashboardPanel dashboardPanel;

    public MainFrame() {
        initUI();
    }

    private void initUI() {
        setTitle("Sistem Monitoring Smart Farming");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 750);
        setLocationRelativeTo(null);

        // Inisialisasi Service
        LahanServiceDefault ls = new LahanServiceDefault();
        TanamanServiceDefault ts = new TanamanServiceDefault();
        SensorServiceDefault ss = new SensorServiceDefault();

        // Inisialisasi Panel
        lahanPanel = new LahanPanel();
        sensorPanel = new SensorPanel();
        tanamanPanel = new TanamanPanel();
        dashboardPanel = new DashboardPanel(ls, ts, ss);

        // Setup TabbedPane
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Dashboard", dashboardPanel);
        tabs.addTab("Data Lahan", lahanPanel);
        tabs.addTab("Data Sensor", sensorPanel);
        tabs.addTab("Data Tanaman", tanamanPanel);

        // SINKRONISASI: Update dashboard setiap kali tab Dashboard dibuka
        tabs.addChangeListener(e -> {
            if (tabs.getSelectedIndex() == 0) {
                dashboardPanel.refreshData();
            }
        });

        setLayout(new MigLayout("fill", "[grow]", "[grow]"));
        add(tabs, "grow");

        // Jalankan Controller
        new LahanController(lahanPanel);
        new SensorController(sensorPanel);
        new TanamanController(tanamanPanel);

        // Panggil data pertama kali saat aplikasi dibuka
        SwingUtilities.invokeLater(() -> dashboardPanel.refreshData());
    }
}