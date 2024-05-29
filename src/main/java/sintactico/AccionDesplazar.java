package sintactico;

public class AccionDesplazar extends Accion {
    private Integer estado;
    private String token;

    public AccionDesplazar(Integer estado, String token) {
        super.setTipo("desplazar");
        this.estado = estado;
        this.token = token;
    }

    public Integer getEstado() {
        return estado;
    }

    @Override
    public Integer ejecutar(GestorPilas gestorPilas) {
        gestorPilas.getPilaEstados().push(estado);
        gestorPilas.getPilaSimbolos().push(token);
        return null;
    }
}