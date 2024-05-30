package main;

import java.io.FileReader;
import java.io.IOException;

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

    public static void main(String[] args) {
        if (args.length < 1) {
            GestorErrores.lanzarError(GestorErrores.TipoError.GENERICO, GestorErrores.ESPECIFICAR_INPUT);
        }

        String rutaArchivo = args[0];
        try {
            FileReader fichero = new FileReader(rutaArchivo);
            GestorAnalisis gestorAnalisis = GestorAnalisis.getInstance();
            gestorAnalisis.procesarFichero(fichero);
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
