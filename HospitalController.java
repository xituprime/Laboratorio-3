import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HospitalController {
    private List<TrabajadorMedico> personal;
    private List<CitaMedica> citas;

    public HospitalController() {
        personal = new ArrayList<>();
        citas = new ArrayList<>();
    }

    public void agregarTrabajador(TrabajadorMedico t) {
        if (t != null) personal.add(t);
    }

    public void crearCita(CitaMedica c) {
        if (c != null) {
            // detectar choques simples: misma fecha y mismo medico
            boolean choque = citas.stream()
                    .anyMatch(existing -> existing.getMedicoAsignado() != null
                            && existing.getMedicoAsignado().equals(c.getMedicoAsignado())
                            && existing.getFechaHora().equals(c.getFechaHora()));
            if (!choque) {
                citas.add(c);
            } else {
                // si choque, se podría reagendar o notificar; acá no hacemos prints, la vista gestiona notificaciones
                // Simple: no agregar la cita si choca.
            }
        }
    }

    public List<TrabajadorMedico> getPersonal() {
        return personal;
    }

    public List<CitaMedica> getCitas() {
        return citas;
    }

    public double calcularNominaTotal() {
        return personal.stream().mapToDouble(TrabajadorMedico::calcularSalario).sum();
    }

    public TrabajadorMedico buscarDisponible(String claseSimpleName) {
        // Devuelve el primer trabajador cuyo tipo de clase coincide con claseSimpleName
        return personal.stream()
                .filter(p -> p.getClass().getSimpleName().equalsIgnoreCase(claseSimpleName))
                .findFirst()
                .orElse(null);
    }

    public List<CitaMedica> citasEnFecha(LocalDateTime fecha) {
        return citas.stream()
                .filter(c -> c.getFechaHora().toLocalDate().equals(fecha.toLocalDate()))
                .collect(Collectors.toList());
    }

    public void reagendarAutomatico(CitaMedica cita, LocalDateTime nuevaFecha) {
        // ejemplo simple: actualizar y marcar
        cita.reagendar(nuevaFecha);
    }
}
