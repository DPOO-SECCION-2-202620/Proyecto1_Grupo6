package modelo.usuarios;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import modelo.entrenamiento.Instalacion;
import modelo.torneos.BonoDeDescuento;

public class Socio extends Usuario{

	private Instalacion instalacionActual;
	private boolean tieneBebidaCaliente;
	
	private List<BonoDeDescuento> bonos;
	private double saldo;
	public Socio(String login, String password, String nombre, LocalDate fechaNacimiento) {
		super(login, password, nombre, fechaNacimiento);
		this.instalacionActual = null;
		this.tieneBebidaCaliente = false;
		this.bonos = new ArrayList<>();
		this.saldo = 0.0;
	}
	public Instalacion getInstalacionActual() {
		return instalacionActual;
	}
	public boolean isTieneBebidaCaliente() {
		return tieneBebidaCaliente;
	}
	
	
	public void entrarInstalacion(Instalacion instalacion) {
	    if (instalacionActual != null) {
	        throw new IllegalStateException("El socio ya está dentro de una instalación");
	    }
	    if (tieneBebidaCaliente && instalacion.restringeBebidasCalientes()) {
	        throw new IllegalStateException("No puede entrar a esta instalación con una bebida caliente");
	    }
	    instalacionActual = instalacion;
	}

	public void salirInstalacion() {
	    instalacionActual = null;
	}

	public void recibirBebidaCaliente() {
	    if (instalacionActual != null && instalacionActual.restringeBebidasCalientes()) {
	        throw new IllegalStateException("No se puede despachar una bebida caliente dentro de esta instalación");
	    }
	    tieneBebidaCaliente = true;
	}

	public void terminarBebidaCaliente() {
	    tieneBebidaCaliente = false;
	}
	
	public void recibirBono(BonoDeDescuento bono) {
		bonos.add(bono);
	}
	
    /** Necesario para guardar todos los bonos, incluidos los ya usados. */
    public List<BonoDeDescuento> getBonos() {
        return new ArrayList<>(bonos);
    }

    public BonoDeDescuento getBonoDeDescuento() {
		for(BonoDeDescuento d : bonos) {
			if(!d.isUsado()){
				return d;
			}
		}
		return null;
	}
    public double getSaldo() {
		return saldo;
	}

	public void agregarSaldo(double monto) {
		if (monto > 0) {
			this.saldo += monto;
		}
	}
	
}
