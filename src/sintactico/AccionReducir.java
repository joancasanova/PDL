package src.sintactico;

import java.io.IOException;
import java.util.Map;

public class AccionReducir extends Accion {
    private Integer regla;
    private String noTerminal;
    private Integer numeroDesapilar;

    private static Map<Integer,Map<String,Integer>> gotoTable;

    public AccionReducir(Integer regla, String noTerminal, Integer numeroDesapilar) throws IOException {
        super.setTipo("reducir");
        this.noTerminal = noTerminal;
        this.regla = regla;
        this.numeroDesapilar = numeroDesapilar;
        this.gotoTable = new ParserGramatica().getTablaGoTo();
    }

    public Integer getRegla() {
        return regla;
    }

    public String getNoTerminal() {
        return noTerminal;
    }
    
    public Integer ejecutar(GestorPilas gestorPilas) {

        // System.out.println("\tReduccion: ");
        // System.out.println("\t- Regla: "+ regla);
        // System.out.println("\t- No terminal: "+ noTerminal);
        // System.out.println("\t- Cima estado: "+ gestorPilas.getPilaEstados().peek());
        // System.out.println("\t- Desapilar: "+ numeroDesapilar);

        // Desapila la cantidad calculada de estados de una sola vez
        int i = numeroDesapilar;
        while (i > 0) {
            gestorPilas.getPilaEstados().pop();
            gestorPilas.getPilaSimbolos().pop();
            i--;
        }

        // Actualizar las pilas
        gestorPilas.getPilaSimbolos().push(noTerminal); 
        int estado = gotoTable.get(gestorPilas.getPilaEstados().peek()).get(noTerminal);
        gestorPilas.getPilaEstados().push(estado);

        return regla + 1;
    }
}