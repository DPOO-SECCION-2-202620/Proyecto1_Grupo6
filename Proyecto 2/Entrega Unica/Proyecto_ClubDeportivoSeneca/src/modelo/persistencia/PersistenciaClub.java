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
import modelo.comun.DiaSemana;
import modelo.entrenamiento.Turno;
import modelo.torneos.InscripcionTorneoEmpleado;
import modelo.torneos.Torneo;
import modelo.usuarios.Administrador;
import modelo.usuarios.Empleado;
import modelo.usuarios.Entrenador;
import modelo.usuarios.Fisioterapeuta;
import modelo.usuarios.Jugador;
import modelo.usuarios.Socio;
import modelo.usuarios.Usuario;

/** Persistencia parcial del Proyecto 2: usuarios, turnos, torneos e inscripciones
 * de empleados en texto UTF-8 separado por punto y coma.
 * No guarda todavía las entidades de deportes, tienda ni reservas. */
public class PersistenciaClub {
    private final Path carpeta;

    public PersistenciaClub(Path carpeta) {
        if (carpeta == null) throw new IllegalArgumentException("La carpeta es obligatoria");
        this.carpeta = carpeta;
    }

    public void guardar(Club club) throws IOException {
        if (club == null) throw new IllegalArgumentException("El club es obligatorio");
        Files.createDirectories(carpeta);
        List<String> usuarios = new ArrayList<>();
        List<String> turnos = new ArrayList<>();
        for (Usuario usuario : club.getUsuarios()) {
            String tipo = tipoUsuario(usuario);
            usuarios.add(linea(tipo, usuario.getLogin(), usuario.getPassword(), usuario.getNombre(),
                    usuario.getFechaNacimiento().toString(), String.valueOf(usuario.getPuntos())));
            if (usuario instanceof Empleado) {
                Empleado empleado = (Empleado) usuario;
                for (Turno turno : empleado.getTurnos()) {
                    turnos.add(linea(empleado.getLogin(), turno.getDia().name(),
                            turno.getHoraInicio().toString(), turno.getHoraFin().toString()));
                }
            }
        }

        List<String> torneos = new ArrayList<>();
        List<String> inscripciones = new ArrayList<>();
        List<String> inscripcionesSocios = new ArrayList<>();
        List<String> bonos = new ArrayList<>();
        for (Usuario u : club.getUsuarios()) {
            if (u instanceof Socio) {
                for (modelo.torneos.BonoDeDescuento bono : ((Socio) u).getBonos()) {
                    bonos.add(linea(u.getLogin(), String.valueOf(bono.getPorcentaje()), String.valueOf(bono.isUsado())));
                }
            }
        }
        for (Torneo t : club.getTorneos()) {
            torneos.add(linea(t.getIdTorneo(), t.getNombre(), t.getTipo().name(),
                    t.getModalidad().name(), String.valueOf(t.getCupoMaximo()),
                    String.valueOf(t.getTarifa()),
                    t.getFecha() == null ? "" : t.getFecha().toString(),
                    t.getHoraInicio() == null ? "" : t.getHoraInicio().toString(),
                    t.getHoraFin() == null ? "" : t.getHoraFin().toString(),
                    t.getEstado().name(), String.valueOf(t.getCuposPreferencialesDisponibles()),
                    String.valueOf(t.getCuposEstandarDisponibles()), String.valueOf(t.getRecaudoTotal())));
            for (InscripcionTorneoEmpleado inscripcion : t.getInscripcionesEmpleados()) {
                inscripciones.add(linea(t.getIdTorneo(), inscripcion.getEmpleado().getLogin()));
            }
            for (Socio socio : t.getInscripcionesSocios()) {
                inscripcionesSocios.add(linea(t.getIdTorneo(), socio.getLogin()));
            }
        }
        escribir("usuarios.txt", usuarios);
        escribir("turnos.txt", turnos);
        escribir("torneos.txt", torneos);
        escribir("inscripciones_empleados.txt", inscripciones);
        escribir("inscripciones_socios.txt", inscripcionesSocios);
        escribir("bonos.txt", bonos);
        new PersistenciaDatosClub(carpeta).guardar(club);
    }

    /** Reconstruye un Club nuevo; no agrega datos sobre un Club ya cargado. */
    public Club cargar() throws IOException {
        Club club = new Club();
        for (String[] row : leer("usuarios.txt", 6)) {
            Usuario usuario = nuevoUsuario(row[0],row[1],row[2],row[3],LocalDate.parse(row[4]));
            usuario.sumarPuntos(Integer.parseInt(row[5]));
            if (club.buscarUsuario(usuario.getLogin()) != null)
                throw new IOException("Login duplicado en usuarios.txt: " + usuario.getLogin());
            club.agregarUsuario(usuario);
        }
        for (String[] row : leer("turnos.txt", 4)) {
            Usuario u = club.buscarUsuario(row[0]);
            if (!(u instanceof Empleado)) throw new IOException("Empleado no encontrado: " + row[0]);
            ((Empleado) u).agregarTurno(new Turno(DiaSemana.valueOf(row[1]),
                    LocalTime.parse(row[2]),LocalTime.parse(row[3])));
        }
        // Se aceptan 9 columnas antiguas o 13 columnas con estado y cupos.
        List<String[]> estadosTorneo = new ArrayList<>();
        for (String[] row : leerTorneos()) {
            Torneo torneo = new Torneo(row[0],row[1],Torneo.Tipo.valueOf(row[2]),
                    Torneo.Modalidad.valueOf(row[3]),Integer.parseInt(row[4]),Double.parseDouble(row[5]));
            if (!row[6].isEmpty()) torneo.programar(LocalDate.parse(row[6]),
                    LocalTime.parse(row[7]),LocalTime.parse(row[8]));
            if (club.buscarTorneo(torneo.getIdTorneo()) != null)
                throw new IOException("ID de torneo duplicado: " + torneo.getIdTorneo());
            club.agregarTorneo(torneo);
            if (row.length == 13) estadosTorneo.add(row);
        }
        for (String[] row : leer("inscripciones_empleados.txt", 2)) {
            Usuario u = club.buscarUsuario(row[1]);
            if (!(u instanceof Empleado)) throw new IOException("Inscripcion sin empleado: " + row[1]);
            club.inscribirEmpleadoEnTorneo(row[0], (Empleado) u);
        }
        for (String[] row : leer("inscripciones_socios.txt", 2)) {
            Usuario u = club.buscarUsuario(row[1]);
            if (!(u instanceof Socio)) throw new IOException("Socio inexistente en inscripcion: " + row[1]);
            try {
                club.inscribirTorneo(row[0], (Socio) u);
            } catch (RuntimeException ex) {
                throw new IOException("No se pudo recuperar inscripcion de socio para torneo " + row[0], ex);
            }
        }
        for (String[] row : estadosTorneo) {
            try {
                club.buscarTorneo(row[0]).restaurarEstado(Torneo.Estado.valueOf(row[9]),
                    Integer.parseInt(row[10]), Integer.parseInt(row[11]), Double.parseDouble(row[12]));
            } catch (RuntimeException ex) {
                throw new IOException("Estado de torneo invalido: " + row[0], ex);
            }
        }
        for (String[] row : leer("bonos.txt", 3)) {
            Usuario u = club.buscarUsuario(row[0]);
            if (!(u instanceof Socio)) throw new IOException("Socio inexistente en bono: " + row[0]);
            try {
                modelo.torneos.BonoDeDescuento bono =
                    new modelo.torneos.BonoDeDescuento(Double.parseDouble(row[1]));
                if (Boolean.parseBoolean(row[2])) bono.marcarUsado();
                ((Socio) u).recibirBono(bono);
            } catch (RuntimeException ex) {
                throw new IOException("Bono invalido de " + row[0], ex);
            }
        }
        new PersistenciaDatosClub(carpeta).cargarSobre(club);
        return club;
    }

    private List<String[]> leerTorneos() throws IOException {
        Path ruta = carpeta.resolve("torneos.txt");
        List<String[]> filas = new ArrayList<>();
        if (!Files.exists(ruta)) return filas;
        for (String linea : Files.readAllLines(ruta, StandardCharsets.UTF_8)) {
            if (linea.isEmpty()) continue;
            String[] row = linea.split(";", -1);
            if (row.length != 9 && row.length != 13)
                throw new IOException("torneos.txt requiere 9 o 13 columnas");
            filas.add(row);
        }
        return filas;
    }

    private String tipoUsuario(Usuario usuario) {
        if (usuario instanceof Entrenador) return "ENTRENADOR";
        if (usuario instanceof Fisioterapeuta) return "FISIOTERAPEUTA";
        if (usuario instanceof Jugador) return "JUGADOR";
        if (usuario instanceof Socio) return "SOCIO";
        if (usuario instanceof Administrador) return "ADMINISTRADOR";
        if (usuario instanceof Empleado) return "EMPLEADO";
        throw new IllegalArgumentException("Tipo de usuario no soportado: " + usuario.getClass());
    }

    private Usuario nuevoUsuario(String tipo, String login, String pass, String nombre, LocalDate fecha) throws IOException {
        switch (tipo) {
            case "ENTRENADOR": return new Entrenador(login,pass,nombre,fecha);
            case "FISIOTERAPEUTA": return new Fisioterapeuta(login,pass,nombre,fecha);
            case "JUGADOR": return new Jugador(login,pass,nombre,fecha);
            case "SOCIO": return new Socio(login,pass,nombre,fecha);
            case "ADMINISTRADOR": return new Administrador(login,pass,nombre,fecha);
            case "EMPLEADO": return new Empleado(login,pass,nombre,fecha);
            default: throw new IOException("Tipo de usuario desconocido: " + tipo);
        }
    }

    private String linea(String... campos) {
        for (String campo : campos) {
            if (campo == null || campo.contains(";") || campo.contains("\n") || campo.contains("\r")) {
                throw new IllegalArgumentException("Los campos no pueden contener ; ni saltos de linea");
            }
        }
        return String.join(";", campos);
    }

    private void escribir(String nombre, List<String> filas) throws IOException {
        Files.write(carpeta.resolve(nombre), filas, StandardCharsets.UTF_8);
    }

    private List<String[]> leer(String nombre, int columnas) throws IOException {
        Path ruta = carpeta.resolve(nombre);
        List<String[]> filas = new ArrayList<>();
        if (!Files.exists(ruta)) return filas;
        int numero = 0;
        for (String linea : Files.readAllLines(ruta, StandardCharsets.UTF_8)) {
            numero++;
            if (linea.isEmpty()) continue;
            String[] campos = linea.split(";", -1);
            if (campos.length != columnas) throw new IOException("Columnas incorrectas en " + nombre + " linea " + numero);
            filas.add(campos);
        }
        return filas;
    }
}
