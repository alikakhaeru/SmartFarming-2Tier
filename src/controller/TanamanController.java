package controller;

import view.TanamanFormDialog;
import view.TanamanPanel;
import service.impl.TanamanServiceDefault;
import model.Tanaman;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.table.TableRowSorter;

public class TanamanController {

    private TanamanServiceDefault service = new TanamanServiceDefault();

    public TanamanController(TanamanPanel panel) {
        // setup sorter + search
        TableRowSorter<TanamanPanel.TanamanTableModel> sorter = new TableRowSorter<>(panel.getModel());
        panel.getTable().setRowSorter(sorter);

        Runnable updateCount = () -> panel.setRecordCount(panel.getTable().getRowCount());

        sorter.addRowSorterListener(new RowSorterListener() {
            @Override
            public void sorterChanged(RowSorterEvent e) { updateCount.run(); }
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
            TanamanFormDialog dlg = new TanamanFormDialog(SwingUtilities.getWindowAncestor(panel), null);
            dlg.setVisible(true);
            if (dlg.isSaved()) {
                panel.showLoading(true, "Loading data Tanaman");
                SwingWorker<Void, Void> w = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        service.create(new Tanaman(0, dlg.getNama(), dlg.getJenis()));
                        return null;
                    }

                    @Override
                    protected void done() {
                        try {
                            get();
                            panel.getModel().setData(service.getAll());
                            updateCount.run();
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

        panel.getUpdate().addActionListener(e -> {
            int viewRow = panel.getTable().getSelectedRow();
            if (viewRow >= 0) {
                int modelRow = panel.getTable().convertRowIndexToModel(viewRow);
                Tanaman t = panel.getModel().getAt(modelRow);

                JPanel form = new JPanel(new java.awt.GridLayout(0, 2, 8, 8));
                JTextField namaField = new JTextField(t.getNama(), 20);
                JTextField jenisField = new JTextField(t.getJenis(), 20);

                form.add(new JLabel("Nama Tanaman:"));
                form.add(namaField);
                form.add(new JLabel("Jenis Tanaman:"));
                form.add(jenisField);

                int ok = JOptionPane.showConfirmDialog(null, form, "Update Tanaman", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (ok == JOptionPane.OK_OPTION) {
                    String nama = namaField.getText();
                    String jenis = jenisField.getText();
                    if (nama != null && jenis != null) {
                        t.setNama(nama);
                        t.setJenis(jenis);
                        panel.showLoading(true, "Loading data Tanaman");
                        SwingWorker<Void, Void> w = new SwingWorker<Void, Void>() {
                            @Override
                            protected Void doInBackground() throws Exception {
                                service.update(t);
                                return null;
                            }

                            @Override
                            protected void done() {
                                try {
                                    get();
                                    panel.getModel().setData(service.getAll());
                                    updateCount.run();
                                } catch (Exception ex) {
                                    JOptionPane.showMessageDialog(panel, "Error mengupdate data: " + ex.getMessage());
                                } finally {
                                    panel.showLoading(false);
                                }
                            }
                        };
                        w.execute();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(panel, "Pilih baris yang ingin diupdate");
            }
        });

        panel.getHapus().addActionListener(e -> {
            int viewRow = panel.getTable().getSelectedRow();
            if (viewRow >= 0) {
                int modelRow = panel.getTable().convertRowIndexToModel(viewRow);
                Tanaman t = panel.getModel().getAt(modelRow);
                panel.showLoading(true, "Loading data Tanaman");
                SwingWorker<Void, Void> w = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        service.delete(t);
                        return null;
                    }

                    @Override
                    protected void done() {
                        try {
                            get();
                            panel.getModel().setData(service.getAll());
                            updateCount.run();
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(panel, "Error menghapus data: " + ex.getMessage());
                        } finally {
                            panel.showLoading(false);
                        }
                    }
                };
                w.execute();
            }
        });

        // Load awal
        panel.getModel().setData(service.getAll());
        updateCount.run();
    }
}
