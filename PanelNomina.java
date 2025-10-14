
import javax.swing.*;
import java.awt.*;

public class PanelNomina extends JPanel {
    private HospitalController manager;
    private JLabel lblTotal;

    public PanelNomina(HospitalController manager) {
        this.manager = manager;
        initComponents();
    }

    private void initComponents() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
        JButton btnCalcular = new JButton("Calcular nómina total");
        lblTotal = new JLabel("Total: Q0.00");
        btnCalcular.addActionListener(e -> mostrarNomina());

        add(btnCalcular);
        add(lblTotal);
    }

    public void mostrarNomina() {
        double total = manager.calcularNominaTotal();
        lblTotal.setText(String.format("Total: Q%.2f", total));
    }
}
