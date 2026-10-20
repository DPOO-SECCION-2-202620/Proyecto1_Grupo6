package modelo.individual;
import java.util.Objects;
import modelo.comun.Nivel;
import modelo.usuarios.Socio;

public class InscripcionIndividual {
	
	private Socio socio;
    private DisciplinaIndividual disciplina;
    private Nivel nivel;
    private int puntajeRanking;

    public InscripcionIndividual(Socio socio,
                                 DisciplinaIndividual disciplina,
                                 Nivel nivel,
                                 int puntajeInicial) {

        this.socio = Objects.requireNonNull(
            socio, "El socio es obligatorio"
        );

        this.disciplina = Objects.requireNonNull(
            disciplina, "La disciplina es obligatoria"
        );

        this.nivel = Objects.requireNonNull(
            nivel, "El nivel es obligatorio"
        );

        if (puntajeInicial < 0) {
            throw new IllegalArgumentException(
                "El ranking inicial no puede ser negativo"
            );
        }

        this.puntajeRanking = puntajeInicial;
    }

    public Socio getSocio() {
        return socio;
    }

    public DisciplinaIndividual getDisciplina() {
        return disciplina;
    }

    public Nivel getNivel() {
        return nivel;
    }
    
    public int getPuntajeRanking() {
        return puntajeRanking;
    }

    public void actualizarRanking(int nuevoPuntaje) {
        if (nuevoPuntaje < 0) {
            throw new IllegalArgumentException(
                "El ranking no puede ser negativo"
            );
        }

        this.puntajeRanking = nuevoPuntaje;
    }
    
}    
