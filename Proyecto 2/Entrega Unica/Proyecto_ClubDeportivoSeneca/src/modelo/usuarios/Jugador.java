package modelo.usuarios;

import java.time.LocalDate;

import modelo.comun.EstadoJugador;

public class Jugador extends Socio{

	private EstadoJugador estado;

	public Jugador(String login, String password, String nombre, LocalDate fechaNacimiento) {
		super(login, password, nombre, fechaNacimiento);
		this.estado = EstadoJugador.ACTIVO;
	}

	public EstadoJugador getEstado() {
		return estado;
	}
	
	
	public boolean estaActivo() {
	    return estado == EstadoJugador.ACTIVO;
	}

	public void darDeBaja() {
	    if (!estaActivo()) {
	        throw new IllegalStateException("El jugador ya está dado de baja");
	    }
	    estado = EstadoJugador.INACTIVO;
	}

	public void reactivar() {
	    if (estaActivo()) {
	        throw new IllegalStateException("El jugador ya está activo");
	    }
	    estado = EstadoJugador.ACTIVO;
	}
	
	
	
}
