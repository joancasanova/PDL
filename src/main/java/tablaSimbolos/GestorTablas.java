package tablaSimbolos;

import java.util.List;
import java.util.Stack;
import java.util.ArrayList;
import java.util.LinkedList;

public class GestorTablas {

    private static List<TablaSimbolos> tablas = new ArrayList<>();
    private static int indiceTabla = -1;
    private static int numeroTabla = -1;

    // Simbolos que aún no tienen un tipo asignado (FIFO)
    private static LinkedList<Simbolo> simbolosSinTipo = new LinkedList<>();

    // Ultimos simbolos insertados en la tabla (LIFO)
    private static Stack<Simbolo> ultimosSimbolos = new Stack<>();

    private static StringBuilder impresionTabla = new StringBuilder();

    private static Boolean zonaParametros = false;
    private static Boolean zonaDeclaracion = false;

    private GestorTablas() {
    }

    public static void nuevaTabla() {
        tablas.add(new TablaSimbolos(numeroTabla));
        indiceTabla++;
        numeroTabla++;
    }

    public static void destruirTabla() throws IllegalStateException {
        impresionTabla.append(tablas.get(indiceTabla).imprimirTabla());
        tablas.remove(indiceTabla);
        indiceTabla--;
    }

    public static StringBuilder getImpresionTabla() {
        return GestorTablas.impresionTabla;
    }

    public static TablaSimbolos obtenerTablaActual() {
        return tablas.get(indiceTabla);
    }

    public static TablaSimbolos obtenerTablaGlobal() {
        return tablas.get(0);
    }

    public static void setUltimoSimbolo(Simbolo simbolo) {
        ultimosSimbolos.push(simbolo);

        if (simbolo.getTipo() == null) { // El problema está aqui
            simbolosSinTipo.add(simbolo);
        }
    }

    public static Simbolo getUltimoSimbolo() {
        return ultimosSimbolos.pop();
    }

    public static Simbolo getSimboloFuncion(String nombreFuncion) {
        TablaSimbolos tablaGlobal = GestorTablas.obtenerTablaGlobal();
        for (Simbolo simboloGlobal : tablaGlobal.getTabla().values()) {
            if (nombreFuncion.equals(simboloGlobal.getNombre())) {
                return simboloGlobal;
            }
        }
        return null;
    }

    public static Simbolo getUltimoSimboloFuncion() {
        Stack<Simbolo> tempStack = new Stack<>();
        Simbolo simbolo = null;

        // Buscar el elemento mientras la pila original no esté vacía
        while (!ultimosSimbolos.isEmpty()) {
            Simbolo elemento = ultimosSimbolos.pop();
            tempStack.push(elemento);

            if (elemento.getNumeroParametros() != null) {
                simbolo = elemento;
                tempStack.pop();
                break;
            }
        }

        // Restaurar el stack original
        while (!tempStack.isEmpty()) {
            ultimosSimbolos.push(tempStack.pop());
        }

        return simbolo;
    }

    public static Simbolo consumirSimboloSinTipo() throws IllegalStateException {

        if (simbolosSinTipo.size() < 1) {
            throw new IllegalStateException("se está redeclarando una variable");
        }

        Simbolo simboloSinTipo = null;

        if (zonaParametros) {
            simboloSinTipo = simbolosSinTipo.removeLast();
        } else {
            simboloSinTipo = simbolosSinTipo.removeFirst();
        }

        return simboloSinTipo;
    }

    public static Simbolo verPrimerSimboloSinTipo() throws IllegalStateException {

        if (simbolosSinTipo.size() < 1) {
            return null;
        }

        Simbolo simboloSinTipo = simbolosSinTipo.get(0);
        return simboloSinTipo;
    }

    public static Boolean getZonaDeclaracion() {
        return zonaDeclaracion;
    }

    public static void setZonaDeclaracion(Boolean value) {
        zonaDeclaracion = value;
    }

    public static Boolean getZonaParametros() {
        return zonaParametros;
    }

    public static void setZonaParametros(Boolean value) {
        zonaParametros = value;
    }

    public static void resetGestorTablas() {
        tablas = new ArrayList<>();
        indiceTabla = -1;
        numeroTabla = -1;
        simbolosSinTipo = new LinkedList<>();
        ultimosSimbolos = new Stack<>();
        impresionTabla = new StringBuilder();
        zonaParametros = false;
    }
}
