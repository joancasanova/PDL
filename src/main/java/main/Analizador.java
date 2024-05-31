package main;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

import util.GestorErrores;

/**
 * Clase principal del analizador que se encarga de procesar el archivo de
 * entrada.
 * 
 * @autor Juan Francisco Casanova Ferrer
 * @institution Universidad Politécnica de Madrid
 * @contact juancasanovaferrer@gmail.com
 */
public class Analizador {

    private final static String DIRECTORIO_ENTRADA = "input";
    private final static String ARCHIVO_INPUT = Paths.get(DIRECTORIO_ENTRADA, "input.txt").toString();

    public static void main(String[] args) {
        String rutaArchivo;
        if (args.length > 0) {
            rutaArchivo = args[0];
        } else {
            rutaArchivo = ARCHIVO_INPUT;
        }

        try {
            FileReader fichero = new FileReader(rutaArchivo);
            GestorAnalisis.getInstance().procesarFichero(fichero);
            System.out.println(
                    "Análisis completo. Se han generado los archivos de tokens, reglas, y tabla de símbolos.");
            fichero.close();
        } catch (IllegalStateException e) {
            System.err.println(e.getLocalizedMessage());
        } catch (IOException e) {
            GestorErrores.lanzarError(GestorErrores.TipoError.GENERICO, "Error de entrada/salida: " + e.getMessage());
        } catch (Exception e) {
            GestorErrores.lanzarError(GestorErrores.TipoError.GENERICO, "Error inesperado: " + e.getMessage());
        }
    }
}
