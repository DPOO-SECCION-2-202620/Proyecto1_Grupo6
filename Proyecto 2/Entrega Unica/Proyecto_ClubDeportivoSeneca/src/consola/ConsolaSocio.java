package consola;

import modelo.usuarios.Socio;
import modelo.usuarios.Usuario;
import modelo.torneos.Torneo;

public class ConsolaSocio extends ConsolaBase {
    public static void main(String[] args) { new ConsolaSocio().ejecutar(Socio.class); }

    @Override protected void mostrarMenu(Usuario usuario) {
        Socio socio = (Socio) usuario;
        int op;
        do {
            System.out.println("\n=== MENU SOCIO ===\n1. Ver torneos\n2. Inscribirme a torneo individual\n3. Consultar mis inscripciones\n4. Ver bonos\n0. Guardar y salir");
            op = leerEntero("Opcion",0,4);
            try {
                switch (op) {
                    case 1 -> torneos();
                    case 2 -> { torneos(); club.inscribirTorneo(leerTexto("ID del torneo"), socio); System.out.println("Inscripcion registrada."); }
                    case 3 -> { for (Torneo t: club.getTorneos()) if (t.getInscripcionesSocios().contains(socio)) System.out.println(t.getIdTorneo()+" | "+t.getNombre()); }
                    case 4 -> { System.out.println("Bonos: " + socio.getBonos().size()); socio.getBonos().forEach(b -> System.out.println("Descuento: " + (b.getPorcentaje()*100) + "% | Usado: " + b.isUsado())); }
                }
            } catch (IllegalArgumentException | IllegalStateException ex) { System.out.println("Operacion rechazada: " + ex.getMessage()); }
        } while (op != 0);
    }
    private void torneos() {
        if (club.getTorneos().isEmpty()) System.out.println("No hay torneos.");
        for (Torneo t : club.getTorneos()) System.out.println(t.getIdTorneo() + " | " + t.getNombre() + " | " + t.getModalidad() + " | " + t.getEstado());
    }
}
