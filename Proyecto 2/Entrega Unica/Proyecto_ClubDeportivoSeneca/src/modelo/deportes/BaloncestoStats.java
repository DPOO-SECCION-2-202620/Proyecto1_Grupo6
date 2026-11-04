package modelo.deportes;

public class BaloncestoStats extends StatsPartido{
	private int puntos;
	private int rebotes;
	public BaloncestoStats(int minJugados, int puntos, int rebotes) {
		super(minJugados);
		this.puntos = puntos;
		this.rebotes = rebotes;
	}
	public int getPuntos() {
		return puntos;
	}
	public void setPuntos(int puntos) {
		this.puntos = puntos;
	}
	public int getRebotes() {
		return rebotes;
	}
	public void setRebotes(int rebotes) {
		this.rebotes = rebotes;
	}
	
	
}
