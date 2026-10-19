package modelo.tienda;

import modelo.usuarios.Empleado;
import modelo.usuarios.Usuarios;

public class CodigoDescuento {

	private static final double PORCENTAJE_PROPIO = 0.15;
	private static final double PORCENTAJE_OTROS = 0.08;
	
	private String codigo;
	private Empleado empleado;
	
	
	public CodigoDescuento(String codigo, Empleado empleado) {
		super();
		this.codigo = codigo;
		this.empleado = empleado;
	}


	public String getCodigo() {
		return codigo;
	}


	public Empleado getEmpleado() {
		return empleado;
	}
	
    public double calcularPorcentaje(Usuarios comprador) {
        if (empleado.getLogin().equals(comprador.getLogin())) {
            return PORCENTAJE_PROPIO;
        }
        return PORCENTAJE_OTROS;
    }
	
	
	
}
