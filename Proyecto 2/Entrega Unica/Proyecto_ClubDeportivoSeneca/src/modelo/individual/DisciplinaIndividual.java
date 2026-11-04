package modelo.individual;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import modelo.comun.Nivel;
import modelo.comun.TipoDisciplina;
import modelo.usuarios.Socio;

public class DisciplinaIndividual {
	
    private TipoDisciplina tipo;
    private int maxInstalacionesSimultaneas;
    private List<InscripcionIndividual> inscripciones;
    private List<Reserva> reservas;

    
    public DisciplinaIndividual(
            TipoDisciplina tipo,
            int maxInstalacionesSimultaneas) {

        this.tipo = Objects.requireNonNull(
            tipo, "La disciplina es obligatoria"
        );

        if (maxInstalacionesSimultaneas <= 0) {
            throw new IllegalArgumentException(
                "El maximo de instalaciones debe ser positivo"
            );
        }

        this.maxInstalacionesSimultaneas =
            maxInstalacionesSimultaneas;

        this.inscripciones = new ArrayList<>();
        this.reservas = new ArrayList<>();
    }

    
    public TipoDisciplina getTipo() {
        return tipo;
    }

    
    public int getMaxInstalacionesSimultaneas() {
        return maxInstalacionesSimultaneas;
    }

    
    public List<InscripcionIndividual> getInscripciones() {
        return new ArrayList<>(inscripciones);
    }
    

    public List<Reserva> getReservas() {
        return new ArrayList<>(reservas);
    }

    
    public InscripcionIndividual inscribirSocio(
            Socio socio, Nivel nivel) {

        Objects.requireNonNull(
            socio, "El socio es obligatorio"
        );

        Objects.requireNonNull(
            nivel, "El nivel es obligatorio"
        );

        if (buscarInscripcion(socio) != null) {
            throw new IllegalStateException(
                "El socio ya esta inscrito en esta disciplina"
            );
        }

        InscripcionIndividual nueva =
            new InscripcionIndividual(
                socio, this, nivel, 1000
            );

        inscripciones.add(nueva);
        return nueva;
    }

    public InscripcionIndividual buscarInscripcion(Socio socio) {

        if (socio == null) {
            return null;
        }

        for (InscripcionIndividual inscripcion : inscripciones) {

            if (inscripcion.getSocio() == socio) {
                return inscripcion;
            }
        }

        return null;
    }

    public void registrarReserva(Reserva nueva) {

        Objects.requireNonNull(
            nueva, "La reserva es obligatoria"
        );

        if (nueva.getDisciplina() != this) {
            throw new IllegalArgumentException(
                "La reserva corresponde a otra disciplina"
            );
        }

        if (buscarInscripcion(nueva.getSocio()) == null) {
            throw new IllegalStateException(
                "El socio no esta inscrito en esta disciplina"
            );
        }

        if (reservas.contains(nueva)) {
            throw new IllegalStateException(
                "La reserva ya fue registrada"
            );
        }

        int instalacionesOcupadas = 0;

        for (Reserva existente : reservas) {

            if (existente.seCruzaCon(nueva)) {

                if (existente.getCodigoInstalacion()
                        .equals(nueva.getCodigoInstalacion())) {

                    throw new IllegalStateException(
                        "La instalacion ya esta reservada"
                    );
                }

                instalacionesOcupadas++;
            }
        }

        if (instalacionesOcupadas >=
                maxInstalacionesSimultaneas) {

            throw new IllegalStateException(
                "Se alcanzo el maximo de instalaciones simultaneas"
            );
        }

        reservas.add(nueva);
    }

}
