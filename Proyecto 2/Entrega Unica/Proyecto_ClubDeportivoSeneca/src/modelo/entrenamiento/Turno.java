package modelo.entrenamiento;

import java.time.LocalTime;

import modelo.comun.DiaSemana;

public class Turno {
	
	private DiaSemana dia;
	private LocalTime horaInicio;
	private LocalTime horaFin;
	
	public Turno(DiaSemana dia, LocalTime horaInicio, LocalTime horaFin) {
		this.dia = dia;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
	}

	public DiaSemana getDia() {
		return dia;
	}

	public LocalTime getHoraInicio() {
		return horaInicio;
	}

	public LocalTime getHoraFin() {
		return horaFin;
	}
	
	public boolean cubre(DiaSemana dia, LocalTime hora) {
	    return this.dia == dia && !hora.isBefore(horaInicio) && hora.isBefore(horaFin);
	}

	public boolean seCruzaCon(Turno otro) {
	    return this.dia == otro.dia && this.horaInicio.isBefore(otro.horaFin) && otro.horaInicio.isBefore(this.horaFin);
	}
	
}
