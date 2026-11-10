package modelo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import modelo.comun.Modalidad;
import modelo.comun.TipoDisciplina;
import modelo.deportes.Equipo;
import modelo.individual.DisciplinaIndividual;
import modelo.individual.Reserva;
import modelo.tienda.ArticuloCafeteria;
import modelo.tienda.ArticuloTienda;
import modelo.tienda.DetalleVenta;
import modelo.tienda.ExistenciaArticulo;
import modelo.tienda.Producto;
import modelo.tienda.SugerenciaProducto;
import modelo.tienda.Ubicacion;
import modelo.tienda.Venta;
import modelo.tienda.VentaCafeteria;
import modelo.tienda.VentaTienda;
import modelo.torneos.BonoDeDescuento;
import modelo.torneos.Torneo;
import modelo.torneos.InscripcionTorneoEmpleado;
import modelo.usuarios.Empleado;
import modelo.usuarios.Socio;
import modelo.usuarios.Usuario;
public class Club {

	private List<Usuario> usuarios;
	private List<DisciplinaIndividual> disciplinas;
	private List<Ubicacion> ubicaciones;
	private List<ExistenciaArticulo> existencias;
	private List<Producto> productos;
	private List<SugerenciaProducto>sugerencias;
	private List<Torneo> torneos;
	public Club() {
		this.disciplinas = new ArrayList<>();
		this.existencias = new ArrayList<>();
		this.productos = new ArrayList<>();
		this.sugerencias = new ArrayList<>();
		this.torneos = new ArrayList<>();
		this.ubicaciones = new ArrayList<>();
		this.usuarios = new ArrayList<>();
	}
	
	
	public void agregarUsuario(Usuario usuario) {
		usuarios.add(usuario);
	}
	
	public void agregarDisciplinaIndividual(DisciplinaIndividual disciplinaIndividual) {
		disciplinas.add(disciplinaIndividual);
	}
	
	
	public void agregarUbicacion(Ubicacion ubicacion) {
		ubicaciones.add(ubicacion);
		
	}
	
	
	public void agregarExistenciaArticulo(ExistenciaArticulo existenciaArticulo) {
		existencias.add(existenciaArticulo);
	}
	
	public void agregarProducto(Producto producto) {
		productos.add(producto);
		
	}
	
	public void agregarSugerenciaProducto(SugerenciaProducto sugerenciaProducto) {
		sugerencias.add(sugerenciaProducto);
	
	}
	
	public void agregarTorneo(Torneo torneo) {
		torneos.add(torneo);
	}


	public List<Usuario> getUsuarios() {
		return new ArrayList<>(usuarios);
	}


	public List<DisciplinaIndividual> getDisciplinas() {
		return new ArrayList<>(disciplinas);
	}


	public List<Ubicacion> getUbicaciones() {
		return new ArrayList<>(ubicaciones);
	}


	public List<ExistenciaArticulo> getExistencias() {
		return new ArrayList<>(existencias);
	}


	public List<Producto> getProductos() {
		return new ArrayList<>(productos);
	}


	public List<SugerenciaProducto> getSugerencias() {
		return new ArrayList<>(sugerencias);
	}


	public List<Torneo> getTorneos() {
		return new ArrayList<>(torneos);
	}

	public void setExistencias(List<ExistenciaArticulo> existencias) {
		this.existencias = existencias;
	}
	
	
	public Usuario buscarUsuario(String login) {
		for(Usuario u: usuarios){
			if(u.getLogin().equals(login)) {
				return u;
			}
		}
		return null;
	}
	
	
	public Ubicacion buscarUbicacion(String nombre) {
		for(Ubicacion ubi :ubicaciones ) {
			if(ubi.getNombre().equals(nombre)) {
				return ubi;
			}
		}
		return null;
	}
	
	
	public Producto buscarProducto(String codigo) {
		for(Producto p : productos) {
			if(p.getCodigo().equals(codigo)) {
				return p;
				}
		}
		return null;
	}
	
	public DisciplinaIndividual buscarDisciplina(TipoDisciplina tipo) {
		for (DisciplinaIndividual dI : disciplinas) {
			if (dI.getTipo() == tipo) {
				return dI;
			}
		}
		return null;
	}
	
	
	
	public Usuario autenticar(String login, String password) {
		Usuario u = buscarUsuario(login);
		if (u == null || !u.verificarPassword(password)) {
			throw new IllegalArgumentException("Usuario o contraseña incorrectos");
		}
		return u;
	}
	
	public Reserva reservar(Socio socio , TipoDisciplina tipo, String codigo, LocalDate fecha , LocalTime horaInicio , int duracionMinutos, int cantidadJugadores,Modalidad modalidad) {
		
		DisciplinaIndividual disciplina = buscarDisciplina(tipo);
		
		if(disciplina == null) {
			throw new IllegalArgumentException("La disciplina no existe");
		}
		
		Reserva reserva =  new Reserva(socio, disciplina, codigo, fecha, horaInicio, duracionMinutos, cantidadJugadores, modalidad);
		
		disciplina.registrarReserva(reserva);
		
		return reserva ;
	}
	public ExistenciaArticulo buscarExistencia(String nombreUbicacion, String codigoProducto) {
		for (ExistenciaArticulo ex : existencias) {
			if (ex.getUbicacion().getNombre().equals(nombreUbicacion) && 
				ex.getArticulo().getCodigo().equals(codigoProducto)) {
				return ex;
			}
		}
		return null;
	}

	public int consultarStock(String nombreUbicacion, String codigoProducto) {
		ExistenciaArticulo ex = buscarExistencia(nombreUbicacion, codigoProducto);
		return ex != null ? ex.getCantidad() : 0;
	}

	public void trasladarProducto(String origenId, String destinoId, String codigoProducto, int cantidad) {
		Ubicacion origen = buscarUbicacion(origenId);
		Ubicacion destino = buscarUbicacion(destinoId);

		if (origen == null || destino == null) {
			throw new IllegalArgumentException("Una de las ubicaciones no existe.");
		}

		ExistenciaArticulo exOrigen = buscarExistencia(origenId, codigoProducto);

		if (exOrigen == null || !exOrigen.hayDisponibles(cantidad)) {
			throw new IllegalArgumentException("El producto no existe en la sede origen o no hay stock suficiente.");
		}

		ExistenciaArticulo exDestino = buscarExistencia(destinoId, codigoProducto);

		if (exDestino != null) {
			throw new IllegalArgumentException("El producto ya está en el catálogo de la sede destino. Traslado denegado.");
		}

		exOrigen.retirarUnidades(cantidad);

		Producto prod = buscarProducto(codigoProducto);
		ExistenciaArticulo nuevaEx = new ExistenciaArticulo((ArticuloTienda) prod, destino, cantidad);
		agregarExistenciaArticulo(nuevaEx);
	}
	public Torneo buscarTorneo(String idTorneo) {
		for (Torneo t : torneos) {
			if (t.getIdTorneo().equals(idTorneo)) {
				return t;
			}
		}
		return null;
	}
    
    public InscripcionTorneoEmpleado inscribirEmpleadoEnTorneo(String idTorneo, Empleado empleado) {
        if (empleado == null || buscarUsuario(empleado.getLogin()) != empleado) {
            throw new IllegalArgumentException("El empleado debe estar registrado en el club");
        }
        Torneo torneo = buscarTorneo(idTorneo);
        if (torneo == null) {
            throw new IllegalArgumentException("El torneo no existe");
        }
        return torneo.inscribirEmpleado(empleado);
    }

	public void inscribirTorneo(String idTorneo ,Socio socio ){
		Torneo torneo = buscarTorneo(idTorneo);
		if(torneo == null){
			throw new IllegalArgumentException("torneo no existe");
		}
		if (torneo.getModalidad() != Torneo.Modalidad.INDIVIDUAL) {
			throw new IllegalArgumentException("No se puede inscribir a un socio individual en un torneo de modalidad " + torneo.getModalidad());
		}
        if (socio == null || buscarUsuario(socio.getLogin()) != socio) {
            throw new IllegalArgumentException("El socio debe estar registrado en el club");
        }
        boolean esPreferencial= socio.getPuntos() >= 100;
		
	 
		if (esPreferencial) {
			if (torneo.getCuposPreferencialesDisponibles() <= 0) {
				throw new IllegalStateException("No hay cupos preferenciales disponibles.");
			}
		} else {
			if (torneo.getCuposEstandarDisponibles() <= 0) {
				throw new IllegalStateException("No hay cupos estándar disponibles.");
			}
		}
        torneo.inscribirSocio(socio, esPreferencial);
    }
    public void inscribirEquipoEnTorneo(String idTorneo, Equipo equipo) {
		Torneo torneo = buscarTorneo(idTorneo);
		if (torneo == null) {
			throw new IllegalArgumentException("El torneo no existe.");
		}
		
		if (torneo.getModalidad() == Torneo.Modalidad.INDIVIDUAL) {
			throw new IllegalArgumentException("No se puede inscribir un equipo en un torneo INDIVIDUAL.");
		}

		boolean esPreferencial = false;

		if (esPreferencial) {
			if (torneo.getCuposPreferencialesDisponibles() <= 0) {
				throw new IllegalStateException("No hay cupos preferenciales disponibles.");
			}
		} else {
			if (torneo.getCuposEstandarDisponibles() <= 0) {
				throw new IllegalStateException("No hay cupos estándar disponibles.");
			}
		}
		torneo.consumirCupo(esPreferencial);
		
	}
	
	public void aprobarSugerencia(SugerenciaProducto sugerencia){
		
		if(sugerencia.isEsSnack() && !sugerencia.tieneAlergenosDeclarados()) {
			throw new IllegalStateException("No se puede aprobar un snack sin alergenos declarado");
			}
		sugerencia.aprobar();
		
		Producto p = sugerencia.crearProducto();
		agregarProducto(p);
	
	}
	
	public void rechazarSugerencia(SugerenciaProducto sugerencia) {
		sugerencia.rechazar();
	}
	
	public List<SugerenciaProducto> sugerenciasDe(Empleado empleado){
		List<SugerenciaProducto> resultado = new ArrayList<>();
		for(SugerenciaProducto s : sugerencias) {
			if(s.getEmpleado()==empleado) {
				resultado.add(s);
			}
		}
		
		return resultado;
	}
	
	
	
	public Venta comprar(Socio socio, String codigoProducto,int cantidad, boolean usarBono) {
		Producto producto = buscarProducto(codigoProducto);
		if(producto == null) {
			throw new IllegalArgumentException("El producto no existe");
		}
		if(cantidad<=0) {
			throw new IllegalArgumentException("La cantidad debe ser mayor que 0");
		}
		Venta venta;
		if(producto instanceof ArticuloCafeteria) {
			venta = new VentaCafeteria(LocalDate.now(),socio);
		}
		else {
			venta = new VentaTienda(LocalDate.now(),socio,null);
		}
		
		venta.agregarDetalle(new DetalleVenta(producto,cantidad,producto.getPrecio()));
		
		if (usarBono) {
		    BonoDeDescuento bono = socio.getBonoDeDescuento();

		    if (bono == null) {
		        throw new IllegalStateException("No tienes bonos disponibles");
		    }

		    venta.aplicarBono(bono);
		}
		
		venta.registrarPuntosGanados();
		return venta;
	}
		public String finalizarTorneoYEntregarPremio(String idTorneo, String loginGanador) {
			Torneo torneo = buscarTorneo(idTorneo);
			if (torneo == null) {
				throw new IllegalArgumentException("El torneo no existe.");
			}
			
			Usuario usuario = buscarUsuario(loginGanador);
			if (!(usuario instanceof Socio)) {
				throw new IllegalArgumentException("El ganador debe ser un socio registrado.");
			}
			
			Socio ganador = (Socio) usuario;

			
			torneo.finalizar(ganador);

			
			if (torneo.getTipo() == Torneo.Tipo.RECREATIVO) {
				
				BonoDeDescuento bono = new BonoDeDescuento(0); 
				ganador.recibirBono(bono);
				
				return "Torneo Recreativo finalizado con éxito. Se ha entregado un bono de descuento al socio: " + ganador.getNombre();
				
			} else {
				
				
				double pozoAcumulado = torneo.getRecaudoTotal();
				
				 ganador.agregarSaldo(pozoAcumulado);
				
				return "Torneo Competitivo finalizado. El ganador " + ganador.getNombre() + " se lleva el pozo total de $" + pozoAcumulado;
			}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
}