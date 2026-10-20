package modelo.tienda;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import modelo.comun.TipoMovimiento;
import modelo.usuarios.Usuario;

public abstract class Venta {

	
	public Venta(LocalDate fecha,Usuario comprador) {
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
	protected Usuario comprador;
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
	public Usuario getComprador() {
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
	
	
	
	public MovimientoPuntos redimirPuntos(int puntos) {
		if (puntos <= 0) {
            throw new IllegalArgumentException("La cantidad de puntos debe ser positiva");
        }
        if (descuentoPorPuntos > 0) {
            throw new IllegalStateException("Ya se redimieron puntos en esta venta");
        }
        if (puntos > comprador.getPuntos()) {
            throw new IllegalStateException("El comprador no tiene suficientes puntos");
        }
        if (puntos > calcularSubtotal()) {
            throw new IllegalArgumentException("No se pueden redimir más puntos que el subtotal");
        }
        descuentoPorPuntos = puntos;
        comprador.restarPuntos(puntos);
        return new MovimientoPuntos(fecha, puntos, TipoMovimiento.REDIMIDOS, comprador);
	}
	
	
	
	public MovimientoPuntos registrarPuntosGanados() {
        int puntos = calcularPuntosGanados();
        comprador.sumarPuntos(puntos);
        return new MovimientoPuntos(fecha, puntos, TipoMovimiento.GANADOS, comprador);
    }
}
