package tablaSimbolos;

import java.util.List;
import java.util.Stack;

import util.GestorErrores;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Clase GestorTablas que gestiona las tablas de símbolos en un compilador.
 */
public class GestorTablas {

    private List<TablaSimbolos> tablas;
    private int indiceTabla;
    private int numeroTabla;

    // Variables booleanas que indican si estamos en zonas especiales
    private Boolean zonaParametros;
    private Boolean zonaDeclaracion;

    // Simbolos que aún no tienen un tipo asignado (FIFO)
    private LinkedList<Simbolo> simbolosSinTipo;

    // Ultimos simbolos insertados en la tabla (LIFO)
    private Stack<Simbolo> ultimosSimbolos;

    private StringBuilder impresionTabla;

    // Instancia única de la clase
    private static GestorTablas instancia;

    /**
     * Constructor privado para evitar la creación de instancias.
     */
    private GestorTablas() {
        tablas = new ArrayList<>();
        indiceTabla = -1;
        numeroTabla = -1;
        simbolosSinTipo = new LinkedList<>();
        ultimosSimbolos = new Stack<>();
        impresionTabla = new StringBuilder();
        zonaParametros = false;
        zonaDeclaracion = false;
    }

    /**
     * Devuelve la instancia única de la clase.
     * Si la instancia no ha sido creada aún, la crea.
     * 
     * @return La instancia única de GestorTablas.
     */
    public static synchronized GestorTablas getInstance() {
        if (instancia == null) {
            instancia = new GestorTablas();
        }
        return instancia;
    }

    /**
     * Crea una nueva tabla de símbolos y la añade a la lista de tablas.
     */
    public void nuevaTabla() {
        tablas.add(new TablaSimbolos(numeroTabla));
        indiceTabla++;
        numeroTabla++;
    }

    /**
     * Destruye la tabla de símbolos actual y la elimina de la lista de tablas.
     * 
     * @throws IllegalStateException si no hay tablas para destruir.
     */
    public void destruirTabla() throws IllegalStateException {
        impresionTabla.append(tablas.get(indiceTabla).imprimirTabla());
        tablas.remove(indiceTabla);
        indiceTabla--;
    }

    /**
     * Obtiene la impresión de la tabla de símbolos.
     * 
     * @return Un StringBuilder con la impresión de la tabla.
     */
    public StringBuilder getImpresionTabla() {
        return this.impresionTabla;
    }

    /**
     * Obtiene la tabla de símbolos actual.
     * 
     * @return La tabla de símbolos actual.
     */
    public TablaSimbolos obtenerTablaActual() {
        return tablas.get(indiceTabla);
    }

    /**
     * Obtiene la tabla de símbolos global.
     * 
     * @return La tabla de símbolos global.
     */
    public TablaSimbolos obtenerTablaGlobal() {
        return tablas.get(0);
    }

    /**
     * Añade a la lista de últimos símbolos añadidos un símbolo.
     * Si el simbolo no tiene tipo, se añade a la lista de simbolos sin tipo.
     * 
     * @param simbolo El símbolo a añadir.
     */
    public void setUltimoSimbolo(Simbolo simbolo) {
        ultimosSimbolos.push(simbolo);

        if (simbolo.getTipo() == null) {
            simbolosSinTipo.add(simbolo);
        }
    }

    /**
     * Obtiene el último símbolo insertado en la tabla.
     * 
     * @return El último símbolo insertado.
     */
    public Simbolo getUltimoSimbolo() {
        return ultimosSimbolos.pop();
    }

    /**
     * Obtiene el último símbolo de función insertado en la tabla.
     * 
     * @return El último símbolo de función.
     */
    public Simbolo getUltimoSimboloFuncion() {
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

    /**
     * Consume y devuelve un símbolo sin tipo de la lista.
     * 
     * @return El símbolo sin tipo.
     * @throws IllegalStateException si no hay símbolos sin tipo.
     */
    public Simbolo consumirSimboloSinTipo() {
        if (simbolosSinTipo.size() < 1) {
            GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO, GestorErrores.ERROR_VARIABLE_REDECLARADA);
        }

        Simbolo simboloSinTipo = null;

        if (zonaParametros) {
            simboloSinTipo = simbolosSinTipo.removeLast();
        } else {
            simboloSinTipo = simbolosSinTipo.removeFirst();
        }

        return simboloSinTipo;
    }

    /**
     * Verifica el primer símbolo sin tipo en la lista.
     * 
     * @return El primer símbolo sin tipo o null si no hay símbolos sin tipo.
     */
    public Simbolo verPrimerSimboloSinTipo() {
        if (simbolosSinTipo.size() < 1) {
            return null;
        }

        return simbolosSinTipo.get(0);
    }

    /**
     * Obtiene el estado de la zona de declaración.
     * 
     * @return true si está en zona de declaración, false en caso contrario.
     */
    public Boolean getZonaDeclaracion() {
        return zonaDeclaracion;
    }

    /**
     * Establece el estado de la zona de declaración.
     * 
     * @param value true para activar la zona de declaración, false para
     *              desactivarla.
     */
    public void setZonaDeclaracion(Boolean value) {
        zonaDeclaracion = value;
    }

    /**
     * Obtiene el estado de la zona de parámetros.
     * 
     * @return true si está en zona de parámetros, false en caso contrario.
     */
    public Boolean getZonaParametros() {
        return zonaParametros;
    }

    /**
     * Establece el estado de la zona de parámetros.
     * 
     * @param value true para activar la zona de parámetros, false para
     *              desactivarla.
     */
    public void setZonaParametros(Boolean value) {
        zonaParametros = value;
    }

    /**
     * Reinicia el gestor de tablas a su estado inicial.
     */
    public void resetGestorTablas() {
        tablas = new ArrayList<>();
        indiceTabla = -1;
        numeroTabla = -1;
        simbolosSinTipo = new LinkedList<>();
        ultimosSimbolos = new Stack<>();
        impresionTabla = new StringBuilder();
        zonaParametros = false;
        zonaDeclaracion = false;
    }
}
