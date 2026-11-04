package modelo.individual;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import modelo.comun.Nivel;

public class PartidoPractica {

    private LocalDate fecha;
    private DisciplinaIndividual disciplina;

    private List<EstadisticaIndividual> equipo1;
    private List<EstadisticaIndividual> equipo2;

    private boolean resultadoRegistrado;

    private static final int FACTOR_ELO = 32;

    public PartidoPractica(
            LocalDate fecha,
            DisciplinaIndividual disciplina,
            List<EstadisticaIndividual> equipo1,
            List<EstadisticaIndividual> equipo2) {

        this.fecha = Objects.requireNonNull(
            fecha, "La fecha es obligatoria"
        );

        this.disciplina = Objects.requireNonNull(
            disciplina, "La disciplina es obligatoria"
        );

        Objects.requireNonNull(equipo1);
        Objects.requireNonNull(equipo2);

        if (equipo1.size() != equipo2.size()
                || (equipo1.size() != 1
                    && equipo1.size() != 2)) {

            throw new IllegalArgumentException(
                "El partido debe ser de sencillos o dobles"
            );
        }

        if (disciplina.getTipo()
                == modelo.comun.TipoDisciplina.PADEL
                && equipo1.size() != 2) {

            throw new IllegalArgumentException(
                "Padel solo permite partidos de dobles"
            );
        }

        this.equipo1 = new ArrayList<>(equipo1);
        this.equipo2 = new ArrayList<>(equipo2);

        validarParticipantes();
        this.resultadoRegistrado = false;
    }

    private void validarParticipantes() {

        List<EstadisticaIndividual> todos =
            new ArrayList<>();

        todos.addAll(equipo1);
        todos.addAll(equipo2);

        for (EstadisticaIndividual estadistica : todos) {

            if (estadistica == null) {
                throw new IllegalArgumentException(
                    "No puede haber participantes nulos"
                );
            }

            InscripcionIndividual inscripcion =
                estadistica.getJugador();

            if (inscripcion.getDisciplina() != disciplina) {
                throw new IllegalArgumentException(
                    "Todos deben pertenecer a la misma disciplina"
                );
            }

            if (disciplina.buscarInscripcion(
                    inscripcion.getSocio()) != inscripcion) {

                throw new IllegalArgumentException(
                    "El participante no tiene una inscripcion valida"
                );
            }

            for (EstadisticaIndividual anterior : todos) {

                if (anterior == estadistica) {
                    break;
                }

                if (anterior != null
                        && anterior.getJugador().getSocio()
                        == inscripcion.getSocio()) {

                    throw new IllegalArgumentException(
                        "Un jugador no puede participar dos veces"
                    );
                }
            }
        }

        for (EstadisticaIndividual jugador1 : equipo1) {
            for (EstadisticaIndividual jugador2 : equipo2) {

                Nivel nivel1 = jugador1.getJugador().getNivel();
                Nivel nivel2 = jugador2.getJugador().getNivel();

                if (Math.abs(nivel1.ordinal()
                        - nivel2.ordinal()) > 1) {

                    throw new IllegalArgumentException(
                        "Los niveles de los rivales no son compatibles"
                    );
                }
            }
        }
    }

    private double promedioRanking(
            List<EstadisticaIndividual> equipo) {

        double suma = 0;

        for (EstadisticaIndividual estadistica : equipo) {
            suma += estadistica.getJugador().getPuntajeRanking();
        }

        return suma / equipo.size();
    }

    private int totalSets(
            List<EstadisticaIndividual> equipo) {

        int suma = 0;

        for (EstadisticaIndividual estadistica : equipo) {
            suma += estadistica.getSetsGanados();
        }

        return suma;
    }

    public void registrarResultado() {

        if (resultadoRegistrado) {
            throw new IllegalStateException(
                "El resultado ya fue registrado"
            );
        }

        int sets1 = totalSets(equipo1);
        int sets2 = totalSets(equipo2);

        if (sets1 == sets2) {
            throw new IllegalStateException(
                "El partido debe tener un ganador"
            );
        }

        double rating1 = promedioRanking(equipo1);
        double rating2 = promedioRanking(equipo2);

        double esperado1 =
            1.0 / (1.0 + Math.pow(
                10, (rating2 - rating1) / 400.0
            ));

        double esperado2 = 1.0 - esperado1;

        double resultado1 = sets1 > sets2 ? 1.0 : 0.0;
        double resultado2 = 1.0 - resultado1;

        int cambio1 = (int) Math.round(
            FACTOR_ELO * (resultado1 - esperado1)
        );

        int cambio2 = (int) Math.round(
            FACTOR_ELO * (resultado2 - esperado2)
        );

        for (EstadisticaIndividual estadistica : equipo1) {

            InscripcionIndividual jugador =
                estadistica.getJugador();

            int nuevo = Math.max(
                0, jugador.getPuntajeRanking() + cambio1
            );

            jugador.actualizarRanking(nuevo);
        }

        for (EstadisticaIndividual estadistica : equipo2) {

            InscripcionIndividual jugador =
                estadistica.getJugador();

            int nuevo = Math.max(
                0, jugador.getPuntajeRanking() + cambio2
            );

            jugador.actualizarRanking(nuevo);
        }

        resultadoRegistrado = true;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public DisciplinaIndividual getDisciplina() {
        return disciplina;
    }

    public List<EstadisticaIndividual> getEquipo1() {
        return new ArrayList<>(equipo1);
    }

    public List<EstadisticaIndividual> getEquipo2() {
        return new ArrayList<>(equipo2);
    }

    public boolean isResultadoRegistrado() {
        return resultadoRegistrado;
    }
	
}
