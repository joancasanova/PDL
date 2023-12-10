package lexico;

import java.util.*;

import main.Analizador;
import util.TablaSimbolos;
import util.Token;
import util.TokenType;

/**
 * Clase que implementa un analizador léxico para un lenguaje de programación.
 * Esta clase es responsable de leer un archivo fuente y generar tokens a partir
 * de los lexemas identificados. Utiliza un enfoque de análisis caracter por
 * caracter para identificar los diferentes tipos de tokens.
 */
public class AnalizadorLexico {

    // Almacena los caracteres leídos para la formación de tokens
    private StringBuilder bufferCaracteres;

    // Caracter actual en análisis.
    private Character charActual;

    // Estado actual de transición durante el análisis léxico.
    private EstadoTransito estadoTransito;
    
    // Estado final alcanzado tras el análisis de una serie de caracteres.
    private EstadoFinal estadoFinal;

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

    // Conjunto de palabras reservadas del lenguaje.
    private static final HashSet<String> palabrasReservadas = new HashSet<>(Arrays.asList(
            "boolean", "function", "if", "int", "let",
            "put", "return", "string", "void", "while", "get"));

    // Máximo número de caracteres en una cadena
    private static final int MAX_CARACTERES_CADENA = 64;

    // Máximo valor para una constante entera
    private static final int MAX_VALOR_ENTERO = 32767;
   
    /**
     * Constructor que inicializa el analizador léxico.
     */
    public AnalizadorLexico() {
        this.estadoTransito = EstadoTransito.INICIO;
        this.estadoFinal = EstadoFinal.PENDIENTE;
        this.bufferCaracteres = new StringBuilder();
    }

    /**
     * Procesa un caracter y actualiza el estado del analizador léxico.
     * 
     * @param ch Caracter a procesar.
     * @return Lista de tokens identificados tras procesar el caracter.
     * @throws IllegalStateException Si se encuentran errores en el estado del analizador.
     */
    public List<Token> procesarCaracter(Character ch) throws IllegalStateException {
        verificarPrecondiciones(ch);
        charActual = ch;

        List<Token> listaToken = new ArrayList<>();
        while (charActual != null) {

            // Actualizar el estado dependiento del caracter actual
            actualizarEstado();

            // Obtener token si se ha alcanzado un estado final al procesar el caracter
            listaToken.add(procesarToken());

            // Actualizar buffer de caracteres dependiendo de si es estado inicial o no
            if (estadoTransito == EstadoTransito.INICIO) {
                bufferCaracteres.setLength(0);
            } else {
                bufferCaracteres.append(charActual);
            }

            // Consumir el caracter si el analizador no se encuentra en ciertos estados
            if (debeConsumirCaracter(estadoFinal)) {
                charActual = null;
            }
        }

        return listaToken;
    }

    /**
     * Actualiza el estado de transición del analizador en base al caracter actual.
     * 
     * @throws IllegalStateException Si se encuentran errores en el estado del analizador.
     */
    private void actualizarEstado() throws IllegalStateException {

        switch (estadoTransito) {

            case INICIO:
                procesarEstadoInicial();
                break;

            case COMENTARIO:
                if (charActual == '/') {
                    estadoTransito = EstadoTransito.TEXTOCOMENTARIO;
                } else {
                    throw new IllegalStateException("Caracter no esperado. Se esperaba /.");
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
                    if (palabrasReservadas.contains(bufferCaracteres.toString())) {
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
     * Procesa el token actual en base al estado final alcanzado.
     * 
     * @return El token procesado o null si no hay token.
     * @throws IllegalStateException Si se encuentra un error en el procesamiento del token.
     */
    private Token procesarToken() throws IllegalStateException {

        Token token = null;

        switch (estadoFinal) {
            case PENDIENTE:
            case FINCOMENTARIO:
                // Emitir un token nulo
                break;

            case FINDEFICHERO:
                token = new Token(TokenType.FINDEFICHERO, "");
                break;

            case NEGACION:
                token = new Token(TokenType.NEGACION, "");
                break;

            case COMA:
                token = new Token(TokenType.COMA, "");
                break;

            case PUNTOCOMA:
                token = new Token(TokenType.PUNTOCOMA, "");
                break;

            case ABREPARENTESIS:
                token = new Token(TokenType.ABREPARENTESIS, "");
                break;

            case CIERRAPARENTESIS:
                token = new Token(TokenType.CIERRAPARENTESIS, "");
                break;

            case ABRECORCHETE:
                token = new Token(TokenType.ABRECORCHETE, "");
                break;

            case CIERRACORCHETE:
                token = new Token(TokenType.CIERRACORCHETE, "");
                break;

            case COMPARADOR:
                token = new Token(TokenType.COMPARADOR, "");
                break;

            case ASIGNACION:
                token = new Token(TokenType.ASIGNACION, "");
                break;

            case ASIGNACIONSUMA:
                token = new Token(TokenType.ASIGNACIONSUMA, "");
                break;

            case SUMA:
                token = new Token(TokenType.SUMA, "");
                break;

            case PALABRARESERVADA:
                token = new Token(TokenType.PALABRARESERVADA, bufferCaracteres.toString());
                break;

            case IDENTIFICADOR:
                TablaSimbolos tablaActual = Analizador.tablas.peek();

                String nombre = bufferCaracteres.toString();
                if (tablaActual.simboloExiste(nombre)) {
                    token = new Token(TokenType.ID, tablaActual.obtenerPosicionSimbolo(nombre));
                } else {
                    int nuevaPosicion = tablaActual.numeroEntradas();
                    tablaActual.agregarSimbolo(nuevaPosicion, nombre, null, null, null);
                    token = new Token(TokenType.ID, nuevaPosicion);
                }
                break;

            case CADENA:
                if (bufferCaracteres.length() - 2 >= MAX_CARACTERES_CADENA) {
                    throw new IllegalStateException("Error: Cadena demasiado larga: " + bufferCaracteres.toString());
                }
                token = new Token(TokenType.CADENA, bufferCaracteres.toString());
                break;

            case ENTERO:
                Integer valorEntero = Integer.valueOf(bufferCaracteres.toString());

                // Se comprueba si el valor del entero no supera el valor máximo
                if (valorEntero < MAX_VALOR_ENTERO) {
                    token = new Token(TokenType.ENTERO, valorEntero);
                } else {
                    throw new IllegalArgumentException(
                            "Se ha superado el valor máximo de la representación.\n Valor: " + valorEntero);
                }
                break;
        }

        estadoFinal = EstadoFinal.PENDIENTE;
        return token;
    }

    /**
     * Procesa el estado inicial del analizador en base al caracter actual.
     * 
     * @throws IllegalStateException Si se encuentra un error en el procesamiento del estado inicial.
     */
    private void procesarEstadoInicial() {

        if (bufferCaracteres.length() > 1) {
            throw new IllegalStateException(
                    "Hay varios caracteres pendientes de procesar.\nContenido del buffer de caracteres: "
                            + bufferCaracteres.toString());
        }

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
                    throw new IllegalStateException("Error al procesar el caracter actual: " + charActual);
                }
                break;
        }
    }

    /**
     * Verifica las precondiciones antes de procesar un nuevo caracter.
     * 
     * @param ch Caracter a procesar.
     * @throws IllegalStateException Si se encuentran errores en las precondiciones.
     */
    private void verificarPrecondiciones(Character ch) throws IllegalStateException {
        // Comprobar que no se envía un caracter nulo
        if (ch == null) {
            throw new IllegalStateException("Se ha enviado un caracter nulo al analizador lexico.");
        }
        // Comprobar que no se sobreescribe un caracter que no se ha procesado
        if (charActual != null) {
            throw new IllegalStateException("Se ha enviado un caracter cuando aún hay otro pendiente de procesar.");
        }
    }
    
    /**
     * Determina si se debe consumir el caracter actual en base al estado final.
     * 
     * @param estado Estado final actual.
     * @return true si se debe consumir el caracter, false en caso contrario.
     */
    private boolean debeConsumirCaracter(EstadoFinal estado) {
        return !(estado == EstadoFinal.SUMA || estado == EstadoFinal.ASIGNACION ||
                estado == EstadoFinal.IDENTIFICADOR || estado == EstadoFinal.ENTERO);
    }
}
