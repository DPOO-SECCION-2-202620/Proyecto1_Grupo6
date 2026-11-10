package modelo;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import modelo.comun.CategoriaArticulo;
import modelo.comun.Modalidad;
import modelo.comun.Nivel;
import modelo.comun.TipoDisciplina;
import modelo.individual.DisciplinaIndividual;
import modelo.individual.InscripcionIndividual;
import modelo.individual.Reserva;
import modelo.persistencia.PersistenciaClub;
import modelo.tienda.ArticuloTienda;
import modelo.tienda.ExistenciaArticulo;
import modelo.tienda.Ubicacion;
import modelo.usuarios.Socio;

/** Prueba aislada: no sobrescribe los archivos de data/ del equipo. */
public class DemoPersistenciaAmpliada {
    public static void main(String[] args) throws Exception {
        Club club = new Club();
        Socio socio = new Socio("s001", "123", "Ana", LocalDate.of(2002,5,10));
        club.agregarUsuario(socio);
        DisciplinaIndividual tenis = new DisciplinaIndividual(TipoDisciplina.TENIS, 2);
        club.agregarDisciplinaIndividual(tenis);
        InscripcionIndividual inscripcion=tenis.inscribirSocio(socio, Nivel.INTERMEDIO);
        inscripcion.actualizarRanking(1250);
        tenis.registrarReserva(new Reserva(socio,tenis,"CANCHA-01", LocalDate.of(2026,11,10),
                LocalTime.of(10,0),60,2,Modalidad.SENCILLOS));
        Ubicacion bodega = new Ubicacion("BODEGA-01");
        ArticuloTienda raqueta = new ArticuloTienda("R001","Raqueta azul",250000,CategoriaArticulo.RAQUETAS);
        club.agregarUbicacion(bodega);
        club.agregarProducto(raqueta);
        club.agregarExistenciaArticulo(new ExistenciaArticulo(raqueta,bodega,8));
        Path temp=Files.createTempDirectory("cds-persistencia-ampliada-");
        PersistenciaClub persistencia=new PersistenciaClub(temp);
        persistencia.guardar(club);
        Club restaurado=persistencia.cargar();
        if (!"Raqueta azul".equals(restaurado.buscarProducto("R001").getNombre())) throw new AssertionError("Nombre de producto no recuperado");
        if (restaurado.consultarStock("BODEGA-01","R001")!=8) throw new AssertionError("Stock no recuperado");
        DisciplinaIndividual d=restaurado.buscarDisciplina(TipoDisciplina.TENIS);
        if (d == null || d.getInscripciones().size()!=1 || d.getReservas().size()!=1) throw new AssertionError("Inscripciones/reservas no recuperadas");
        if (d.getInscripciones().get(0).getPuntajeRanking()!=1250) throw new AssertionError("Ranking no recuperado");
        if (d.getReservas().get(0).getSocio()!=restaurado.buscarUsuario("s001")) throw new AssertionError("Referencia a socio incorrecta");
        System.out.println("OK: usuario, disciplina, nivel, ranking y reserva recuperados");
        System.out.println("OK: producto con nombre, ubicacion y existencias recuperados");
        System.out.println("Datos de prueba en carpeta temporal: " + temp);
    }
}
