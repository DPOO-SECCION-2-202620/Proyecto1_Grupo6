package modelo;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import modelo.tienda.Producto;
import modelo.tienda.Tienda;

// Importa las excepciones personalizadas si las tienen, por ejemplo:
// import excepciones.ProductoNoExisteException;
// import excepciones.ProductoYaExisteException;

class TrasladoTest {

    private Club club;
    private Tienda tiendaOrigen;
    private Tienda tiendaDestino;
    private Producto raqueta;

    @BeforeEach
    void setUp() {
        club = new Club();
        tiendaOrigen = new Tienda("T-01", "Sede Principal");
        tiendaDestino = new Tienda("T-02", "Sede Norte");
        
        club.agregarUbicacion(tiendaOrigen);
        club.agregarUbicacion(tiendaDestino);

        raqueta = new Producto("PRD-100", "Raqueta Wilson", 150000.0);
        
        tiendaOrigen.agregarProducto(raqueta, 10); 
    } 
    @Test
    void testTrasladoExitoso() {
        // Ejecución: Trasladamos 5 unidades
        club.trasladarProducto("T-01", "T-02", "PRD-100", 5);
        
        // Verificación: El origen debe restar 5 y el destino sumar 5
        assertEquals(5, club.consultarStock("T-01", "PRD-100"), "El origen debe quedar con 5 unidades");
        assertEquals(5, club.consultarStock("T-02", "PRD-100"), "El destino debe quedar con 5 unidades");
    }

    @Test
    void testFallaProductoNoExisteEnOrigen() {
        // Ejecución y Verificación: Intentar trasladar un código fantasma debe lanzar excepción
        assertThrows(IllegalArgumentException.class, () -> {
            club.trasladarProducto("T-01", "T-02", "PRD-999", 5);
        }, "Debe lanzar excepción si el producto no existe en el origen");
        
        // Verificación: El stock de la raqueta original no debió alterarse
        assertEquals(10, club.consultarStock("T-01", "PRD-100"), "El stock del origen no debe alterarse tras un error");
    }

    @Test
    void testFallaProductoYaExisteEnDestino() {
        // Preparación específica de este test: El destino ya tiene la raqueta en su catálogo
        tiendaDestino.agregarProducto(raqueta, 2); 
        
        // Ejecución y Verificación: CA-3 exige que falle si ya existe en destino
        assertThrows(IllegalArgumentException.class, () -> {
            club.trasladarProducto("T-01", "T-02", "PRD-100", 5);
        }, "Debe lanzar excepción porque el producto ya existe en el catálogo de destino");
        
        // Verificación: Los inventarios quedan intactos (10 en origen, 2 en destino)
        assertEquals(10, club.consultarStock("T-01", "PRD-100"), "El stock de origen queda intacto");
        assertEquals(2, club.consultarStock("T-02", "PRD-100"), "El stock de destino queda intacto");
    }