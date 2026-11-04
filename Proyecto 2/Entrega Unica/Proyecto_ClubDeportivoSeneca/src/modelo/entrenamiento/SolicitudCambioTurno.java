package modelo.entrenamiento;

import java.time.LocalDate;

import modelo.comun.EstadoSugerencia;
import modelo.usuarios.Empleado;

public class SolicitudCambioTurno {

	
	private LocalDate fecha;
	private Empleado empleado;
	private Turno turnoActual;
	private Turno turnoNuevo;
	private EstadoSugerencia estado;
	
	
	public SolicitudCambioTurno(LocalDate fecha, Empleado empleado, Turno turnoActual, Turno turnoNuevo) {
	    if (turnoActual == null || turnoNuevo == null) {
	        throw new IllegalArgumentException("La solicitud debe tener el turno actual y el turno nuevo");
	    }
	    if (!empleado.getTurnos().contains(turnoActual)) {
	        throw new IllegalArgumentException("El empleado no tiene el turno que quiere cambiar");
	    }
	    this.fecha = fecha;
	    this.empleado = empleado;
	    this.turnoActual = turnoActual;
	    this.turnoNuevo = turnoNuevo;
	    this.estado = EstadoSugerencia.PENDIENTE;
	}


	public LocalDate getFecha() {
		return fecha;
	}


	public Empleado getEmpleado() {
		return empleado;
	}


	public Turno getTurnoActual() {
		return turnoActual;
	}


	public Turno getTurnoNuevo() {
		return turnoNuevo;
	}


	public EstadoSugerencia getEstado() {
		return estado;
	}
	
	
	
	public void aprobar() {
	    if (estado != EstadoSugerencia.PENDIENTE) {
	        throw new IllegalStateException("Solo se pueden aprobar solicitudes pendientes");
	    }
	    empleado.quitarTurno(turnoActual);
	    try {
	        empleado.agregarTurno(turnoNuevo);
	    } catch (IllegalStateException e) {
	        empleado.agregarTurno(turnoActual);
	        throw e;
	    }
	    estado = EstadoSugerencia.APROBADO;
	}

	public void rechazar() {
	    if (estado != EstadoSugerencia.PENDIENTE) {
	        throw new IllegalStateException("Solo se pueden rechazar solicitudes pendientes");
	    }
	    estado = EstadoSugerencia.RECHAZADO;
	}
	
	
	
}
