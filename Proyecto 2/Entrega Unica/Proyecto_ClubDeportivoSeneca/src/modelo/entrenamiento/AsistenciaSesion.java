package modelo.entrenamiento;

import modelo.comun.EstadoAsistencia;
import modelo.usuarios.Socio;

public class AsistenciaSesion {

	
	private Socio socio;
	private EstadoAsistencia asistencia;
	private int nota;
	private String comentario;
	public AsistenciaSesion(Socio socio, EstadoAsistencia asistencia) {
		super();
		this.socio = socio;
		this.asistencia = asistencia;
		this.nota = 0;
		this.comentario = null;
		
	}
	public Socio getSocio() {
		return socio;
	}
	public EstadoAsistencia getAsistencia() {
		return asistencia;
	}
	public int getNota() {
		return nota;
	}
	public String getComentario() {
		return comentario;
	}
	
	
	
	public boolean tieneEvaluacion() {
	    return nota > 0;
	}

	public void evaluar(int nota, String comentario) {
	    if (asistencia == EstadoAsistencia.FALTO) {
	        throw new IllegalStateException("No se puede evaluar a un socio que faltó");
	    }
	    if (nota < 1 || nota > 5) {
	        throw new IllegalArgumentException("La nota debe estar entre 1 y 5");
	    }
	    this.nota = nota;
	    this.comentario = comentario;
	}
	
	
}
