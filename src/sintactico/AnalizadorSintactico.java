package sintactico;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import token.*;
/**
 * Clase AnalizadorSintactico para procesar tokens y aplicar reglas de análisis
 * sintáctico LR(1).
 */
public class AnalizadorSintactico {

    private static ParserGramatica gramatica = new ParserGramatica();
    private static GestorPilas gestorPilas = new GestorPilas();

    private Boolean aceptado;

    /**
     * Constructor del analizador sintáctico.
     * @throws IOException
     */
    public AnalizadorSintactico() {
        this.aceptado = false;
    }

    public List<Integer> procesarToken(Token token) throws IllegalStateException {
        List<Integer> reglasAplicadas = new ArrayList<>();
        while (true) {
            String contenidoDeToken = obtenerContenidoToken(token);
            Accion accion = obtenerAccion(contenidoDeToken, gestorPilas.getPilaEstados().peek());

            //System.out.println("---");
            //System.out.println("Cima pila: "+ gestorPilas.getPilaEstados().peek());
            //System.out.println("Token: "+ contenidoDeToken);
            //System.out.println("Accion: "+ accion.getTipo());

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

        return reglasAplicadas;
    }

    private Accion obtenerAccion(String textoToken, Integer estadoCima) throws IllegalStateException {
        Map<String, Accion> accionesEstado = gramatica.getTablaAccion().get(estadoCima);
        Accion accion = accionesEstado.getOrDefault(textoToken, accionesEstado.get("$DEFAULT"));

        if (accion == null) {
            throw new IllegalStateException(
                    "sintáctico: Error de análisis sintáctico en el token " + textoToken);
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
}
