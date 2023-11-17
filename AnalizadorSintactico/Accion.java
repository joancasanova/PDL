package AnalizadorSintactico;

public abstract class Accion {
    String tipo;

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }
}