import java.io.*;
import java.util.*;

// Enumeración para representar los tipos de tokens
enum TokenType {
    PalabraReservada,   // Para palabras reservadas (e.g., boolean, function)
    CteEntera,          // Para constantes enteras (e.g., 123)
    Cadena,             // Para cadenas de caracteres (e.g., "¡Hola, mundo!")
    Identificador,      // Para identificadores (e.g., nombreVariable)
    AsignacionSuma,     // Para la asignación con suma (e.g., +=)
    Comparador,         // Para comparadores (e.g., ==)
    Asignacion,         // Para la asignación (e.g., =)
    Suma,               // Para operadores (e.g., +)
    Negacion,           // Para operadores (e.g., !)
    Simbolo             // Para símbolos (e.g., ,, ;, (, ), {, })
}

public class AnalizadorLexico {

    private FileReader fichero;
    private Character charActual;
    Character charSiguiente = null;
    private boolean leerSiguiente = true;
    private int lineaActual = 1;

    // Máximo número de caracteres en una cadena
    private static final int MAX_CARACTERES_CADENA = 64;

    // Máximo valor para una constante entera
    private static final int MAX_VALOR_ENTERO = 32767;

    // Lista de palabras reservadas en el lenguaje
    private static final List<String> palabrasReservadas = Arrays.asList("boolean", "function", "if", "int", "let", "put", "return", "string", "void", "while", "get");

    /**
     * Constructor de la clase AnalizadorLexico.
     *
     * @param ruta Ruta al archivo fuente a analizar.
     * @throws FileNotFoundException Si el archivo no se encuentra.
     */
    public AnalizadorLexico(FileReader fichero) throws FileNotFoundException {
        this.fichero = fichero;
        charActual = leerSiguienteCaracter();
    }

    /**
     * Lee el siguiente carácter del archivo fuente.
     *
     * @return El siguiente carácter leído o null si se llegó al final del archivo.
     * @throws IOException Si ocurre un error al leer el archivo.
     */
    private Character leerSiguienteCaracter() {
        try {
            int aux = fichero.read();
            if ((char) aux == '\n') { // Si se encuentra con salto de linea, se aumenta la lineaActual en 1
                lineaActual++;
            }
            if (aux == -1) { // Fin del fichero
                return (char) aux;
            }
            return (char) aux;
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
            return null;
        }
    }

    /**
     * Obtiene el siguiente token del archivo fuente.
     *
     * @return El token obtenido.
     * @throws IOException Si ocurre un error al leer el archivo.
     */
    public Token obtenerToken() throws IOException, EOFException {
        Token token = null; 
    
        
        if ((int) charActual == -1) {
            // Detener el analizador léxico ya que se encontró el EOF.
            return null;
        }

        // Ignora los delimitadores como espacios en blanco, tabuladores y saltos de línea.
        while (Character.isWhitespace(charActual) || charActual == '\t' || charActual == '\n') {
            charActual = leerSiguienteCaracter();
        }

        // Analizar el carácter actual y determinar el tipo de token
        switch (charActual) {
            case '+':
                charSiguiente = leerSiguienteCaracter();
                if (charSiguiente == '=') {
                    token = new Token(TokenType.AsignacionSuma, ""); // Token de AsignacionSuma (+=)
                } else {
                    token = new Token(TokenType.Suma, ""); // Token de Suma (+)
                    leerSiguiente = false;
                }
                break;

            case '=':
                charSiguiente = leerSiguienteCaracter();
                if (charSiguiente == '=') {
                    token = new Token(TokenType.Comparador, ""); // Token de Comparador (==)
                } else {
                    token = new Token(TokenType.Asignacion, ""); // Token de Asignacion (=)
                    leerSiguiente = false;
                }
                break;

            case '!':
                token = new Token(TokenType.Negacion, "");// Token de Negacion (!)
                break;
        
            case ',':
            case ';':
            case '(':
            case ')':
            case '{':
            case '}':
                int valorAscii = (int) charActual; // Se obtieve el valor ASCII del simbolo para el atributo del token
                token = new Token(TokenType.Simbolo, valorAscii); // Token de Simbolo
                break;

            case '\"':
                token = leerCadena(); // Cadenas de caracteres
                break;
    
            default:
                if (Character.isLetter(charActual) || charActual == '_') {
                    token = leerLexema(); // Identificadores y palabras reservadas
                } 
                else if (Character.isDigit(charActual)) {
                    token = leerConstanteEntera(); // Constantes enteras
                } 
                else {
                    System.err.print("Carácter no reconocido: " +
                    "\n Linea: " + lineaActual +
                    "\n Caracter: " + charActual);
                }
        }

        // Actualizamos el siguiente caracter
        if (leerSiguiente) {
            charActual = leerSiguienteCaracter();
        }
        else {
            charActual = charSiguiente;
        }

        leerSiguiente = true;

        return token;
    }
    

  /**
     * Lee una cadena encerrada entre comillas dobles.
     *
     * @return El token representando la cadena.
     * @throws IOException Si ocurre un error al leer el archivo.
     */
    private Token leerCadena() throws IOException {
        Token token = null;
        StringBuilder lexema = new StringBuilder();
    
        lexema.append(charActual);// Concatenar primeras "
        while (true) {
            charActual = leerSiguienteCaracter();
    
            if (charActual == '\"') { // Se detecta comilla de cierre, generamos token de cadena de caracteres
                lexema.append(charActual);// Concatenar ultima "
                token = new Token(TokenType.Cadena, lexema.toString());
                break;
            } 
            else {
                lexema.append(charActual);
            }
        }
            
        if (lexema.length()-2 >= MAX_CARACTERES_CADENA) {
            throw new IllegalArgumentException("Error: Cadena demasiado larga." +
            "\n Linea: " + lineaActual +
            "\n Cadena: " + lexema.toString());
        }
    
        return token;
    }
    
    

    /**
     * Lee un identificador o una palabra reservada.
     *
     * @return El token representando el identificador o la palabra reservada.
     * @throws IOException Si ocurre un error al leer el archivo.
     */
    private Token leerLexema() throws IOException {
        Token token = null;
        StringBuilder lexema = new StringBuilder();
        leerSiguiente = false;

        lexema.append(charActual); // Se añade al lexema el primer caracter

        while (true) {
            charActual = leerSiguienteCaracter();
            if (!(Character.isLetterOrDigit(charActual) || charActual == '_')) {
                break;
            }
            lexema.append(charActual);
        }
        
        charSiguiente = charActual;

        String nombre = lexema.toString();
        
        // Se comprueba si es una palabra reservada o un identificador
        if (palabrasReservadas.contains(nombre)) {
            token = new Token(TokenType.PalabraReservada, nombre);
        }
        else {
            TablaSimbolos tablaActual = Analizador.tablas.peek();

            if (tablaActual.simboloExiste(nombre)) {
                token = new Token(TokenType.Identificador, tablaActual.obtenerPosicionSimbolo(nombre));
            }
            else {
                int nuevaPosicion = tablaActual.numeroEntradas();
                tablaActual.agregarSimbolo(nuevaPosicion, nombre, null, null, null);
                token = new Token(TokenType.Identificador, nuevaPosicion);
            }
        }

        return token;
    }

    /**
     * Lee una constante entera.
     *
     * @return El token representando la constante entera.
     * @throws IOException Si ocurre un error al leer el archivo.
     */
    private Token leerConstanteEntera() throws IOException {
        Token token = null;
		int valorEntero = 0;
        leerSiguiente = false;
    
        valorEntero = 10 * valorEntero + (charActual - '0');

        while (true) {
            charActual = leerSiguienteCaracter();
            if (!Character.isDigit(charActual)) {
                break;
            }
            valorEntero = 10 * valorEntero + (charActual - '0');
        }

        charSiguiente = charActual;

        if (valorEntero < MAX_VALOR_ENTERO) {
            token = new Token(TokenType.CteEntera, valorEntero); 
        }
        else {
            throw new IllegalArgumentException("Se ha superado el valor máximo de la representación. " +
            "\n Linea: " + lineaActual +
            "\n Valor: " + valorEntero);
        }

        return token;
    }
}
