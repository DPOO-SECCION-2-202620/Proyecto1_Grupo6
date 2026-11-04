package modelo.usuarios;

import java.time.LocalDate;
import java.time.Period;

public abstract class Usuario {

	private String login;
	private String password;
	private String nombre;
	private LocalDate fechaNacimiento;
	private int puntos;
	
	public Usuario(String login, String password, String nombre, LocalDate fechaNacimiento) {
		this.login = login;
		this.password = password;
		this.nombre = nombre;
		this.fechaNacimiento = fechaNacimiento;
		this.puntos = 0;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLogin() {
		return login;
	}

	public String getNombre() {
		return nombre;
	}

	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	public int getPuntos() {
		return puntos;
	}
	
	public boolean verificarPassword(String password) {
	    return this.password.equals(password);
	}

	public void sumarPuntos(int cantidad) {
	    if (cantidad < 0) {
	        throw new IllegalArgumentException("La cantidad de puntos no puede ser negativa");
	    }
	    puntos += cantidad;
	}

	public void restarPuntos(int cantidad) {
	    if (cantidad < 0) {
	        throw new IllegalArgumentException("La cantidad de puntos no puede ser negativa");
	    }
	    if (cantidad > puntos) {
	        throw new IllegalStateException("El usuario no tiene suficientes puntos");
	    }
	    puntos -= cantidad;
	}
	
	
	public int calcularEdad(LocalDate fecha) {
	    return Period.between(fechaNacimiento, fecha).getYears();
	}
	
	
}
