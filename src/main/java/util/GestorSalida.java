package util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import token.Token;

public class GestorSalida {
    private final static String DIRECTORIO_SALIDA = "output";
    private final static String ARCHIVO_REGLAS = Paths.get(DIRECTORIO_SALIDA, "reglasAplicadas.txt").toString();
    private final static String ARCHIVO_TS = Paths.get(DIRECTORIO_SALIDA, "archivoTablaSimbolos.txt").toString();
    private final static String ARCHIVO_TOKENS = Paths.get(DIRECTORIO_SALIDA, "archivoTokens.txt").toString();

    private GestorSalida() {
    }

    public static void escribirSalida(List<Token> listaTokens, List<Integer> listaReglas, String contenidoTablaSimbolos)
            throws IOException {
        crearDirectorioSalida();
        escribirTokens(listaTokens);
        escribirReglasAplicadas(listaReglas);
        escribirTablaSimbolos(contenidoTablaSimbolos);

    }

    private static void crearDirectorioSalida() throws IOException {
        Path pathDirectorio = Paths.get(DIRECTORIO_SALIDA);
        if (Files.notExists(pathDirectorio)) {
            Files.createDirectory(pathDirectorio);
        }

        for (String archivo : List.of(ARCHIVO_REGLAS, ARCHIVO_TOKENS, ARCHIVO_TS)) {
            Path pathArchivo = Paths.get(archivo);
            if (Files.exists(pathArchivo)) {
                Files.delete(pathArchivo);
            }
            Files.createFile(pathArchivo);
        }
    }

    private static void escribirTokens(List<Token> listaTokens) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_TOKENS))) {
            for (Token item : listaTokens) {
                writer.write(item.toString());
                writer.newLine();
            }
        }
    }

    private static void escribirReglasAplicadas(List<Integer> listaReglas) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_REGLAS))) {
            writer.write("A\n");
            for (Integer item : listaReglas) {
                writer.write(item.toString());
                writer.newLine();
            }
        }
    }

    private static void escribirTablaSimbolos(String contenido) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_TS))) {
            writer.write(contenido);
        }
    }
}
