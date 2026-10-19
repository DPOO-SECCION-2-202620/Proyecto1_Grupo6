package modelo.tienda;

import modelo.comun.TipoTemperatura;

public class Bebida extends ArticuloCafeteria{
	
	
	private TipoTemperatura temperatura;

	public Bebida(String codigo, String nombre, double precio, TipoTemperatura tipoTemperatura) {
		super(codigo, nombre, precio);
		this.temperatura = tipoTemperatura;
	}

	public TipoTemperatura getTipoTemperatura() {
		return temperatura;
	}
	


	public boolean esCaliente() {
		return temperatura == temperatura.CALIENTE;
	}
	
	
	
}
