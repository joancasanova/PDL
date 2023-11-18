package sintactico;

public class AccionReducir extends Accion {
    private Integer regla;
    private String noTerminal;

    public AccionReducir(Integer regla, String noTerminal) {
        super.setTipo("reducir");
        this.noTerminal = noTerminal;
        this.regla = regla;
    }

    public Integer getRegla() {
        return regla;
    }

    public String getNoTerminal() {
        return noTerminal;
    }
}