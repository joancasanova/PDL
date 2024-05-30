package main;

import lexico.AnalizadorLexico;
import semantico.AnalizadorSemantico;
import sintactico.AnalizadorSintactico;
import tablaSimbolos.GestorTablas;
import token.TipoToken;
import token.Token;
import util.GestorErrores;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que gestiona el análisis del archivo fuente.
 * Utiliza los analizadores léxico, sintáctico y semántico para procesar el
 * archivo.
 */
public class GestorAnalisis {

    private GestorTablas gestorTablas;
    private AnalizadorLexico analizadorLexico;
    private AnalizadorSintactico analizadorSintactico;
    private AnalizadorSemantico analizadorSemantico;

    // Instancia única de la clase
    private static GestorAnalisis instancia;

    /**
     * Constructor privado para evitar la creación de instancias.
     */
    private GestorAnalisis() {
        gestorTablas = GestorTablas.getInstance();
        analizadorLexico = AnalizadorLexico.getInstance();
        analizadorSintactico = AnalizadorSintactico.getInstance();
        analizadorSemantico = AnalizadorSemantico.getInstance();
    }

    /**
     * Devuelve la instancia única de la clase.
     * Si la instancia no ha sido creada aún, la crea.
     *
     * @return La instancia única de GestorAnalisis.
     */
    public static synchronized GestorAnalisis getInstance() {
        if (instancia == null) {
            instancia = new GestorAnalisis();
        }
        return instancia;
    }

    /**
     * Procesa el archivo fuente utilizando los analizadores léxico, sintáctico y
     * semántico.
     *
     * @param fichero El FileReader del archivo fuente.
     * @throws IOException Si ocurre un error de lectura.
     */
    public void procesarFichero(FileReader fichero) throws IOException {
        gestorTablas.nuevaTabla();
        GestorErrores.iniciarLinea();
        List<Token> listaTokens = new ArrayList<>();
        List<Integer> listaReglas = new ArrayList<>();

        Boolean finDeFichero = false;
        do {
            char caracter = (char) fichero.read();
            for (Token token : analizadorLexico.procesarCaracter(caracter)) {
                listaTokens.add(token);
                if (token.getTipo().equals(TipoToken.FINDEFICHERO)) {
                    finDeFichero = true;
                }
                List<Integer> reglasProcesadas = analizadorSintactico.procesarToken(token);
                for (Integer regla : reglasProcesadas) {
                    listaReglas.add(regla);
                    analizadorSemantico.procesarRegla(regla);
                }
            }
            if (caracter == '\n') {
                GestorErrores.incrementarLinea();
            }
        } while (!finDeFichero);

        GestorSalida.escribirSalida(listaTokens, listaReglas, gestorTablas.getImpresionTabla().toString());

        gestorTablas.resetGestorTablas();
        analizadorLexico.resetAnalizadorLexico();
        analizadorSintactico.resetAnalizadorSintactico();
        analizadorSemantico.resetAnalizadorSemantico();
    }
}
