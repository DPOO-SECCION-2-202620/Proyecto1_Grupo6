package modelo.torneos;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import modelo.usuarios.Empleado;
import modelo.usuarios.Socio;

public class Torneo {
    public enum Tipo { RECREATIVO, COMPETITIVO }
    public enum Modalidad { INDIVIDUAL, DOBLES, EQUIPO }
    public enum Estado { ABIERTAS, EN_CURSO, FINALIZADO }
    
    private String idTorneo;
    private String nombre;
    private Tipo tipo;
    private Modalidad modalidad;
    private Estado estado;
    
    private int cupoMaximo;
    private int cuposPreferencialesDisponibles; 
    private int cuposEstandarDisponibles;       
    
    private double tarifa; 
    private double recaudoTotal;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
 
    private final List<InscripcionTorneoEmpleado> inscripcionesEmpleados = new ArrayList<>();
    private final List<Socio> inscripcionesSocios = new ArrayList<>();
   
    private Socio ganador;

    public Torneo(String idTorneo, String nombre, Tipo tipo, Modalidad modalidad, int cupoMaximo, double tarifa) {
        this.idTorneo = idTorneo;
        this.nombre = nombre;
        this.tipo = tipo;
        this.modalidad = modalidad;
        this.cupoMaximo = cupoMaximo;
        this.tarifa = (tipo == Tipo.RECREATIVO) ? 0.0 : tarifa;
        this.cuposPreferencialesDisponibles = (int) (cupoMaximo * 0.20);
        this.cuposEstandarDisponibles = cupoMaximo - this.cuposPreferencialesDisponibles;
        
        this.estado = Estado.ABIERTAS;
        this.recaudoTotal = 0.0;
        this.ganador = null;  
    }

    public String getIdTorneo() { return idTorneo; }
    public int getCuposPreferencialesDisponibles() { return cuposPreferencialesDisponibles; }
    public int getCuposEstandarDisponibles() { return cuposEstandarDisponibles; }
 
    public Socio getGanador() { return ganador; }
    
    public void consumirCupo(boolean esPreferencial) {
        if (estado != Estado.ABIERTAS) {
            throw new IllegalStateException("El torneo no admite nuevas inscripciones");
        }
        if (esPreferencial) {
            if (cuposPreferencialesDisponibles <= 0) {
                throw new IllegalStateException("No quedan cupos preferenciales");
            }
            cuposPreferencialesDisponibles--;
        } else {
            if (cuposEstandarDisponibles <= 0) {
                throw new IllegalStateException("No quedan cupos estandar");
            }
            cuposEstandarDisponibles--;
        }
    }

    public void programar(LocalDate fecha, LocalTime inicio, LocalTime fin) {
        if (fecha == null || inicio == null || fin == null || !inicio.isBefore(fin)) {
            throw new IllegalArgumentException("Fecha u horario de torneo invalidos");
        }
        if (!inscripcionesEmpleados.isEmpty()) {
            throw new IllegalStateException("No se puede cambiar el horario con empleados inscritos");
        }
        this.fecha = fecha;
        this.horaInicio = inicio;
        this.horaFin = fin;
    }

    public InscripcionTorneoEmpleado inscribirEmpleado(Empleado empleado) {
        if (empleado == null) throw new IllegalArgumentException("El empleado es obligatorio");
        if (modalidad != Modalidad.INDIVIDUAL) throw new IllegalArgumentException("El empleado solo puede inscribirse a torneos individuales");
        if (fecha == null || horaInicio == null || horaFin == null) throw new IllegalStateException("El torneo debe tener fecha y horario para inscribir empleados");
        
        for (InscripcionTorneoEmpleado inscripcion : inscripcionesEmpleados) {
            if (inscripcion.getEmpleado().getLogin().equals(empleado.getLogin())) {
                throw new IllegalStateException("Empleado ya inscrito en este torneo");
            }
        }
        if (empleado.estaDeTurnoDurante(fecha, horaInicio, horaFin)) {
            throw new IllegalStateException("El torneo coincide con un turno de trabajo");
        }
        
        consumirCupo(false);
        InscripcionTorneoEmpleado inscripcion = new InscripcionTorneoEmpleado(empleado, this);
        inscripcionesEmpleados.add(inscripcion);
        return inscripcion;
    }

     
    public void inscribirSocio(Socio socio, boolean preferencial) {
        if (socio == null) throw new IllegalArgumentException("El socio es obligatorio");
        if (modalidad != Modalidad.INDIVIDUAL) throw new IllegalArgumentException("Este torneo no admite socios individuales");
        
        for (Socio inscrito : inscripcionesSocios) {
            if (inscrito.getLogin().equals(socio.getLogin()))
                throw new IllegalStateException("El socio ya esta inscrito en el torneo");
        }
        
        consumirCupo(preferencial);
        inscripcionesSocios.add(socio);
        
      
        if (this.tipo == Tipo.COMPETITIVO) {
            this.recaudoTotal += this.tarifa;
        }
    }
    
 
    public void retirarSocio(Socio socio, boolean esPreferencial) {
         
        if (estado != Estado.ABIERTAS) {
            throw new IllegalStateException("No se puede retirar al socio porque el torneo ya no está en inscripciones.");
        }
        
        if (inscripcionesSocios.remove(socio)) {
            if (esPreferencial) {
                cuposPreferencialesDisponibles++;
            } else {
                cuposEstandarDisponibles++;
            }
            if (this.tipo == Tipo.COMPETITIVO) {
                this.recaudoTotal -= this.tarifa;
            }
        } else {
            throw new IllegalArgumentException("El socio no estaba inscrito en el torneo.");
        }
    }

    public List<Socio> getInscripcionesSocios() {
        return Collections.unmodifiableList(inscripcionesSocios);
    }

    public void restaurarEstado(Estado estado, int preferenciales, int estandar, double recaudo) {
        if (estado == null || preferenciales < 0 || estandar < 0
                || preferenciales + estandar > cupoMaximo
                || !Double.isFinite(recaudo) || recaudo < 0)
            throw new IllegalArgumentException("Estado de torneo invalido");
        this.estado = estado;
        this.cuposPreferencialesDisponibles = preferenciales;
        this.cuposEstandarDisponibles = estandar;
        this.recaudoTotal = recaudo;
    }

    public void iniciar() {
        if (estado != Estado.ABIERTAS) throw new IllegalStateException("No se puede iniciar el torneo");
        estado = Estado.EN_CURSO;
    }

     
    public void finalizar(Socio ganador) {
        if (estado != Estado.EN_CURSO) throw new IllegalStateException("El torneo no esta en curso");
        
        // Verificar que el ganador esté inscrito (siendo socio)
        boolean inscrito = false;
        for (Socio s : inscripcionesSocios) {
            if (s.getLogin().equals(ganador.getLogin())) {
                inscrito = true;
                break;
            }
        }
        if (!inscrito) {
            throw new IllegalArgumentException("El ganador debe ser un socio inscrito en el torneo.");
        }
        
        this.estado = Estado.FINALIZADO;
        this.ganador = ganador;
    }

    public LocalDate getFecha() { return fecha; }
    public LocalTime getHoraInicio() { return horaInicio; }
    public LocalTime getHoraFin() { return horaFin; }
    public String getNombre() { return nombre; }
    public Tipo getTipo() { return tipo; }
    public Estado getEstado() { return estado; }
    public int getCupoMaximo() { return cupoMaximo; }
    public double getTarifa() { return tarifa; }
    public double getRecaudoTotal() { return recaudoTotal; }
    public List<InscripcionTorneoEmpleado> getInscripcionesEmpleados() {
        return Collections.unmodifiableList(inscripcionesEmpleados);
    }
    public Modalidad getModalidad() { 
        return modalidad; 
    }
}