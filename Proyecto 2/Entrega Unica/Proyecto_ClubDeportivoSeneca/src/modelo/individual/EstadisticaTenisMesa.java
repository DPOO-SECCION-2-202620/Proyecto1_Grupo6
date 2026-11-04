package modelo.individual;
import modelo.comun.TipoDisciplina;

public class EstadisticaTenisMesa extends EstadisticaIndividual{
	
    private int puntosTotales;

    public EstadisticaTenisMesa(
            InscripcionIndividual jugador,
            int setsGanados,
            int puntosTotales) {

        super(jugador, setsGanados);

        if (jugador.getDisciplina().getTipo()
                != TipoDisciplina.TENIS_DE_MESA) {

            throw new IllegalArgumentException(
                "Estas estadisticas solo aplican a tenis de mesa"
            );
        }

        if (puntosTotales < 0) {
            throw new IllegalArgumentException(
                "Los puntos totales no pueden ser negativos"
            );
        }

        this.puntosTotales = puntosTotales;
    }

    public int getPuntosTotales() {
        return puntosTotales;
    }

}
