package sintactico;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import util.Token;
import util.TokenType;

/**
 * Clase AnalizadorSintactico para procesar tokens y aplicar reglas de análisis
 * sintáctico LR(1).
 */
public class AnalizadorSintactico {

    private static generadorTablaAnalisis tablaAnalisis = new generadorTablaAnalisis();
    private static GestorPilas gestorPilas = new GestorPilas();

    private List<Integer> reglasAplicadas;

    private Boolean aceptado;

    /**
     * Constructor del analizador sintáctico.
     */
    public AnalizadorSintactico() {
        this.reglasAplicadas = new ArrayList<>();
        this.aceptado = false;
    }

    public void procesarToken(Token token) {
        while (true) {
            String contenidoDeToken = obtenerContenidoToken(token);
            Accion accion = obtenerAccion(contenidoDeToken, gestorPilas.getPilaEstados().peek());
            Integer reglaAplicada = accion.ejecutar(gestorPilas);
            if (reglaAplicada != null) {
                reglasAplicadas.add(reglaAplicada);
            }

            // Si la acción es de aceptación, finalizar el procesamiento
            if (accion instanceof AccionAceptar) {
                aceptado = true;
                break;
            }

            // Caso especial para el fin de fichero sin aceptación: continuar procesando
            if (contenidoDeToken.equals("FINDEFICHERO") && !aceptado) {
                continue;
            }

            // En caso de una acción de reducción, saltar al siguiente token
            else if (!(accion instanceof AccionReducir)) {
                break;
            }
        }
    }

    public Accion obtenerAccion(String textoToken, Integer estadoCima) throws IllegalStateException {
        Map<String, Accion> accionesEstado = tablaAnalisis.getTablaAccion().get(estadoCima);
        Accion accion = accionesEstado.getOrDefault(textoToken, accionesEstado.get("$DEFAULT"));

        if (accion == null) {
            throw new IllegalStateException(
                    "Error de análisis sintáctico: Error de análisis sintáctico en el token " + textoToken);
        }

        return accion;
    }

    /**
     * Obtiene el token procesado en la posición actual del análisis.
     * Si se alcanza el final de la lista de tokens, devuelve el token especial
     * FIN_DE_FICHERO.
     *
     * @param indiceTokenActual Índice del token actual en la lista de tokens.
     * @return El token procesado en forma de cadena.
     */
    private String obtenerContenidoToken(Token token) {

        // Se devuelve el tipo o el atributo dependiendo de si es palabra reservada
        return token.tipo.equals(TokenType.PALABRARESERVADA) ? String.valueOf(token.atributo).toUpperCase()
                : String.valueOf(token.getTipo()).toUpperCase();
    }

    /**
     * Obtiene la lista de reglas aplicadas durante el análisis sintáctico.
     * Útil para depuración y verificación del proceso de análisis.
     *
     * @return Lista de números de reglas aplicadas.
     */
    public List<Integer> getReglasAplicadas() {
        return reglasAplicadas;
    }
}
