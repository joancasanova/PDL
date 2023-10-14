
import java.util.*;
 
public class Gestor_TS {

	static LinkedList<?> lista_TS;// Una lista paras almacenar las dos tablas distintas
	/* EL PUNTERO NOS INDICARA LA TABLA A UTILIZAR PRIMERO -> PASAR EL PUNTERO A EL AN_LEXICO
	* EL ANALIZADOR LEXICO USARÁ LA TABLA QUER RECIBA COMO PRIMERA(TABLA ACTIVA 
	* SOLO EN EL CASO DE QUE FALLE PUEDE VISITA LA OTRA(LINKEDLIST PERMITE VER NODOS CERCANOS */
	
	
	/* EL ANALIZADOR SEMANTICO PUEDE MVOER EL PUNTERO DE QUE TABLA SE USA PARA CMABIAR EL AMBITO DE USO
	* NO NOS PREOCUPAMOS EN EL AN_LEXICO Y EN EL SEMANTICO SOLO ES EJEUCTAR EL CAMBIO DE AMBITO LO QUE
	* NOS FACILITA GESTIONAR LAS TABLAS*/
	
	static int index; // Nos indica la posicion de la lista activa
	
	/*DECLARAR LAS TABLAS CORRESPONDIENTES U OPTRAS ESTRUCTURAS*/
	
	public Gestor_TS() {
	//CREAR UNA UNIA TABLA DE SIMBOLOS Y AÑADIR A LA LISTA
	//ACTUALIZAR EL PUNTERO INDEX
	//CREAR OTRA LISTA CORRESPONDIENTE A LA TABLAD E PAÑLABRAS RESERVADAS
	}
	
	// NOMBRES PROVISIONALES
	
	//@Pre: Existe tabla(debe haber dos tablas)
	//@Post: solo queda una tabla y cambiar puntero
	public static void eliminartablaactiva(){// Elimina la tabla activa y cambia el puntero a la tabla global
	}
	
	//@Pre: Existe tabla (solo una)
	//@Post: debe jhaber dos tablas y cambiar puntero
	public static void creartablaactiva(){//crea la tabla activa y acturlaiza el puntero
	}
	
	
	
	//@Pre: Existe tabla && cadena
	//@Post: si cadena existe devuelve pos sino -1
	public static int buscarenTS(String cadena){//llaman al metodo buscar para que el gestor les de su posicion
		return 0;
	}
	
	//@Pre: Existe tabla && cadena
	//@Post: si cadena existe devuelve pos sino -1
	public static int buscarpalabrareserv(String cadena){//Buscar en la tablad e pañlabras reservadas,devuelve la posicion
	return 0;
	}
	
	
	
	//@Pre: Existe tabla && cadena && puntero(index)
	//@Post: lista_TS(index) -> Añade nuevo elemento(Se añade siemrtpe en la activa (la apunta el idnex))
			//devuelve la posicion en la que se haañadido
	public static int aniadirelementoTS(String cadena) {
		return 0;
	}
	
	// Metodo para probar que funciona la clase
	public static void main(String[] args) {

	}

}
