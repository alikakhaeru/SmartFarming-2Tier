package controller;

import view.LahanFormDialog;
import view.LahanPanel;
import service.impl.LahanServiceDefault;
import model.Lahan;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.table.TableRowSorter;

public class LahanController {

    private LahanServiceDefault service = new LahanServiceDefault();
    private service.impl.TanamanServiceDefault tanamanService = new service.impl.TanamanServiceDefault();

    public LahanController(LahanPanel panel) {
        // setup row sorter and search
        TableRowSorter<LahanPanel.LahanTableModel> sorter = new TableRowSorter<>(panel.getModel());
        panel.getTable().setRowSorter(sorter);

        // update record count helper
        Runnable updateCount = () -> panel.setRecordCount(panel.getTable().getRowCount());

        sorter.addRowSorterListener(new RowSorterListener() {
            @Override
            public void sorterChanged(RowSorterEvent e) {
                updateCount.run();
            }
        });

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
                updateCount.run();
            }

            public void insertUpdate(DocumentEvent e) { updateFilter(); }
            public void removeUpdate(DocumentEvent e) { updateFilter(); }
            public void changedUpdate(DocumentEvent e) { updateFilter(); }
        });

        panel.getTambah().addActionListener(e -> {
            LahanFormDialog dlg = new LahanFormDialog(SwingUtilities.getWindowAncestor(panel), null);
            dlg.setVisible(true);
            if (dlg.isSaved()) {
                panel.showLoading(true, "Loading data Lahan");
                // perform create in background
                SwingWorker<Void, Void> w = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        service.create(new Lahan(0, dlg.getNama(), dlg.getLokasi(), dlg.getLuas(), dlg.getTanamanId()));
                        return null;
                    }

                    @Override
                    protected void done() {
                        try {
                            get();
                            updateModelAndMap(panel);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(panel, "Error menyimpan data: " + ex.getMessage());
                        } finally {
                            panel.showLoading(false);
                        }
                    }
                };
                w.execute();
            }
        });

        panel.getHapus().addActionListener(e -> {
            int viewRow = panel.getTable().getSelectedRow();
            if (viewRow >= 0) {
                int modelRow = panel.getTable().convertRowIndexToModel(viewRow);
                Lahan l = panel.getModel().getAt(modelRow);
                int ok = JOptionPane.showConfirmDialog(null, "Hapus lahan: " + l.getNamaLahan() + " ?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                if (ok == JOptionPane.YES_OPTION) {
                    panel.showLoading(true, "Loading data Lahan");
                    SwingWorker<Void, Void> w = new SwingWorker<Void, Void>() {
                        @Override
                        protected Void doInBackground() throws Exception {
                            service.delete(l);
                            return null;
                        }

                        @Override
                        protected void done() {
                            try {
                                get();
                                updateModelAndMap(panel);
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(panel, "Error menghapus data: " + ex.getMessage());
                            } finally {
                                panel.showLoading(false);
                            }
                        }
                    };
                    w.execute();
                }
            }
        });


        panel.getUpdate().addActionListener(e -> {
            int viewRow = panel.getTable().getSelectedRow();
            if (viewRow < 0) {
                JOptionPane.showMessageDialog(panel, "Pilih baris yang ingin diupdate");
                return;
            }
            int modelRow = panel.getTable().convertRowIndexToModel(viewRow);

            Lahan l = panel.getModel().getAt(modelRow);
            LahanFormDialog dlg = new LahanFormDialog(SwingUtilities.getWindowAncestor(panel), l);
            dlg.setVisible(true);
            if (dlg.isSaved()) {
                l.setNamaLahan(dlg.getNama());
                l.setLokasi(dlg.getLokasi());
                l.setLuas(dlg.getLuas());
                l.setTanamanId(dlg.getTanamanId());
                panel.showLoading(true, "Loading data Lahan");
                SwingWorker<Void, Void> w = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        service.update(l);
                        return null;
                    }

                    @Override
                    protected void done() {
                        try {
                            get();
                            updateModelAndMap(panel);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(panel, "Error mengupdate data: " + ex.getMessage());
                        } finally {
                            panel.showLoading(false);
                        }
                    }
                };
                w.execute();
            }
        });


        updateModelAndMap(panel);
    }

    private void updateModelAndMap(LahanPanel panel) {
        // set tanaman map
        java.util.Map<Integer, String> map = new java.util.HashMap<>();
        for (model.Tanaman t : tanamanService.getAll()) {
            map.put(t.getId(), t.getNama());
        }
        panel.getModel().setTanamanMap(map);
        panel.getModel().setData(service.getAll());
        panel.setRecordCount(panel.getTable().getRowCount());
    }
}
