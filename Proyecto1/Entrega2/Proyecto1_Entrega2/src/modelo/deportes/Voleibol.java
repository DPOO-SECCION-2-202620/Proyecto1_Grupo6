package modelo.deportes;

public class Voleibol extends DeporteColectivo{

	public Voleibol(String nombre, int minJugadores) {
		super(nombre, minJugadores);
		// TODO Auto-generated constructor stub
	}

	public Voleibol(String nombre) {
		super(nombre,6);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Boolean permiteEmpate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double puntosLiga() {
		// TODO Auto-generated method stub
		return 2.0;
	}

	@Override
	public void crearStats() {
		// TODO Auto-generated method stub
		
	}

}
