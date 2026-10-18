package modelo.tienda;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import modelo.usuarios.Usuarios;

public abstract class Venta {

	
	public Venta(LocalDate fecha,Usuarios comprador) {
		super();
		this.fecha = fecha;
		this.impuesto = 0;
		this.descuentoPorPuntos = 0;
		this.comprador = comprador;
		this.detalles = new ArrayList<>();
	}
	protected LocalDate fecha;
	protected double impuesto;
	protected double descuentoPorPuntos;
	protected Usuarios comprador;
	protected List<DetalleVenta> detalles;
	
	
	
	
	public double getImpuesto() {
		return impuesto;
	}
	public void setImpuesto(double impuesto) {
		this.impuesto = impuesto;
	}
	public double getDescuentoPorPuntos() {
		return descuentoPorPuntos;
	}
	public void setDescuentoPorPuntos(double descuentoPorPuntos) {
		this.descuentoPorPuntos = descuentoPorPuntos;
	}
	public LocalDate getFecha() {
		return fecha;
	}
	public Usuarios getComprador() {
		return comprador;
	}
	public List<DetalleVenta> getDetalles() {
		return detalles;
	}
	
	
	public void agregarDetalle(DetalleVenta detalle) {
		detalles.add(detalle);
	}
	
	public double calcularSubtotal() {
		double subtotal = 0;
		
		for (DetalleVenta detalle : detalles) {
			subtotal += detalle.calcularSubtotal();
		}
		
		return subtotal;
	}
	
	public abstract double calcularImpuesto();
	
	public double calcularTotalConDescuento() {
		return calcularSubtotal() - descuentoPorPuntos;
	}
	
	public double calcularTotal() {
		return calcularTotalConDescuento() + calcularImpuesto();
	}
	
	public int calcularPuntosGanados() {
		return (int) (calcularTotal() * 0.02);
	}
	
	
	
}
