package modulos.tablaSimbolos;

import java.util.Stack;

import modulos.tablaSimbolos.enums.Tipo;
import util.GestorErrores;

import java.util.LinkedList;

/**
 * Clase GestorTablas que gestiona las tablas de símbolos en un compilador.
 */
public class GestorTablas {

    private Stack<TablaSimbolos> tablas;
    private Boolean tablaGlobal;
    private int numeroDeTablas;

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
        numeroDeTablas = 0;
        tablas = new Stack<TablaSimbolos>();
        tablas.add(new TablaSimbolos(numeroDeTablas));
        simbolosSinTipo = new LinkedList<>();
        ultimosSimbolos = new Stack<>();
        impresionTabla = new StringBuilder();
        zonaParametros = false;
        zonaDeclaracion = false;
        tablaGlobal = true;
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
        tablaGlobal = false;
        numeroDeTablas += 1;
        tablas.push(new TablaSimbolos(numeroDeTablas));
    }

    /**
     * Destruye la tabla de símbolos actual y la elimina de la lista de tablas.
     */
    public void destruirTabla() {
        tablaGlobal = true;
        TablaSimbolos ts = tablas.pop();
        impresionTabla.append(ts.imprimirTabla());
    }

    /**
     * Obtiene la impresión de la tabla de símbolos.
     * 
     * @String Un String con la impresión de la tabla.
     */
    public String getImpresionTablas() {
        return this.impresionTabla.toString();
    }

    /**
     * Obtiene la tabla de símbolos actual.
     * 
     * @return La tabla de símbolos actual.
     */
    public TablaSimbolos obtenerTablaActual() {
        return tablas.peek();
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
     * Elimina un simbolo sin tipo de la lista.
     *
     * @param simbolo El simbolo que se desea eliminar de la lista.
     */
    public void eliminarSimboloSinTipo(Simbolo simbolo) {
        simbolosSinTipo.remove(simbolo);
    }

    /**
     * Obtiene si la tabla actual es global o local.
     * 
     * @return true si es la tabla global.
     */
    public Boolean isTablaGlobal() {
        return tablaGlobal;
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
     * Asigna un tipo a un símbolo.
     *
     * @param simbolo El símbolo al que se le asignará el tipo.
     * @param tipo    El tipo a asignar.
     */
    public void asignarTipo(Simbolo simbolo, Tipo tipo) {
        TablaSimbolos tabla = obtenerTablaActual();

        if (simbolo.getTipo() != null) {
            GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO, GestorErrores.ERROR_VARIABLE_REDECLARADA);
        }
        simbolo.setDesplazamiento(tabla.getDesplazamiento());
        simbolo.setTipo(tipo);
        simbolo.setAncho(calcularAncho(tipo));
        tabla.setDesplazamiento(tabla.getDesplazamiento() + simbolo.getAncho());
    }

    /**
     * Calcula el ancho de un tipo.
     *
     * @param tipo El tipo cuyo ancho se va a calcular.
     * @return El ancho del tipo.
     */
    private int calcularAncho(Tipo tipo) {
        switch (tipo) {
            case STRING:
                return 128;
            case BOOLEAN:
            case INT:
                return 2;
            case VOID:
                return 0;
            default:
                GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO, GestorErrores.ERROR_TIPO_NO_COMPATIBLE);
                return -1; // no se alcanza
        }
    }

    /**
     * Reinicia el gestor de tablas a su estado inicial.
     */
    public void resetGestorTablas() {
        numeroDeTablas = 0;
        tablas = new Stack<TablaSimbolos>();
        tablas.add(new TablaSimbolos(numeroDeTablas));
        simbolosSinTipo = new LinkedList<>();
        ultimosSimbolos = new Stack<>();
        impresionTabla = new StringBuilder();
        zonaParametros = false;
        zonaDeclaracion = false;
        tablaGlobal = true;
    }
}
