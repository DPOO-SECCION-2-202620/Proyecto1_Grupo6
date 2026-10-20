package modelo.deportes;
import java.time.LocalDate;
public class PartidoInterno extends Partido{

	public PartidoInterno(String idPartido, LocalDate fecha, Equipo local, Equipo visitante) {
		super(idPartido, fecha, local, visitante);
		// TODO Auto-generated constructor stub
	}
	
	public boolean cumpleRequisitos(){
		return  visitante.cumpleMinimoJugadores() && local.cumpleMinimoJugadores();
	}
	
}
