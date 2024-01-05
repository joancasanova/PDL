package estructuras;

import java.util.*;

public class TablaSimbolos {
    
    // Estructura para almacenar los símbolos
    private final Map<Integer, Simbolo> tabla;

    // Ultimo identificador emitido
    private Token ultimoIdentificador;

    // Numero identificador para la tabla de simbolos
    private Integer numeroTabla;

    // Zona de declaracion
    private Boolean zonaDeclaracion;

    private Integer desplazamiento;

    public TablaSimbolos(Integer numeroTabla) {
        tabla = new HashMap<>();
        this.numeroTabla = numeroTabla;
        this.zonaDeclaracion = false;
        this.desplazamiento = 0;
    }

    public void setUltimoIdentificador(Token token) {
        if(token.getTipo() == TokenType.ID) {
            ultimoIdentificador = token;
        }
    }

    public Token getUltimoIdentificador() {
        return ultimoIdentificador;
    }

    public Boolean getZonaDeclaracion() {
        return this.zonaDeclaracion;
    }

    public void setZonaDeclaracion(Boolean zonaDeclaracion) {
        this.zonaDeclaracion = zonaDeclaracion;
    }

    public void agregarSimboloNormal(Integer posicion, SimboloNormal simboloNormal) {
        tabla.put(posicion, simboloNormal);
        if (!(simboloNormal.getAncho() == null)){
            desplazamiento = desplazamiento + simboloNormal.getAncho();
        }
    }

    public void agregarSimboloFuncion(Integer posicion, SimboloFuncion simboloFuncion) {
        tabla.put(posicion, simboloFuncion);
    }

    // Método para obtener un símbolo existente de la tabla
    public Simbolo obtenerSimbolo(Integer posicion) {
        return tabla.get(posicion);
    }

    public Integer getDesplazamiento() {
        return this.desplazamiento;
    }

    // Método para obtener posicion un símbolo existente de la tabla
    public Integer obtenerPosicionSimbolo(String nombre) {
        Integer posicion = null;
        for (Map.Entry<Integer, Simbolo> entry : tabla.entrySet()) {
            if (entry.getValue().getNombre().equals(nombre)) {
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
            if (simbolo.getNombre().equals(nombre)) {
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
            String nombre = simbolo.getNombre().toString();
            Tipo tipo = simbolo.getTipo();
            Integer desplazamientoSimbolo;
            
            if (simbolo instanceof SimboloNormal) {
                desplazamientoSimbolo = ((SimboloNormal)simbolo).getDesplazamiento();
            } else {
                desplazamientoSimbolo = -1;
            }

            // Imprimimos la entrada
            sb.append("\n*\t");
            sb.append("LEXEMA\t:\t");
            sb.append("'" + nombre + "'\n");

            sb.append("Atributos: ");
            sb.append("+ tipo: ");
            sb.append("'" + tipo + "'\t");

            sb.append("+ despl: ");
            sb.append(desplazamientoSimbolo);
        }

        return sb.toString();
    }
    
}
