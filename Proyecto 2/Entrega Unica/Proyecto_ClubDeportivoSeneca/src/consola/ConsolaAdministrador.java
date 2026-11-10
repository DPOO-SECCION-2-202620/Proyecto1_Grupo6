package consola;

import modelo.usuarios.Administrador;
import modelo.usuarios.Usuario;
import modelo.tienda.SugerenciaProducto;
import modelo.torneos.Torneo;

public class ConsolaAdministrador extends ConsolaBase {
    public static void main(String[] args) { new ConsolaAdministrador().ejecutar(Administrador.class); }

    @Override protected void mostrarMenu(Usuario usuario) {
        int op;
        do {
        	System.out.println("\n=== MENU ADMINISTRADOR ===\n1. Listar torneos\n2. Crear torneo individual\n3. Iniciar torneo\n4. Finalizar torneo\n5. Ver sugerencias\n6. Aprobar sugerencia\n7. Rechazar sugerencia\n8. Trasladar producto\n0. Guardar y salir");
            op=leerEntero("Opcion",0,8);
            try {
                switch(op) {
                    case 1 -> listarTorneos();
                    case 2 -> crearTorneo();
                    case 3 -> { club.buscarTorneo(leerIdTorneo()).iniciar(); System.out.println("Torneo iniciado."); }
                    case 4 -> { 
                        String idTorneo = leerIdTorneo();
                        String loginGanador = leerTexto("Login del socio ganador");
                        String mensajePremio = club.finalizarTorneoYEntregarPremio(idTorneo, loginGanador);
                        System.out.println(mensajePremio); 
                    }
                    case 5 -> listarSugerencias();
                    case 6 -> { club.aprobarSugerencia(buscarSugerencia()); System.out.println("Sugerencia aprobada."); }
                    case 7 -> { club.rechazarSugerencia(buscarSugerencia()); System.out.println("Sugerencia rechazada."); }
                    case 8 -> ejecutarTraslado();
                }
            } catch (IllegalArgumentException | IllegalStateException ex) { System.out.println("Operacion rechazada: " + ex.getMessage()); }
        } while(op!=0);
    }
    private void listarTorneos() {
        for(Torneo t:club.getTorneos()) System.out.println(t.getIdTorneo()+" | "+t.getNombre()+" | "+t.getEstado());
    }
    private String leerIdTorneo() {
        listarTorneos();
        String id=leerTexto("ID del torneo");
        if(club.buscarTorneo(id)==null) throw new IllegalArgumentException("Torneo inexistente");
        return id;
    }
    private void crearTorneo() {
        String id=leerTexto("ID nuevo");
        if(club.buscarTorneo(id)!=null) throw new IllegalArgumentException("El ID ya existe");
        String nombre=leerTexto("Nombre");
        int tipo=leerEntero("Tipo (1 recreativo / 2 competitivo)",1,2);
        int cupo=leerEntero("Cupo maximo",1,10000);
        double tarifa=tipo==1?0:leerDecimal("Tarifa",0);
        Torneo t=new Torneo(id,nombre,tipo==1?Torneo.Tipo.RECREATIVO:Torneo.Tipo.COMPETITIVO,
                Torneo.Modalidad.INDIVIDUAL,cupo,tarifa);
        t.programar(leerFecha("Fecha"),leerHora("Hora inicial"),leerHora("Hora final"));
        club.agregarTorneo(t);
        System.out.println("Torneo creado.");
    }
    private void listarSugerencias() {
        for(SugerenciaProducto s:club.getSugerencias())
            System.out.println(s.getCodigo()+" | "+s.getNombre()+" | "+s.getEstado());
    }
    private SugerenciaProducto buscarSugerencia() {
        listarSugerencias();
        String codigo=leerTexto("Codigo de la sugerencia");
        for(SugerenciaProducto s:club.getSugerencias())
            if(s.getCodigo().equals(codigo)) return s;
        throw new IllegalArgumentException("Sugerencia inexistente");
    }
    private void ejecutarTraslado() {
        String origen = leerTexto("ID Sede Origen (ej. T-01)");
        String destino = leerTexto("ID Sede Destino (ej. T-02)");
        String codigo = leerTexto("Codigo del Producto");
        int cantidad = leerEntero("Cantidad a trasladar", 1, 10000);
        
        club.trasladarProducto(origen, destino, codigo, cantidad);
        System.out.println("Traslado exitoso. El inventario ha sido actualizado.");
    }
}
