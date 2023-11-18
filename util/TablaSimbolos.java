package util;
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
        String nombre;
        String tipo;
        Integer desplazamiento;
        Object valor;

        Simbolo(String nombre, String tipo, Object valor, Integer desplazamiento) {
            this.nombre = nombre;
            this.tipo = tipo;
            this.desplazamiento = desplazamiento;
            this.valor = valor;
        }
    }

    // Método para agregar un nuevo símbolo a la tabla
    public void agregarSimbolo(Integer posicion, String lexema, String tipo, Object valor, Integer desplazamiento) {
        Simbolo nuevoSimbolo = new Simbolo(lexema, tipo, valor, desplazamiento);
        tabla.put(posicion, nuevoSimbolo);
    }

    // Método para obtener un símbolo existente de la tabla
    public Simbolo obtenerSimbolo(Integer posicion) {
        return tabla.get(posicion);
    }

    // Método para obtener posicion un símbolo existente de la tabla
    public Integer obtenerPosicionSimbolo(String nombre) {
        Integer posicion = null;
        for (Map.Entry<Integer, Simbolo> entry : tabla.entrySet()) {
            if (entry.getValue().nombre.equals(nombre)) {
                posicion = entry.getKey();
            }
        }
        return posicion;
    }

    // Método para eliminar un símbolo existente de la tabla
    public void eliminarSimbolo(Integer posicion) {
        tabla.remove(posicion);
    }

    // Método para verificar si un símbolo ya existe en la tabla
    public boolean simboloExiste(String nombre) {

        for (Simbolo simbolo : tabla.values()) {
            if (simbolo.nombre.equals(nombre)) {
                return true;
            }
        }
        return false;
    }

    // Método para obtener un símbolo existente de la tabla
    public Integer numeroEntradas() {
        return tabla.size();
    }    

    public String imprimirTabla() {
    
        StringBuilder sb = new StringBuilder();
        
        sb.append("CONTENIDOS DE LA TABLA #" + numeroTabla + ":\n");

        for (Map.Entry<Integer, Simbolo> entrada : tabla.entrySet()) {

            // Obtenemos parametros de la entrada
            Simbolo simbolo = entrada.getValue();
            String nombre = simbolo.nombre.toString();
            String tipo = simbolo.tipo;
            Object valor = simbolo.valor;
            Integer desplazamiento = simbolo.desplazamiento;


            // Imprimimos la entrada
            sb.append("\n*\t");
            sb.append("LEXEMA\t:\t");
            sb.append("'" + nombre + "'\n");

            //sb.append("+\t");
            //sb.append("tipo\\t:\t");
            //sb.append("'" + tipo + "'\n");

            //sb.append("+\t");
            //sb.append("despl\t:\t");
            //sb.append(desplazamiento);
        }

        return sb.toString();
    }
    
}
