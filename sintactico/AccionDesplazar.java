package sintactico;

public class AccionDesplazar extends Accion {
    private Integer estado;

    public AccionDesplazar(Integer estado) {
        super.setTipo("desplazar");
        this.estado = estado;
    }

    public Integer getEstado() {
        return estado;
    }

}