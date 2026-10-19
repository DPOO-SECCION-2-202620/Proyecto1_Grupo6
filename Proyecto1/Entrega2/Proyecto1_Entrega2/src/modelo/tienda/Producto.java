package modelo.tienda;

public abstract class Producto {
	
	private String codigo;
	private String nombre;
	private double precio;
	public Producto(String codigo, String nombre, double precio) {
		super();
		this.codigo = codigo;
		this.nombre = nombre;
		this.precio = precio;
	}
	public double getPrecio() {
		return precio;
	}
	public void setPrecio(double precio) {
		this.precio = precio;
	}
	public String getCodigo() {
		return codigo;
	}
	
	
	
	
		

}
