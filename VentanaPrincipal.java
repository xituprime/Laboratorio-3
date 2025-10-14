/*
 * Nombre: Axel Xitumul
 * Carnet: 25783
 */

import javax.swing.*;
import java.awt.*;

/*
 * Clase principal de la interfaz Swing.
 * Contiene tres paneles: Personal, Citas y Nómina.
 * Todos comparten el mismo controlador (HospitalController).
 */
public class VentanaPrincipal extends JFrame {
    private HospitalController manager;
    private PanelPersonal panelPersonal;
    private PanelCitas panelCitas;
    private PanelNomina panelNomina;

    public VentanaPrincipal(HospitalController manager) {
        super("Sistema de Gestión Hospitalaria");
        this.manager = manager;
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        // 🔹 Todos los paneles usan el mismo objeto manager
        panelPersonal = new PanelPersonal(manager);
        panelCitas = new PanelCitas(manager);
        panelNomina = new PanelNomina(manager);

        // 🔹 Conectamos los paneles: cuando se agrega un médico, se actualiza el combo en PanelCitas
        panelPersonal.setPanelCitas(panelCitas);

        tabs.addTab("Personal", panelPersonal);
        tabs.addTab("Citas", panelCitas);
        tabs.addTab("Nómina", panelNomina);

        getContentPane().add(tabs, BorderLayout.CENTER);
    }
}
