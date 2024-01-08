package main;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import lexico.AnalizadorLexico;
import semantico.AnalizadorSemantico;
import sintactico.AnalizadorSintactico;
import tablaSimbolos.*;
import token.*;

public class Analizador {
    public static GestorTablas gestorTablas = new GestorTablas();

    public static void main(String[] args) {

        if (args.length < 2) {
            System.err.println("Por favor, especifique la ruta del archivo a analizar como argumento.");
            System.err.println("Por favor, especifique la ruta del directorio de salida (por ejemplo, output).");
            return;
        }

        String rutaArchivo = args[0];
        String rutaSalida = args[1];

        Boolean sintactico = false;
        Boolean semantico = false;

        if (args.length > 2) {
            if (args[2].equals("-sintactico")) {
                sintactico = true;
            } else {
                System.out.println("Está ejecutando el analizado léxico solamente.");
                System.out.println("Si desea ejecutar el analizador sintáctico también utilice el comando -sintactico.");
            }
            if (args.length > 3) {
                if (args[3].equals("-semantico")) {
                    semantico = true;
                }
            } else {
                System.out.println("No está ejecutando el analizador semantico.");
                System.out.println("Si desea ejecutar el analizador semántico utilice el comando -semantico.");
            }
        }

        procesarFichero(rutaArchivo, rutaSalida, sintactico, semantico);
    }

    private static void procesarFichero(String rutaArchivo, String rutaSalida, Boolean sintactico, Boolean semantico) {

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
            
            crearDirectorio(rutaSalida);
            String archivoReglas = rutaSalida + "/reglasAplicadas.txt";
            crearArchivo(rutaSalida, archivoReglas);
            String archivoTokens = rutaSalida + "/archivoTokens.txt";
            crearArchivo(rutaSalida, archivoTokens);
            String archivoTS = rutaSalida + "/archivoTablaSimbolos.txt";
            crearArchivo(rutaSalida, archivoTS);

            escribirReglasAplicadas(listaReglas, archivoReglas);
            writeListToFile(listaTokens, archivoTokens);
            writeStringToFile(gestorTablas.getImpresionTabla().toString(), archivoTS);

        } catch (FileNotFoundException e) {
            System.err.println("Archivo no encontrado: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error de entrada/salida: " + e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println("Error en el análisis " + e.getMessage() + " en línea " + linea);
        }
    }

    private static void crearDirectorio(String directorio) {
        Path path = Paths.get(directorio);

        // Intenta crear el directorio si no existe
        try {
            // Usa Files.exists(path) para verificar si el directorio ya existe
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            System.err.println("Error al crear el directorio de salida");
        }

    }

    private static void crearArchivo(String directorio, String archivo) {
        Path directorioPath = Paths.get(directorio);
        Path archivoPath = directorioPath.resolve(archivo);

        try {
            // Crea el directorio si no existe
            if (!Files.exists(directorioPath)) {
                Files.createDirectories(directorioPath);
            }

            // Crea el archivo si no existe
            if (!Files.exists(archivoPath)) {
                Files.createFile(archivoPath);
            }
        } catch (IOException e) {
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