package lexico;

/**
 * Clase GestorEstados que maneja los estados de transición y los estados finales
 * durante el análisis léxico de un conjunto de caracteres.
 */
public class GestorEstados {

    // Estado actual de transición durante el análisis léxico.
    private EstadoTransito estadoTransito;

    // Estado final alcanzado tras el análisis de una serie de caracteres.
    private EstadoFinal estadoFinal;

    /**
     * Constructor de GestorEstados. Inicializa el estado de transición y el estado final.
     */
    public GestorEstados() {
        this.estadoTransito = EstadoTransito.INICIO;
        this.estadoFinal = EstadoFinal.PENDIENTE;
    }

    /**
     * Obtiene el estado actual de transición.
     * 
     * @return El estado actual de transición.
     */
    public EstadoTransito getEstadoTransito() {
        return estadoTransito;
    }

    /**
     * Obtiene el estado final alcanzado.
     * 
     * @return El estado final alcanzado.
     */
    public EstadoFinal getEstadoFinal() {
        return estadoFinal;
    }

    /**
     * Actualiza el estado de transición del analizador en base al carácter actual.
     * 
     * @param charActual El carácter actual a procesar.
     * @param lexema El lexema actual a procesar.
     * @throws IllegalStateException Si se encuentran errores en el estado del analizador.
     */
    public void actualizarEstado(Character charActual, String lexema) throws IllegalStateException {

        estadoFinal = EstadoFinal.PENDIENTE;

        switch (estadoTransito) {

            case INICIO:
                procesarEstadoInicial(charActual);
                break;

            case COMENTARIO:
                if (charActual == '/') {
                    estadoTransito = EstadoTransito.TEXTOCOMENTARIO;
                } else {
                    throw new IllegalStateException("léxico: Caracter no esperado. Se esperaba /.");
                }
                break;

            case TEXTOCOMENTARIO:
                if (charActual == '\n' || (int) charActual == 65535) {
                    estadoFinal = EstadoFinal.FINCOMENTARIO;
                }
                break;

            case SIMBOLOIGUAL:
                if (charActual == '=') {
                    estadoFinal = EstadoFinal.COMPARADOR;
                } else {
                    estadoFinal = EstadoFinal.ASIGNACION;
                }
                break;

            case SIMBOLOSUMA:
                if (charActual == '=') {
                    estadoFinal = EstadoFinal.ASIGNACIONSUMA;
                } else {
                    estadoFinal = EstadoFinal.SUMA;
                }
                break;

            case LEXEMA:
                if (!(Character.isLetterOrDigit(charActual) || charActual == '_')) {
                    if (PalabraReservada.contiene(lexema)) {
                        estadoFinal = EstadoFinal.PALABRARESERVADA;
                    } else {
                        estadoFinal = EstadoFinal.IDENTIFICADOR;
                    }
                }
                break;

            case CARACTERNUMERICO:
                if (!Character.isDigit(charActual)) {
                    estadoFinal = EstadoFinal.ENTERO;
                }
                break;

            case TEXTOCADENA:
                if (charActual == '\"') {
                    estadoFinal = EstadoFinal.CADENA;
                }
                break;
        }

        // En el caso de pasar a un estado final, regresar al estado de transito inicial
        if (estadoFinal != EstadoFinal.PENDIENTE) {
            estadoTransito = EstadoTransito.INICIO;
        }
    }

    /**
     * Procesa el estado inicial del analizador en base al carácter actual.
     * 
     * @param charActual El carácter actual a procesar.
     * @throws IllegalStateException Si se encuentra un error en el procesamiento del estado inicial.
     */
    private void procesarEstadoInicial(Character charActual) {

        switch (charActual) {

            // Transiciones a estados finales
            case '!':
                estadoFinal = EstadoFinal.NEGACION;
                break;

            case ',':
                estadoFinal = EstadoFinal.COMA;
                break;

            case ';':
                estadoFinal = EstadoFinal.PUNTOCOMA;
                break;

            case '(':
                estadoFinal = EstadoFinal.ABREPARENTESIS;
                break;

            case ')':
                estadoFinal = EstadoFinal.CIERRAPARENTESIS;
                break;

            case '{':
                estadoFinal = EstadoFinal.ABRECORCHETE;
                break;

            case '}':
                estadoFinal = EstadoFinal.CIERRACORCHETE;
                break;

            // Transiciones a estados intermedios
            case '/':
                estadoTransito = EstadoTransito.COMENTARIO;
                break;

            case '+':
                estadoTransito = EstadoTransito.SIMBOLOSUMA;
                break;

            case '=':
                estadoTransito = EstadoTransito.SIMBOLOIGUAL;
                break;

            case '\"':
                estadoTransito = EstadoTransito.TEXTOCADENA;
                break;

            // Otros casos
            default:
                if ((int) charActual == 65535) {
                    estadoFinal = EstadoFinal.FINDEFICHERO;
                } else if (Character.isLetter(charActual) || charActual == '_') {
                    estadoTransito = EstadoTransito.LEXEMA;
                } else if (Character.isDigit(charActual)) {
                    estadoTransito = EstadoTransito.CARACTERNUMERICO;
                } else if (Character.isWhitespace(charActual) || charActual == '\t' || charActual == '\n') {
                    // Permanecer en el estado inicial
                } else {
                    throw new IllegalStateException("léxico: Error al procesar el caracter actual: " + charActual);
                }
                break;
        }
    }
    
}
