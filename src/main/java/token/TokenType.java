package token;

public enum TokenType {
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
    FINDEFICHERO("$end"),
    ENTERO("ENTERO"),
    CADENA("CADENA"),
    ID("ID"),
    PALABRARESERVADA("");

    private final String symbol;

    TokenType(String symbol) {
        this.symbol = symbol;
    }

    public static TokenType procesarTipoToken(String texto) {
        for (TokenType tipoToken : TokenType.values()) {
            if (tipoToken.symbol.equalsIgnoreCase(texto)) {
                return tipoToken;
            }
        }
        return null;
    }
}
