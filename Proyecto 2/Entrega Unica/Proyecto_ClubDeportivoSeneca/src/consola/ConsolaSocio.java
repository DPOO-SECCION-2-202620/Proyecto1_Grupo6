package consola;

import java.time.LocalDate;
import java.time.LocalTime;

import modelo.comun.Modalidad;
import modelo.comun.TipoDisciplina;
import modelo.tienda.Producto;
import modelo.tienda.Venta;
import modelo.torneos.BonoDeDescuento;
import modelo.torneos.Torneo;
import modelo.usuarios.Socio;
import modelo.usuarios.Usuario;

public class ConsolaSocio extends ConsolaBase {
    public static void main(String[] args) { new ConsolaSocio().ejecutar(Socio.class); }

    @Override protected void mostrarMenu(Usuario usuario) {
        Socio socio = (Socio) usuario;
        int op;
        do {
            System.out.println("\n=== MENU SOCIO ===\n1. Ver torneos\n2. Inscribirme a torneo individual\n3. Consultar mis inscripciones\n4. Ver bonos\n5. Reservar Instalacion\n6. Comprar en tienda o cafeteria \n0. Guardar y salir");
            op = leerEntero("Opcion",0,6);
            try {
                switch (op) {
                    case 1 -> torneos();
                    case 2 -> { torneos(); club.inscribirTorneo(leerTexto("ID del torneo"), socio); System.out.println("Inscripcion registrada."); }
                    case 3 -> { for (Torneo t: club.getTorneos()) if (t.getInscripcionesSocios().contains(socio)) System.out.println(t.getIdTorneo()+" | "+t.getNombre()); }
                    case 4 -> { System.out.println("Bonos: " + socio.getBonos().size()); socio.getBonos().forEach(b -> System.out.println("Descuento: " + (b.getPorcentaje()*100) + "% | Usado: " + b.isUsado())); }
                    case 5 -> reservar(socio);
                    case 6 -> comprar(socio);
                }
            } catch (IllegalArgumentException | IllegalStateException ex) { System.out.println("Operacion rechazada: " + ex.getMessage()); }
        } while (op != 0);
    }
    private void torneos() {
        if (club.getTorneos().isEmpty()) System.out.println("No hay torneos.");
        for (Torneo t : club.getTorneos()) System.out.println(t.getIdTorneo() + " | " + t.getNombre() + " | " + t.getModalidad() + " | " + t.getEstado());
    }
    
    private void reservar(Socio socio) {
        System.out.println("Disciplinas: 1. TENIS  2. PADEL  3. TENIS_DE_MESA");
        int d = leerEntero("Disciplina", 1, 3);
        TipoDisciplina tipo = TipoDisciplina.values()[d - 1];

        String instalacion = leerTexto("Código de la instalación (ej: T1)");
        LocalDate fecha = leerFecha("Fecha");
        LocalTime hora = leerHora("Hora de inicio");
        int duracion = leerEntero("Duración en minutos", 30, 240);
        int jugadores = leerEntero("Cantidad de jugadores", 2, 4);

        System.out.println("Modalidad: 1. SENCILLOS  2. DOBLES");
        Modalidad modalidad = leerEntero("Modalidad", 1, 2) == 1 ? Modalidad.SENCILLOS : Modalidad.DOBLES;

        club.reservar(socio, tipo, instalacion, fecha, hora, duracion, jugadores, modalidad);
        System.out.println("Reserva confirmada: " + tipo + " | " + instalacion + " | " + fecha + " " + hora);
    }
    
    
    private void comprar(Socio socio) {
        System.out.println("Productos disponibles:");
        for (Producto p : club.getProductos()) {
            System.out.println(p.getCodigo() + " | " + p.getNombre() + " | $" + p.getPrecio());
        }

        String codigo = leerTexto("Código del producto");
        int cantidad = leerEntero("Cantidad", 1, 100);

        boolean usarBono = false;
        BonoDeDescuento bono = socio.getBonoDeDescuento();
        if (bono != null) {
            usarBono = leerSiNo("Tienes un bono del " + (bono.getPorcentaje() * 100) + "%. ¿Quieres usarlo?");
        }

        Venta venta = club.comprar(socio, codigo, cantidad, usarBono);
        System.out.println("Compra realizada. Total a pagar: $" + venta.calcularTotalConDescuento());
    }
    
    
}
