package modelo.individual;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import modelo.comun.Modalidad;
import modelo.comun.TipoDisciplina;
import modelo.usuarios.Socio;

public class Reserva {
	
    private Socio socio;
    private DisciplinaIndividual disciplina;
    private String codigoInstalacion;

    private LocalDate fecha;
    private LocalTime horaInicio;
    private int duracionMinutos;

    private int cantidadJugadores;
    private Modalidad modalidad;

    public Reserva(
            Socio socio,
            DisciplinaIndividual disciplina,
            String codigoInstalacion,
            LocalDate fecha,
            LocalTime horaInicio,
            int duracionMinutos,
            int cantidadJugadores,
            Modalidad modalidad) {

        this.socio = Objects.requireNonNull(
            socio, "El socio es obligatorio"
        );

        this.disciplina = Objects.requireNonNull(
            disciplina, "La disciplina es obligatoria"
        );

        this.fecha = Objects.requireNonNull(
            fecha, "La fecha es obligatoria"
        );

        this.horaInicio = Objects.requireNonNull(
            horaInicio, "La hora es obligatoria"
        );

        this.modalidad = Objects.requireNonNull(
            modalidad, "La modalidad es obligatoria"
        );

        if (codigoInstalacion == null
                || codigoInstalacion.isBlank()) {

            throw new IllegalArgumentException(
                "El codigo de instalacion es obligatorio"
            );
        }

        if (duracionMinutos <= 0) {
            throw new IllegalArgumentException(
                "La duracion debe ser positiva"
            );
        }

        if (horaInicio.toSecondOfDay()
                + duracionMinutos * 60L > 86400) {

            throw new IllegalArgumentException(
                "La reserva no puede pasar de medianoche"
            );
        }

        validarModalidad(
            disciplina.getTipo(),
            modalidad,
            cantidadJugadores
        );

        this.codigoInstalacion = codigoInstalacion;
        this.duracionMinutos = duracionMinutos;
        this.cantidadJugadores = cantidadJugadores;
    }

    private void validarModalidad(
            TipoDisciplina tipo,
            Modalidad modalidad,
            int cantidadJugadores) {

        if (tipo == TipoDisciplina.PADEL) {

            if (modalidad != Modalidad.DOBLES
                    || cantidadJugadores != 4) {

                throw new IllegalArgumentException(
                    "Padel solo permite dobles con 4 jugadores"
                );
            }

        } else if (tipo == TipoDisciplina.TENIS
                || tipo == TipoDisciplina.TENIS_DE_MESA) {

            boolean sencillos =
                modalidad == Modalidad.SENCILLOS
                && cantidadJugadores == 2;

            boolean dobles =
                modalidad == Modalidad.DOBLES
                && cantidadJugadores == 4;

            if (!sencillos && !dobles) {
                throw new IllegalArgumentException(
                    "La modalidad no coincide con los jugadores"
                );
            }

        } else {
            throw new IllegalArgumentException(
                "Disciplina no soportada"
            );
        }
    }

    public Socio getSocio() {
        return socio;
    }

    public DisciplinaIndividual getDisciplina() {
        return disciplina;
    }

    public String getCodigoInstalacion() {
        return codigoInstalacion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaInicio.plusMinutes(duracionMinutos);
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public int getCantidadJugadores() {
        return cantidadJugadores;
    }

    public Modalidad getModalidad() {
        return modalidad;
    }

    public boolean seCruzaCon(Reserva otra) {

        if (!fecha.equals(otra.fecha)) {
            return false;
        }

        int inicio1 = horaInicio.toSecondOfDay();
        long fin1 = inicio1 + duracionMinutos * 60L;

        int inicio2 = otra.horaInicio.toSecondOfDay();
        long fin2 = inicio2 + otra.duracionMinutos * 60L;

        return inicio1 < fin2 && inicio2 < fin1;
    }
}
