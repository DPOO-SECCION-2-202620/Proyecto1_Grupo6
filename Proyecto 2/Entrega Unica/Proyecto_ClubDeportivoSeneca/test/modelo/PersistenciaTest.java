package modelo;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import modelo.comun.CategoriaArticulo;
import modelo.comun.DiaSemana;
import modelo.comun.Modalidad;
import modelo.comun.Nivel;
import modelo.comun.TipoDisciplina;
import modelo.entrenamiento.Turno;
import modelo.individual.DisciplinaIndividual;
import modelo.individual.InscripcionIndividual;
import modelo.individual.Reserva;
import modelo.persistencia.PersistenciaClub;
import modelo.tienda.ArticuloTienda;
import modelo.tienda.ExistenciaArticulo;
import modelo.tienda.Ubicacion;
import modelo.torneos.BonoDeDescuento;
import modelo.torneos.InscripcionTorneoEmpleado;
import modelo.torneos.Torneo;
import modelo.usuarios.Empleado;
import modelo.usuarios.Entrenador;
import modelo.usuarios.Socio;

/** Pruebas aisladas: cada prueba utiliza @TempDir y no modifica data/. */
public class PersistenciaTest {

    @TempDir
    Path temporal;

    @Test
    void empleadosLoginTurnosYRechazos() throws Exception {
        Club club = new Club();
        Entrenador entrenador = new Entrenador("ent01", "clave123", "Ana", LocalDate.of(1998, 5, 4));
        entrenador.agregarTurno(new Turno(DiaSemana.LUNES, LocalTime.of(8, 0), LocalTime.of(12, 0)));
        club.agregarUsuario(entrenador);

        Torneo disponible = new Torneo("T-01", "Torneo raqueta", Torneo.Tipo.COMPETITIVO,
                Torneo.Modalidad.INDIVIDUAL, 10, 50000);
        disponible.programar(LocalDate.of(2026, 11, 9), LocalTime.of(14, 0), LocalTime.of(16, 0));
        club.agregarTorneo(disponible);

        InscripcionTorneoEmpleado inscripcion = club.inscribirEmpleadoEnTorneo("T-01", entrenador);
        assertEquals(0.0, inscripcion.getValorPagado());
        assertFalse(inscripcion.isRecibePremio());
        assertThrows(IllegalStateException.class, () -> club.inscribirEmpleadoEnTorneo("T-01", entrenador));

        Torneo ocupado = new Torneo("T-02", "Torneo ocupado", Torneo.Tipo.RECREATIVO,
                Torneo.Modalidad.INDIVIDUAL, 10, 0);
        ocupado.programar(LocalDate.of(2026, 11, 9), LocalTime.of(11, 30), LocalTime.of(13, 0));
        club.agregarTorneo(ocupado);
        assertThrows(IllegalStateException.class, () -> club.inscribirEmpleadoEnTorneo("T-02", entrenador));

        PersistenciaClub persistencia = new PersistenciaClub(temporal);
        persistencia.guardar(club);
        Club restaurado = persistencia.cargar();
        assertNotNull(restaurado.autenticar("ent01", "clave123"));
        assertEquals(1, restaurado.buscarTorneo("T-01").getInscripcionesEmpleados().size());
        assertEquals(7, restaurado.buscarTorneo("T-01").getCuposEstandarDisponibles());
    }

    @Test
    void disciplinasReservasInventarioYReferencias() throws Exception {
        Club club = new Club();
        Socio socio = new Socio("s001", "123", "Ana", LocalDate.of(2002, 5, 10));
        club.agregarUsuario(socio);
        DisciplinaIndividual tenis = new DisciplinaIndividual(TipoDisciplina.TENIS, 2);
        club.agregarDisciplinaIndividual(tenis);
        InscripcionIndividual inscripcion = tenis.inscribirSocio(socio, Nivel.INTERMEDIO);
        inscripcion.actualizarRanking(1250);
        tenis.registrarReserva(new Reserva(socio, tenis, "CANCHA-01", LocalDate.of(2026, 11, 10),
                LocalTime.of(10, 0), 60, 2, Modalidad.SENCILLOS));

        Ubicacion bodega = new Ubicacion("BODEGA-01");
        ArticuloTienda raqueta = new ArticuloTienda("R001", "Raqueta azul", 250000,
                CategoriaArticulo.RAQUETAS);
        club.agregarUbicacion(bodega);
        club.agregarProducto(raqueta);
        club.agregarExistenciaArticulo(new ExistenciaArticulo(raqueta, bodega, 8));

        PersistenciaClub persistencia = new PersistenciaClub(temporal);
        persistencia.guardar(club);
        Club restaurado = persistencia.cargar();

        assertEquals("Raqueta azul", restaurado.buscarProducto("R001").getNombre());
        assertEquals(8, restaurado.consultarStock("BODEGA-01", "R001"));
        DisciplinaIndividual copia = restaurado.buscarDisciplina(TipoDisciplina.TENIS);
        assertNotNull(copia);
        assertEquals(1, copia.getInscripciones().size());
        assertEquals(1, copia.getReservas().size());
        assertEquals(1250, copia.getInscripciones().get(0).getPuntajeRanking());
        assertSame(restaurado.buscarUsuario("s001"), copia.getReservas().get(0).getSocio());
    }

    @Test
    void torneosSociosCuposEstadosYBonos() throws Exception {
        Club club = new Club();
        Socio ana = new Socio("ana", "clave", "Ana", LocalDate.of(2003, 2, 1));
        Socio juan = new Socio("juan", "clave", "Juan", LocalDate.of(2003, 3, 1));
        ana.sumarPuntos(200);
        club.agregarUsuario(ana);
        club.agregarUsuario(juan);

        Torneo torneo = new Torneo("T-100", "Torneo individual", Torneo.Tipo.COMPETITIVO,
                Torneo.Modalidad.INDIVIDUAL, 10, 30000);
        torneo.programar(LocalDate.of(2026, 11, 20), LocalTime.of(9, 0), LocalTime.of(11, 0));
        club.agregarTorneo(torneo);
        club.inscribirTorneo("T-100", ana);
        club.inscribirTorneo("T-100", juan);
        assertThrows(IllegalStateException.class, () -> club.inscribirTorneo("T-100", ana));

        BonoDeDescuento disponible = new BonoDeDescuento(0.10);
        BonoDeDescuento usado = new BonoDeDescuento(0.15);
        usado.marcarUsado();
        ana.recibirBono(disponible);
        ana.recibirBono(usado);
        int preferenciales = torneo.getCuposPreferencialesDisponibles();
        int estandar = torneo.getCuposEstandarDisponibles();
        torneo.iniciar();

        PersistenciaClub persistencia = new PersistenciaClub(temporal);
        persistencia.guardar(club);
        Club restaurado = persistencia.cargar();
        Torneo copia = restaurado.buscarTorneo("T-100");
        assertEquals(Torneo.Estado.EN_CURSO, copia.getEstado());
        assertEquals(2, copia.getInscripcionesSocios().size());
        assertEquals(preferenciales, copia.getCuposPreferencialesDisponibles());
        assertEquals(estandar, copia.getCuposEstandarDisponibles());
        Socio anaRecuperada = (Socio) restaurado.buscarUsuario("ana");
        assertEquals(2, anaRecuperada.getBonos().size());
        assertFalse(anaRecuperada.getBonos().get(0).isUsado());
        assertTrue(anaRecuperada.getBonos().get(1).isUsado());
    }

    @Test
    void usuariosPuntosYParticipacionMixta() throws Exception {
        Club club = new Club();
        Socio socio = new Socio("testSocio", "clave", "Ana", LocalDate.of(2004, 3, 12));
        socio.sumarPuntos(100);
        club.agregarUsuario(socio);
        Empleado empleado = new Empleado("testEmp", "clave", "Luis", LocalDate.of(1990, 2, 5));
        club.agregarUsuario(empleado);
        Torneo torneo = new Torneo("T1", "Torneo test", Torneo.Tipo.RECREATIVO,
                Torneo.Modalidad.INDIVIDUAL, 10, 0);
        torneo.programar(LocalDate.of(2026, 11, 28), LocalTime.of(14, 0), LocalTime.of(16, 0));
        club.agregarTorneo(torneo);
        club.inscribirTorneo("T1", socio);
        club.inscribirEmpleadoEnTorneo("T1", empleado);
        socio.recibirBono(new BonoDeDescuento(0.1));

        PersistenciaClub persistencia = new PersistenciaClub(temporal);
        persistencia.guardar(club);
        Club restaurado = persistencia.cargar();
        assertEquals(2, restaurado.getUsuarios().size());
        assertEquals(100, restaurado.buscarUsuario("testSocio").getPuntos());
        assertNotNull(restaurado.autenticar("testEmp", "clave"));
        assertEquals(1, restaurado.buscarTorneo("T1").getInscripcionesSocios().size());
        assertEquals(1, restaurado.buscarTorneo("T1").getInscripcionesEmpleados().size());
        assertEquals(1, ((Socio) restaurado.buscarUsuario("testSocio")).getBonos().size());
    }
}
