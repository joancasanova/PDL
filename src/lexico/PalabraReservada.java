package lexico;

public enum PalabraReservada {
    BOOLEAN,
    FUNCTION,
    IF,
    INT,
    LET,
    PUT,
    RETURN,
    STRING,
    VOID,
    WHILE,
    GET;

    public static Boolean contiene(String texto) {
        for (PalabraReservada palabraReservada : PalabraReservada.values()) {
            if (palabraReservada.name().equalsIgnoreCase(texto)) {
                return true;
            }
        }
        return false;
    }
}
