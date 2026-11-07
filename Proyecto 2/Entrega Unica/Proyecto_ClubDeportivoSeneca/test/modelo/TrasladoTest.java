package modelo;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import modelo.tienda.ArticuloTienda;
import modelo.tienda.Ubicacion; 
import modelo.tienda.ExistenciaArticulo;
class TrasladoTest {

    private Club club;
    private Ubicacion tiendaOrigen;
    private Ubicacion tiendaDestino;
    private ArticuloTienda raqueta;

    @BeforeEach
    void setUp() {
        club = new Club();
        tiendaOrigen = new Ubicacion("T-01");
        tiendaDestino = new Ubicacion("T-02");       
        club.agregarUbicacion(tiendaOrigen);
        club.agregarUbicacion(tiendaDestino);
        raqueta = new ArticuloTienda("PRD-100", "Raqueta Wilson", 150000.0, null);      
        club.agregarProducto(raqueta);
        club.agregarExistenciaArticulo(new ExistenciaArticulo(raqueta, tiendaOrigen, 10));
    }
     
    @Test
    void testTrasladoExitoso() {
    
        club.trasladarProducto("T-01", "T-02", "PRD-100", 5);
      
        assertEquals(5, club.consultarStock("T-01", "PRD-100"), "El origen debe quedar con 5 unidades");
        assertEquals(5, club.consultarStock("T-02", "PRD-100"), "El destino debe quedar con 5 unidades");
    }

    @Test
    void testFallaProductoNoExisteEnOrigen() {

        assertThrows(IllegalArgumentException.class, () -> {
            club.trasladarProducto("T-01", "T-02", "PRD-999", 5);
        }, "Debe lanzar excepción si el producto no existe en el origen");
        assertEquals(10, club.consultarStock("T-01", "PRD-100"), "El stock del origen no debe alterarse tras un error");
    }
 
    @Test
    void testFallaProductoYaExisteEnDestino() {
        club.agregarExistenciaArticulo(new ExistenciaArticulo(raqueta, tiendaDestino, 2)); 
        
        assertThrows(IllegalArgumentException.class, () -> {
            club.trasladarProducto("T-01", "T-02", "PRD-100", 5);
        }, "Debe lanzar excepción porque el producto ya existe en el catálogo de destino");
        
        assertEquals(10, club.consultarStock("T-01", "PRD-100"), "El stock de origen queda intacto");
        assertEquals(2, club.consultarStock("T-02", "PRD-100"), "El stock de destino queda intacto");
    }
}