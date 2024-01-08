package tablaSimbolos;

import java.util.List;
import java.util.Map;

import token.*;

import java.util.HashMap;

public class TablaSimbolos {

    // Estructura para almacenar los símbolos
    private final Map<Integer, Simbolo> tabla;

    // Ultimo identificador emitido
    private Token ultimoIdentificador;

    // Numero identificador para la tabla de simbolos
    private Integer numeroTabla;

    // Zona de asignacion
    private Boolean zonaAsignacion;

    private Token identificadorEnZonaAsignacion;

    private Integer desplazamiento;

    public TablaSimbolos(Integer numeroTabla) {
        tabla = new HashMap<>();
        this.numeroTabla = numeroTabla;
        this.zonaAsignacion = false;
        this.desplazamiento = 0;
    }

    public void setIdentificador(Token token) {
        if (token.getTipo() == TokenType.ID) {
            if (zonaAsignacion) {
                identificadorEnZonaAsignacion = token;
            } else {
                ultimoIdentificador = token;
            }
        }
    }

    public Token getIdentificadorEnZonaAsignacion() {
        return identificadorEnZonaAsignacion;
    }

    public Token getUltimoIdentificador() {
        return ultimoIdentificador;
    }

    public Boolean getZonaAsignacion() {
        return this.zonaAsignacion;
    }

    public void setZonaAsignacion(Boolean zonaAsignacion) {
        this.zonaAsignacion = zonaAsignacion;
    }

    public void agregarSimboloNormal(Integer posicion, Simbolo simboloNormal) {
        tabla.put(posicion, simboloNormal);
    }
    
    // Método para obtener un símbolo existente de la tabla
    public Simbolo obtenerSimbolo(Integer posicion) {
        return tabla.get(posicion);
    }

    public void setDesplazamiento(int desplazamiento) {
        this.desplazamiento = desplazamiento;
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

        Integer numeroTablaAlterado = numeroTabla + 1;

        sb.append("CONTENIDOS DE LA TABLA #" + numeroTablaAlterado + ":\n");

        for (Map.Entry<Integer, Simbolo> entrada : tabla.entrySet()) {

            // Obtenemos parametros de la entrada
            Simbolo simbolo = entrada.getValue();
            String nombre = simbolo.getNombre().toString();
            Tipo tipo = simbolo.getTipo();
            Integer desplazamientoSimbolo = simbolo.getDesplazamiento();
            Tipo tipoRetorno = simbolo.getTipoRetorno();

            // Imprimimos la entrada

            if (tipoRetorno == null) {
                sb.append("\n*\t");
                sb.append("LEXEMA\t:\t");
                sb.append("'" + nombre + "'\n");

                sb.append("Atributos: ");
                sb.append("\n+ tipo: ");
                sb.append("'" + tipo + "'\t");

                sb.append("\n+ despl: ");
                sb.append(desplazamientoSimbolo);
            } else {

                Integer numParam = simbolo.getNumeroParametros();
                List<Tipo> parametros = simbolo.getTipoParametros();
                List<Modo> modoParametros = simbolo.getModoPaso();

                sb.append("\n*\t");
                sb.append("LEXEMA\t:\t");
                sb.append("'" + nombre + "'\n");

                sb.append("Atributos: ");
                sb.append("\n+ tipo: ");
                sb.append("'" + tipo + "'\t");

                sb.append("\n+ numParam: ");
                sb.append("'" + numParam + "'\t");

                int i = 0;
                for (Tipo tipoParametro : parametros) {
                    sb.append("\n+ TipoParam" + i + ": ");
                    sb.append("'" + tipoParametro + "'\t");
                    sb.append("\n+ ModoParam" + i + ": ");
                    sb.append("'" + modoParametros.get(i) + "'\t");
                    i++;
                }

                sb.append("\n+ TipoRetorno: ");
                sb.append("'" + tipoRetorno + "'\t");

                sb.append("\n+ EtiqFuncion: ");
                sb.append("'" + nombre + "'\t");
            }
        }

        sb.append("\n");
        sb.append("\n");

        return sb.toString();
    }

}
