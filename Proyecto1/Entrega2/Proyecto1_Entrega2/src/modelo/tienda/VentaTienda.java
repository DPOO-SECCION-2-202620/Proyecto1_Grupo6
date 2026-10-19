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


	public double getDescuentoAplicado() {
		return descuentoAplicado;
	}


	public void setDescuentoAplicado(double descuentoAplicado) {
		this.descuentoAplicado = descuentoAplicado;
	}


	public CodigoDescuento getCodigoDescuento() {
		return codigoDescuento;
	}
	
	
	
	 public void aplicarCodigoDescuento() {
	        if (codigoDescuento == null) {
	            descuentoAplicado = 0;
	            return;
	        }
	        if (descuentoPorPuntos > 0) {
	            throw new IllegalStateException("El código de descuento no es acumulable con puntos");
	        }
	        descuentoAplicado = calcularSubtotal() * codigoDescuento.calcularPorcentaje(comprador);
	    }

	    @Override
	    public MovimientoPuntos redimirPuntos(int puntos) {
	        if (descuentoAplicado > 0) {
	            throw new IllegalStateException("No se pueden redimir puntos si ya se aplicó un código de descuento");
	        }
	        return super.redimirPuntos(puntos);
	    }
	
	
	
	
	
	
}
