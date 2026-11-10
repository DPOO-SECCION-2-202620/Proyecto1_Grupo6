package modelo.torneos;

import modelo.usuarios.Empleado;

/** Registro de un empleado inscrito en un torneo individual.
 * Los empleados participan gratis y no reciben premios ni bonos. */
public class InscripcionTorneoEmpleado {
    private final Empleado empleado;
    private final Torneo torneo;
    private final double valorPagado;
    private final boolean recibePremio;

    public InscripcionTorneoEmpleado(Empleado empleado, Torneo torneo) {
        if (empleado == null || torneo == null) {
            throw new IllegalArgumentException("Empleado y torneo son obligatorios");
        }
        this.empleado = empleado;
        this.torneo = torneo;
        this.valorPagado = 0.0;
        this.recibePremio = false;
    }

    public Empleado getEmpleado() { return empleado; }
    public Torneo getTorneo() { return torneo; }
    public double getValorPagado() { return valorPagado; }
    public boolean isRecibePremio() { return recibePremio; }
}
