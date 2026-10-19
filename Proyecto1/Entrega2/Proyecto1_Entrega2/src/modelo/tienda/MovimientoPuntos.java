package modelo.tienda;

import java.time.LocalDate;

import modelo.comun.TipoMovimiento;
import modelo.usuarios.Usuarios;

public class MovimientoPuntos {

	private LocalDate fecha;
	private int cantidad;
	private TipoMovimiento tipo;
	private Usuarios usuario;
	
	
	public MovimientoPuntos(LocalDate fecha, int cantidad, TipoMovimiento tipo, Usuarios usuario) {
		if(cantidad < 0) {
			throw new IllegalArgumentException("La cantidad de puntos no puede ser negativa");
		}
		this.fecha = fecha;
		this.cantidad = cantidad;
		this.tipo = tipo;
		this.usuario = usuario;
	}


	public LocalDate getFecha() {
		return fecha;
	}


	public int getCantidad() {
		return cantidad;
	}


	public TipoMovimiento getTipo() {
		return tipo;
	}


	public Usuarios getUsuario() {
		return usuario;
	}
	
	
	
	
	
	
	
	
	
}
