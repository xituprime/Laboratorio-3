import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Calendar;
import javax.swing.SpinnerDateModel;

public class PanelCitas extends JPanel {
    private HospitalController manager;
    private JTextField txtPaciente;
    private JComboBox<TrabajadorMedico> cmbMedico;
    private JComboBox<String> cmbTipoCita;
    // txtFechaHora removed; using dateSpinner for date input
    private JSpinner dateSpinner;
    private DefaultTableModel tableModel;

    public PanelCitas(HospitalController manager) {
        this.manager = manager;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        txtPaciente = new JTextField();
        cmbMedico = new JComboBox<>();
        cmbTipoCita = new JComboBox<>(new String[]{"Consulta General", "Cirugía", "Terapia", "Diagnóstico"});
    // Campo de fecha: usar JSpinner con SpinnerDateModel para entrada más amigable
    dateSpinner = new JSpinner(new SpinnerDateModel());
    JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd HH:mm");
    dateSpinner.setEditor(timeEditor);

        form.add(new JLabel("Paciente:"));
        form.add(txtPaciente);
        form.add(new JLabel("Médico:"));
        form.add(cmbMedico);
        form.add(new JLabel("Tipo cita:"));
        form.add(cmbTipoCita);
    form.add(new JLabel("Fecha y hora (yyyy-MM-dd HH:mm):"));
    form.add(dateSpinner);

    JButton btnProgramar = new JButton("Programar cita");
    btnProgramar.addActionListener(e -> onProgramarCita());
    form.add(btnProgramar);

    JButton btnReprogramar = new JButton("Reprogramar cita seleccionada");
    btnReprogramar.addActionListener(e -> onReprogramarSeleccionada());
    form.add(btnReprogramar);

        add(form, BorderLayout.NORTH);

        String[] cols = {"ID", "Paciente", "Medico", "Tipo", "FechaHora", "Estado"};
        tableModel = new DefaultTableModel(cols, 0);
        JTable tabla = new JTable(tableModel);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

    actualizarMedicos();
    actualizarTabla();
    }

    private void actualizarMedicos() {
        // Filtrar médicos según el tipo de cita seleccionado (ej: "Cirugía" -> Cirujano)
        String tipoC = (String) cmbTipoCita.getSelectedItem();
        String claseNecesaria = null;
        if ("Cirugía".equalsIgnoreCase(tipoC)) claseNecesaria = "Cirujano";
        else if ("Consulta General".equalsIgnoreCase(tipoC)) claseNecesaria = "DoctorGeneral";
        else if ("Terapia".equalsIgnoreCase(tipoC)) claseNecesaria = "Terapeuta";
        else if ("Diagnóstico".equalsIgnoreCase(tipoC)) claseNecesaria = "Radiologo";

        DefaultComboBoxModel<TrabajadorMedico> model = new DefaultComboBoxModel<>();
        for (TrabajadorMedico t : manager.getPersonal()) {
            if (claseNecesaria == null || t.getClass().getSimpleName().equalsIgnoreCase(claseNecesaria)) {
                model.addElement(t);
            }
        }
        cmbMedico.setModel(model);
        cmbMedico.setRenderer((list, value, index, isSelected, cellHasFocus) ->
                new JLabel(value == null ? "" : value.toString()));
    }

    // permitir que otras vistas forcen la recarga de médicos
    public void recargarMedicos() {
        actualizarMedicos();
    }

    private void onProgramarCita() {
        try {
            String paciente = txtPaciente.getText().trim();
            TrabajadorMedico medico = (TrabajadorMedico) cmbMedico.getSelectedItem();
            String tipo = (String) cmbTipoCita.getSelectedItem();
            Date fechaDate = (Date) dateSpinner.getValue();
            LocalDateTime fecha = LocalDateTime.ofInstant(fechaDate.toInstant(), ZoneId.systemDefault());

            String fechaStr = fecha.format(CitaMedica.FORMATTER);

            if (paciente.isEmpty() || medico == null || fechaStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Completa todos los campos.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // fecha ya parseada arriba
            CitaMedica cita = new CitaMedica(paciente, medico, fecha, tipo);

            // controller verificará choques simples
            // Intentar crear; si hay choque, ofrecer reprogramar
            boolean choque = false;
            for (CitaMedica existing : manager.getCitas()) {
                if (existing.getMedicoAsignado() != null
                        && existing.getMedicoAsignado().equals(cita.getMedicoAsignado())
                        && existing.getFechaHora().equals(cita.getFechaHora())) {
                    choque = true;
                    break;
                }
            }
            if (!choque) {
                manager.crearCita(cita);
                actualizarTabla();
                JOptionPane.showMessageDialog(this, "Cita programada.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                int opt = JOptionPane.showConfirmDialog(this, "Ya existe una cita para este médico en esa fecha. ¿Deseas reprogramar?", "Choque de cita", JOptionPane.YES_NO_OPTION);
                if (opt == JOptionPane.YES_OPTION) {
                    // mostrar un JSpinner para escoger nueva fecha
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(fechaDate);
                    SpinnerDateModel sdm = new SpinnerDateModel(cal.getTime(), null, null, Calendar.MINUTE);
                    JSpinner spinner = new JSpinner(sdm);
                    JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "yyyy-MM-dd HH:mm");
                    spinner.setEditor(editor);
                    int r2 = JOptionPane.showConfirmDialog(this, spinner, "Selecciona nueva fecha", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if (r2 == JOptionPane.OK_OPTION) {
                        Date nueva = (Date) spinner.getValue();
                        LocalDateTime nuevaFecha = LocalDateTime.ofInstant(nueva.toInstant(), ZoneId.systemDefault());
                        // intentar crear en nueva fecha (verificando choques)
                        boolean choque2 = manager.getCitas().stream()
                                .anyMatch(existing -> existing.getMedicoAsignado() != null
                                        && existing.getMedicoAsignado().equals(cita.getMedicoAsignado())
                                        && existing.getFechaHora().equals(nuevaFecha));
                        if (choque2) {
                            JOptionPane.showMessageDialog(this, "La nueva fecha también choca con otra cita.", "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            cita.reagendar(nuevaFecha);
                            manager.crearCita(cita);
                            actualizarTabla();
                            JOptionPane.showMessageDialog(this, "Cita reprogramada.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "No se programó la cita.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Usa yyyy-MM-dd HH:mm", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al programar cita.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onReprogramarSeleccionada() {
        // pedir id de cita a reprogramar
        String idStr = JOptionPane.showInputDialog(this, "Ingresa ID de la cita a reprogramar:", "Reprogramar", JOptionPane.QUESTION_MESSAGE);
        if (idStr == null || idStr.trim().isEmpty()) return;
        try {
            int id = Integer.parseInt(idStr.trim());
            CitaMedica encontrada = null;
            for (CitaMedica c : manager.getCitas()) {
                if (c.getIdCita() == id) { encontrada = c; break; }
            }
            if (encontrada == null) {
                JOptionPane.showMessageDialog(this, "No se encontró la cita.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // mostrar spinner inicializado con la fecha actual de la cita
            Date initDate = Date.from(encontrada.getFechaHora().atZone(ZoneId.systemDefault()).toInstant());
            SpinnerDateModel sdm2 = new SpinnerDateModel(initDate, null, null, Calendar.MINUTE);
            JSpinner spinner2 = new JSpinner(sdm2);
            JSpinner.DateEditor editor2 = new JSpinner.DateEditor(spinner2, "yyyy-MM-dd HH:mm");
            spinner2.setEditor(editor2);
            int r = JOptionPane.showConfirmDialog(this, spinner2, "Ingresa nueva fecha (yyyy-MM-dd HH:mm):", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (r == JOptionPane.OK_OPTION) {
                Date nueva = (Date) spinner2.getValue();
                LocalDateTime nf = LocalDateTime.ofInstant(nueva.toInstant(), ZoneId.systemDefault());
                boolean choque2 = false;
                for (CitaMedica existing : manager.getCitas()) {
                    if (existing != encontrada
                            && existing.getMedicoAsignado() != null
                            && existing.getMedicoAsignado().equals(encontrada.getMedicoAsignado())
                            && existing.getFechaHora().equals(nf)) {
                        choque2 = true;
                        break;
                    }
                }
                if (choque2) {
                    JOptionPane.showMessageDialog(this, "La nueva fecha choca con otra cita.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                encontrada.reagendar(nf);
                actualizarTabla();
                JOptionPane.showMessageDialog(this, "Cita reprogramada.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID inválido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void actualizarTabla() {
        tableModel.setRowCount(0);
        for (CitaMedica c : manager.getCitas()) {
            tableModel.addRow(new Object[]{
                    c.getIdCita(),
                    c.getPaciente(),
                    c.getMedicoAsignado() != null ? c.getMedicoAsignado().getNombreCompleto() : "Sin medico",
                    c.getTipoCita(),
                    c.getFechaHora().format(CitaMedica.FORMATTER),
                    c.getEstado()
            });
        }
        actualizarMedicos();
    }
}
