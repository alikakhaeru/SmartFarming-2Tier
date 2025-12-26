package view;

import model.Tanaman;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TanamanFormDialog extends JDialog {
    private JTextField namaField = new JTextField(20);
    private JTextField jenisField = new JTextField(20);

    private boolean saved = false;

    public TanamanFormDialog(Window owner, Tanaman initial) {
        super(owner, "Tanaman", ModalityType.APPLICATION_MODAL);
        initUI(initial);
    }

    private void initUI(Tanaman initial) {
        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        form.add(new JLabel("Nama Tanaman:"));
        form.add(namaField);
        form.add(new JLabel("Jenis Tanaman:"));
        form.add(jenisField);

        if (initial != null) {
            namaField.setText(initial.getNama());
            jenisField.setText(initial.getJenis());
        }

        JButton save = new JButton("Save");
        JButton cancel = new JButton("Cancel");

        save.addActionListener(e -> {
            if (namaField.getText().trim().isEmpty() || jenisField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua field harus diisi", "Validasi", JOptionPane.WARNING_MESSAGE);
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
    public String getJenis() { return jenisField.getText().trim(); }
}
