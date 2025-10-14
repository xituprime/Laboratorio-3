public class Cirujano extends TrabajadorMedico {
    protected String tipoOperacion;
    protected int horasCirugia;
    protected double tarifaHora;
    protected double bonoRiesgo;

    public Cirujano(String nombre, String departamento, int aniosExp, double salarioBase,
                    String tipoOperacion, int horasCirugia, double tarifaHora, double bonoRiesgo) {
        super(nombre, departamento, aniosExp, salarioBase);
        this.tipoOperacion = tipoOperacion;
        this.horasCirugia = horasCirugia;
        this.tarifaHora = tarifaHora;
        this.bonoRiesgo = bonoRiesgo;
    }

    @Override
    public double calcularSalario() {
        return salarioBase + (horasCirugia * tarifaHora) + bonoRiesgo;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" [Op: %s]", tipoOperacion);
    }
}
