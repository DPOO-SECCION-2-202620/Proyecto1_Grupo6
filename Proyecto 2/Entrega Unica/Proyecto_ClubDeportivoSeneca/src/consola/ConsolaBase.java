package consola;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import modelo.Club;
import modelo.persistencia.PersistenciaClub;
import modelo.usuarios.Usuario;

/** Entrada validada y ciclo cargar/autenticar/guardar compartido por los tres roles. */
public abstract class ConsolaBase {
    protected final Scanner lector = new Scanner(System.in);
    protected final PersistenciaClub persistencia;
    protected Club club;

    protected ConsolaBase() {
        this.persistencia = new PersistenciaClub(Path.of("data"));
    }

    protected String leerTexto(String pregunta) {
        while (true) {
            System.out.print(pregunta + ": ");
            if (!lector.hasNextLine()) throw new IllegalStateException("Entrada finalizada");
            String valor = lector.nextLine().trim();
            if (!valor.isEmpty() && !valor.contains(";") && !valor.contains("\n")) return valor;
            System.out.println("Entrada invalida: escriba un texto no vacio y sin punto y coma.");
        }
    }

    protected int leerEntero(String pregunta, int minimo, int maximo) {
        while (true) {
            String valor = leerTexto(pregunta);
            try {
                int n = Integer.parseInt(valor);
                if (n >= minimo && n <= maximo) return n;
            } catch (NumberFormatException e) { /* informar y reintentar */ }
            System.out.println("Numero invalido. Debe estar entre " + minimo + " y " + maximo + ".");
        }
    }

    protected double leerDecimal(String pregunta, double minimo) {
        while (true) {
            String valor = leerTexto(pregunta).replace(',', '.');
            try {
                double n = Double.parseDouble(valor);
                if (Double.isFinite(n) && n >= minimo) return n;
            } catch (NumberFormatException e) { /* informar y reintentar */ }
            System.out.println("Numero invalido. Debe ser mayor o igual a " + minimo + ".");
        }
    }

    protected boolean leerSiNo(String pregunta) {
        while (true) {
            String valor = leerTexto(pregunta + " (s/n)").toLowerCase();
            if (valor.equals("s") || valor.equals("si")) return true;
            if (valor.equals("n") || valor.equals("no")) return false;
            System.out.println("Escriba s o n.");
        }
    }

    protected LocalDate leerFecha(String pregunta) {
        while (true) {
            try { return LocalDate.parse(leerTexto(pregunta + " (AAAA-MM-DD)")); }
            catch (DateTimeParseException e) { System.out.println("Fecha invalida. Ejemplo: 2026-11-20"); }
        }
    }

    protected LocalTime leerHora(String pregunta) {
        while (true) {
            try { return LocalTime.parse(leerTexto(pregunta + " (HH:MM)")); }
            catch (DateTimeParseException e) { System.out.println("Hora invalida. Ejemplo: 14:30"); }
        }
    }

    protected <T extends Usuario> T iniciarSesion(Class<T> rol) {
        System.out.println("=== Club Deportivo Seneca ===");
        for (int intento = 0; intento < 3; intento++) {
            String login = leerTexto("Login");
            String clave = leerTexto("Contrasena");
            try {
                Usuario usuario = club.autenticar(login, clave);
                if (!rol.isInstance(usuario)) {
                    System.out.println("Este usuario no tiene permiso para esta consola.");
                    continue;
                }
                System.out.println("Bienvenido/a, " + usuario.getNombre());
                return rol.cast(usuario);
            } catch (IllegalArgumentException ex) {
                System.out.println("No se pudo iniciar sesion: " + ex.getMessage());
            }
        }
        System.out.println("Se agotaron los intentos de acceso.");
        return null;
    }

    protected final void ejecutar(Class<? extends Usuario> rol) {
        try {
            // No usar new Club() como reemplazo si falta data: indicamos error.
            this.club = persistencia.cargar();
            if (club.getUsuarios().isEmpty()) {
                System.out.println("No hay usuarios en data/. Ejecute consola.GenerarDatosIniciales primero.");
                return;
            }
            Usuario usuario = iniciarSesion(rol);
            if (usuario == null) return;
            try {
                mostrarMenu(usuario);
            } finally {
                persistencia.guardar(club);
                System.out.println("Cambios guardados en data/.");
            }
        } catch (IOException ex) {
            System.err.println("Error de archivos de persistencia: " + ex.getMessage());
        } catch (IllegalStateException ex) {
            System.err.println("Entrada interrumpida: " + ex.getMessage());
        }
    }

    protected abstract void mostrarMenu(Usuario usuario);
}
