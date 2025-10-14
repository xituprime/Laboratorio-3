/*
 * Nombre: Axel Xitumul
 * Carnet: 25783
 */

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

/*
 * Panel para registrar médicos y mostrarlos en tabla.
 * Actualiza el PanelCitas automáticamente cuando se agrega un nuevo trabajador.
 */
public class PanelPersonal extends JPanel {
    private HospitalController manager;
    private PanelCitas panelCitas; // 🔹 referencia al panel de citas

    private JTextField txtNombre;
    private JTextField txtDepartamento;
    private JTextField txtAnos;
    private JTextField txtSalarioBase;
    private JComboBox<String> cmbTipo;
    private JTextField txtExtra1;
    private JTextField txtExtra2;
    private JComboBox<String> cmbBoolean; // para campos booleanos (licencia controlada)
    private JPanel cardPanelExtra2; // para alternar entre txtExtra2 y cmbBoolean
    private JLabel lblExtra1;
    private JLabel lblExtra2;
    private DefaultTableModel tableModel;

    public PanelPersonal(HospitalController manager) {
        this.manager = manager;
        initComponents();
    }

    // 🔹 Método para conectar este panel con PanelCitas
    public void setPanelCitas(PanelCitas panelCitas) {
        this.panelCitas = panelCitas;
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(0, 4, 8, 8));
        txtNombre = new JTextField();
        txtDepartamento = new JTextField();
        txtAnos = new JTextField();
        txtSalarioBase = new JTextField();

        cmbTipo = new JComboBox<>(new String[]{
                "DoctorGeneral", "Cirujano", "Enfermero", "Radiologo", "Farmaceutico", "Terapeuta"
        });
        cmbTipo.addActionListener(e -> actualizarCamposExtra());

    lblExtra1 = new JLabel("Campo extra 1:");
    lblExtra2 = new JLabel("Campo extra 2:");
    txtExtra1 = new JTextField();
    txtExtra2 = new JTextField();
    cmbBoolean = new JComboBox<>(new String[]{"false","true"});
    cardPanelExtra2 = new JPanel(new CardLayout());
    cardPanelExtra2.add(txtExtra2, "TEXT");
    cardPanelExtra2.add(cmbBoolean, "BOOL");

        form.add(new JLabel("Nombre:")); form.add(txtNombre);
        form.add(new JLabel("Departamento:")); form.add(txtDepartamento);
        form.add(new JLabel("Años experiencia:")); form.add(txtAnos);
        form.add(new JLabel("Salario base:")); form.add(txtSalarioBase);
    form.add(new JLabel("Tipo trabajador:")); form.add(cmbTipo);
    form.add(lblExtra1); form.add(txtExtra1);
    form.add(lblExtra2); form.add(cardPanelExtra2);

        JButton btnAgregar = new JButton("Agregar trabajador");
        btnAgregar.addActionListener(this::onAgregarTrabajador);
        form.add(btnAgregar);

        add(form, BorderLayout.NORTH);

    String[] cols = {"ID", "Nombre", "Tipo", "Departamento", "Salario Base"};
    tableModel = new DefaultTableModel(cols, 0);
    JTable tabla = new JTable(tableModel);
    // Hacer la tabla más grande para ver mejor los trabajadores
    tabla.setPreferredScrollableViewportSize(new Dimension(800, 300));
    tabla.setFillsViewportHeight(true);
    JScrollPane scroll = new JScrollPane(tabla);
    scroll.setPreferredSize(new Dimension(800, 300));
    add(scroll, BorderLayout.CENTER);

        actualizarCamposExtra();
        actualizarTabla();
    }

    private void actualizarCamposExtra() {
        String tipo = (String) cmbTipo.getSelectedItem();
        switch (tipo) {
            case "DoctorGeneral" -> {
                lblExtra1.setText("Especialización:");
                lblExtra2.setText("Pacientes;Tarifa:");
                txtExtra1.setToolTipText("Especialización");
                txtExtra2.setToolTipText("Pacientes por día ; tarifa (ej: 20;15.0)");
            }
            case "Cirujano" -> {
                lblExtra1.setText("Tipo operación:");
                lblExtra2.setText("Horas;Tarifa;Bono:");
                txtExtra1.setToolTipText("Tipo operación");
                txtExtra2.setToolTipText("Horas;tarifaHora;bonoRiesgo (ej:3;150.0;200.0)");
            }
            case "Enfermero" -> {
                lblExtra1.setText("Turno:");
                lblExtra2.setText("Bonificación nocturna:");
                txtExtra1.setToolTipText("Turno (diurno/nocturno)");
                txtExtra2.setToolTipText("Bonificación nocturna (ej:50.0)");
            }
            case "Radiologo" -> {
                lblExtra1.setText("Equipo certificado:");
                lblExtra2.setText("Tarifa;Estudios:");
                txtExtra1.setToolTipText("Equipo certificado");
                txtExtra2.setToolTipText("Tarifa;estudiosRealizados (ej:30.0;5)");
            }
            case "Farmaceutico" -> {
                lblExtra1.setText("Límite prescripciones:");
                lblExtra2.setText("Licencia controlada:");
                txtExtra1.setToolTipText("Límite prescripciones (int)");
                txtExtra2.setToolTipText("Licencia controlada (true/false)");
                CardLayout cl = (CardLayout) cardPanelExtra2.getLayout();
                cl.show(cardPanelExtra2, "BOOL");
            }
            case "Terapeuta" -> {
                lblExtra1.setText("Tipo terapia:");
                lblExtra2.setText("Duración;Sesiones;Pago:");
                txtExtra1.setToolTipText("Tipo terapia");
                txtExtra2.setToolTipText("Duración;sesiones;pagoPorSesion (ej:45;10;20.0)");
                CardLayout cl2 = (CardLayout) cardPanelExtra2.getLayout();
                cl2.show(cardPanelExtra2, "TEXT");
            }
            default -> {
                lblExtra1.setText("Campo extra 1:");
                lblExtra2.setText("Campo extra 2:");
                txtExtra1.setToolTipText("");
                txtExtra2.setToolTipText("");
                CardLayout cl3 = (CardLayout) cardPanelExtra2.getLayout();
                cl3.show(cardPanelExtra2, "TEXT");
            }
        }
    }

    private void onAgregarTrabajador(ActionEvent e) {
        try {
            String nombre = txtNombre.getText().trim();
            String depto = txtDepartamento.getText().trim();
            int anos = Integer.parseInt(txtAnos.getText().trim());
            double salarioBase = Double.parseDouble(txtSalarioBase.getText().trim());
            String tipo = (String) cmbTipo.getSelectedItem();
            TrabajadorMedico t = null;

            String e1 = txtExtra1.getText().trim();
            String e2 = txtExtra2.getText().trim();

            switch (tipo) {
                case "DoctorGeneral" -> {
                    String esp = e1;
                    String[] p = e2.split(";");
                    int pacientes = p.length > 0 ? Integer.parseInt(p[0]) : 0;
                    double tarifa = p.length > 1 ? Double.parseDouble(p[1]) : 0.0;
                    t = new DoctorGeneral(nombre, depto, anos, salarioBase, esp, pacientes, tarifa);
                }
                case "Cirujano" -> {
                    String tipoOp = e1;
                    String[] c = e2.split(";");
                    int horas = c.length > 0 ? Integer.parseInt(c[0]) : 0;
                    double tarifaHora = c.length > 1 ? Double.parseDouble(c[1]) : 0.0;
                    double bono = c.length > 2 ? Double.parseDouble(c[2]) : 0.0;
                    t = new Cirujano(nombre, depto, anos, salarioBase, tipoOp, horas, tarifaHora, bono);
                }
                case "Enfermero" -> {
                    double bon = e2.isEmpty() ? 0.0 : Double.parseDouble(e2);
                    t = new Enfermero(nombre, depto, anos, salarioBase, e1, "N/A", bon);
                }
                case "Radiologo" -> {
                    String[] r = e2.split(";");
                    double tarifa = r.length > 0 ? Double.parseDouble(r[0]) : 0.0;
                    int estudios = r.length > 1 ? Integer.parseInt(r[1]) : 0;
                    t = new Radiologo(nombre, depto, anos, salarioBase, e1, tarifa, estudios);
                }
                    case "Farmaceutico" -> {
                        int lim = e1.isEmpty() ? 0 : Integer.parseInt(e1);
                        // leer licencias desde combobox si está visible, sino desde texto (retrocompatibilidad)
                        boolean lic;
                        // si el card muestra BOOL, leer desde cmbBoolean, sino intentar leer texto
                        // no hay método público para obtener el card actual, así que intentar leer cmbBoolean si e2 vacío
                        if (!e2.isEmpty()) {
                            lic = "true".equalsIgnoreCase(e2);
                        } else {
                            lic = "true".equalsIgnoreCase((String) cmbBoolean.getSelectedItem());
                        }
                        t = new Farmaceutico(nombre, depto, anos, salarioBase, lim, lic);
                    }
                case "Terapeuta" -> {
                    String[] tp = e2.split(";");
                    int dur = tp.length > 0 ? Integer.parseInt(tp[0]) : 0;
                    int ses = tp.length > 1 ? Integer.parseInt(tp[1]) : 0;
                    double pago = tp.length > 2 ? Double.parseDouble(tp[2]) : 0.0;
                    t = new Terapeuta(nombre, depto, anos, salarioBase, e1, dur, ses, pago);
                }
            }

            if (t != null) {
                manager.agregarTrabajador(t);
                actualizarTabla();

                // 🔹 Actualiza el combo de médicos en PanelCitas automáticamente
                if (panelCitas != null) {
                    panelCitas.recargarMedicos();
                }

                limpiarFormulario();
                JOptionPane.showMessageDialog(this, "Trabajador agregado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Revisa los campos numéricos.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al agregar trabajador.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarFormulario() {
        txtNombre.setText("");
        txtDepartamento.setText("");
        txtAnos.setText("");
        txtSalarioBase.setText("");
        txtExtra1.setText("");
        txtExtra2.setText("");
    }

    public void actualizarTabla() {
        tableModel.setRowCount(0);
        for (TrabajadorMedico t : manager.getPersonal()) {
            tableModel.addRow(new Object[]{
                    t.getId(),
                    t.getNombreCompleto(),
                    t.getClass().getSimpleName(),
                    t.getDepartamento(),
                    t.getSalarioBase()
            });
        }
    }
}
