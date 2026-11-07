package modelo.tienda;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import modelo.comun.Alergeno;
import modelo.comun.EstadoSugerencia;
import modelo.usuarios.Empleado;

public class SugerenciaProducto {


	private LocalDate fecha;
	private String descripcion;
	private EstadoSugerencia estado;
	private Empleado empleado;
	
	private String codigo;
	private String nombre;
	private double precio;
	private boolean esSnack;
	private List<Alergeno> alergenos;
	
	
	public SugerenciaProducto(LocalDate fecha, String descripcion, Empleado empleado, String codigo, String nombre, double precio, boolean esSnack) {
		
		this.fecha = fecha;
		this.descripcion = descripcion;
		this.empleado = empleado;
		this.estado = EstadoSugerencia.PENDIENTE;
		
		this.codigo = codigo;
		this.nombre = nombre;
		this.precio = precio;
		this.esSnack = esSnack;
		this.alergenos = null;
		
	}
	public LocalDate getFecha() {
		return fecha;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public EstadoSugerencia getEstado() {
		return estado;
	}
	public Empleado getEmpleado() {
		return empleado;
	}

	
	public void aprobar() {
	    if (estado != EstadoSugerencia.PENDIENTE) {
	        throw new IllegalStateException("Solo se pueden aprobar sugerencias pendientes");
	    }
	    estado = EstadoSugerencia.APROBADO;
	}

	public void rechazar() {
	    if (estado != EstadoSugerencia.PENDIENTE) {
	        throw new IllegalStateException("Solo se pueden rechazar sugerencias pendientes");
	    }
	    estado = EstadoSugerencia.RECHAZADO;
	}
	public String getCodigo() {
		return codigo;
	}
	public String getNombre() {
		return nombre;
	}
	public double getPrecio() {
		return precio;
	}
	public boolean isEsSnack() {
		return esSnack;
	}
	public List<Alergeno> getAlergenos() {
		return alergenos;
	}
	
	
	public void declararAlergenos(List<Alergeno> lista) {
		this.alergenos = new ArrayList<>(lista);
	}
	
	public boolean tieneAlergenosDeclarados() {
		return alergenos != null;
	}
	
	
	public Producto crearProducto() {
		if(esSnack == true) {
			Snack snack = new Snack(codigo,nombre,precio);
			for(Alergeno a:alergenos) {
				snack.agregarAlergeno(a);
			}
			return snack;
					
		}			
			else {
				return new ArticuloTienda(codigo,nombre,precio,null);
			}
				
				
		
	}
	
}
