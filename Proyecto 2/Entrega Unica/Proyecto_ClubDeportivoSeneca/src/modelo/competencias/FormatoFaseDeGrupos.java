package modelo.competencias;

public class FormatoFaseDeGrupos extends FormatoLiga {
    private int cantidadGrupos;

    public FormatoFaseDeGrupos(int cantidadGrupos) {
        if (cantidadGrupos < 1) {
            throw new IllegalArgumentException("Debe existir al menos un grupo");
        }
        this.cantidadGrupos = cantidadGrupos;
    }

    public int getCantidadGrupos() {
        return cantidadGrupos;
    }

    @Override
    public String getNombre() {
        return "FASE_DE_GRUPOS";
    }
}
