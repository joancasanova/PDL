package main;

import java.io.FileReader;
import java.io.IOException;

import util.GestorErrores;

public class Analizador {

    public static void main(String[] args) {
        if (args.length < 1) {
            GestorErrores.lanzarError(GestorErrores.TipoError.GENERICO, GestorErrores.ESPECIFICAR_INPUT);
        }

        String rutaArchivo = args[0];
        GestorEntrada gestorArchivo = GestorEntrada.getInstance();
        try {
            FileReader fichero = gestorArchivo.abrirArchivo(rutaArchivo);
            GestorAnalisis gestorAnalisis = GestorAnalisis.getInstance();
            gestorAnalisis.procesarFichero(fichero);
            System.out.println(
                    "Análisis completo. Se han generado los archivos de tokens, reglas, y tabla de símbolos.");
        } catch (IOException e) {
            GestorErrores.lanzarError(GestorErrores.TipoError.GENERICO, "Error de entrada/salida: " + e.getMessage());
        } catch (Exception e) {
            GestorErrores.lanzarError(GestorErrores.TipoError.GENERICO, "Error inesperado: " + e.getMessage());
        } finally {
            gestorArchivo.cerrarArchivo();
        }
    }
}
