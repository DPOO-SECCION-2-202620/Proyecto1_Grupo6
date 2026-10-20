package modelo.competencias;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import modelo.comun.Categoria;
import modelo.deportes.Deporte;
import modelo.deportes.Equipo;
import modelo.usuarios.Jugador;


public class Competencia {
    private String nombre;
    private String organizador;
    private int temporada;
    private Deporte deporte;
    private Categoria categoria;
    private Formato formato;
    private List<Equipo> equipos;
    private Map<Equipo, List<Jugador>> nominas;
    private List<PartidoCompetencia> partidos;
    private Map<Equipo, Integer> grupos;

    public Competencia(String nombre, String organizador, int temporada,
                       Deporte deporte, Categoria categoria, Formato formato) {
        if (nombre == null || nombre.isBlank() || organizador == null || organizador.isBlank() ||
                temporada <= 0) {
            throw new IllegalArgumentException("Datos de competencia invalidos");
        }
        this.nombre = nombre;
        this.organizador = organizador;
        this.temporada = temporada;
        this.deporte = Objects.requireNonNull(deporte);
        this.categoria = Objects.requireNonNull(categoria);
        this.formato = Objects.requireNonNull(formato);
        this.equipos = new ArrayList<>();
        this.nominas = new HashMap<>();
        this.partidos = new ArrayList<>();
        this.grupos = new HashMap<>();
    }

    
    public void inscribirEquipo(Equipo equipo, List<Jugador> nomina) {
        Objects.requireNonNull(equipo);
        Objects.requireNonNull(nomina);
        if (equipos.contains(equipo)) throw new IllegalStateException("Equipo ya inscrito");
        if (!equipo.getDeporte().getClass().equals(deporte.getClass())) {
            throw new IllegalArgumentException("El deporte del equipo no coincide");
        }
        if (equipo.getCategoria() != categoria) {
            throw new IllegalArgumentException("Categoria no permitida");
        }
        if (nomina.size() < ((modelo.deportes.DeporteColectivo) deporte).getMinJugadores()) {
            throw new IllegalArgumentException("Nomina insuficiente");
        }
        Set<Jugador> unicos = new HashSet<>(nomina);
        if (unicos.size() != nomina.size() || !equipo.getNominaJugadores().containsAll(nomina)) {
            throw new IllegalArgumentException("La nomina tiene duplicados o jugadores ajenos al equipo");
        }
        for (Jugador jugador : nomina) {
            if (jugador == null || !jugador.estaActivo()) {
                throw new IllegalArgumentException("La nomina contiene jugadores inactivos");
            }
        }
        equipos.add(equipo);
        nominas.put(equipo, new ArrayList<>(nomina));
    }

    public void asignarGrupo(Equipo equipo, int grupo) {
        if (!(formato instanceof FormatoFaseDeGrupos)) {
            throw new IllegalStateException("Solo las fases de grupos admiten grupos");
        }
        if (!equipos.contains(equipo) || grupo < 1 ||
                grupo > ((FormatoFaseDeGrupos) formato).getCantidadGrupos()) {
            throw new IllegalArgumentException("Equipo o grupo invalido");
        }
        grupos.put(equipo, grupo);
    }

    public void registrarPartido(PartidoCompetencia partido, List<Jugador> alineados) {
        Objects.requireNonNull(partido);
        Objects.requireNonNull(alineados);
        Equipo equipo = partido.getLocal();
        if (!equipos.contains(equipo)) {
            throw new IllegalArgumentException("Equipo no inscrito");
        }
        if (!partido.isResultadoRegistrado()) {
            throw new IllegalStateException("Falta registrar el marcador");
        }
        if (formato instanceof FormatoFaseDeGrupos && !grupos.containsKey(equipo)) {
            throw new IllegalStateException("Debe asignar grupo primero");
        }
        formato.puntosPorPartido(deporte, partido.getMarcadorCDS(), partido.getMarcadorRival());
        List<Jugador> nomina = nominas.get(equipo);
        if (alineados.size() < ((modelo.deportes.DeporteColectivo) deporte).getMinJugadores()
                || new HashSet<>(alineados).size() != alineados.size()) {
            throw new IllegalArgumentException("Alineacion insuficiente o duplicada");
        }
        for (Jugador jugador : alineados) {
            if (!nomina.contains(jugador) || !jugador.estaActivo()) {
                throw new IllegalArgumentException("Jugador no habilitado en la nomina");
            }
        }
        for (PartidoCompetencia otro : partidos) {
            if (otro.getIdPartido().equals(partido.getIdPartido())) {
                throw new IllegalStateException("ID de partido repetido");
            }
        }
        partidos.add(partido);
    }

    public int puntosDe(Equipo equipo) {
        if (!equipos.contains(equipo)) throw new IllegalArgumentException("Equipo no inscrito");
        int puntos = 0;
        for (PartidoCompetencia partido : partidos) {
            if (partido.getLocal() == equipo) {
                puntos += formato.puntosPorPartido(deporte,
                        partido.getMarcadorCDS(), partido.getMarcadorRival());
            }
        }
        return puntos;
    }

    public List<Equipo> tablaPosiciones() {
        if (formato instanceof FormatoEliminacion) {
            throw new IllegalStateException("La eliminacion no usa tabla por puntos");
        }
        List<Equipo> tabla = new ArrayList<>(equipos);
        tabla.sort((a, b) -> Integer.compare(puntosDe(b), puntosDe(a)));
        return tabla;
    }

    public String getNombre() { return nombre; }
    public String getOrganizador() { return organizador; }
    public int getTemporada() { return temporada; }
    public Deporte getDeporte() { return deporte; }
    public Categoria getCategoria() { return categoria; }
    public Formato getFormato() { return formato; }
    public List<Equipo> getEquipos() { return new ArrayList<>(equipos); }
    public List<PartidoCompetencia> getPartidos() { return new ArrayList<>(partidos); }
    public List<Jugador> getNomina(Equipo equipo) {
        List<Jugador> lista = nominas.get(equipo);
        return lista == null ? Collections.emptyList() : new ArrayList<>(lista);
    }
    

}
