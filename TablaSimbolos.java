import java.util.HashMap;
import java.util.Map;

public class TablaSimbolos {

    // Numero de registros que tiene la tabla de simbolos
    private int filas;
    
    // Estructura para almacenar los símbolos
    private final Map<Integer, Simbolo> tabla;

    public TablaSimbolos() {
        tabla = new HashMap<>();
    }

    // Clase interna para representar un símbolo
    private static class Simbolo {
        String identificador;
        TokenType tipo;
        Object valor;

        Simbolo(String identificador, TokenType tipo, Object valor) {
            this.identificador = identificador;
            this.tipo = tipo;
            this.valor = valor;
        }
    }

    // Método para agregar un nuevo símbolo a la tabla
    public void agregarSimbolo(Integer posicion, String identificador, TokenType tipo, Object valor) {
        Simbolo nuevoSimbolo = new Simbolo(identificador, tipo, valor);
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
