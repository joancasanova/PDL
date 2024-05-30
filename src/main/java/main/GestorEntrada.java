package main;

import java.io.FileReader;
import java.io.IOException;

import util.GestorErrores;

public class GestorEntrada {

    private FileReader fichero;
    private static GestorEntrada instancia;

    /**
     * Constructor privado para evitar la creación de instancias.
     */
    private GestorEntrada() {
    }

    /**
     * Devuelve la instancia única de la clase.
     * Si la instancia no ha sido creada aún, la crea.
     *
     * @return La instancia única de GestorArchivo.
     */
    public static synchronized GestorEntrada getInstance() {
        if (instancia == null) {
            instancia = new GestorEntrada();
        }
        return instancia;
    }

    /**
     * Abre un archivo y devuelve un FileReader.
     *
     * @param rutaArchivo La ruta del archivo a abrir.
     * @return Un FileReader para el archivo abierto.
     * @throws IOException Si ocurre un error al abrir el archivo.
     */
    public FileReader abrirArchivo(String rutaArchivo) throws IOException {
        fichero = new FileReader(rutaArchivo);
        return fichero;
    }

    /**
     * Cierra el archivo si está abierto.
     */
    public void cerrarArchivo() {
        if (fichero != null) {
            try {
                fichero.close();
            } catch (IOException e) {
                GestorErrores.lanzarError(GestorErrores.TipoError.GENERICO,
                        "Error al cerrar el archivo: " + e.getMessage());
            }
        }
    }
}
