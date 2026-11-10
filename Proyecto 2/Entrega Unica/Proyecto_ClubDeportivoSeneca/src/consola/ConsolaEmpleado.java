package consola;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import modelo.tienda.SugerenciaProducto;
import modelo.comun.Alergeno;
import modelo.torneos.Torneo;
import modelo.usuarios.Empleado;
import modelo.usuarios.Usuario;

public class ConsolaEmpleado extends ConsolaBase {
    public static void main(String[] args) { new ConsolaEmpleado().ejecutar(Empleado.class); }

    @Override protected void mostrarMenu(Usuario usuario) {
        Empleado empleado = (Empleado) usuario;
        int opcion;
        do {
            System.out.println("\n=== MENU EMPLEADO ===");
            System.out.println("1. Hacer sugerencia de producto");
            System.out.println("2. Consultar estado de mis sugerencias");
            System.out.println("3. Inscribirme a torneo individual");
            System.out.println("4. Ver torneos disponibles");
            System.out.println("0. Guardar y salir");
            opcion = leerEntero("Seleccione una opcion", 0, 4);
            try {
                switch (opcion) {
                    case 1 -> sugerir(empleado);
                    case 2 -> verSugerencias(empleado);
                    case 3 -> inscribirse(empleado);
                    case 4 -> verTorneos();
                    default -> { }
                }
            } catch (IllegalArgumentException | IllegalStateException ex) {
                System.out.println("Operacion rechazada: " + ex.getMessage());
            }
        } while (opcion != 0);
    }

    private void sugerir(Empleado empleado) {
        String codigo = leerTexto("Codigo unico del producto");
        String nombre = leerTexto("Nombre del producto");
        String descripcion = leerTexto("Descripcion de la sugerencia");
        double precio = leerDecimal("Precio sugerido", 0.01);
        boolean snack = leerSiNo("Es un snack");
        SugerenciaProducto s = new SugerenciaProducto(LocalDate.now(), descripcion,
                empleado, codigo, nombre, precio, snack);
        if (snack && leerSiNo("Desea declarar alergenos ahora")) {
            List<Alergeno> alergenos = new ArrayList<>();
            for (Alergeno a : Alergeno.values()) {
                if (leerSiNo("Contiene " + a.name())) alergenos.add(a);
            }
            // Una lista vacia declarada significa que se verifico que no tiene alergenos.
            s.declararAlergenos(alergenos);
        }
        club.agregarSugerenciaProducto(s);
        System.out.println("Sugerencia registrada con estado " + s.getEstado());
    }

    private void verSugerencias(Empleado empleado) {
        List<SugerenciaProducto> lista = club.sugerenciasDe(empleado);
        if (lista.isEmpty()) { System.out.println("No hay sugerencias registradas."); return; }
        for (SugerenciaProducto s : lista) {
            String alerg = s.isEsSnack() ?
                (s.tieneAlergenosDeclarados() ? "declarados" : "SIN DECLARAR") : "no aplica";
            System.out.println(s.getFecha() + " | " + s.getCodigo() + " | " +
                s.getNombre() + " | " + s.getEstado() + " | alergenos: " + alerg);
        }
    }

    private void verTorneos() {
        if (club.getTorneos().isEmpty()) { System.out.println("No hay torneos."); return; }
        for (Torneo t : club.getTorneos()) {
            System.out.println(t.getIdTorneo() + " | " + t.getNombre() + " | " + t.getModalidad() +
                " | " + t.getEstado() + " | " + t.getFecha() + " " + t.getHoraInicio() +
                "-" + t.getHoraFin());
        }
    }

    private void inscribirse(Empleado empleado) {
        verTorneos();
        String id = leerTexto("ID del torneo individual");
        club.inscribirEmpleadoEnTorneo(id, empleado);
        System.out.println("Inscripcion aceptada: gratuita y sin derecho a premio.");
    }
}
