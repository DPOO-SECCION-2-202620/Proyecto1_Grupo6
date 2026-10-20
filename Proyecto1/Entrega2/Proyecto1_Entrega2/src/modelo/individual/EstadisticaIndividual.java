package modelo.individual;
import java.util.Objects;


public abstract class EstadisticaIndividual {
	
	    private final InscripcionIndividual jugador;
	    private final int setsGanados;
	
	    protected EstadisticaIndividual(
	            InscripcionIndividual jugador,
	            int setsGanados) {
	
	        this.jugador = Objects.requireNonNull(
	            jugador, "El jugador es obligatorio"
	        );
	
	        if (setsGanados < 0) {
	            throw new IllegalArgumentException(
	                "Los sets ganados no pueden ser negativos"
	            );
	        }
	
	        this.setsGanados = setsGanados;
	    }
	
	    public InscripcionIndividual getJugador() {
	        return jugador;
	    }
	
	    public int getSetsGanados() {
	        return setsGanados;
	    }
	
}
