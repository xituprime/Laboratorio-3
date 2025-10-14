import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CitaMedica {
    private static int contadorId = 0;

    protected int idCita;
    protected String paciente;
    protected TrabajadorMedico medicoAsignado;
    protected LocalDateTime fechaHora;
    protected String tipoCita;
    protected String estado; // PROGRAMADA, CONFIRMADA, EN_PROGRESO, COMPLETADA, CANCELADA, REAGENDADA

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public CitaMedica(String paciente, TrabajadorMedico medicoAsignado, LocalDateTime fechaHora, String tipoCita) {
        this.idCita = ++contadorId;
        this.paciente = paciente;
        this.medicoAsignado = medicoAsignado;
        this.fechaHora = fechaHora;
        this.tipoCita = tipoCita;
        this.estado = "PROGRAMADA";
    }

    public int getIdCita() {
        return idCita;
    }

    public String getPaciente() {
        return paciente;
    }

    public TrabajadorMedico getMedicoAsignado() {
        return medicoAsignado;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public String getTipoCita() {
        return tipoCita;
    }

    public String getEstado() {
        return estado;
    }

    public void reagendar(LocalDateTime nuevaFecha) {
        this.fechaHora = nuevaFecha;
        this.estado = "REAGENDADA";
    }

    public void cambiarEstado(String nuevoEstado) {
        this.estado = nuevoEstado;
    }

    @Override
    public String toString() {
        return String.format("%d - %s con %s (%s) %s", idCita, paciente,
                medicoAsignado != null ? medicoAsignado.getNombreCompleto() : "Sin medico",
                tipoCita, fechaHora.format(FORMATTER));
    }
}
