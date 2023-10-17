import java.util.HashMap;
import java.util.Map;

public class TablaSimbolos {
    
    // Estructura para almacenar los símbolos
    private final Map<Integer, Simbolo> tabla;

    // Numero identificador para la tabla de simbolos
    private Integer numeroTabla;

    public TablaSimbolos(Integer numeroTabla) {
        tabla = new HashMap<>();
        this.numeroTabla = numeroTabla;
    }
    
    // Clase interna para representar un símbolo
    private static class Simbolo {
        StringBuilder nombre;
        String tipo;
        Integer desplazamiento;
        Object valor;

        Simbolo(StringBuilder nombre, String tipo, Object valor, Integer desplazamiento) {
            this.nombre = nombre;
            this.tipo = tipo;
            this.desplazamiento = desplazamiento;
            this.valor = valor;
        }
    }

    // Método para agregar un nuevo símbolo a la tabla
    public void agregarSimbolo(Integer posicion, StringBuilder lexema, String tipo, Object valor, Integer desplazamiento) {
        Simbolo nuevoSimbolo = new Simbolo(lexema, tipo, valor, desplazamiento);
        tabla.put(posicion, nuevoSimbolo);
    }

    // Método para obtener un símbolo existente de la tabla
    public Simbolo obtenerSimbolo(Integer posicion) {
        return tabla.get(posicion);
    }

    // Método para eliminar un símbolo existente de la tabla
    public void eliminarSimbolo(Integer posicion) {
        tabla.remove(posicion);
    }

    // Método para verificar si un símbolo ya existe en la tabla
    public boolean simboloExiste(Integer posicion) {
        return tabla.containsKey(posicion);
    }

    // Método para obtener un símbolo existente de la tabla
    public Integer numeroEntradas() {
        return tabla.size();
    }    

    public void imprimirTabla() {
        System.out.println("CONTENIDOS DE LA TABLA #: " + numeroTabla);
    
        for (Map.Entry<Integer, Simbolo> entrada : tabla.entrySet()) {

            // Obtenemos parametros de la entrada
            Simbolo simbolo = entrada.getValue();
            String nombre = simbolo.nombre.toString();
            String tipo = simbolo.tipo;
            Object valor = simbolo.valor;
            Integer desplazamiento = simbolo.desplazamiento;


            // Imprimimos la entrada
            System.out.print("*\t");
            System.out.print("LEXEMA\t:\t");
            System.out.print("'" + nombre + "' ");

            System.out.println("ATRIBUTOS\t:\t");

            System.out.print("+\t");
            System.out.print("tipo:\t");
            System.out.print("'" + tipo + "' ");

            System.out.println("+\t");
            System.out.print("despl:\t");
            System.out.print(desplazamiento);
        }
    }
    
}
