import java.time.LocalDateTime;

public class CitaMedica {
    private static int contadorId = 0;
    protected int idCita;
    protected String paciente;
    protected TrabajadorMedico medicoAsignado;
    protected LocalDateTime fechaHora;
    protected String tipoCita;
    protected String estado;

    public CitaMedica(String paciente, TrabajadorMedico medicoAsignado, LocalDateTime fechaHora,
                     String tipoCita, String estado) {
        this.idCita = ++contadorId;
        this.paciente = paciente;
        this.medicoAsignado = medicoAsignado;
        this.fechaHora = fechaHora;
        this.tipoCita = tipoCita;
        this.estado = estado;
    }

    public void reagendar(LocalDateTime nuevaFecha) {
        this.fechaHora = nuevaFecha;
        this.estado = "Reagendada";
    }

    public void cambiarEstado(String nuevoEstado) {
        this.estado = nuevoEstado;
    }

    @Override
    public String toString() {
        return String.format("Cita ID: %d, Paciente: %s, Médico: %s, Fecha y Hora: %s, Tipo: %s, Estado: %s",
                idCita, paciente, medicoAsignado.getNombreCompleto(), fechaHora.toString(), tipoCita, estado);
    }
    
}
