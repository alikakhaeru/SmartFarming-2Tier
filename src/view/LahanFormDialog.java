package view;

import model.Lahan;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LahanFormDialog extends JDialog {
    private JTextField namaField = new JTextField(20);
    private JTextField lokasiField = new JTextField(20);
    private JTextField luasField = new JTextField(10);
    private JComboBox<ComboItem> tanamanCombo = new JComboBox<>();

    private boolean saved = false;

    public LahanFormDialog(Window owner, Lahan initial) {
        super(owner, "Lahan", ModalityType.APPLICATION_MODAL);
        initUI(initial);
    }

    private void initUI(Lahan initial) {
        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        form.add(new JLabel("Nama Lahan:"));
        form.add(namaField);
        form.add(new JLabel("Lokasi:"));
        form.add(lokasiField);
        form.add(new JLabel("Luas:"));
        form.add(luasField);
        form.add(new JLabel("Tanaman:"));
        form.add(tanamanCombo);

        // populate tanaman combo with sorted list + placeholder
        try {
            java.util.List<ComboItem> items = new java.util.ArrayList<>();
            service.impl.TanamanServiceDefault ts = new service.impl.TanamanServiceDefault();
            for (model.Tanaman t : ts.getAll()) items.add(new ComboItem(t.getId(), t.getNama()));
            items.sort((a,b) -> a.name.compareToIgnoreCase(b.name));
            tanamanCombo.addItem(new ComboItem(0, "-- Pilih Tanaman --"));
            for (ComboItem it : items) tanamanCombo.addItem(it);
        } catch (Exception ex) { /* ignore */ }

        if (initial != null) {
            namaField.setText(initial.getNamaLahan());
            lokasiField.setText(initial.getLokasi());
            luasField.setText(String.valueOf(initial.getLuas()));
            // select tanaman in combo if present
            for (int i = 0; i < tanamanCombo.getItemCount(); i++) {
                ComboItem it = tanamanCombo.getItemAt(i);
                if (it.id == initial.getTanamanId()) { tanamanCombo.setSelectedIndex(i); break; }
            }
        } else {
            tanamanCombo.setSelectedIndex(0);
        }

        JButton save = new JButton("Save");
        JButton cancel = new JButton("Cancel");

        save.addActionListener(e -> {
            if (namaField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nama harus diisi", "Validasi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                Double.parseDouble(luasField.getText());
                ComboItem sel = (ComboItem) tanamanCombo.getSelectedItem();
                if (sel == null || sel.id == 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Luas harus angka dan Tanaman harus dipilih", "Validasi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            saved = true;
            dispose();
        });

        cancel.addActionListener(e -> {
            saved = false;
            dispose();
        });

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(cancel);
        buttons.add(save);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(form, BorderLayout.CENTER);
        getContentPane().add(buttons, BorderLayout.SOUTH);
        pack();
        setResizable(false);
        setLocationRelativeTo(getOwner());
        // keyboard shortcuts: Enter=Save, Esc=Cancel
        getRootPane().setDefaultButton(save);
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cancel");
        getRootPane().getActionMap().put("cancel", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { saved = false; dispose(); }
        });
        // focus first field
        SwingUtilities.invokeLater(() -> namaField.requestFocusInWindow());
    }

    public boolean isSaved() { return saved; }

    public String getNama() { return namaField.getText().trim(); }
    public String getLokasi() { return lokasiField.getText().trim(); }
    public double getLuas() { return Double.parseDouble(luasField.getText().trim()); }
    public int getTanamanId() { ComboItem it = (ComboItem) tanamanCombo.getSelectedItem(); return it == null ? 0 : it.id; }

    private static class ComboItem {
        final int id; final String name;
        ComboItem(int id, String name) { this.id = id; this.name = name; }
        @Override public String toString() { return name; }
    }
}
