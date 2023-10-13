Import java.io;

public class AnalizadorLexico {
    
    private int puntero;
    private int contador;
    private int valor;
    private char nextChar;

    private File fichero

    public AnalizadorLexico(File fichero) {
        FileReader fileReader = new FileReader("archivo.txt");
        BufferedReader bufferedReader = new BufferedReader(fileReader);
    }

    private char Leer() {
        puntero++;
        get next char
        return char
    }

    private generarToken()

    private char Leer(BufferedReader bufferedReader) {
        puntero++;

        int nextChar;
        if ((nextChar = bufferedReader.read()) != -1) {
            char character = (char) nextChar;
            
        }
        else {
            // Se ha alcanzado el EOF
        }
        

        catch (IOException e) {
            e.printStackTrace();
        }
    }

    

    public procesarCaracter()
}
