package modelo.competencias;

import java.time.LocalDate;
import java.util.Objects;
import modelo.deportes.Equipo;

public class PartidoCompetencia extends modelo.deportes.Partido {
    private String institucionRival;
    private String escenario;
    private boolean resultadoRegistrado;

    public PartidoCompetencia(String idPartido, LocalDate fecha, Equipo equipoCDS,
                              String institucionRival, String escenario) {
        super(idPartido, fecha, Objects.requireNonNull(equipoCDS), null);
        if (idPartido == null || idPartido.isBlank() || fecha == null ||
                institucionRival == null || institucionRival.isBlank() ||
                escenario == null || escenario.isBlank()) {
            throw new IllegalArgumentException("Faltan datos del partido");
        }
        this.institucionRival = institucionRival;
        this.escenario = escenario;
    }

    public void registrarResultado(int golesOPuntosCDS, int golesOPuntosRival) {
        if (resultadoRegistrado) {
            throw new IllegalStateException("Ya se registro el resultado");
        }
        if (golesOPuntosCDS < 0 || golesOPuntosRival < 0) {
            throw new IllegalArgumentException("Marcadores negativos");
        }
        this.marcadorLocal = golesOPuntosCDS;
        this.marcadorVisitante = golesOPuntosRival;
        this.resultadoRegistrado = true;
    }

    public String getIdPartido() { return idPartido; }
    public String getInstitucionRival() { return institucionRival; }
    public String getEscenario() { return escenario; }
    public int getMarcadorCDS() { return marcadorLocal; }
    public int getMarcadorRival() { return marcadorVisitante; }
    public boolean isResultadoRegistrado() { return resultadoRegistrado; }
}
