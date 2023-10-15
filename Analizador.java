import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Analizador {

    static Stack<TablaSimbolos> tablas = new Stack<>();

    public static void main(String[] args) {

        if (args.length < 1) {
            System.err.println("Por favor, especifique el nombre del archivo a analizar como argumento.");
            return;
        }

        String rutaArchivo = args[0];

        try (FileReader fichero = new FileReader(rutaArchivo)) {
            tablas.push(new TablaSimbolos());
            AnalizadorLexico analizadorLexico = new AnalizadorLexico(fichero);
            Token token;
            do {
                token = analizadorLexico.obtenerToken();
            } while (token != null);

            tablas.peek().imprimirTabla();

        } catch (FileNotFoundException e) {
            System.err.println("Archivo no encontrado: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error de entrada/salida: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Error en el análisis léxico: " + e.getMessage());
        }
    }
}
