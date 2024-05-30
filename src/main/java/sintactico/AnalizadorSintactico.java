package sintactico;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import sintactico.accion.Accion;
import sintactico.accion.AccionAceptar;
import sintactico.accion.AccionReducir;
import token.*;
import util.GestorErrores;

/**
 * Clase AnalizadorSintactico para procesar tokens y aplicar reglas de análisis
 * sintáctico LR(1).
 */
public class AnalizadorSintactico {

    private GestorPilas gestorPilas;
    private Boolean aceptado;

    // Instancia única de la clase
    private static AnalizadorSintactico instancia;

    /**
     * Constructor privado del analizador sintáctico para evitar la creación de
     * instancias.
     */
    private AnalizadorSintactico() {
        this.aceptado = false;
        gestorPilas = GestorPilas.getInstance();
    }

    /**
     * Devuelve la instancia única de la clase.
     * Si la instancia no ha sido creada aún, la crea.
     * 
     * @return La instancia única de AnalizadorSintactico.
     */
    public static synchronized AnalizadorSintactico getInstance() {
        if (instancia == null) {
            instancia = new AnalizadorSintactico();
        }
        return instancia;
    }

    /**
     * Procesa un token y aplica las reglas sintácticas correspondientes.
     * 
     * @param token El token a procesar.
     * @return Lista de reglas aplicadas durante el procesamiento.
     * @throws IllegalStateException Si se encuentra un error en el análisis
     *                               sintáctico.
     */
    public List<Integer> procesarToken(Token token) throws IllegalStateException {
        List<Integer> reglasAplicadas = new ArrayList<>();
        while (true) {
            String contenidoDeToken = obtenerContenidoToken(token);
            Accion accion = obtenerAccion(contenidoDeToken, gestorPilas.getPilaEstados().peek());

            Integer reglaAplicada = accion.ejecutar();
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

    /**
     * Obtiene la acción correspondiente a un token y un estado.
     * 
     * @param textoToken El texto del token.
     * @param estadoCima El estado en la cima de la pila.
     * @return La acción correspondiente.
     * @throws IllegalStateException Si no se encuentra una acción válida.
     */
    private Accion obtenerAccion(String textoToken, Integer estadoCima) throws IllegalStateException {
        Map<String, Accion> accionesEstado = ParserGramatica.getInstance().getTablaAccion().get(estadoCima);
        Accion accion = accionesEstado.getOrDefault(textoToken, accionesEstado.get("$DEFAULT"));

        if (accion == null) {
            GestorErrores.lanzarError(
                    GestorErrores.TipoError.SINTACTICO,
                    GestorErrores.ERROR_TOKEN_NO_ESPERADO + textoToken);
        }

        return accion;
    }

    /**
     * Obtiene el contenido del token procesado en la posición actual del análisis.
     * Si el token es una palabra reservada, devuelve su atributo en mayúsculas.
     * 
     * @param token El token a procesar.
     * @return El contenido del token.
     */
    private String obtenerContenidoToken(Token token) {
        // Se devuelve el tipo o el atributo dependiendo de si es palabra reservada
        return token.getTipo().equals(TipoToken.PALABRARESERVADA) ? String.valueOf(token.getAtributo()).toUpperCase()
                : String.valueOf(token.getTipo()).toUpperCase();
    }

    /**
     * Reinicia el analizador sintactico a su estado inicial.
     */
    public void resetAnalizadorSintactico() {
        this.aceptado = false;
        ParserGramatica.getInstance().resetParserGramatica();
        gestorPilas.resetGestorPilas();
        gestorPilas = GestorPilas.getInstance();
    }
}
