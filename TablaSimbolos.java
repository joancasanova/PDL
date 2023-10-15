import java.util.HashMap;
import java.util.Map;

public class TablaSimbolos {
    
    // Estructura para almacenar los símbolos
    private final Map<Integer, Simbolo> tabla;

    public TablaSimbolos() {
        tabla = new HashMap<>();
    }

    // Clase interna para representar un símbolo
    private static class Simbolo {
        StringBuilder identificador;
        String tipo;
        Object valor;

        Simbolo(StringBuilder lexema, String string, Object valor) {
            this.identificador = lexema;
            this.tipo = string;
            this.valor = valor;
        }
    }

    // Método para agregar un nuevo símbolo a la tabla
    public void agregarSimbolo(Integer posicion, StringBuilder lexema, String string, Object valor) {
        Simbolo nuevoSimbolo = new Simbolo(lexema, string, valor);
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
        System.out.println("Tabla de Símbolos:");
        System.out.println("Posición\tIdentificador\tTipo\t\tValor");
    
        for (Map.Entry<Integer, Simbolo> entrada : tabla.entrySet()) {
            Integer posicion = entrada.getKey();
            Simbolo simbolo = entrada.getValue();
            System.out.println(posicion + "\t\t" + simbolo.identificador + "\t\t" + simbolo.tipo + "\t\t" + simbolo.valor);
        }
    }
    
}
