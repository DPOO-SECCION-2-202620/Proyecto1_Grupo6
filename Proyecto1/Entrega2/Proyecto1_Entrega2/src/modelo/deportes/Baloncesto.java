package modelo.deportes;

public class Baloncesto extends DeporteColectivo{

	public Baloncesto(String nombre, int minJugadores) {
		super(nombre, minJugadores);
		// TODO Auto-generated constructor stub
	}

	public Baloncesto(String nombre) {
		super(nombre,5);
		// TODO Auto-generated constructor stub
	}
	public Boolean permiteEmpate() {
		return false;
	}
	public Double puntosLiga(){
		return 2.0;
		
	}
	public void crearStats() {}
	}
