public class Radiologo extends TrabajadorMedico {
    protected String equipoCertificado;
    protected double tarifaPorEstudio;
    protected int estudiosRealizados;

    public Radiologo(String nombre, String departamento, int aniosExp, double salarioBase,
                     String equipoCertificado, double tarifaPorEstudio, int estudiosRealizados) {
        super(nombre, departamento, aniosExp, salarioBase);
        this.equipoCertificado = equipoCertificado;
        this.tarifaPorEstudio = tarifaPorEstudio;
        this.estudiosRealizados = estudiosRealizados;
    }

    @Override
    public double calcularSalario() {
        return salarioBase + (tarifaPorEstudio * estudiosRealizados);
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" [Equipo: %s]", equipoCertificado);
    }
}
