package estructuras;

public class SimboloNormal extends Simbolo {

    private Integer desplazamiento;
    private Integer ancho;

    public SimboloNormal(Tipo tipo, String nombre, Integer desplazamiento, Integer ancho) {
        super(tipo, nombre);
        this.desplazamiento = desplazamiento;
        this.ancho = ancho;
    }

    public Integer getAncho() {
        return this.ancho;
    }

    public void setAncho(Integer ancho) {
        this.ancho = ancho;
    }

    public Integer getDesplazamiento() {
        return this.desplazamiento;
    }

    public void setDesplazamiento(Integer desplazamiento) {
        this.desplazamiento = desplazamiento;
    }

}
