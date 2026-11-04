package modelo.deportes;
import java.time.LocalDate;
public abstract class Partido {
	protected String idPartido;
    protected LocalDate fecha;
    protected int marcadorLocal;
    protected int marcadorVisitante;
    protected Equipo local;
    protected Equipo visitante;
	public Partido(String idPartido, LocalDate fecha, Equipo local, Equipo visitante) {
		super();
		this.idPartido = idPartido;
		this.fecha = fecha;
		this.local = local;
		this.visitante = visitante;
	}
	public LocalDate getFecha() {
		return fecha;
	}
	public Equipo getLocal() {
		return local;
	}
	public Equipo getVisitante() {
		return visitante;
	}
    
}
