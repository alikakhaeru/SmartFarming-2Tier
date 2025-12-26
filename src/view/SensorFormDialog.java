package view;

import model.Sensor;
import javax.swing.*;
import java.awt.*;

public class SensorFormDialog extends JDialog {
    private JComboBox<ComboItem> lahanCombo = new JComboBox<>();
    private JTextField suhuField = new JTextField(10);
    private JTextField kelembapanField = new JTextField(10);
    private JTextField cahayaField = new JTextField(10);

    private boolean saved = false;

    public SensorFormDialog(Window owner, Sensor initial) {
        super(owner, "Input Data Sensor", ModalityType.APPLICATION_MODAL);
        initUI(initial);
    }

    private void initUI(Sensor initial) {
        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        form.add(new JLabel("Pilih Lahan:"));
        form.add(lahanCombo);
        
        // Menambahkan satuan di label agar user-friendly
        form.add(new JLabel("Suhu (°C):")); 
        form.add(suhuField);
        
        form.add(new JLabel("Kelembapan Tanah (%):")); 
        form.add(kelembapanField);
        
        form.add(new JLabel("Intensitas Cahaya (Lux):")); 
        form.add(cahayaField);

        // Load data lahan
        try {
            java.util.List<ComboItem> items = new java.util.ArrayList<>();
            service.impl.LahanServiceDefault ls = new service.impl.LahanServiceDefault();
            for (model.Lahan l : ls.getAll()) items.add(new ComboItem(l.getId(), l.getNamaLahan()));
            items.sort((a,b) -> a.name.compareToIgnoreCase(b.name));
            lahanCombo.addItem(new ComboItem(0, "-- Pilih Lahan --"));
            for (ComboItem it : items) lahanCombo.addItem(it);
        } catch (Exception ex) { }

        if (initial != null) {
            for (int i = 0; i < lahanCombo.getItemCount(); i++) {
                ComboItem it = lahanCombo.getItemAt(i);
                if (it.id == initial.getLahanId()) { lahanCombo.setSelectedIndex(i); break; }
            }
            // Tampilkan data awal dengan format yang bersih
            suhuField.setText(String.format("%.2f", initial.getSuhu()));
            kelembapanField.setText(String.format("%.2f", initial.getKelembapanTanah()));
            cahayaField.setText(String.format("%.2f", initial.getIntensitasCahaya()));
        } else {
            lahanCombo.setSelectedIndex(0);
        }

        JButton save = new JButton("Simpan");
        JButton cancel = new JButton("Batal");

        save.addActionListener(e -> {
            try {
                ComboItem sel = (ComboItem) lahanCombo.getSelectedItem();
                if (sel == null || sel.id == 0) {
                    JOptionPane.showMessageDialog(this, "Silakan pilih lahan terlebih dahulu!");
                    return;
                }
                // Validasi angka
                Double.parseDouble(suhuField.getText().trim());
                Double.parseDouble(kelembapanField.getText().trim());
                Double.parseDouble(cahayaField.getText().trim());
                
                saved = true;
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Input harus berupa angka (gunakan titik untuk desimal)", "Gagal Validasi", JOptionPane.WARNING_MESSAGE);
            }
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
        
        getRootPane().setDefaultButton(save);
    }

    public boolean isSaved() { return saved; }
    public int getLahanId() { ComboItem it = (ComboItem) lahanCombo.getSelectedItem(); return it == null ? 0 : it.id; }
    public double getSuhu() { return Double.parseDouble(suhuField.getText().trim()); }
    public double getKelembapan() { return Double.parseDouble(kelembapanField.getText().trim()); }
    public double getCahaya() { return Double.parseDouble(cahayaField.getText().trim()); }

    private static class ComboItem {
        final int id; final String name;
        ComboItem(int id, String name) { this.id = id; this.name = name; }
        @Override public String toString() { return name; }
    }
}