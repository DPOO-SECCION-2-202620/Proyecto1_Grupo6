package modelo;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import modelo.torneos.Torneo;
import modelo.usuarios.Socio;

class TorneoTest {
    private Club club;
    private Torneo torneo;
    private Socio socioTryhard;
    private Socio socioNoob;

    @BeforeEach
    void setUp() {
        club = new Club();
        torneo = new Torneo("TRN-01", "Torneo Cerros", Torneo.Tipo.COMPETITIVO, Torneo.Modalidad.INDIVIDUAL, 10, 50000.0);
        club.agregarTorneo(torneo);
        socioTryhard = new Socio("pro_login", "123", "Carlos", LocalDate.of(2000, 1, 1));
        socioTryhard.setPuntos(150);
        socioNoob = new Socio("nov_login", "123", "Ana", LocalDate.of(2002, 5, 5));
       socioNoob.setPuntos(10);
        club.agregarUsuario(socioTryhard);
        club.agregarUsuario(socioNoob);
    }
    @Test
    void testCalculoInicialDeCupos() {
        assertEquals(2, torneo.getCuposPreferencialesDisponibles(), "Debería tener 2 cupos preferenciales (20% de 10)");
        assertEquals(8, torneo.getCuposEstandarDisponibles(), "Debería tener 8 cupos estándar (80% de 10)");
    } 
    @Test
    void testInscripcionSocioEstandar() {
        club.inscribirTorneo("TRN-01", socioNoob);
        assertEquals(7, torneo.getCuposEstandarDisponibles(), "El cupo estándar debió bajar de 8 a 7");
        assertEquals(2, torneo.getCuposPreferencialesDisponibles(), "Los cupos preferenciales deben seguir en 2");
    }
    @Test
    void testInscripcionSocioPreferencial() {
        club.inscribirTorneo("TRN-01", socioTryhard);
        assertEquals(8, torneo.getCuposEstandarDisponibles(), "El cupo estándar debió quedar intacto en 8");
        assertEquals(1, torneo.getCuposPreferencialesDisponibles(), "El cupo preferencial debió bajar de 2 a 1");
    }
    @Test
    void testFallaInscripcionModalidadIncorrecta() {
        Torneo torneoEquipos = new Torneo("TRN-EQ", "Torneo de Equipos", Torneo.Tipo.COMPETITIVO, Torneo.Modalidad.EQUIPO, 10, 50000.0);
        club.agregarTorneo(torneoEquipos);
        assertThrows(IllegalArgumentException.class, () -> {
            club.inscribirTorneo("TRN-EQ", socioNoob);
        }, "Debe lanzar excepción porque el torneo es para equipos, no individual.");
    }
}