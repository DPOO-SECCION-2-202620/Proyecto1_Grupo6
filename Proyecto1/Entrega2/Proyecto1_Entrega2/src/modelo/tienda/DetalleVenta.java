package modelo.tienda;

public class DetalleVenta {

	private Producto producto;
	private int cantidad;
	private double precioUnitario;
	
	
	public DetalleVenta(Producto producto, int cantidad, double precioUnitario) {
		super();
		this.producto = producto;
		this.cantidad = cantidad;
		this.precioUnitario = precioUnitario;
	}


	public Producto getProducto() {
		return producto;
	}


	public int getCantidad() {
		return cantidad;
	}


	public double getPrecioUnitario() {
		return precioUnitario;
	}
	
	
	public double calcularSubtotal() {
		return cantidad * precioUnitario;
		
	}
	
	
	
}
