package lexico;

import java.util.ArrayList;
import java.util.List;

import tablaSimbolos.GestorTablas;
import tablaSimbolos.Simbolo;
import tablaSimbolos.TablaSimbolos;
import token.*;

/**
 * Clase GeneradorToken que se encarga de generar tokens en base a los estados
 * finales
 * y caracteres actuales durante el análisis léxico.
 */
public class GeneradorToken {

    // Token generado
    private Token token;

    // Simbolos a la espera de ser enviados
    private List<Simbolo> simbolosPorEnviar;

    // Ultimo token es PUNTOyCOMA
    private Boolean ultimoTokenPuntoComa;

    // Máximo número de caracteres en una cadena
    private static final int MAX_CARACTERES_CADENA = 64;

    // Máximo valor para una constante entera
    private static final int MAX_VALOR_ENTERO = 32767;

    /**
     * Constructor por defecto de GeneradorToken.
     */
    public GeneradorToken() {
        ultimoTokenPuntoComa = false;
        simbolosPorEnviar = new ArrayList<>();
    }

    /**
     * Procesa el token actual en base al estado final alcanzado.
     * Este método se encarga de generar un token apropiado según el estado final
     * del análisis léxico y el lexema actual.
     * 
     * @param estadoFinal El estado final alcanzado en el análisis léxico.
     * @param charActual  El carácter actual en el análisis.
     * @param lexema      El lexema actual a procesar.
     * @return El token procesado o null si no hay token.
     * @throws IllegalStateException Si se encuentra un error en el procesamiento
     *                               del token.
     */
    public Token generarToken(EstadoFinal estadoFinal, Character charActual, String lexema)
            throws IllegalStateException {

        token = null;
        TablaSimbolos tablaActual = GestorTablas.obtenerTablaActual();
        TablaSimbolos tablaGlobal = GestorTablas.obtenerTablaGlobal();

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
                if (!lexema.chars().allMatch(Character::isLowerCase)) {
                    throw new IllegalStateException(
                            "léxico: Las palabras reservadas deben ser escritas en minúsculas: " + lexema);
                }
                token = new Token(TokenType.PALABRARESERVADA, lexema);
                break;

            case IDENTIFICADOR:
                String nombre = lexema;
                Simbolo simbolo = null;
                Integer posicionTS = null;

                // Obtener simbolo de tabla actual
                if (tablaActual.simboloExiste(nombre)) {
                    simbolo = tablaActual.obtenerSimboloPorNombre(nombre);
                    posicionTS = tablaActual.obtenerPosicionSimbolo(simbolo);

                }

                // Si no está, comprobar tabla global
                else if (tablaGlobal.simboloExiste(nombre)
                        && !GestorTablas.getZonaDeclaracion()
                        && !GestorTablas.getZonaParametros()) {
                    simbolo = tablaGlobal.obtenerSimboloPorNombre(nombre);
                    posicionTS = tablaGlobal.obtenerPosicionSimbolo(simbolo);
                }

                // Si no está en ninguna tabla, crear e insertar en tabla actual
                else {
                    simbolo = new Simbolo(null, nombre, null, null);
                    posicionTS = tablaActual.numeroEntradas();
                    tablaActual.agregarSimbolo(posicionTS, simbolo);
                }

                // Establecer símbolo como pendiente de gestionar
                if (!ultimoTokenPuntoComa) {
                    GestorTablas.setUltimoSimbolo(simbolo);
                } else {
                    simbolosPorEnviar.add(simbolo);
                }

                token = new Token(TokenType.ID, posicionTS);

                break;

            case CADENA:
                if (lexema.length() - 2 >= MAX_CARACTERES_CADENA) {
                    throw new IllegalStateException("léxico: Cadena demasiado larga: " + lexema);
                }
                if (lexema.contains("\n")) {
                    throw new IllegalStateException("léxico: Cadena no puede contener salto de línea");
                }
                token = new Token(TokenType.CADENA, lexema);
                break;

            case ENTERO:
                Integer valorEntero = Integer.valueOf(lexema);

                // Se comprueba si el valor del entero no supera el valor máximo
                if (valorEntero <= MAX_VALOR_ENTERO) {
                    token = new Token(TokenType.ENTERO, valorEntero);
                } else {
                    throw new IllegalStateException(
                            "léxico: Se ha superado el valor numérico máximo.\n Valor: " + valorEntero);
                }
                break;
        }

        if (!ultimoTokenPuntoComa) {
            for (Simbolo simboloPorEnviar : simbolosPorEnviar) {
                GestorTablas.setUltimoSimbolo(simboloPorEnviar);
            }
            simbolosPorEnviar.clear();
        }

        if (token != null) {
            if (token.getTipo().equals(TokenType.PALABRARESERVADA) && token.getAtributo().equals("let")) {
                GestorTablas.setZonaDeclaracion(true);
            }
            if (token.getTipo() == TokenType.PUNTOCOMA || token.getTipo() == TokenType.ID) {
                ultimoTokenPuntoComa = true;
                GestorTablas.setZonaDeclaracion(false);
            } else {
                ultimoTokenPuntoComa = false;
            }
        }

        return token;
    }
}
