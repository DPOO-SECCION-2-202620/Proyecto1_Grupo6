package modelo.deportes;

public abstract class Deporte {
	protected String nombre;

	public Deporte(String nombre) {
		super();
		this.nombre = nombre;
	}
	
	public abstract Boolean permiteEmpate();
	
	public abstract Double puntosLiga();
	
	public abstract void crearStats();

}
