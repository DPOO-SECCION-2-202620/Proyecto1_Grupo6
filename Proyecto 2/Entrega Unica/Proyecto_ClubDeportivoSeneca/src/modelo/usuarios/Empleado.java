package modelo.usuarios;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import modelo.comun.DiaSemana;
import modelo.entrenamiento.Turno;
import modelo.tienda.CodigoDescuento;

public class Empleado extends Usuario{

	private CodigoDescuento codigoDescuento;
	private List<Turno> turnos;
	public Empleado(String login, String password, String nombre, LocalDate fechaNacimiento) {
		super(login, password, nombre, fechaNacimiento);
		this.codigoDescuento = new CodigoDescuento(login, this);
		this.turnos = new ArrayList<>(); 
	}
	
	
	public CodigoDescuento getCodigoDescuento() {
		return codigoDescuento;
	}
	public List<Turno> getTurnos() {
		return turnos;
	}
	
	
	public void agregarTurno(Turno nuevo) {
	    for (Turno turno : turnos) {
	        if (turno.seCruzaCon(nuevo)) {
	            throw new IllegalStateException("El turno se cruza con otro turno del empleado");
	        }
	    }
	    turnos.add(nuevo);
	}

	public void quitarTurno(Turno turno) {
	    if (!turnos.remove(turno)) {
	        throw new IllegalArgumentException("El empleado no tiene ese turno");
	    }
	}

	public boolean estaDisponible(DiaSemana dia, LocalTime hora) {
	    for (Turno turno : turnos) {
	        if (turno.cubre(dia, hora)) {
	            return true;
	        }
	    }
	    return false;
	}
	
}
