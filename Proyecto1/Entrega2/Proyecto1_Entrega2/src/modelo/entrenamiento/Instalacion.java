package modelo.entrenamiento;

import modelo.comun.TipoInstalacion;

public class Instalacion {

	private String codigo;
	private TipoInstalacion tipo;
	private int capacidad;
	private boolean techada;
	
	public Instalacion(String codigo, TipoInstalacion tipo, int capacidad, boolean techada) {
		super();
		this.codigo = codigo;
		this.tipo = tipo;
		this.capacidad = capacidad;
		this.techada = techada;
		
		
	}

	public String getCodigo() {
		return codigo;
	}

	public TipoInstalacion getTipo() {
		return tipo;
	}

	public int getCapacidad() {
		return capacidad;
	}

	public boolean isTechada() {
		return techada;
	}
	
	public boolean restringeBebidasCalientes() {
	    if (tipo == TipoInstalacion.CANCHA_PADEL) {
	        return true;
	    }
	    return tipo == TipoInstalacion.MESA_TENIS_MESA && techada;
	}
	
}
