package consola;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import modelo.Club;
import modelo.comun.CategoriaArticulo;
import modelo.comun.Nivel;
import modelo.comun.TipoDisciplina;
import modelo.individual.DisciplinaIndividual;
import modelo.persistencia.PersistenciaClub;
import modelo.tienda.ArticuloTienda;
import modelo.tienda.ExistenciaArticulo;
import modelo.tienda.Snack;
import modelo.tienda.Ubicacion;
import modelo.torneos.BonoDeDescuento;
import modelo.torneos.Torneo;
import modelo.usuarios.Administrador;
import modelo.usuarios.Empleado;
import modelo.usuarios.Socio;

public class GenerarDatosIniciales {
    public static void main(String[] args) throws Exception {
        Path carpeta=Path.of("data");
        Path usuarios=carpeta.resolve("usuarios.txt");
        if (Files.exists(usuarios) && Files.size(usuarios)>0) {
            System.out.println("data/ ya tiene usuarios. No se sobrescribio nada.");
            return;
        }
        Club club=new Club();
        Socio socio=new Socio("socio1","1234","Socio de prueba",LocalDate.of(2004,2,15));
        socio.sumarPuntos(120);
        club.agregarUsuario(socio);
        club.agregarUsuario(new Empleado("empleado1","1234","Empleado de prueba",LocalDate.of(1998,7,10)));
        club.agregarUsuario(new Administrador("admin1","1234","Admin de prueba",LocalDate.of(1986,3,20)));
        Ubicacion almacen=new Ubicacion("TiendaPrincipal");
        club.agregarUbicacion(almacen);
        ArticuloTienda pelota=new ArticuloTienda("PELOTA-01","Pelota de tenis",30000,CategoriaArticulo.PELOTAS);
        club.agregarProducto(pelota);
        club.agregarExistenciaArticulo(new ExistenciaArticulo(pelota,almacen,10));
        DisciplinaIndividual tenis = new DisciplinaIndividual(TipoDisciplina.TENIS, 2);
        club.agregarDisciplinaIndividual(tenis);
        tenis.inscribirSocio(socio, Nivel.INTERMEDIO);       
        club.agregarProducto(new Snack("SNACK-01", "Galletas Oreo", 3000));   
        socio.recibirBono(new BonoDeDescuento(0.20));           
        club.agregarUbicacion(new Ubicacion("TiendaNorte"));     
        club.agregarDisciplinaIndividual(new DisciplinaIndividual(TipoDisciplina.PADEL,1));
        Torneo torneo=new Torneo("TOR-01","Torneo recreativo de tenis",Torneo.Tipo.RECREATIVO,
                Torneo.Modalidad.INDIVIDUAL,10,0);
        torneo.programar(LocalDate.of(2026,11,28),LocalTime.of(14,0),LocalTime.of(16,0));
        club.agregarTorneo(torneo);
        new PersistenciaClub(carpeta).guardar(club);
        System.out.println("Datos iniciales generados en " + carpeta.toAbsolutePath());
        System.out.println("Usuarios: socio1/1234, empleado1/1234, admin1/1234");
    }
}
