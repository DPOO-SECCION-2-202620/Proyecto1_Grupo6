package modelo.individual;
import modelo.comun.TipoDisciplina;

public class EstadisticaTenisPadel extends EstadisticaIndividual{
	
    private int gamesGanados;
    private int aces;
    private int erroresNoForzados;

    public EstadisticaTenisPadel(
            InscripcionIndividual jugador,
            int setsGanados,
            int gamesGanados,
            int aces,
            int erroresNoForzados) {

        super(jugador, setsGanados);

        TipoDisciplina tipo =
            jugador.getDisciplina().getTipo();

        if (tipo != TipoDisciplina.TENIS
                && tipo != TipoDisciplina.PADEL) {

            throw new IllegalArgumentException(
                "Estas estadisticas solo aplican a tenis o padel"
            );
        }

        if (gamesGanados < 0
                || aces < 0
                || erroresNoForzados < 0) {

            throw new IllegalArgumentException(
                "Las estadisticas no pueden ser negativas"
            );
        }

        this.gamesGanados = gamesGanados;
        this.aces = aces;
        this.erroresNoForzados = erroresNoForzados;
    }

    public int getGamesGanados() {
        return gamesGanados;
    }

    public int getAces() {
        return aces;
    }

    public int getErroresNoForzados() {
        return erroresNoForzados;
    }

}
