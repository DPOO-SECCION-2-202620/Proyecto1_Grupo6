package modelo;

import java.util.ArrayList;
import java.util.List;

import modelo.individual.DisciplinaIndividual;
import modelo.tienda.ExistenciaArticulo;
import modelo.tienda.Producto;
import modelo.tienda.SugerenciaProducto;
import modelo.tienda.Ubicacion;
import modelo.torneos.Torneo;
import modelo.usuarios.Usuario;

public class Club {

	private List<Usuario> usuarios;
	private List<DisciplinaIndividual> disciplinas;
	private List<Ubicacion> ubicaciones;
	private List<ExistenciaArticulo> existencias;
	private List<Producto> productos;
	private List<SugerenciaProducto>sugerencias;
	private List<Torneo> torneos;
	public Club() {
		this.disciplinas = new ArrayList<>();
		this.existencias = new ArrayList<>();
		this.productos = new ArrayList<>();
		this.sugerencias = new ArrayList<>();
		this.torneos = new ArrayList<>();
		this.ubicaciones = new ArrayList<>();
		this.usuarios = new ArrayList<>();
	}
	
	
	public void agregarUsuario(Usuario usuario) {
		usuarios.add(usuario);
	}
	
	public void agregarDisciplinaIndividual(DisciplinaIndividual disciplinaIndividual) {
		disciplinas.add(disciplinaIndividual);
	}
	
	
	public void agregarUbicacion(Ubicacion ubicacion) {
		ubicaciones.add(ubicacion);
		
	}
	
	
	public void agregarExistenciaArticulo(ExistenciaArticulo existenciaArticulo) {
		existencias.add(existenciaArticulo);
	}
	
	public void agregarProducto(Producto producto) {
		productos.add(producto);
		
	}
	
	public void agregarSugerenciaProducto(SugerenciaProducto sugerenciaProducto) {
		sugerencias.add(sugerenciaProducto);
	
	}
	
	public void agregarTorneo(Torneo torneo) {
		torneos.add(torneo);
	}


	public List<Usuario> getUsuarios() {
		return new ArrayList<>(usuarios);
	}


	public List<DisciplinaIndividual> getDisciplinas() {
		return new ArrayList<>(disciplinas);
	}


	public List<Ubicacion> getUbicaciones() {
		return new ArrayList<>(ubicaciones);
	}


	public List<ExistenciaArticulo> getExistencias() {
		return new ArrayList<>(existencias);
	}


	public List<Producto> getProductos() {
		return new ArrayList<>(productos);
	}


	public List<SugerenciaProducto> getSugerencias() {
		return new ArrayList<>(sugerencias);
	}


	public List<Torneo> getTorneos() {
		return new ArrayList<>(torneos);
	}
	
	
	
	
	
	
}




