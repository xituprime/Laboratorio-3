public class Terapeuta extends TrabajadorMedico {
    protected String tipoTerapia;
    protected int duracionSesion;
    protected int sesionesRealizadas;
    protected double pagoPorSesion;

    public Terapeuta(String nombre, String departamento, int aniosExp, double salarioBase,
                    String tipoTerapia, int duracionSesion, int sesionesRealizadas, double pagoPorSesion) {
        super(nombre, departamento, aniosExp, salarioBase);
        this.tipoTerapia = tipoTerapia;
        this.duracionSesion = duracionSesion;
        this.sesionesRealizadas = sesionesRealizadas;
        this.pagoPorSesion = pagoPorSesion;
    }

    @Override
    public double calcularSalario() {
        return salarioBase + (pagoPorSesion * sesionesRealizadas);
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" [Terapia: %s]", tipoTerapia);
    }
}
