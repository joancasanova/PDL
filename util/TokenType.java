package util;

// Enumeración para representar los tipos de tokens
public enum TokenType {
    PALABRARESERVADA,   // Para palabras reservadas (e.g., boolean, function)
    ENTERO,          // Para constantes enteras (e.g., 123)
    CADENA,             // Para cadenas de caracteres (e.g., "¡Hola, mundo!")
    ID,      // Para identificadores (e.g., nombreVariable)
    ASIGNACIONSUMA,     // Para la asignación con suma (e.g., +=)
    COMPARADOR,         // Para comparadores (e.g., ==)
    ASIGNACION,         // Para la asignación (e.g., =)
    SUMA,               // Para operadores (e.g., +)
    NEGACION,           // Para operadores (e.g., !)
    COMA,
    PUNTOCOMA,
    ABREPARENTESIS,
    CIERRAPARENTESIS,
    ABRECORCHETE,
    CIERRACORCHETE,
    FINDEFICHERO
}