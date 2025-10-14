import javax.swing.*;

public class Hospital {
    public static void main(String[] args) {
        // Ejecutar GUI en EDT
        SwingUtilities.invokeLater(() -> {
            HospitalController controller = new HospitalController();
            VentanaPrincipal vp = new VentanaPrincipal(controller);
            vp.setVisible(true);
        });
    }
}
