package src.sintactico;

public class AccionAceptar extends Accion {
    public AccionAceptar() {
        super.setTipo("aceptar");
    }

    @Override
    public Integer ejecutar(GestorPilas gestorPilas) {
        return 1;
    }
}
