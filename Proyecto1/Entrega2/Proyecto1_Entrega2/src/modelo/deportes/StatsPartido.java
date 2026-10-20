package modelo.deportes;

public abstract class StatsPartido {
	protected int minJugados;

	public StatsPartido(int minJugados) {
		super();
		this.minJugados = minJugados;
	}

	public int getMinJugados() {
		return minJugados;
	}

	public void setMinJugados(int minJugados) {
		this.minJugados = minJugados;
	}
	
}
