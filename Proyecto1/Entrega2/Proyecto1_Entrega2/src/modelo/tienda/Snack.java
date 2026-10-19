package modelo.tienda;

import java.util.ArrayList;
import java.util.List;

import modelo.comun.Alergeno;

public class Snack extends ArticuloCafeteria{

	private List<Alergeno> alergenos;

	public Snack(String codigo, String nombre, double precio) {
		super(codigo, nombre, precio);
		this.alergenos  = new ArrayList<>();
	
	}

	public List<Alergeno> getAlergenos() {
		return alergenos;
	}
	
	
	public void agregarAlergeno(Alergeno alergeno) {
	    if (!alergenos.contains(alergeno)) {
	        alergenos.add(alergeno);
	    }
	}

	public String describirAlergenos() {
	    if (alergenos.isEmpty()) {
	        return "Sin alérgenos registrados";
	    }
	    String descripcion = "Contiene: ";
	    for (int i = 0; i < alergenos.size(); i++) {
	        descripcion += alergenos.get(i);
	        if (i < alergenos.size() - 1) {
	            descripcion += ", ";
	        }
	    }
	    return descripcion;
	}
	
	
	
}
