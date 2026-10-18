package modelo.tienda;

import java.time.LocalDate;
import java.util.List;

import modelo.usuarios.Usuarios;

public class VentaTienda extends Venta{

	public VentaTienda(LocalDate fecha, Usuarios comprador, CodigoDescuento codigoDescuento) {
		super(fecha, comprador);
		this.codigoDescuento = codigoDescuento; 
		// TODO Auto-generated constructor stub
	}
	private double descuentoAplicado;
	private CodigoDescuento codigoDescuento;
	
	private static final double IVA = 0.19;
	
	@Override
	public double calcularImpuesto() {
		// TODO Auto-generated method stub
		return calcularTotalConDescuento() * IVA;
	}


	@Override
	public double calcularTotalConDescuento() {
		// TODO Auto-generated method stub
		return super.calcularTotalConDescuento() - descuentoAplicado;
	}
	
	
	
	
	
	
	
	
}
