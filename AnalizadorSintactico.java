import java.util.*;
import javafx.util;

// Enumeración para representar las acciones posibles
enum Accion {
    DESPLAZAR, REDUCIR, ACEPTAR, ERROR
}

// Clase para representar una entrada en la tabla de análisis
class EntradaTabla {
    Accion accion;
    int numero; // Número de regla o estado, dependiendo de la acción

    EntradaTabla(Accion accion, int numero) {
        this.accion = accion;
        this.numero = numero;
    }
}

// Clase para el analizador sintáctico
public class AnalizadorSintactico {

    private Stack<Integer> pila; // Pila de estados y terminales
    
    private Map<Pair<Integer, Character>, EntradaTabla> tablaAnalisis; // Tabla de análisis LR
    private List<String> tokens; // Lista de tokens de entrada -> No hace falta, el llama al analizador lexico para cada token que ingresa a la pila

    public AnalizadorSintactico() {
        pila = new Stack<>();
        inicializarTablaAnalisis();
    }

    private void inicializarTablaAnalisis() {
        
        //delimitador ":" -> Para diferenciar los parámetros del fichero
        //      _____&:____  ______...
        //   1: |  D : 6:  |-|      ...
        //   2: |  X : 1:  |-|      ...

        // Formato fichero -> Estado:Simbolo:Accion:Digito
        // ejemplo:
        // 0:+:Desplazar:7
        // 0:+:Reducir:3
        // ...

        // leer ficherox2(leer estado y simbolo)
        // asignar valores al pair(clave del hashmap)
        // leer ficherox2 accion y digito

        // Crear par clave(pair<estado,simbolo>) y valor(pair<accion,digito>)(Pongo pair pero es cualquier mierda similar)
        //repetir hata find e fichero

        //tabla creada--yupii
    }

    public void analizar() {
        pila.push(0); // Estado inicial
        int indiceToken = 0;

        while (indiceToken < tokens.size()) {
            String tokenActual = tokens.get(indiceToken);
            int estadoActual = pila.peek();

            EntradaTabla entrada = tablaAnalisis.getOrDefault(estadoActual, new HashMap<>()).get(tokenActual);

            if (entrada == null) {
                System.out.println("Error sintáctico");
                return;
            }

            switch (entrada.accion) {
                case DESPLAZAR:
                    pila.push(Integer.parseInt(tokenActual));
                    pila.push(entrada.numero);
                    indiceToken++;
                    break;
                case REDUCIR:
                    // Implementar lógica de reducción según la regla especificada en entrada.numero
                    break;
                case ACEPTAR:
                    System.out.println("Análisis sintáctico completado exitosamente");
                    return;
                case ERROR:
                default:
                    System.out.println("Error sintáctico");
                    return;
            }
        }
    }
}
