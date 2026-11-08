package modelo.torneos;

public class BonoDeDescuento {

	private double porcentaje;
	private boolean usado;
	
	
	public BonoDeDescuento(double porcentaje) {
		if(porcentaje<=0 || porcentaje>1) {
			throw new IllegalArgumentException("El porcentaje debe estar entre 0 y 1");
		}
		this.porcentaje = porcentaje;
		this.usado = false;
	}


	public double getPorcentaje() {
		return porcentaje;
	}


	public boolean isUsado() {
		return usado;
	}
	
	public double calcularDescuento(double subtotal) {
		return subtotal*porcentaje;
	}
	
	public void marcarUsado() {
		if(usado == true) {
			throw new IllegalStateException("El bono ya fue usado");
		}
		else {
			usado = true;
		}
			
	}
	
	
}
