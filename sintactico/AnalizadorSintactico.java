package sintactico;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import util.Token;
import util.TokenType;

/**
 * Clase AnalizadorSintactico para procesar tokens y aplicar reglas de análisis
 * sintáctico LR(1).
 */
public class AnalizadorSintactico {
    private static final String FIN_DE_FICHERO = "FINDEFICHERO";

    private TablaAnalisis tablaAnalisis;
    private Stack<Integer> pilaEstados;
    private Stack<String> pilaSimbolos;
    private List<Integer> reglasAplicadas;

    private Boolean aceptado;

    /**
     * Constructor del analizador sintáctico.
     * 
     * @param tablaAnalisis Tabla de análisis sintáctico.
     * @param tokens        Lista de tokens a analizar.
     */
    public AnalizadorSintactico() {
        this.tablaAnalisis = new TablaAnalisis();
        this.pilaEstados = new Stack<>();
        this.pilaSimbolos = new Stack<>();
        this.reglasAplicadas = new ArrayList<>();
        this.aceptado = false;

        inicializarPilas();
    }

    public void procesarToken(Token token) {
        while (true) {
            String contenidoDeToken = obtenerContenidoToken(token);
            Accion accion = obtenerAccion(contenidoDeToken);
            ejecutarAccion(accion, contenidoDeToken);


            // Si la acción es de aceptación, finalizar el procesamiento
            if (accion instanceof AccionAceptar) {
                aceptado = true;
                break; // Salir del bucle
            }
            
            // Caso especial para el fin de fichero sin aceptación
            if (contenidoDeToken.equals("FINDEFICHERO") && !aceptado) {
                continue;
            }

            // En caso de una acción de reducción, continuar con el siguiente token
            else if (!(accion instanceof AccionReducir)) {
                break; // Salir del bucle
            }


            imprimirEstadoDePilas();
        }
    }

    /**
     * Inicializa las pilas de estados y símbolos con los valores iniciales.
     */
    private void inicializarPilas() {
        pilaEstados.push(0);
        pilaSimbolos.push(FIN_DE_FICHERO);
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
     * Obtiene la acción a realizar basándose en el token actual y el estado actual
     * de la pila.
     * Si no hay una acción definida para el token actual, se utiliza una acción por
     * defecto.
     *
     * @param tokenActualProcesado El token actual procesado.
     * @return La acción a realizar para el token y estado actual.
     */
    private Accion obtenerAccion(String tokenActualProcesado) {
        Integer estadoCima = pilaEstados.peek();
        Map<String, Accion> accionesEstado = tablaAnalisis.getActionTable().get(estadoCima);
        Accion accion = accionesEstado.getOrDefault(tokenActualProcesado, accionesEstado.get("$DEFAULT"));

        if (accion == null) {
            reportarErrorSintactico(tokenActualProcesado);
        }

        return accion;
    }

    /**
     * Ejecuta la acción determinada, ya sea desplazar, reducir o aceptar.
     *
     * @param accion La acción a ejecutar.
     * @param token  El token actual que se está procesando.
     */
    private void ejecutarAccion(Accion accion, String token) {
        if (accion instanceof AccionDesplazar) {
            desplazar((AccionDesplazar) accion, token);
        } else if (accion instanceof AccionReducir) {
            reducir((AccionReducir) accion);
        } else if (accion instanceof AccionAceptar) {
            aceptar();
        }
    }

    /**
     * Reporta un error de análisis sintáctico y termina la ejecución.
     * TODO: Esto se debe gestionar desde el analizador con la linea y token
     * correspondiente que se esté analizando
     * 
     * @param tokenActualProcesado El token en el que se encontró el error.
     */
    private void reportarErrorSintactico(String tokenActualProcesado) {
        System.err.println("Error de análisis sintáctico en el token " + tokenActualProcesado);
        throw new RuntimeException("Error de análisis sintáctico");
    }

    /**
     * Imprime el estado actual de las pilas de estados y símbolos.
     * Útil para depuración y seguimiento del proceso de análisis.
     */
    private void imprimirEstadoDePilas() {
        System.out.print("\tPila Estados: ");
        for (Integer integer : pilaEstados) {
            System.out.print(integer + ", ");
        }
        System.out.println();

        System.out.print("\tPila Simbolos: ");
        for (String string : pilaSimbolos) {
            System.out.print(string + ", ");
        }
        System.out.println();
    }

    /**
     * Realiza la acción de desplazamiento en la pila de estados y símbolos.
     * Agrega el nuevo estado y el token actual a las pilas.
     *
     * @param accion La acción de desplazamiento a realizar.
     * @param token  El token actual que se está procesando.
     */
    private void desplazar(AccionDesplazar accion, String token) {
        System.out.println("Desplazar: " + accion.getEstado() + ", Token: " + token);

        pilaEstados.push(accion.getEstado());
        pilaSimbolos.push(token); // Apilar el simbolo terminal
    }

    /**
     * Realiza la acción de reducción según la regla especificada.
     * Desapila los elementos correspondientes de las pilas y agrega
     * el símbolo no terminal resultante de la reducción.
     *
     * @param accion La acción de reducción a realizar.
     */
    private void reducir(AccionReducir accion) {
        System.out.println("Reducir: ");
        System.out.println("\tRegla: " + accion.getRegla());
        System.out.println("\tNo Terminal: " + accion.getNoTerminal());

        List<String> regla = tablaAnalisis.getReglas().get(accion.getRegla());

        // La cantidad de elementos a desapilar es igual a la longitud de la regla menos
        // uno (excluyendo el lado izquierdo)
        int cantidadDesapilar = regla.size() - 1;
        // Desapila la cantidad calculada de estados de una sola vez
        while (cantidadDesapilar > 0) {
            pilaEstados.pop();
            pilaSimbolos.pop(); // Desapilar los simbolos correspondientes a la regla
            cantidadDesapilar--;
        }

        // Agrega el estado correspondiente al no terminal de la regla
        String noTerminal = accion.getNoTerminal();
        pilaSimbolos.push(noTerminal); // Apilar el simbolo no terminal
        try {
            int estado = tablaAnalisis.getGotoTable().get(pilaEstados.peek()).get(noTerminal);
            pilaEstados.push(estado);
        } catch (NullPointerException e) {
            System.err.println("Error de análisis sintáctico. No existe el no terminal: " + noTerminal
                    + " en el estado " + pilaEstados.peek() + " para la tabla de goto");
        }
        // Registro de la regla aplicada en la numeracion del formato para vAST
        reglasAplicadas.add(accion.getRegla() + 1);
    }

    /**
     * Maneja la acción de aceptación, indicando que el análisis
     * sintáctico ha sido completado exitosamente.
     */
    private void aceptar() {
        System.out.println("Aceptar");
        reglasAplicadas.add(1);
        // Implementar la lógica de aceptación
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
