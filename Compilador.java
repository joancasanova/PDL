
public class Compilador {
	
	AnalizadorLexico An_lex;

	public Compilador(String ruta) {
		
		// Crea los obnjetos de las fases de compilacion
		An_lex = new AnalizadorLexico(ruta);
	}
	
	/* Este método main implementará el compilador */
	public static void main(String[] args) {
		
		// CLASE PRINCIPAL, RECIBE EL FICHERO Y PONE EN FUNCIONAMIENTO TODOS LOS ANALIZADORES
		// CREA E INCIALIZA CAD AUNO DE ELLOS
		
		String ruta = args[1];//La ruta del fichero o nombre
		Compilador comp = new Compilador(ruta);
	}

}
