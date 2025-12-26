package controller;

import view.SensorFormDialog;
import view.SensorPanel;
import service.SensorService;
import service.impl.SensorServiceDefault;
import model.Sensor;
import worker.sensor.LoadSensorWorker;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.table.TableRowSorter;
import java.sql.Timestamp;

public class SensorController {

    private SensorService service = new SensorServiceDefault();
    private SensorPanel panel;

    public SensorController(SensorPanel panel) {
        this.panel = panel;

        // 1. Setup Sorter + Search
        TableRowSorter<SensorPanel.SensorTableModel> sorter = new TableRowSorter<>(panel.getModel());
        panel.getTable().setRowSorter(sorter);

        // Fungsi pembantu untuk update label jumlah record di pojok bawah
        Runnable updateCount = () -> {
            int count = panel.getTable().getRowCount();
            panel.setRecordCount(count);
        };

        // Update jumlah record kalau tabel di-sort atau di-filter
        sorter.addRowSorterListener(new RowSorterListener() {
            @Override
            public void sorterChanged(RowSorterEvent e) { 
                updateCount.run(); 
            }
        });

        // Logika Pencarian (Search)
        panel.getSearchField().getDocument().addDocumentListener(new DocumentListener() {
            private void updateFilter() {
                String text = panel.getSearchField().getText();
                if (text == null || text.trim().isEmpty()) {
                    sorter.setRowFilter(null);
                    panel.setStatus("Ready");
                } else {
                    try {
                        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(text)));
                        panel.setStatus("Filter: " + text);
                    } catch (java.util.regex.PatternSyntaxException ex) {
                        sorter.setRowFilter(null);
                    }
                }
                updateCount.run(); // Update angka record saat mengetik search
            }
            public void insertUpdate(DocumentEvent e) { updateFilter(); }
            public void removeUpdate(DocumentEvent e) { updateFilter(); }
            public void changedUpdate(DocumentEvent e) { updateFilter(); }
        });

        // 2. Load Data Awal
        loadData();

        // 3. Tombol Tambah
        panel.getTambah().addActionListener(e -> {
            SensorFormDialog dlg = new SensorFormDialog(SwingUtilities.getWindowAncestor(panel), null);
            dlg.setVisible(true);
            if (dlg.isSaved()) {
                panel.showLoading(true, "Menambahkan data...");
                SwingWorker<Void, Void> w = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        Timestamp waktu = new Timestamp(System.currentTimeMillis());
                        Sensor s = new Sensor(0, dlg.getLahanId(), dlg.getSuhu(), dlg.getKelembapan(), dlg.getCahaya(), waktu);
                        service.create(s);
                        return null;
                    }

                    @Override
                    protected void done() {
                        try {
                            get();
                            loadData(); // Memanggil loadData akan memicu LoadSensorWorker yang mengupdate count
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(panel, "Gagal menambah data: " + ex.getMessage());
                        } finally {
                            panel.showLoading(false);
                        }
                    }
                };
                w.execute();
            }
        });

        // 4. Tombol Hapus
        panel.getHapus().addActionListener(e -> {
            int viewRow = panel.getTable().getSelectedRow();
            if (viewRow >= 0) {
                int modelRow = panel.getTable().convertRowIndexToModel(viewRow);
                Sensor s = panel.getModel().getAt(modelRow);
                int confirm = JOptionPane.showConfirmDialog(panel, "Hapus data sensor ID " + s.getId() + "?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    panel.showLoading(true, "Menghapus data...");
                    SwingWorker<Void, Void> w = new SwingWorker<Void, Void>() {
                        @Override
                        protected Void doInBackground() throws Exception {
                            service.delete(s);
                            return null;
                        }

                        @Override
                        protected void done() {
                            try {
                                get();
                                loadData();
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(panel, "Error menghapus data: " + ex.getMessage());
                            } finally {
                                panel.showLoading(false);
                            }
                        }
                    };
                    w.execute();
                }
            } else {
                JOptionPane.showMessageDialog(panel, "Pilih baris yang akan dihapus!");
            }
        });

        // 5. Tombol Update
        panel.getUpdate().addActionListener(e -> {
            int viewRow = panel.getTable().getSelectedRow();
            if (viewRow >= 0) {
                int modelRow = panel.getTable().convertRowIndexToModel(viewRow);
                Sensor s = panel.getModel().getAt(modelRow);

                SensorFormDialog dlg = new SensorFormDialog(SwingUtilities.getWindowAncestor(panel), s);
                dlg.setVisible(true);
                if (dlg.isSaved()) {
                    panel.showLoading(true, "Mengupdate data...");
                    SwingWorker<Void, Void> w = new SwingWorker<Void, Void>() {
                        @Override
                        protected Void doInBackground() throws Exception {
                            Timestamp waktu = new Timestamp(System.currentTimeMillis());
                            Sensor updated = new Sensor(s.getId(), dlg.getLahanId(), dlg.getSuhu(), dlg.getKelembapan(), dlg.getCahaya(), waktu);
                            service.delete(s); 
                            service.create(updated);
                            return null;
                        }

                        @Override
                        protected void done() {
                            try {
                                get();
                                loadData();
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(panel, "Gagal update data: " + ex.getMessage());
                            } finally {
                                panel.showLoading(false);
                            }
                        }
                    };
                    w.execute();
                }
            } else {
                JOptionPane.showMessageDialog(panel, "Pilih baris yang akan diupdate!");
            }
        });
    }

    private void loadData() {
        panel.showLoading(true);
        // LoadSensorWorker di dalamnya harus memanggil panel.setRecordCount(data.size())
        LoadSensorWorker worker = new LoadSensorWorker(panel, service);
        worker.execute();
    }
}