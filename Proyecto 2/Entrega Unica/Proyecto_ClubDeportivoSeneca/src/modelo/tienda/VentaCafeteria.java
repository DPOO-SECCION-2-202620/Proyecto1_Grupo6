package modelo.tienda;

import java.time.LocalDate;

import modelo.usuarios.Usuario;

public class VentaCafeteria extends Venta{

	private static final double IMPUESTO_CONSUMO = 0.08;
	private static final double PROPINA_SUGERIDA = 0.10;
	
	private double propina;

	public VentaCafeteria(LocalDate fecha, Usuario comprador) {
		super(fecha, comprador);
		this.propina = 0;
		// TODO Auto-generated constructor stub
	}

	public double getPropina() {
		return propina;
	}

	public void setPropina(double propina) {
		this.propina = propina;
	}

	@Override
	public double calcularImpuesto() {
		// TODO Auto-generated method stub
		return calcularTotalConDescuento() * IMPUESTO_CONSUMO;
	}
	
	public void aplicarPropina() {
		this.propina = calcularTotal() * PROPINA_SUGERIDA;
	}
	
}
