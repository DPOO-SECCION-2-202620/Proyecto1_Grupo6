package modelo.deportes;

public class FutbolStats extends StatsPartido{
	private int goles;
	private int asistencias;
	private int amarillas;
	private int rojas;
	public FutbolStats(int minJugados, int goles, int asistencias, int amarillas, int rojas) {
		super(minJugados);
		this.goles = goles;
		this.asistencias = asistencias;
		this.amarillas = amarillas;
		this.rojas = rojas;
	}
	
	//Suspencion
	public boolean estaSuspendido() {
		return amarillas >= 2||rojas >=1;
	}

	public int getGoles() {
		return goles;
	}

	public void setGoles(int goles) {
		this.goles = goles;
	}

	public int getAsistencias() {
		return asistencias;
	}

	public void setAsistencias(int asistencias) {
		this.asistencias = asistencias;
	}

	public int getAmarillas() {
		return amarillas;
	}

	public void setAmarillas(int amarillas) {
		this.amarillas = amarillas;
	}

	public int getRojas() {
		return rojas;
	}

	public void setRojas(int rojas) {
		this.rojas = rojas;
	}
	
}
