import java.util.*;

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
}
