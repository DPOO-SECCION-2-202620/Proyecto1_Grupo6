package modelo.deportes;
import java.util.ArrayList;
import java.util.List;
import modelo.comun.Categoria;
import modelo.usuarios.Jugador;
public class Equipo {
 private String nombre;
 private int cupoMaximo;
 private Categoria categoria;
 private String estado;
 private Deporte deporte;
 private List<Jugador> nominaJugadores;
 public Equipo(String nombre, int cupoMaximo, Categoria categoria, String estado, Deporte deporte,
		List<Jugador> nominaJugadores) {
	super();
	this.nombre = nombre;
	this.cupoMaximo = cupoMaximo;
	this.categoria = categoria;
	
	this.estado="Activo";
	this.deporte = deporte;
	this.nominaJugadores = nominaJugadores;
 }
 public boolean agregarJugador(Jugador jugador) {
	 if (nominaJugadores.size() < cupoMaximo) {
		 nominaJugadores.add(jugador);
		 //Se puede a;adir el jugador ?
		 return true;
	 }
	 return false;
 }
 public boolean cumpleMinimoJugadores() {
 	if (deporte instanceof DeporteColectivo) {
 		DeporteColectivo dc = (DeporteColectivo) deporte;
 		return nominaJugadores.size() >= dc.getMinJugadores();
 		
 	}
 	return true;
 	}
 public String getNombre() {
	return nombre;
 }
 public Deporte getDeporte() {
	return deporte;
 }
 public List<Jugador> getNominaJugadores() {
	return nominaJugadores;
 }

	public Categoria getCategoria() {
		return this.categoria;
	}


 }
 
 
 


