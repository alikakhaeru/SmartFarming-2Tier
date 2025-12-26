package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import service.impl.UserServiceDefault;

public class LoginForm extends JFrame {
    private JTextField txtUsername = new JTextField();
    private JPasswordField txtPassword = new JPasswordField();
    private JButton btnLogin = new JButton("LOGIN SISTEM");

    public LoginForm() {
        setTitle("Smart Farming - BUMDes Madusari");
        setSize(450, 500); // Ukuran frame disesuaikan agar lebih proporsional
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // --- Panel Utama (Background Gradient) ---
        JPanel bgPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(13, 71, 16), 0, getHeight(), new Color(46, 125, 50));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        // --- Container Card ---
        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(340, 400));
        card.setBackground(Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(30, 30, 30, 30));

        // --- Header Section (Tetap Center) ---
        JLabel lblBumdes = new JLabel("BUMDes Madusari");
        lblBumdes.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
        lblBumdes.setForeground(new Color(46, 125, 50));
        lblBumdes.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitle = new JLabel("SMART FARMING");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(33, 33, 33));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(lblBumdes);
        card.add(lblTitle);
        card.add(Box.createRigidArea(new Dimension(0, 30))); // Jarak bawah judul

        // --- Input Section (Sub-Panel untuk Rata Kiri) ---
        JPanel inputWrapper = new JPanel();
        inputWrapper.setBackground(Color.WHITE);
        inputWrapper.setLayout(new BoxLayout(inputWrapper, BoxLayout.Y_AXIS));
        inputWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);
        inputWrapper.setMaximumSize(new Dimension(270, 180));

        // Username
        addInputLabel("USERNAME", inputWrapper);
        styleTextField(txtUsername, "Masukkan Username");
        inputWrapper.add(txtUsername);
        inputWrapper.add(Box.createRigidArea(new Dimension(0, 15)));

        // Password
        addInputLabel("PASSWORD", inputWrapper);
        styleTextField(txtPassword, "Masukkan Password");
        inputWrapper.add(txtPassword);
        
        card.add(inputWrapper);
        
        // --- Button Section ---
        // Jarak ini dikurangi agar tombol lebih dekat dengan kotak password
        card.add(Box.createRigidArea(new Dimension(0, 20))); 

        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setMaximumSize(new Dimension(270, 40)); 
        btnLogin.setBackground(new Color(27, 94, 32));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setFocusPainted(false);
        btnLogin.setBorder(BorderFactory.createEmptyBorder());

        card.add(btnLogin);

        bgPanel.add(card);
        add(bgPanel);

        // Action Listener
        btnLogin.addActionListener(e -> {
            String user = txtUsername.getText().trim();
            String pass = new String(txtPassword.getPassword()).trim();
            UserServiceDefault service = new UserServiceDefault();
            
            if (service.login(user, pass)) {
                dispose();
                new MainFrame().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Username atau Password salah!", "Login Gagal", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void styleTextField(JTextField tf, String placeholder) {
        tf.setAlignmentX(Component.LEFT_ALIGNMENT);
        tf.setMaximumSize(new Dimension(270, 35)); 
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.putClientProperty("JTextField.placeholderText", placeholder);
        tf.putClientProperty("JTextField.showClearButton", true);
    }

    private void addInputLabel(String text, JPanel panel) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 10));
        l.setForeground(new Color(120, 120, 120));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(l);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
    }
}