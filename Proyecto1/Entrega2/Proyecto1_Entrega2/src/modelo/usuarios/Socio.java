package modelo.usuarios;

import java.time.LocalDate;

import modelo.entrenamiento.Instalacion;

public class Socio extends Usuario{

	private Instalacion instalacionActual;
	private boolean tieneBebidaCaliente;
	public Socio(String login, String password, String nombre, LocalDate fechaNacimiento) {
		super(login, password, nombre, fechaNacimiento);
		this.instalacionActual = null;
		this.tieneBebidaCaliente = false;
	
	}
	public Instalacion getInstalacionActual() {
		return instalacionActual;
	}
	public boolean isTieneBebidaCaliente() {
		return tieneBebidaCaliente;
	}
	
	
	public void entrarInstalacion(Instalacion instalacion) {
	    if (instalacionActual != null) {
	        throw new IllegalStateException("El socio ya está dentro de una instalación");
	    }
	    if (tieneBebidaCaliente && instalacion.restringeBebidasCalientes()) {
	        throw new IllegalStateException("No puede entrar a esta instalación con una bebida caliente");
	    }
	    instalacionActual = instalacion;
	}

	public void salirInstalacion() {
	    instalacionActual = null;
	}

	public void recibirBebidaCaliente() {
	    if (instalacionActual != null && instalacionActual.restringeBebidasCalientes()) {
	        throw new IllegalStateException("No se puede despachar una bebida caliente dentro de esta instalación");
	    }
	    tieneBebidaCaliente = true;
	}

	public void terminarBebidaCaliente() {
	    tieneBebidaCaliente = false;
	}
	
	
}
