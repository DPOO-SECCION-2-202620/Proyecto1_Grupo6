package modelo.deportes;
import modelo.usuarios.Jugador;
public class InscripcionEquipo {
	private int numero;
	private String posicion;
	private Equipo equipo;
	private Jugador jugador;
	public InscripcionEquipo(int numero, String posicion, Equipo equipo, Jugador jugador) {
		super();
		this.numero = numero;
		this.posicion = posicion;
		this.equipo = equipo;
		this.jugador = jugador;
	}
	public int getNumero() {
		return numero;
	}
	public String getPosicion() {
		return posicion;
	}
	public Equipo getEquipo() {
		return equipo;
	}
	public Jugador getJugador() {
		return jugador;
	}
	
}

