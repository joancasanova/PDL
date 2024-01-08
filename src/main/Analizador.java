package main;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import lexico.AnalizadorLexico;
import semantico.AnalizadorSemantico;
import sintactico.AnalizadorSintactico;
import tablaSimbolos.*;
import token.*;

public class Analizador {
    public static GestorTablas gestorTablas = new GestorTablas();

    public static void main(String[] args) {

        if (args.length < 1) {
            System.err.println("Por favor, especifique el nombre del archivo a analizar como argumento.");
            return;
        }

        String rutaArchivo = args[0];
        
        
        Boolean sintactico = false;
        Boolean semantico = false;

        if (args.length > 1) {
            if (args[1].equals("-sintactico")) {
                sintactico = true;
            }
            else {
                System.out.println("Está ejecutando el analizado léxico solamente.");
                System.out.println("Si desea ejecutar el analizador sintáctico también utilice el comando -sintactico.");
            }
            if (args.length > 2) {
                if (args[2].equals("-semantico")) {
                    System.out.println("Por favor, especifique el nombre del archivo a analizar como argumento.");
                    semantico = true;
                }
            }
            else {
                System.out.println("No está ejecutando el analizador semantico.");
                System.out.println("Si desea ejecutar el analizador semántico utilice el comando -semantico.");
            }
        }

        procesarFichero(rutaArchivo, sintactico, semantico);
    }

    private static void procesarFichero(String rutaArchivo, Boolean sintactico, Boolean semantico) {

        int linea = 1;

        try (FileReader fichero = new FileReader(rutaArchivo)) {
            List<Token> listaTokens = new ArrayList<Token>();
            List<Integer> listaReglas = new ArrayList<Integer>();

            AnalizadorLexico analizadorLexico = new AnalizadorLexico();
            AnalizadorSintactico analizadorSintactico = new AnalizadorSintactico();
            AnalizadorSemantico analizadorSemantico = new AnalizadorSemantico();

            Boolean finDeFichero = false;

            do {
                char caracter = (char) fichero.read();
                if (caracter == '\n') {
                    linea++;
                }

                for (Token token : analizadorLexico.procesarCaracter(caracter)) {

                    // Anadir token a la lista de tokens
                    listaTokens.add(token);
                    if (token.getTipo().equals(TokenType.FINDEFICHERO)) {
                        finDeFichero = true;
                    }

                    // Analizador sintactico
                    if (sintactico) {
                        for (Integer regla : analizadorSintactico.procesarToken(token)) {

                            listaReglas.add(regla);

                            // Analizador semantico
                            if (semantico) {
                                analizadorSemantico.procesarRegla(regla);
                            }
                        }
                    }

                    if (token.getTipo().equals(TokenType.PUNTOCOMA)) {
                        gestorTablas.obtenerTablaActual().setZonaAsignacion(false);
                    }
                }
            } while (!finDeFichero);

            fichero.close();

            escribirReglasAplicadas(listaReglas, "./output/reglasAplicadas.txt");
            writeListToFile(listaTokens, "./output/archivoTokens.txt");
            writeStringToFile(gestorTablas.getImpresionTabla().toString(), "./output/archivoTablaSimbolos.txt");

        } catch (FileNotFoundException e) {
            System.err.println("Archivo no encontrado: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error de entrada/salida: " + e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println("Error en el análisis " + e.getMessage() + " en línea " + linea);
        }
    }

    public static void writeListToFile(List<Token> listaTokens, String fileName) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

        for (Token item : listaTokens) {
            writer.write(item.toString());
            writer.newLine(); // Añade un salto de línea después de cada elemento
        }

        writer.close();
    }

    public static void escribirReglasAplicadas(List lista, String fileName) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

        writer.write("A\n");
        for (Object item : lista) {
            writer.write(item.toString());
            writer.newLine(); // Añade un salto de línea después de cada elemento
        }

        writer.close();
    }

    public static void writeStringToFile(String content, String fileName) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write(content);
        writer.close();
    }
}