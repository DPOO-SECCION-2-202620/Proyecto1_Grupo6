package modelo;


import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import modelo.comun.Alergeno;
import modelo.comun.EstadoSugerencia;
import modelo.tienda.SugerenciaProducto;
import modelo.usuarios.Empleado;

class SugerenciaTest {

	private Club club;
	private Empleado tomas;
	private SugerenciaProducto sugerenciaSnack;
	
	@BeforeEach
	void setUp() {
		club = new Club();
		
		tomas = new Empleado("tomas","abc","Tomas Vargas",LocalDate.of(2000, 03, 03) );
		club.agregarUsuario(tomas);
		
		sugerenciaSnack = new SugerenciaProducto(LocalDate.of(2026,11, 7),"Galleta de chocolate", tomas, "snack-1","Oreos", 2000,true);
		
		club.agregarSugerenciaProducto(sugerenciaSnack);
		
	}
	
		
	@Test
	void apruebaSnackConAlergenosYCreaProducto() {
		
		
		List<Alergeno> alergenos = new ArrayList<>();
		alergenos.add(Alergeno.GLUTEN);
		alergenos.add(Alergeno.LACTEOS);
		sugerenciaSnack.declararAlergenos(alergenos);
		
		club.aprobarSugerencia(sugerenciaSnack);
		
		
		
		assertEquals(EstadoSugerencia.APROBADO, sugerenciaSnack.getEstado());
        assertNotNull(club.buscarProducto("snack-1"));
	}
	
	
	@Test
	void noApruebaSnackSinAlergenos() {
		
		 assertThrows(IllegalStateException.class, () -> club.aprobarSugerencia(sugerenciaSnack));
		 assertEquals(EstadoSugerencia.PENDIENTE, sugerenciaSnack.getEstado());
		 assertNull(club.buscarProducto("snack-1"));
		
	}
	
	
	
	@Test
	void sugerenciaRechazadaVisibleParaEmpleado() {
		club.rechazarSugerencia(sugerenciaSnack);
		
		List<SugerenciaProducto> deTomas = club.sugerenciasDe(tomas); 
		
		
		assertTrue(deTomas.contains(sugerenciaSnack));
	    assertEquals(EstadoSugerencia.RECHAZADO, sugerenciaSnack.getEstado());
	    assertNull(club.buscarProducto("snack-1"));
		
	}
	
	
	
	
	
	
	

}
