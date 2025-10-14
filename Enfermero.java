public class Enfermero extends TrabajadorMedico {
    protected String turno; // "diurno" o "nocturno"
    protected String nivelCertificacion;
    protected double bonificacionNocturna;

    public Enfermero(String nombre, String departamento, int aniosExp, double salarioBase,
                     String turno, String nivelCertificacion, double bonificacionNocturna) {
        super(nombre, departamento, aniosExp, salarioBase);
        this.turno = turno;
        this.nivelCertificacion = nivelCertificacion;
        this.bonificacionNocturna = bonificacionNocturna;
    }

    @Override
    public double calcularSalario() {
        double adicional = "nocturno".equalsIgnoreCase(turno) ? bonificacionNocturna : 0.0;
        return salarioBase + adicional;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" [Turno: %s]", turno);
    }
}
