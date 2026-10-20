package modelo.deportes;

import java.time.LocalDate;

public class PartidoCompetencia extends Partido {
	private String institucionRival;
	private String escenarioDondeSeJugo;
	
	public PartidoCompetencia(String idPartido, LocalDate fecha, Equipo local, Equipo visitante,
			String institucionRival, String escenarioDondeSeJugo) {
		super(idPartido, fecha, local, visitante);
		this.institucionRival = institucionRival;
		this.escenarioDondeSeJugo = escenarioDondeSeJugo;
	}
	public PartidoCompetencia(String idPartido, LocalDate fecha, Equipo local, Equipo visitante) {
		super(idPartido, fecha, local, visitante);
		// TODO Auto-generated constructor stub
	}
	public String getInstitucionRival() {
		return institucionRival;
	}
	public String getEscenarioDondeSeJugo() {
		return escenarioDondeSeJugo;
	}
	
		 

}
