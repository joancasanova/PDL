import java.io.*;

public class AnalizadorLexico {
	
	FileReader fichero;// puntero al fichero
	char char_actual;// Siguiente caracter a analizar
	
	//@Pre: --
	//@Post: fichero abierto 
	public AnalizadorLexico(String ruta) {
		
		try(FileReader filereader= new FileReader(ruta)){
			
			fichero = filereader;
			
			int aux = fichero.read();
			
			if(aux == -1) {
				// Problema con la lectrua del primer caracter
				System.out.printf("Error con la lectura del primer carácter\n");
				
			} else {
				char_actual = ( char ) aux;
			}
			
		}catch(IOException ex){
	    	
	        System.err.println("Erorr al leer el archivo");
	        ex.printStackTrace();

	    }
	}
	
	//@Pre: caracter incial
	//@Post: nuevo token del fichero &6 caracter inicial -> nuevo caracter
	public <E> Token Get_token() throws IOException {
	
		//Inicializar las variabbles a devolver o actualizar
		Token nuevo_token = null;
		String tipo;
		E valor;
	        
		//Analizar el caraceter ya leido
		
		int aux = analizarchar(char_actual);
	
			// Elegir rama del automata
	
		switch(aux) {
		case -1:
			System.out.println("Fin de fichero\n");
			break;
			
		case 0://leer delimitador
			char_actual = (char) fichero.read();// Seguir leyendo
			break;
			
		case 1:// Caracter es l o a
			String lexema;
			break;
			
		case 2:
			break ;
			
		case 3:
			break;
			
		case 4:
			break;
			
		case 5:
			break;
			
			default:
			System.out.println("Caracter no reconocido\n");
		}
		// Tras acaabar con el automata se crea el token
		
		// Devolcer el token
		return nuevo_token;
	}
	
	//Métodos privados apra comprobar siu un caracter perteneceo o no a diferentes conjutnos de simbolos
	private boolean is_del(char caracter) {
		if((int)caracter == -1) return false; // Si es -1 entonces acaba el fichero
		return Character.isWhitespace(caracter) || caracter == '\t';
	}
	
	private boolean is_l(char caracter) {
		if((int)caracter == -1) return false; // Si es -1 entonces acaba el fichero
		return Character.isLetter(caracter) && Character.isLowerCase(caracter);
	}
	
	private boolean is_a(char caracter) {
		if((int)caracter == -1) return false; // Si es -1 entonces acaba el fichero
		return Character.isLetter(caracter) && Character.isUpperCase(caracter);
	}
	
	private boolean is_d(char caracter) {
		
		if((int)caracter == -1) return false; // Si es -1 entonces acaba el fichero
		return Character.isDigit(caracter);
	}
	
	private boolean is_s(char caracter) {
		if((int)caracter == -1) return false; // Si es -1 entonces acaba el fichero
		return caracter == ',' || caracter == ';' || caracter == '(' || caracter == ')' || caracter == '{' || caracter == '}';   
	}
	
	private boolean is_f(char caracter) {
		if((int)caracter == -1) return false; // Si es -1 entonces acaba el fichero
		return caracter == '\n'; // Fin de linea
	}
	
	private int analizarchar(char caracter) {
		if((int) caracter == -1) return -1; // Solo si es el caracter fin de fichero
		
		if(is_del(caracter)) return 0;
		if(is_l(caracter) || is_a(caracter)) return 1;
		if(is_d(caracter)) return 2;
		if(is_s(caracter)) return 3;
		if(is_f(caracter)) return 4;
		
		return 5;//Caso para simbolos de los operadores aritmeicos y logicos
	}
	
		

	
	/* ESTE METODO MAIN ES UNICAMENTE PARA PROBAR LA FUNCIONALIDAD DEL AN_LEXICO */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}