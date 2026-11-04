package modelo.deportes;

public abstract class DeporteColectivo extends Deporte{
	protected int minJugadores ;
	
	
		public DeporteColectivo(String nombre) {
			super(nombre);
			// TODO Auto-generated constructor stub
		}


		public DeporteColectivo(String nombre, int minJugadores) {
			super(nombre);
			this.minJugadores = minJugadores;
		}


		public int getMinJugadores() {
			return minJugadores;
		}


		public void setMinJugadores(int minJugadores) {
			this.minJugadores = minJugadores;
		}
	
}
