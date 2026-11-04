package modelo.tienda;

import java.time.LocalDate;

import modelo.usuarios.Socio;

public class Mensualidad {

	private static final double VALOR = 78000000;
	
	private LocalDate fechaPago;
	private Socio socio;
	public Mensualidad(LocalDate fechaPago, Socio socio) {
		super();
		this.fechaPago = fechaPago;
		this.socio = socio;
	}
	public LocalDate getFechaPago() {
		return fechaPago;
	}
	public Socio getSocio() {
		return socio;
	}
	
	public double getValor() {
		return VALOR;
	}
	
	
	
}
