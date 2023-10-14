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
		String lexema = null;
		int valor = 0;
	        
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
			
			// Empezar lexema
			lexema = String.valueOf(char_actual);//primer valor
			char_actual = (char) fichero.read();// leer el siguiente caracter
			//leer caracetres hassta del,s
			
			while((int) char_actual != -1 || is_del(char_actual) || is_s(char_actual) || is_op(char_actual) ){
				
					lexema.concat(String.valueOf(char_actual));//concateno el caracter nuevo
					char_actual = (char) fichero.read();	
				
			} 
			
			if(lexema.length() < 64) {
			// Tras acabar se crea el token
			nuevo_token = new Token("Nombredeltoken", lexema);
			} else {
				System.err.print("Maxima longitud de la cadena alcanzada");
			}
		/* PENDIENTE POR HACER PARA LA TABLA DE SIMBOLOS*/
			
			// Comprobar si el lexema pertenece a tabla palabra reservada
				//Si pertenece genra token palabra reservada y su pos
				//Si No pertenece 
					//buscar en tabla de simbolos
						//Si esta generar token tabla simbolos y su pos
						//Si NO esta, crear en TS y devolver token
			break;
			
		case 2:// Caracter d
			
			valor = (int) char_actual;//Guardo el primer digiton leido
			char_actual = (char) fichero.read();// leo el siguiente caracter
			
			//leer hasta que char_actual != d
			
			while((int) char_actual != -1 || is_d(char_actual)) {
				
				valor = 10*valor + (int) char_actual;
				char_actual = (char) fichero.read();
				
			}
			
			if(valor < 32767) {
			// Tras acabar se crea el token
						nuevo_token = new Token("cte_entera", String.valueOf(valor) );
			} else {
				System.err.println("Se ha superado el valor maximo de la representacion");
			}
			
			break ;
			
		case 3:
			
			char_actual = (char) fichero.read();// leo el siguiente caracter
			
			if(char_actual == '/') {//se han leido dos \\
				
				char_actual = (char) fichero.read();// leo el siguiente caracter
				
				while( (int) char_actual != -1 && !is_f(char_actual)) { // Cualquier cosa menos f
					char_actual = (char) fichero.read();//Continuo leyendo el comentairo hastas fin de linea o fichero
				}
				
				//Deja leido el ultimo caracter para la siguiente lectura, no hay que leer
			} else {
				System.err.println("Error lexico, solo se ha leido / en lugar de //(no triene token) ");
			}
			
			break;
			
		case 4:
			
			char_actual = (char) fichero.read();// leo el siguiente caracter
			lexema = String.valueOf(char_actual);//primer valor
			
			while(char_actual != '"') {// Continua leyendo la cadena hasta encontrar ", pasra acabar
				
				if((int) char_actual != -1	||  is_f(char_actual)){// Si se lee fin de linea o fichero antesde acabar la cadena da error
					System.err.println("Error la cadena nose ha cerrado con \"");
					break;
				}
				
				lexema.concat(String.valueOf(char_actual));//concateno el caracter nuevo
				char_actual = (char) fichero.read();
			}
			
			if(lexema.length() < 64) {
				// Tras acabar se crea el token
				nuevo_token = new Token("Nombredeltoken", lexema);
				} else {
					System.err.print("Maxima longitud de la cadena alcanzada");
				}
			
			break;
			
		case 5:
			
			if(char_actual == ',') valor = 1;
			if(char_actual == ';') valor = 2;
			if(char_actual == '(') valor = 3;
			if(char_actual == ')') valor = 4;
			if(char_actual == '{') valor = 5;
			if(char_actual == '}') valor = 6;
			
			nuevo_token = new Token("Simbolo",String.valueOf(valor));
			char_actual = (char) fichero.read();// leo el siguiente caracter para el siguiente token
			
			break;
			
		case 6:
			
			if(char_actual == '!') {
				nuevo_token = new Token("operador", "2");
				char_actual = (char) fichero.read();// leo el siguiente caracter para el siguiente token
			} else {
				
				if(char_actual == '+') {//Primer simbolo es +
					char_actual = (char) fichero.read();// leo el siguiente caracter para comprobar que op es
					
					if(char_actual == '=') {
						nuevo_token = new Token("asignacion", "1");// Token asignador +=
						char_actual = (char) fichero.read();// leo el siguiente caracter para el siguiente token
					} else {
						//El caracter es unicamente +
						nuevo_token = new Token("operador", "1");// Token operador +
					}
					
				} else {
					char_actual = (char) fichero.read();// leo el siguiente caracter para comprobar que op es
					
					if(char_actual == '=') {
						nuevo_token = new Token("operador", "3");// Token asignador +=
						char_actual = (char) fichero.read();// leo el siguiente caracter para el siguiente token
					} else {
						//El caracter es unicamente +
						nuevo_token = new Token("asignacion", "2");// Token operador +
					}
				}
			}
			
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

	private boolean is_op(char caracter) {
		if((int)caracter == -1) return false; // Si es -1 entonces acaba el fichero
		return caracter == '+' || caracter == '=' || caracter == '!';
	}
	
	private int analizarchar(char caracter) {
		if((int) caracter == -1) return -1; // Solo si es el caracter fin de fichero
		
		// Todas las ramas del automata cubiertas
		if(is_del(caracter)) return 0;
		if(is_l(caracter) || is_a(caracter)) return 1;
		if(is_d(caracter)) return 2;
		if(caracter == '/') return 3;
		if(caracter == '"') return 4;
		if(is_s(caracter)) return 5;
		if(is_op(caracter)) return 6;

		return -1;
	}
		

	
	/* ESTE METODO MAIN ES UNICAMENTE PARA PROBAR LA FUNCIONALIDAD DEL AN_LEXICO */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}