package sintactico;

public enum Terminal {
    SUMA("'+'"),
    NEGACION("'!'"),
    COMA("','"),
    PUNTOCOMA("';'"),
    ABREPARENTESIS("'('"),
    CIERRAPARENTESIS("')'"),
    ABRECORCHETE("'{'"),
    CIERRACORCHETE("'}'"),
    ASIGNACION("'='"),
    COMPARADOR("EQ_OP"),
    ASIGNACIONSUMA("ADD_OP"),
    FINDEFICHERO("$end");

    private final String symbol;

    Terminal(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public static Terminal procesarTerminal(String texto) {
        for (Terminal terminal : Terminal.values()) {
            if (terminal.symbol.equalsIgnoreCase(texto)) {
                return terminal;
            }
        }
        return null;
    }
}
