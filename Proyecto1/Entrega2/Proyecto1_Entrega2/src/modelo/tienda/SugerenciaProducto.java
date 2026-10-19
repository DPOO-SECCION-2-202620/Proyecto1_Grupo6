package modelo.tienda;

import java.time.LocalDate;

import modelo.comun.EstadoSugerencia;
import modelo.usuarios.Empleado;

public class SugerenciaProducto {

	private LocalDate fecha;
	private String descripcion;
	private EstadoSugerencia estado;
	private Empleado empleado;
	public SugerenciaProducto(LocalDate fecha, String descripcion, Empleado empleado) {
		super();
		this.fecha = fecha;
		this.descripcion = descripcion;
		this.empleado = empleado;
		this.estado = EstadoSugerencia.PENDIENTE;
		
	}
	public LocalDate getFecha() {
		return fecha;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public EstadoSugerencia getEstado() {
		return estado;
	}
	public Empleado getEmpleado() {
		return empleado;
	}

	
	public void aprobar() {
	    if (estado != EstadoSugerencia.PENDIENTE) {
	        throw new IllegalStateException("Solo se pueden aprobar sugerencias pendientes");
	    }
	    estado = EstadoSugerencia.APROBADO;
	}

	public void rechazar() {
	    if (estado != EstadoSugerencia.PENDIENTE) {
	        throw new IllegalStateException("Solo se pueden rechazar sugerencias pendientes");
	    }
	    estado = EstadoSugerencia.RECHAZADO;
	}
	
	
}
