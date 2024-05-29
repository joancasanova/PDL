package lexico;

// Enumeración de posibles estados de transición durante el análisis léxico.
public enum EstadoTransito {
    INICIO,
    COMENTARIO,
    TEXTOCOMENTARIO,
    SIMBOLOIGUAL,
    SIMBOLOSUMA,
    LEXEMA,
    CARACTERNUMERICO,
    TEXTOCADENA
}
