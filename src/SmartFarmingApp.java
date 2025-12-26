import javax.swing.SwingUtilities;
import com.formdev.flatlaf.FlatLightLaf;
import view.LoginForm; // Ubah import ini

public class SmartFarmingApp {
    public static void main(String[] args) {
        // Pasang FlatLaf untuk tampilan modern
        try {
            FlatLightLaf.setup();
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf: " + ex.getMessage());
        }

        SwingUtilities.invokeLater(() -> {
            // JANGAN langsung MainFrame, panggil LoginForm dulu
            new LoginForm().setVisible(true);
        });
    }
}