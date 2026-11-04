package modelo.competencias;

import modelo.deportes.Deporte;

public abstract class Formato {
    public abstract String getNombre();

    public int puntosPorPartido(Deporte deporte, int favor, int contra) {
        if (favor < 0 || contra < 0) {
            throw new IllegalArgumentException("Los marcadores no pueden ser negativos");
        }
        if (favor == contra) {
            if (!deporte.permiteEmpate()) {
                throw new IllegalArgumentException("Este deporte no permite empates");
            }
            return 1;
        }
        if (favor < contra) return 0;
        // Regla explícita para fútbol; para otros deportes se adopta 2 puntos por victoria.
        return deporte.permiteEmpate() ? 3 : 2;
    }
}
