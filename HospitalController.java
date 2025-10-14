import java.util.List;

public class HospitalController {
    private List<TrabajadorMedico> trabajadores;
    private List<CitaMedica> citas;

    public HospitalController(List<TrabajadorMedico> trabajadores, List<CitaMedica> citas) {
        this.trabajadores = trabajadores;
        this.citas = citas;
    }

    public void agregarTrabajador(TrabajadorMedico trabajador) {
        trabajadores.add(trabajador);
    }

    public void crearCita(CitaMedica cita) {
        citas.add(cita);
    }

    public List<TrabajadorMedico> getPersonal() {
        return trabajadores;
    }

    public List<CitaMedica> getCitas() {
        return citas;
    }

    public Double calcularNominaTotal(){
        return trabajadores.stream().mapToDouble(TrabajadorMedico::calcularSalario).sum();
    }

    public TrabajadorMedico buscarDisponible(String especializacion){
        return trabajadores.stream()
                .filter(t -> t.getDepartamento().equalsIgnoreCase(especializacion))
                .findFirst()
                .orElse(null);
    }

    public void reagendarAutomatico(){
        citas.stream()
                .filter(c -> c.estado.equalsIgnoreCase("Pendiente"))
                .forEach(c -> {
                    TrabajadorMedico medico = buscarDisponible(c.medicoAsignado.getDepartamento());
                    if (medico != null) {
                        c.medicoAsignado = medico;
                        c.reagendar(c.fechaHora.plusDays(1));
                    }
                });
    }

}
