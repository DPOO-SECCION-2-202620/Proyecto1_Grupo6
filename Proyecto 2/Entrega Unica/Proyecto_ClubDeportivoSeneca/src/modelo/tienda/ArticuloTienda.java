package modelo.tienda;

import modelo.comun.CategoriaArticulo;

public class ArticuloTienda extends Producto{

	private CategoriaArticulo categoria;

	public ArticuloTienda(String codigo, String nombre, double precio, CategoriaArticulo categoria) {
		super(codigo, nombre, precio);
		this.categoria = categoria;
	}

	public CategoriaArticulo getCategoria() {
		return categoria;
	}
	
	
	
	
	
}
