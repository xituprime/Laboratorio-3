public abstract class TrabajadorMedico {
    private static int contadorId = 0;
    protected int id;
    protected String nombreCompleto;
    protected String departamento;
    protected int aniosExperiencia;
    protected double salarioBase;

    public TrabajadorMedico(String nombre, String departamento, int aniosExperiencia, double salarioBase) {
        this.id = ++contadorId;
        this.nombreCompleto = nombre;
        this.departamento = departamento;
        this.aniosExperiencia = aniosExperiencia;
        this.salarioBase = salarioBase;
    }

    public int getId() {
        return id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getDepartamento() {
        return departamento;
    }

    public int getAniosExperiencia() {
        return aniosExperiencia;
    }

    public double getSalarioBase() {
        return salarioBase;
    }

    public abstract double calcularSalario();

    @Override
    public String toString() {
        return String.format("%d - %s (%s)", id, nombreCompleto, this.getClass().getSimpleName());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof TrabajadorMedico)) return false;
        return this.id == ((TrabajadorMedico) obj).id;
    }
}