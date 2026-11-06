package modelo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import modelo.comun.Modalidad;
import modelo.comun.TipoDisciplina;
import modelo.individual.DisciplinaIndividual;
import modelo.individual.Reserva;
import modelo.tienda.ExistenciaArticulo;
import modelo.tienda.Producto;
import modelo.tienda.SugerenciaProducto;
import modelo.tienda.Ubicacion;
import modelo.torneos.Torneo;
import modelo.usuarios.Socio;
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
	
	
	public Usuario buscarUsuario(String login) {
		for(Usuario u: usuarios){
			if(u.getLogin().equals(login)) {
				return u;
			}
		}
		return null;
	}
	
	
	public Ubicacion buscarUbicacion(String nombre) {
		for(Ubicacion ubi :ubicaciones ) {
			if(ubi.getNombre().equals(nombre)) {
				return ubi;
			}
		}
		return null;
	}
	
	
	public Producto buscarProducto(String codigo) {
		for(Producto p : productos) {
			if(p.getCodigo().equals(codigo)) {
				return p;
				}
		}
		return null;
	}
	
	public DisciplinaIndividual buscarDisciplina(TipoDisciplina tipo) {
		for (DisciplinaIndividual dI : disciplinas) {
			if (dI.getTipo() == tipo) {
				return dI;
			}
		}
		return null;
	}
	
	
	
	public Usuario autenticar(String login, String password) {
		Usuario u = buscarUsuario(login);
		if (u == null || !u.verificarPassword(password)) {
			throw new IllegalArgumentException("Usuario o contraseña incorrectos");
		}
		return u;
	}
	
	public Reserva reservar(Socio socio , TipoDisciplina tipo, String codigo, LocalDate fecha , LocalTime horaInicio , int duracionMinutos, int cantidadJugadores,Modalidad modalidad) {
		
		DisciplinaIndividual disciplina = buscarDisciplina(tipo);
		
		if(disciplina == null) {
			throw new IllegalArgumentException("La disciplina no existe");
		}
		
		Reserva reserva =  new Reserva(socio, disciplina, codigo, fecha, horaInicio, duracionMinutos, cantidadJugadores, modalidad);
		
		disciplina.registrarReserva(reserva);
		
		return reserva ;
	}
	
}




