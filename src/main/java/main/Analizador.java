package main;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import lexico.AnalizadorLexico;
import semantico.AnalizadorSemantico;
import sintactico.AnalizadorSintactico;
import tablaSimbolos.*;
import token.*;
import util.GestorSalida;

/**
 * Clase de acceso por CLI al analizador
 * 
 * @author Juan Francisco Casanova Ferrer
 */
public class Analizador {

    public static void main(String[] args) {

        if (args.length < 1) {
            System.err.println("Por favor, especifique la ruta del archivo a analizar.");
            return;
        }

        String rutaArchivo = args[0];
        procesarFichero(rutaArchivo);
    }

    private static void procesarFichero(String rutaArchivo) {
        int linea = 1;

        GestorTablas.nuevaTabla();
        AnalizadorLexico analizadorLexico = new AnalizadorLexico();
        AnalizadorSintactico analizadorSintactico = new AnalizadorSintactico();
        AnalizadorSemantico analizadorSemantico = new AnalizadorSemantico();

        List<Token> listaTokens = new ArrayList<Token>();
        List<Integer> listaReglas = new ArrayList<Integer>();

        try (FileReader fichero = new FileReader(rutaArchivo)) {

            Boolean finDeFichero = false;
            do {
                char caracter = (char) fichero.read();
                for (Token token : analizadorLexico.procesarCaracter(caracter)) {

                    // Anadir token a la lista de tokens
                    listaTokens.add(token);
                    if (token.getTipo().equals(TokenType.FINDEFICHERO)) {
                        finDeFichero = true;
                    }
                    // Analizador sintactico
                    List<Integer> reglasProcesadas = analizadorSintactico.procesarToken(token);

                    for (Integer regla : reglasProcesadas) {

                        listaReglas.add(regla);

                        // Analizador semantico
                        analizadorSemantico.procesarRegla(regla);
                    }
                }
                if (caracter == '\n') {
                    linea++;
                }
            } while (!finDeFichero);

            fichero.close();

            GestorSalida.escribirSalida(listaTokens, listaReglas, GestorTablas.getImpresionTabla().toString());

            System.out.println("Analisis completo");

        } catch (FileNotFoundException e) {
            System.err.println("Error: Archivo no encontrado: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error de entrada/salida: " + e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println("Error en el análisis " + e.getMessage() + " alrededor de la línea " + linea);
        } catch (Exception e) {
            System.err.println("Error en el análisis:" + e.getMessage());
        }

        GestorTablas.resetGestorTablas();
    }
}