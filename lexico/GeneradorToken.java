package lexico;

import main.Analizador;
import util.TablaSimbolos;
import util.Token;
import util.TokenType;

/**
 * Clase GeneradorToken que se encarga de generar tokens en base a los estados finales
 * y caracteres actuales durante el análisis léxico.
 */
public class GeneradorToken {

    // Token generado
    private Token token;

    // Máximo número de caracteres en una cadena
    private static final int MAX_CARACTERES_CADENA = 64;

    // Máximo valor para una constante entera
    private static final int MAX_VALOR_ENTERO = 32767;

    /**
     * Constructor por defecto de GeneradorToken.
     */
    public GeneradorToken() {}

    /**
     * Procesa el token actual en base al estado final alcanzado.
     * Este método se encarga de generar un token apropiado según el estado final
     * del análisis léxico y el lexema actual.
     * 
     * @param estadoFinal El estado final alcanzado en el análisis léxico.
     * @param charActual El carácter actual en el análisis.
     * @param lexema El lexema actual a procesar.
     * @return El token procesado o null si no hay token.
     * @throws IllegalStateException Si se encuentra un error en el procesamiento del token.
     */
    public Token generarToken(EstadoFinal estadoFinal, Character charActual, String lexema) throws IllegalStateException {
        token = null;
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
                token = new Token(TokenType.PALABRARESERVADA, lexema);
                break;

            case IDENTIFICADOR:
                TablaSimbolos tablaActual = Analizador.tablas.peek();

                String nombre = lexema;
                if (tablaActual.simboloExiste(nombre)) {
                    token = new Token(TokenType.ID, tablaActual.obtenerPosicionSimbolo(nombre));
                } else {
                    int nuevaPosicion = tablaActual.numeroEntradas();
                    tablaActual.agregarSimbolo(nuevaPosicion, nombre, null, null, null);
                    token = new Token(TokenType.ID, nuevaPosicion);
                }
                break;

            case CADENA:
                if (lexema.length() - 2 >= MAX_CARACTERES_CADENA) {
                    throw new IllegalStateException("Error: Cadena demasiado larga: " + lexema);
                }
                token = new Token(TokenType.CADENA, lexema);
                break;

            case ENTERO:
                Integer valorEntero = Integer.valueOf(lexema);

                // Se comprueba si el valor del entero no supera el valor máximo
                if (valorEntero < MAX_VALOR_ENTERO) {
                    token = new Token(TokenType.ENTERO, valorEntero);
                } else {
                    throw new IllegalArgumentException(
                            "Se ha superado el valor máximo de la representación.\n Valor: " + valorEntero);
                }
                break;
        }
        return token;
    }
}
