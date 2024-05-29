package sintactico;

import java.util.Stack;

public class GestorPilas {

    private Stack<Integer> pilaEstados;
    private Stack<String> pilaSimbolos;

    private static final String FIN_DE_FICHERO = "FINDEFICHERO";

    public GestorPilas() {
        this.pilaEstados = new Stack<>();
        this.pilaSimbolos = new Stack<>();
        pilaEstados.push(0);
        pilaSimbolos.push(FIN_DE_FICHERO);
    }

    public Stack<Integer> getPilaEstados() {
        return pilaEstados;
    }

    public Stack<String> getPilaSimbolos() {
        return pilaSimbolos;
    }
}
