package sintactico.accion;

import sintactico.GestorPilas;
import sintactico.ParserGramatica;

/**
 * Clase que representa la acción de reducción en el análisis sintáctico.
 */
public class AccionReducir extends Accion {
    private Integer regla;
    private String noTerminal;
    private Integer numeroDesapilar;

    /**
     * Constructor de AccionReducir.
     * 
     * @param regla           El número de la regla a aplicar.
     * @param noTerminal      El no terminal resultante de la reducción.
     * @param numeroDesapilar El número de elementos a desapilar.
     */
    public AccionReducir(Integer regla, String noTerminal, Integer numeroDesapilar) {
        this.noTerminal = noTerminal;
        this.regla = regla;
        this.numeroDesapilar = numeroDesapilar;
    }

    /**
     * Ejecuta la acción de reducción sobre el gestor de pilas.
     * 
     * @param gestorPilas El gestor de pilas que maneja los estados y símbolos.
     * @return El número de la regla aplicada incrementado en uno.
     */
    @Override
    public Integer ejecutar(GestorPilas gestorPilas) {

        // Desapila la cantidad calculada de estados de una sola vez
        int i = numeroDesapilar;
        while (i > 0) {
            gestorPilas.getPilaEstados().pop();
            gestorPilas.getPilaSimbolos().pop();
            i--;
        }

        int estado = ParserGramatica.getInstance().getTablaGoTo().get(gestorPilas.getPilaEstados().peek())
                .get(noTerminal);

        // Actualizar las pilas
        gestorPilas.getPilaSimbolos().push(noTerminal);
        gestorPilas.getPilaEstados().push(estado);

        return regla + 1;
    }

}