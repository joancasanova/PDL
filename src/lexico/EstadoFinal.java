package src.lexico;

// Enumeración de posibles estados finales tras el análisis de un lexema.
public enum EstadoFinal {
    PENDIENTE,
    FINDEFICHERO,
    FINCOMENTARIO,
    NEGACION,
    SUMA,
    ASIGNACIONSUMA,
    ASIGNACION,
    COMPARADOR,
    IDENTIFICADOR,
    PALABRARESERVADA,
    ENTERO,
    CADENA,
    COMA,
    PUNTOCOMA,
    ABREPARENTESIS,
    CIERRAPARENTESIS,
    ABRECORCHETE,
    CIERRACORCHETE
}
