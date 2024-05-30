package semantico;

import tablaSimbolos.enums.Modo;
import tablaSimbolos.enums.Tipo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Clase GestorParametros que gestiona los parámetros de las funciones
 */
public class GestorParametros {

    private ArrayList<Tipo> tipoParametrosFuncion;
    private ArrayList<Modo> modoPasoParametros;
    private ArrayList<Tipo> listaDeParametros;

    /**
     * Constructor de la clase GestorParametros.
     * Inicializa las listas de tipos y modos de paso de los parámetros.
     */
    public GestorParametros() {
        this.tipoParametrosFuncion = new ArrayList<>();
        this.modoPasoParametros = new ArrayList<>();
        this.listaDeParametros = new ArrayList<>();
    }

    /**
     * Obtiene la lista de tipos de los parámetros de la función.
     *
     * @return La lista de tipos de los parámetros de la función.
     */
    public ArrayList<Tipo> getTipoParametrosFuncion() {
        return tipoParametrosFuncion;
    }

    /**
     * Obtiene la lista de modos de paso de los parámetros de la función.
     *
     * @return La lista de modos de paso de los parámetros de la función.
     */
    public ArrayList<Modo> getModoPasoParametros() {
        return modoPasoParametros;
    }

    /**
     * Obtiene la lista de parámetros.
     *
     * @return La lista de parámetros.
     */
    public ArrayList<Tipo> getListaDeParametros() {
        return listaDeParametros;
    }

    /**
     * Añade un parámetro a las listas de parámetros de la función.
     *
     * @param tipo El tipo de parámetro a añadir.
     */
    public void addParametroFuncion(Tipo tipo, Modo modo) {
        tipoParametrosFuncion.add(tipo);
        modoPasoParametros.add(modo);
    }

    /**
     * Añade un tipo de parámetro a la lista de parámetros.
     *
     * @param tipo El tipo de parámetro a añadir.
     */
    public void addParametro(Tipo tipo) {
        listaDeParametros.add(tipo);
    }

    /**
     * Invierte el orden de los tipos de parámetros y modos de paso de los
     * parámetros de la función.
     */
    public void reverseParametros() {
        Collections.reverse(tipoParametrosFuncion);
        Collections.reverse(modoPasoParametros);
    }

    /**
     * Reinicia las listas de tipos y modos de paso de los parámetros a su estado
     * inicial.
     */
    public void reset() {
        tipoParametrosFuncion.clear();
        modoPasoParametros.clear();
        listaDeParametros.clear();
    }

    /**
     * Comprueba si la lista de tipos de parámetros de la función está vacía.
     *
     * @return true si la lista está vacía, false en caso contrario.
     */
    public boolean isTipoParametrosFuncionEmpty() {
        return tipoParametrosFuncion.isEmpty();
    }

    /**
     * Comprueba si la lista de modos de paso de parámetros de la función está
     * vacía.
     *
     * @return true si la lista está vacía, false en caso contrario.
     */
    public boolean isModoPasoParametrosEmpty() {
        return modoPasoParametros.isEmpty();
    }

    /**
     * Comprueba si la lista de parámetros está vacía.
     *
     * @return true si la lista está vacía, false en caso contrario.
     */
    public boolean isListaDeParametrosEmpty() {
        return listaDeParametros.isEmpty();
    }

    /**
     * Obtiene el número de parámetros de la función.
     *
     * @return El número de parámetros de la función.
     */
    public int getNumeroParametrosFuncion() {
        return tipoParametrosFuncion.size();
    }

    /**
     * Obtiene una copia de la lista de tipos de parámetros de la función.
     *
     * @return Una copia de la lista de tipos de parámetros de la función.
     */
    public List<Tipo> getCopiaTipoParametrosFuncion() {
        return new ArrayList<>(tipoParametrosFuncion);
    }

    /**
     * Obtiene una copia de la lista de modos de paso de parámetros de la función.
     *
     * @return Una copia de la lista de modos de paso de parámetros de la función.
     */
    public List<Modo> getCopiaModoPasoParametros() {
        return new ArrayList<>(modoPasoParametros);
    }
}
