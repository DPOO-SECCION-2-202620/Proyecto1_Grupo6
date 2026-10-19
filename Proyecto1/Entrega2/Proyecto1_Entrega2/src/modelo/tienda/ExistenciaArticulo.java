package modelo.tienda;

public class ExistenciaArticulo {

	private ArticuloTienda articulo;
	private Ubicacion ubicacion;
	private int cantidad;
	public ExistenciaArticulo(ArticuloTienda articulo, Ubicacion ubicacion, int cantidad) {
		if (cantidad < 0) {
			throw new IllegalArgumentException("La cantidad inicial no puede ser negativa");
		}
		this.articulo = articulo;
		this.ubicacion = ubicacion;
		this.cantidad = cantidad;
	}
	public ArticuloTienda getArticulo() {
		return articulo;
	}
	public Ubicacion getUbicacion() {
		return ubicacion;
	}
	public int getCantidad() {
		return cantidad;
	}
	
	
	public boolean hayDisponibles(int unidades) {
		return unidades <= cantidad ;
	}
	
	public void agregarUnidades(int unidades) {
		if (unidades <= 0) {
			throw new IllegalArgumentException("La cantidad a agregar debe ser positiva");
		}
		cantidad += unidades;
	}
	
	public void retirarUnidades(int unidades) {
		if (unidades <= 0) {
	        throw new IllegalArgumentException("La cantidad a retirar debe ser positiva");
	    }
	    if (!hayDisponibles(unidades)) {
	        throw new IllegalStateException("No hay suficientes unidades en " + ubicacion.getNombre());
	    }
	    cantidad -= unidades;
	}
	
}
