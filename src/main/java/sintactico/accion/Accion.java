package sintactico.accion;

import sintactico.GestorPilas;

/**
 * Clase abstracta que representa una acción sintáctica.
 */
public abstract class Accion {

    /**
     * Ejecuta la acción sobre el gestor de pilas.
     * 
     * @param gestorPilas El gestor de pilas que maneja los estados y símbolos.
     * @return El número de la regla aplicada, si corresponde.
     */
    public abstract Integer ejecutar(GestorPilas gestorPilas);
}
