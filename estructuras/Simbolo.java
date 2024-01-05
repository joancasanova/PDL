package estructuras;

public abstract class Simbolo {
    private String nombre;
    private Tipo tipo;

    Simbolo(Tipo tipo, String nombre) {
        this.tipo = tipo;
        this.nombre = nombre;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Tipo getTipo() {
        return this.tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

}