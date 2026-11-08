package modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import modelo.tienda.DetalleVenta;
import modelo.tienda.Snack;
import modelo.tienda.Venta;
import modelo.tienda.VentaCafeteria;
import modelo.torneos.BonoDeDescuento;
import modelo.usuarios.Socio;

class BonoTest {

	
	private Club club;
	private Socio miguel;
	private BonoDeDescuento bono;
	
	
	@BeforeEach
	void setUp() {
		club = new Club();
		
		miguel = new Socio("miguel","12345","Miguel Perdomo",LocalDate.of(2006, 10, 11));
		club.agregarUsuario(miguel);
		
		club.agregarProducto(new Snack("snack-01","Oreos",10000));
		
		bono = new BonoDeDescuento(0.20);
		miguel.recibirBono(bono);
		
		
	}

	@Test
	void comprarConBonoAplicadoDescuento() {
		Venta venta = club.comprar(miguel, "snack-01", 1, true);
		
		 assertEquals(8000, venta.calcularTotalConDescuento(), 0.01);
	     assertTrue(bono.isUsado());
	}
	@Test
	void bonoSoloSeUsaUnaVez() {
		   club.comprar(miguel, "snack-01", 1, true);

	      assertThrows(IllegalStateException.class,
	                () -> club.comprar(miguel, "snack-01", 1, true));
	}
	
	@Test
	void compraSinBonoNoLoUsa() {
		   Venta venta = club.comprar(miguel, "snack-01", 1, false);

	      assertEquals(10000, venta.calcularTotalConDescuento(), 0.01);
	      assertFalse(bono.isUsado());
	}
	
	@Test
	void bonoNoAcumulableConPuntos() {
		miguel.sumarPuntos(5000);

        Venta venta = new VentaCafeteria(LocalDate.now(), miguel);
        venta.agregarDetalle(new DetalleVenta(new Snack("s2", "Papas", 10000), 1, 10000));
        venta.redimirPuntos(1000);

        assertThrows(IllegalStateException.class, () -> venta.aplicarBono(bono));
        assertFalse(bono.isUsado());
	}
	@Test
	void rechazaPorcentajeInvalido() {
		assertThrows(IllegalArgumentException.class, () -> new BonoDeDescuento(1.5));
        assertThrows(IllegalArgumentException.class, () -> new BonoDeDescuento(0));
	}
	
}
