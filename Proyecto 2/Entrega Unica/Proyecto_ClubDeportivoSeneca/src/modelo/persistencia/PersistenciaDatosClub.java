package modelo.persistencia;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import modelo.Club;
import modelo.comun.Alergeno;
import modelo.comun.CategoriaArticulo;
import modelo.comun.EstadoSugerencia;
import modelo.comun.Modalidad;
import modelo.comun.Nivel;
import modelo.comun.TipoDisciplina;
import modelo.comun.TipoTemperatura;
import modelo.individual.DisciplinaIndividual;
import modelo.individual.InscripcionIndividual;
import modelo.individual.Reserva;
import modelo.tienda.ArticuloTienda;
import modelo.tienda.Bebida;
import modelo.tienda.ExistenciaArticulo;
import modelo.tienda.Producto;
import modelo.tienda.Snack;
import modelo.tienda.SugerenciaProducto;
import modelo.tienda.Ubicacion;
import modelo.usuarios.Empleado;
import modelo.usuarios.Socio;
import modelo.usuarios.Usuario;

/** Complementa PersistenciaClub sin modificar clases del modelo del grupo.
 * Formato UTF-8 con separador ';'. Campos con ';' o salto de linea se rechazan.
 * Las inscripciones y reservas se reconstruyen usando objetos del Club ya cargados. */
public class PersistenciaDatosClub {
    private final Path carpeta;
    public PersistenciaDatosClub(Path carpeta) {
        if (carpeta == null) throw new IllegalArgumentException("Carpeta obligatoria");
        this.carpeta = carpeta;
    }
    public void guardar(Club club) throws IOException {
        Files.createDirectories(carpeta);
        List<String> ubicaciones = new ArrayList<>();
        List<String> productos = new ArrayList<>();
        List<String> existencias = new ArrayList<>();
        List<String> sugerencias = new ArrayList<>();
        List<String> disciplinas = new ArrayList<>();
        List<String> inscripciones = new ArrayList<>();
        List<String> reservas = new ArrayList<>();

        for (Ubicacion u : club.getUbicaciones()) {
            ubicaciones.add(linea(u.getNombre()));
        }
        for (Producto p : club.getProductos()) {
            if (p instanceof ArticuloTienda) {
                ArticuloTienda a = (ArticuloTienda) p;
                productos.add(linea("TIENDA", p.getCodigo(), p.getNombre(), Double.toString(p.getPrecio()), a.getCategoria().name(), ""));
            } else if (p instanceof Bebida) {
                Bebida b = (Bebida) p;
                productos.add(linea("BEBIDA", p.getCodigo(), p.getNombre(), Double.toString(p.getPrecio()), b.getTipoTemperatura().name(), ""));
            } else if (p instanceof Snack) {
                Snack s = (Snack) p;
                List<String> alerg = new ArrayList<>();
                for (Alergeno a : s.getAlergenos()) alerg.add(a.name());
                productos.add(linea("SNACK", p.getCodigo(), p.getNombre(), Double.toString(p.getPrecio()), "", String.join(",", alerg)));
            } else {
                throw new IOException("Producto no soportado por persistencia: " + p.getClass().getName());
            }
        }
        for (ExistenciaArticulo e : club.getExistencias()) {
            existencias.add(linea(e.getArticulo().getCodigo(), e.getUbicacion().getNombre(), Integer.toString(e.getCantidad())));
        }
        for (SugerenciaProducto s : club.getSugerencias()) {
            List<String> alerg = new ArrayList<>();
            if (s.getAlergenos() != null) for (Alergeno a : s.getAlergenos()) alerg.add(a.name());
            sugerencias.add(linea(s.getFecha().toString(), s.getDescripcion(), s.getEmpleado().getLogin(),
                    s.getCodigo(), s.getNombre(), Double.toString(s.getPrecio()),
                    Boolean.toString(s.isEsSnack()), s.getEstado().name(),
                    s.getAlergenos() == null ? "SIN_DECLARAR" : String.join(",", alerg)));
        }
        for (DisciplinaIndividual d : club.getDisciplinas()) {
            disciplinas.add(linea(d.getTipo().name(), Integer.toString(d.getMaxInstalacionesSimultaneas())));
            for (InscripcionIndividual i : d.getInscripciones()) {
                inscripciones.add(linea(d.getTipo().name(), i.getSocio().getLogin(), i.getNivel().name(),
                        Integer.toString(i.getPuntajeRanking())));
            }
            for (Reserva r : d.getReservas()) {
                reservas.add(linea(d.getTipo().name(), r.getSocio().getLogin(),r.getCodigoInstalacion(),
                        r.getFecha().toString(), r.getHoraInicio().toString(),
                        Integer.toString(r.getDuracionMinutos()),Integer.toString(r.getCantidadJugadores()),r.getModalidad().name()));
            }
        }
        escribir("ubicaciones.txt",ubicaciones);
        escribir("productos.txt",productos);
        escribir("existencias.txt",existencias);
        escribir("sugerencias.txt",sugerencias);
        escribir("disciplinas.txt",disciplinas);
        escribir("inscripciones_individuales.txt",inscripciones);
        escribir("reservas.txt",reservas);
    }

    public void cargarSobre(Club club) throws IOException {
        try {
            for (String[] row : leer("ubicaciones.txt",1)) {
                if (club.buscarUbicacion(row[0]) != null) throw new IOException("Ubicacion repetida: " + row[0]);
                club.agregarUbicacion(new Ubicacion(row[0]));
            }
            for (String[] row : leer("productos.txt",6)) {
                if (club.buscarProducto(row[1]) != null) throw new IOException("Producto repetido: " + row[1]);
                double precio=Double.parseDouble(row[3]);
                Producto p;
                switch (row[0]) {
                    case "TIENDA": p=new ArticuloTienda(row[1],row[2],precio,CategoriaArticulo.valueOf(row[4]));break;
                    case "BEBIDA": p=new Bebida(row[1],row[2],precio,TipoTemperatura.valueOf(row[4]));break;
                    case "SNACK":
                        Snack snack=new Snack(row[1],row[2],precio);
                        if (!row[5].isEmpty()) for (String a:row[5].split(",")) snack.agregarAlergeno(Alergeno.valueOf(a));
                        p=snack;break;
                    default:throw new IOException("Producto desconocido: "+row[0]);
                }
                club.agregarProducto(p);
            }
            for (String[] row:leer("existencias.txt",3)) {
                Producto p=club.buscarProducto(row[0]);
                Ubicacion u=club.buscarUbicacion(row[1]);
                if (!(p instanceof ArticuloTienda) || u==null) throw new IOException("Existencia sin articulo/ubicacion: "+row[0]);
                club.agregarExistenciaArticulo(new ExistenciaArticulo((ArticuloTienda)p,u,Integer.parseInt(row[2])));
            }
            for (String[] row:leer("sugerencias.txt",9)) {
                Usuario u=club.buscarUsuario(row[2]);
                if (!(u instanceof Empleado)) throw new IOException("Sugerencia sin empleado: " + row[2]);
                SugerenciaProducto s=new SugerenciaProducto(LocalDate.parse(row[0]),row[1],(Empleado)u,
                        row[3],row[4],Double.parseDouble(row[5]),Boolean.parseBoolean(row[6]));
                if (!row[8].equals("SIN_DECLARAR")) {
                    List<Alergeno> alerg=new ArrayList<>();
                    if (!row[8].isEmpty()) for (String a:row[8].split(",")) alerg.add(Alergeno.valueOf(a));
                    s.declararAlergenos(alerg);
                }
                if (EstadoSugerencia.valueOf(row[7]) == EstadoSugerencia.APROBADO) s.aprobar();
                if (EstadoSugerencia.valueOf(row[7]) == EstadoSugerencia.RECHAZADO) s.rechazar();
                club.agregarSugerenciaProducto(s);
            }
            for (String[] row:leer("disciplinas.txt",2)) {
                TipoDisciplina tipo=TipoDisciplina.valueOf(row[0]);
                if (club.buscarDisciplina(tipo)!=null) throw new IOException("Disciplina repetida: " + tipo);
                club.agregarDisciplinaIndividual(new DisciplinaIndividual(tipo,Integer.parseInt(row[1])));
            }
            for (String[] row:leer("inscripciones_individuales.txt",4)) {
                DisciplinaIndividual d=club.buscarDisciplina(TipoDisciplina.valueOf(row[0]));
                Usuario u=club.buscarUsuario(row[1]);
                if(d==null || !(u instanceof Socio)) throw new IOException("Inscripcion sin socio/academia: " + row[1]);
                InscripcionIndividual i=d.inscribirSocio((Socio)u,Nivel.valueOf(row[2]));
                i.actualizarRanking(Integer.parseInt(row[3]));
            }
            for (String[] row:leer("reservas.txt",8)) {
                DisciplinaIndividual d=club.buscarDisciplina(TipoDisciplina.valueOf(row[0]));
                Usuario u=club.buscarUsuario(row[1]);
                if(d==null || !(u instanceof Socio)) throw new IOException("Reserva sin socio/academia: " + row[1]);
                Reserva r=new Reserva((Socio)u,d,row[2],LocalDate.parse(row[3]),LocalTime.parse(row[4]),
                        Integer.parseInt(row[5]),Integer.parseInt(row[6]),Modalidad.valueOf(row[7]));
                d.registrarReserva(r);
            }
        } catch (RuntimeException ex) {
            throw new IOException("Datos de persistencia complementaria invalidos",ex);
        }
    }
    private String linea(String...campos) {
        for(String c:campos) if(c==null||c.contains(";")||c.contains("\n")||c.contains("\r"))
            throw new IllegalArgumentException("Campo invalido para formato CSV con ;");
        return String.join(";",campos);
    }
    private void escribir(String nombre,List<String>filas)throws IOException{
        Files.write(carpeta.resolve(nombre),filas,StandardCharsets.UTF_8);
    }
    private List<String[]> leer(String nombre,int num)throws IOException{
        List<String[]>result=new ArrayList<>();Path f=carpeta.resolve(nombre);
        if(!Files.exists(f))return result;
        int n=0;
        for(String l:Files.readAllLines(f,StandardCharsets.UTF_8)){
            n++;
            if(l.isEmpty())continue;
            String[]row=l.split(";",-1);
            if(row.length!=num)throw new IOException("Columnas invalidas en "+nombre+" linea "+n);
            result.add(row);
        }
        return result;
    }
}
