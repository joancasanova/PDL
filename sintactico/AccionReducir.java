package sintactico;

import java.util.Map;

public class AccionReducir extends Accion {
    private Integer regla;
    private String noTerminal;
    private Integer numeroDesapilar;

    private static Map<Integer,Map<String,Integer>> gotoTable = new generadorTablaAnalisis().getTablaGoTo();

    public AccionReducir(Integer regla, String noTerminal, Integer numeroDesapilar) {
        super.setTipo("reducir");
        this.noTerminal = noTerminal;
        this.regla = regla;
        this.numeroDesapilar = numeroDesapilar;
    }

    public Integer getRegla() {
        return regla;
    }

    public String getNoTerminal() {
        return noTerminal;
    }
    
    public Integer ejecutar(GestorPilas gestorPilas) {

        // Desapila la cantidad calculada de estados de una sola vez
        while (numeroDesapilar > 0) {
            gestorPilas.getPilaEstados().pop();
            gestorPilas.getPilaSimbolos().pop();
            numeroDesapilar--;
        }

        // Actualizar las pilas
        gestorPilas.getPilaSimbolos().push(noTerminal); 
        int estado = gotoTable.get(gestorPilas.getPilaEstados().peek()).get(noTerminal);
        gestorPilas.getPilaEstados().push(estado);

        return regla + 1;
    }
}