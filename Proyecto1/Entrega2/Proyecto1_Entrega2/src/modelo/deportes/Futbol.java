package modelo.deportes;

public class Futbol extends DeporteColectivo{

	public Futbol(String nombre, int minJugadores) {
		super(nombre, minJugadores);
		// TODO Auto-generated constructor stub
	}

	public Futbol(String nombre) {
		super(nombre, 7);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Boolean permiteEmpate() {
		return true;
	}

	@Override
	public Double puntosLiga() {
		// TODO Auto-generated method stub
		return 3.0;
	}

	@Override
	public void crearStats() {
		// TODO Auto-generated method stub
		
		
	}


}	
