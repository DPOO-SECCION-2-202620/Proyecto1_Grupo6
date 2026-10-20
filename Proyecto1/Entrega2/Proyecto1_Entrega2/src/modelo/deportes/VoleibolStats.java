package modelo.deportes;

public class VoleibolStats extends StatsPartido{
	private int aces;
	private int bloqueos;
	public VoleibolStats(int minJugados, int aces, int bloqueos) {
		super(minJugados);
		this.aces = aces;
		this.bloqueos = bloqueos;
	}
	public int getAces() {
		return aces;
	}
	public void setAces(int aces) {
		this.aces = aces;
	}
	public int getBloqueos() {
		return bloqueos;
	}
	public void setBloqueos(int bloqueos) {
		this.bloqueos = bloqueos;
	}
	
}
