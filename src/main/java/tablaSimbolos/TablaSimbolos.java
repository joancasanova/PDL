package tablaSimbolos;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class TablaSimbolos {

    // Estructura para almacenar los símbolos
    private final Map<Integer, Simbolo> tabla;

    // Numero identificador para la tabla de simbolos
    private Integer numeroTabla;
    private Integer desplazamiento;

    public TablaSimbolos(Integer numeroTabla) {
        tabla = new HashMap<>();
        this.numeroTabla = numeroTabla;
        this.desplazamiento = 0;
    }

    public void agregarSimbolo(Integer posicion, Simbolo simbolo) {
        tabla.put(posicion, simbolo);
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
    public Integer obtenerPosicionSimbolo(Simbolo simbolo) {
        Integer posicion = null;
        for (Map.Entry<Integer, Simbolo> entry : tabla.entrySet()) {
            if (entry.getValue().equals(simbolo)) {
                posicion = entry.getKey();
            }
        }
        return posicion;
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

    // Método para verificar si un símbolo ya existe en la tabla
    public Simbolo obtenerSimboloPorNombre(String nombre) {
        for (Simbolo simbolo : tabla.values()) {
            if (simbolo.getNombre().equals(nombre)) {
                return simbolo;
            }
        }
        return null;
    }

    // Método para obtener un símbolo existente de la tabla
    public Integer numeroEntradas() {
        return tabla.size();
    }

    public Map<Integer, Simbolo> getTabla() {
        return this.tabla;
    }

    public Integer getNumeroTabla() {
        return this.numeroTabla;
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
            if (tipo == null) {
                tipo = Tipo.INT;
            }
            Integer desplazamientoSimbolo = simbolo.getDesplazamiento();
            if (desplazamientoSimbolo == null) {
                desplazamientoSimbolo = 2;
            }
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
