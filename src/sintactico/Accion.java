package src.sintactico;

public abstract class Accion {
    private String tipo;

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    public abstract Integer ejecutar(GestorPilas gestorPilas);
}