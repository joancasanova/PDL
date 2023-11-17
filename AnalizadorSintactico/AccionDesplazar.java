package AnalizadorSintactico;

public class AccionDesplazar extends Accion {
    Integer estado;

    public AccionDesplazar(Integer estado) {
        super.setTipo("desplazar");
        this.estado = estado;
    }

    public Integer getEstado() {
        return estado;
    }

}