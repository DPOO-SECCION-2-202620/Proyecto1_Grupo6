package modelo;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import modelo.comun.Modalidad;
import modelo.comun.Nivel;
import modelo.comun.TipoDisciplina;
import modelo.individual.DisciplinaIndividual;
import modelo.individual.Reserva;
import modelo.usuarios.Socio;

class ReservaTest {

	private Club club;
	private Socio juana;
	private DisciplinaIndividual tenis;
	private LocalDate fecha;
	private LocalTime hora;
	
	@BeforeEach

	void setUp() {
		club = new Club();
		
		tenis = new DisciplinaIndividual(TipoDisciplina.TENIS,2);
		
		club.agregarDisciplinaIndividual(tenis);
		
		
		DisciplinaIndividual padel = new DisciplinaIndividual(TipoDisciplina.PADEL,2);
		
		club.agregarDisciplinaIndividual(padel);
		
		
		juana = new Socio("juana", "676767", "Thompson", LocalDate.of(2005, 10, 22));
		club.agregarUsuario(juana);
		
		tenis.inscribirSocio(juana, Nivel.INTERMEDIO);
		
		fecha = LocalDate.of(2026,11, 6);
		hora = LocalTime.of(15, 0);
		
		
	}
	
	

	@Test
	void reservaCuandoHayCupos() {
		Reserva r = club.reservar(juana,TipoDisciplina.TENIS, "T1",fecha,hora,60,2,Modalidad.SENCILLOS);
		
		assertNotNull(r);
		assertTrue(tenis.getReservas().contains(r));
	}

}
