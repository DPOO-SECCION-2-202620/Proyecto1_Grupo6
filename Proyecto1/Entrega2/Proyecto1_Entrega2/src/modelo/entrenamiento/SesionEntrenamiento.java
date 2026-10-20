package modelo.entrenamiento;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import modelo.comun.EstadoAsistencia;
import modelo.usuarios.Entrenador;
import modelo.usuarios.Jugador;
import modelo.usuarios.Socio;

public class SesionEntrenamiento {

	private LocalDate fecha;
	private LocalTime hora;
	private int duracionMinutos;
	private Entrenador entrenador;
	private Instalacion instalacion;
	private List<Socio> convocados;
	private List<AsistenciaSesion> asistencias;
	public SesionEntrenamiento(LocalDate fecha, LocalTime hora, int duracionMinutos, Entrenador entrenador,
			Instalacion instalacion) {
		super();
		this.fecha = fecha;
		this.hora = hora;
		this.duracionMinutos = duracionMinutos;
		this.entrenador = entrenador;
		this.instalacion = instalacion;
		this.convocados = new ArrayList<>();
		this.asistencias = new ArrayList<>();
	}
	public LocalDate getFecha() {
		return fecha;
	}
	public LocalTime getHora() {
		return hora;
	}
	public int getDuracionMinutos() {
		return duracionMinutos;
	}
	public Entrenador getEntrenador() {
		return entrenador;
	}
	public Instalacion getInstalacion() {
		return instalacion;
	}
	public List<Socio> getConvocados() {
		return convocados;
	}
	public List<AsistenciaSesion> getAsistencias() {
		return asistencias;
	}
	
	public boolean estaConvocado(Socio socio) {
	    return convocados.contains(socio);
	}

	public void convocar(Socio socio) {
	    if (estaConvocado(socio)) {
	        throw new IllegalStateException("El socio ya está convocado a esta sesión");
	    }
	    if (convocados.size() >= instalacion.getCapacidad()) {
	        throw new IllegalStateException("La sesión ya alcanzó la capacidad de la instalación");
	    }
	    if (socio instanceof Jugador && !((Jugador) socio).estaActivo()) {
	        throw new IllegalStateException("No se puede convocar a un jugador inactivo");
	    }
	    convocados.add(socio);
	}
	
	public AsistenciaSesion buscarAsistencia(Socio socio) {
	    for (AsistenciaSesion asistencia : asistencias) {
	        if (asistencia.getSocio() == socio) {
	            return asistencia;
	        }
	    }
	    return null;
	}

	public AsistenciaSesion registrarAsistencia(Socio socio, EstadoAsistencia estado) {
	    if (!estaConvocado(socio)) {
	        throw new IllegalArgumentException("El socio no está convocado a esta sesión");
	    }
	    if (buscarAsistencia(socio) != null) {
	        throw new IllegalStateException("Ya se registró la asistencia de este socio");
	    }
	    AsistenciaSesion asistencia = new AsistenciaSesion(socio, estado);
	    asistencias.add(asistencia);
	    return asistencia;
	}

	
	
}
