package lexico;

import java.util.*;

import util.Token;

/**
 * Clase que implementa un analizador léxico para un lenguaje de programación.
 * Esta clase es responsable de leer un archivo fuente y generar tokens a partir
 * de los lexemas identificados. Utiliza un enfoque de análisis caracter por
 * caracter para identificar los diferentes tipos de tokens.
 */
public class AnalizadorLexico {

    // Almacena los caracteres leídos para la formación de tokens
    private StringBuilder bufferCaracteres;

    // Gestor de estados para el análisis léxico
    private GestorEstados gestorEstados;

    // Generador de tokens en base a los estados y lexemas identificados
    private GeneradorToken generadorDeTokens;

    /**
     * Constructor que inicializa el analizador léxico.
     * Inicializa un StringBuilder para el buffer de caracteres y crea instancias
     * de GestorEstados y GeneradorToken.
     */
    public AnalizadorLexico() {
        this.bufferCaracteres = new StringBuilder();
        this.gestorEstados = new GestorEstados();
        this.generadorDeTokens = new GeneradorToken();
    }

    /**
     * Procesa un caracter y actualiza el estado del analizador léxico.
     * 
     * @param caracterProProcesar Caracter a procesar.
     * @return Lista de tokens identificados tras procesar el caracter.
     * @throws IllegalStateException Si se encuentran errores en el estado del analizador.
     */
    public List<Token> procesarCaracter(Character caracterProProcesar) throws IllegalStateException {

        List<Token> listaToken = new ArrayList<>();

        while (caracterProProcesar != null) {
            String lexema = bufferCaracteres.toString();

            // Actualiza el estado según el caracter actual entrante
            gestorEstados.actualizarEstado(caracterProProcesar, lexema);

            // Generar token y almacenarlo si no es nulo
            Token token = generadorDeTokens.generarToken(
                    gestorEstados.getEstadoFinal(),
                    caracterProProcesar,
                    lexema);
            if (token != null) {
                listaToken.add(token);
            }

            // Actualizar buffer de caracteres dependiendo de si es estado inicial o no
            if (gestorEstados.getEstadoTransito() == EstadoTransito.INICIO) {
                bufferCaracteres.setLength(0);
            } else {
                bufferCaracteres.append(caracterProProcesar);
            }

            // Consumir el caracter si el analizador no se encuentra en ciertos estados
            if (debeConsumirCaracter(gestorEstados.getEstadoFinal())) {
                caracterProProcesar = null;
            }
        }

        return listaToken;
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
