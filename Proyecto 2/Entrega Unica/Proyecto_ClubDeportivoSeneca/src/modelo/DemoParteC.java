package modelo;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;

import modelo.comun.DiaSemana;
import modelo.entrenamiento.Turno;
import modelo.persistencia.PersistenciaClub;
import modelo.torneos.InscripcionTorneoEmpleado;
import modelo.torneos.Torneo;
import modelo.usuarios.Entrenador;

/** Demostracion ejecutable sin JUnit para la parte C. */
public class DemoParteC {
    public static void main(String[] args) throws Exception {
        Club club = new Club();
        Entrenador entrenador = new Entrenador("ent01", "clave123", "Ana", LocalDate.of(1998, 5, 4));
        entrenador.agregarTurno(new Turno(DiaSemana.LUNES,LocalTime.of(8,0),LocalTime.of(12,0)));
        club.agregarUsuario(entrenador);

        Torneo torneo = new Torneo("T-01","Torneo raqueta",Torneo.Tipo.COMPETITIVO,
                Torneo.Modalidad.INDIVIDUAL,10,50000);
        torneo.programar(LocalDate.of(2026, 11, 9),LocalTime.of(14,0),LocalTime.of(16,0)); // lunes
        club.agregarTorneo(torneo);
        InscripcionTorneoEmpleado inscripcion = club.inscribirEmpleadoEnTorneo("T-01", entrenador);
        exigir(inscripcion.getValorPagado() == 0 && !inscripcion.isRecibePremio(),"Inscripcion gratuita y sin premio");
        System.out.println("OK: empleado fuera de turno, gratis y sin premio");

        try {
            club.inscribirEmpleadoEnTorneo("T-01", entrenador);
            throw new AssertionError("Debio rechazar duplicado");
        } catch (IllegalStateException esperado) {
            System.out.println("OK: duplicado rechazado");
        }

        Torneo ocupado = new Torneo("T-02","Torneo ocupado",Torneo.Tipo.RECREATIVO,
                Torneo.Modalidad.INDIVIDUAL,10,0);
        ocupado.programar(LocalDate.of(2026,11,9),LocalTime.of(11,30),LocalTime.of(13,0));
        club.agregarTorneo(ocupado);
        try {
            club.inscribirEmpleadoEnTorneo("T-02",entrenador);
            throw new AssertionError("Debio rechazar turno cruzado");
        } catch (IllegalStateException esperado) {
            System.out.println("OK: turno superpuesto rechazado");
        }

        Path data = Files.createTempDirectory("cds-partec-");
        PersistenciaClub persistencia = new PersistenciaClub(data);
        persistencia.guardar(club);
        Club recuperado = persistencia.cargar();
        exigir(recuperado.autenticar("ent01", "clave123") != null,"Login recuperado");
        exigir(recuperado.buscarTorneo("T-01").getInscripcionesEmpleados().size()==1,"Inscripcion recuperada");
        exigir(recuperado.buscarTorneo("T-01").getCuposEstandarDisponibles()==7,"Cupos restaurados");
        System.out.println("OK: login, turnos, torneos e inscripcion restaurados desde " + data);
    }

    private static void exigir(boolean condicion, String mensaje) {
        if (!condicion) throw new AssertionError(mensaje);
    }
}
