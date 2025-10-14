public class DoctorGeneral extends TrabajadorMedico {
    protected String especializacion;
    protected int pacientesPorDia;
    protected double tarifaConsulta;

    public DoctorGeneral(String nombre, String departamento, int aniosExp, double salarioBase,
                         String especializacion, int pacientesPorDia, double tarifaConsulta) {
        super(nombre, departamento, aniosExp, salarioBase);
        this.especializacion = especializacion;
        this.pacientesPorDia = pacientesPorDia;
        this.tarifaConsulta = tarifaConsulta;
    }

    @Override
    public double calcularSalario() {
        return salarioBase + (pacientesPorDia * tarifaConsulta);
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" [Esp: %s]", especializacion);
    }
}
