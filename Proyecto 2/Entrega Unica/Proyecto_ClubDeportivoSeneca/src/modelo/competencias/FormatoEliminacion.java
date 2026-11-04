package modelo.competencias;

import modelo.deportes.Deporte;

public class FormatoEliminacion extends Formato {
    @Override
    public String getNombre() {
        return "ELIMINACION_DIRECTA";
    }

    @Override
    public int puntosPorPartido(Deporte deporte, int favor, int contra) {
        if (favor == contra) {
            throw new IllegalArgumentException("La eliminacion exige un ganador");
        }
        return super.puntosPorPartido(deporte, favor, contra);
    }
}
