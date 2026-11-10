package modelo;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;

import modelo.persistencia.PersistenciaClub;
import modelo.torneos.BonoDeDescuento;
import modelo.torneos.Torneo;
import modelo.usuarios.Socio;

public class DemoPersistenciaTorneos {
    public static void main(String[] args) throws Exception {
        Club club = new Club();
        Socio ana = new Socio("ana", "clave", "Ana", LocalDate.of(2003, 2, 1));
        Socio juan = new Socio("juan", "clave", "Juan", LocalDate.of(2003, 3, 1));
        ana.sumarPuntos(200); // cupo preferencial
        club.agregarUsuario(ana);
        club.agregarUsuario(juan);
        Torneo torneo = new Torneo("T-100", "Torneo individual", Torneo.Tipo.COMPETITIVO,
                Torneo.Modalidad.INDIVIDUAL, 10, 30000);
        torneo.programar(LocalDate.of(2026, 11, 20), LocalTime.of(9,0), LocalTime.of(11,0));
        club.agregarTorneo(torneo);
        club.inscribirTorneo("T-100",ana);
        club.inscribirTorneo("T-100",juan);
        try {
            club.inscribirTorneo("T-100",ana);
            throw new AssertionError("Se permitio una inscripcion duplicada");
        } catch (IllegalStateException esperado) {
            System.out.println("OK: socio duplicado rechazado");
        }
        BonoDeDescuento b1 = new BonoDeDescuento(0.10);
        BonoDeDescuento b2 = new BonoDeDescuento(0.15);
        b2.marcarUsado();
        ana.recibirBono(b1);
        ana.recibirBono(b2);
        int pref=torneo.getCuposPreferencialesDisponibles();
        int est=torneo.getCuposEstandarDisponibles();
        torneo.iniciar();
        Path ruta = Files.createTempDirectory("cds-torneos-");
        PersistenciaClub persistencia = new PersistenciaClub(ruta);
        persistencia.guardar(club);
        Club restaurado = persistencia.cargar();
        Torneo copia = restaurado.buscarTorneo("T-100");
        exigir(copia.getEstado() == Torneo.Estado.EN_CURSO, "estado");
        exigir(copia.getInscripcionesSocios().size()==2, "inscripciones");
        exigir(copia.getCuposPreferencialesDisponibles()==pref, "preferenciales");
        exigir(copia.getCuposEstandarDisponibles()==est, "estandar");
        Socio copiaAna=(Socio)restaurado.buscarUsuario("ana");
        exigir(copiaAna.getBonos().size()==2, "bonos");
        exigir(!copiaAna.getBonos().get(0).isUsado(), "bono disponible");
        exigir(copiaAna.getBonos().get(1).isUsado(), "bono usado");
        System.out.println("OK: inscripciones, cupos, estado y bonos recuperados");
        System.out.println("Datos de prueba en " + ruta);
    }
    private static void exigir(boolean ok, String mensaje) {
        if (!ok) throw new AssertionError("Fallo: " + mensaje);
    }
}
