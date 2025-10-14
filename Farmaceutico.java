public class Farmaceutico extends TrabajadorMedico {
    protected int limitePrescripciones;
    protected boolean licenciaControlada;

    public Farmaceutico(String nombre, String departamento, int aniosExp, double salarioBase,
                       int limitePrescripciones, boolean licenciaControlada) {
        super(nombre, departamento, aniosExp, salarioBase);
        this.limitePrescripciones = limitePrescripciones;
        this.licenciaControlada = licenciaControlada;
    }

    @Override
    public double calcularSalario() {
        double bono = licenciaControlada ? 300.0 : 0.0;
        return salarioBase + bono + (limitePrescripciones * 2.0);
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" [Limite Prescripciones: %d, Licencia Controlada: %b]",
                limitePrescripciones, licenciaControlada);
    }
}
