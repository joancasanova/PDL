package semantico;

import tablaSimbolos.enums.Tipo;

/**
 * Clase que mantiene el estado semántico del análisis, incluyendo
 * indicadores de ejecución de return y if/while, y el tipo de retorno
 * dentro de un bloque if/while.
 */
public class EstadoSemantico {

    private Boolean returnEjecutado;
    private Boolean ifWhileEjecutado;
    private Tipo tipoReturnIfWhile;

    // Instancia única de la clase
    private static EstadoSemantico instancia;

    /**
     * Constructor privado para evitar la creación de instancias.
     * Inicializa los indicadores de ejecución de return y if/while a false.
     */
    private EstadoSemantico() {
        this.returnEjecutado = false;
        this.ifWhileEjecutado = false;
    }

    /**
     * Devuelve la instancia única de la clase.
     * Si la instancia no ha sido creada aún, la crea.
     *
     * @return La instancia única de EstadoSemantico.
     */
    public static synchronized EstadoSemantico getInstance() {
        if (instancia == null) {
            instancia = new EstadoSemantico();
        }
        return instancia;
    }

    /**
     * Obtiene el estado de ejecución de return.
     *
     * @return true si se ha ejecutado un return, false en caso contrario.
     */
    public Boolean getReturnEjecutado() {
        return returnEjecutado;
    }

    /**
     * Establece el estado de ejecución de return.
     *
     * @param returnEjecutado true si se ha ejecutado un return, false en caso
     *                        contrario.
     */
    public void setReturnEjecutado(Boolean returnEjecutado) {
        this.returnEjecutado = returnEjecutado;
    }

    /**
     * Obtiene el estado de ejecución de if/while.
     *
     * @return true si se ha ejecutado un bloque if/while, false en caso contrario.
     */
    public Boolean getIfWhileEjecutado() {
        return ifWhileEjecutado;
    }

    /**
     * Establece el estado de ejecución de if/while.
     *
     * @param ifWhileEjecutado true si se ha ejecutado un bloque if/while, false en
     *                         caso contrario.
     */
    public void setIfWhileEjecutado(Boolean ifWhileEjecutado) {
        this.ifWhileEjecutado = ifWhileEjecutado;
    }

    /**
     * Obtiene el tipo de retorno dentro de un bloque if/while.
     *
     * @return El tipo de retorno dentro de un bloque if/while.
     */
    public Tipo getTipoReturnIfWhile() {
        return tipoReturnIfWhile;
    }

    /**
     * Establece el tipo de retorno dentro de un bloque if/while.
     *
     * @param tipoReturnIfWhile El tipo de retorno dentro de un bloque if/while.
     */
    public void setTipoReturnIfWhile(Tipo tipoReturnIfWhile) {
        this.tipoReturnIfWhile = tipoReturnIfWhile;
    }

    /**
     * Reinicia el estado semántico a su estado inicial.
     */
    public void reset() {
        this.returnEjecutado = false;
        this.ifWhileEjecutado = false;
        this.tipoReturnIfWhile = null;
    }
}
