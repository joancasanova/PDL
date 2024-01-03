package main;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import lexico.AnalizadorLexico;
import sintactico.AnalizadorSintactico;
import estructuras.*;

public class Analizador {

    public static Stack<TablaSimbolos> tablas = new Stack<>();
                                                               
    public static void main(String[] args) {

        if (args.length < 1) {
            System.err.println("Por favor, especifique el nombre del archivo a analizar como argumento.");
            return;
        }

        String rutaArchivo = args[0];

        procesarFichero(rutaArchivo);
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

    private static void procesarFichero(String rutaArchivo) {

        int linea = 0;

        try (FileReader fichero = new FileReader(rutaArchivo)) {
            List<Token> listaTokens = new ArrayList<Token>();

            tablas.push(new TablaSimbolos(0));

            AnalizadorLexico analizadorLexico = new AnalizadorLexico();
            AnalizadorSintactico analizadorSintactico = new AnalizadorSintactico();

            Boolean finDeFichero = false;
            do {
                for (Token token : analizadorLexico.procesarCaracter((char) fichero.read())) {

                    // Anadir token a la lista de tokens
                    linea++;
                    listaTokens.add(token);
                    if (token.getTipo().equals(TokenType.FINDEFICHERO)) {
                        finDeFichero = true;
                    }

                    // Analizador Sintactico
                    analizadorSintactico.procesarToken(token);
                }
            } while (!finDeFichero);

            fichero.close();
            
            escribirReglasAplicadas(analizadorSintactico.getReglasAplicadas(), "output/reglasAplicadas.txt");
            writeListToFile(listaTokens, "output/archivoTokens.txt");
            writeStringToFile(tablas.peek().imprimirTabla(), "output/archivoTablaSimbolos.txt");

        } catch (FileNotFoundException e) {
            System.err.println("Archivo no encontrado: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error de entrada/salida: " + e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println("Error en el análisis " + e.getMessage() + " en línea " + linea);
        }
    }
}