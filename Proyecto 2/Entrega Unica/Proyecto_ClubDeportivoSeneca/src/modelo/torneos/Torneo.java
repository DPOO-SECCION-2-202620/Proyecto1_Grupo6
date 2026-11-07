package modelo.torneos;

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
    }

 
    public String getIdTorneo() { return idTorneo; }
    public int getCuposPreferencialesDisponibles() { return cuposPreferencialesDisponibles; }
    public int getCuposEstandarDisponibles() { return cuposEstandarDisponibles; }
    
  
    public void consumirCupo(boolean esPreferencial) {
        if (esPreferencial) {
            cuposPreferencialesDisponibles--;
        } else {
            cuposEstandarDisponibles--;
        }
    }
    public Modalidad getModalidad() { 
        return modalidad; 
    }
}
