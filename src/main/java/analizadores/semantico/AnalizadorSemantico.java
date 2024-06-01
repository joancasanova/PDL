package analizadores.semantico;

public class AnalizadorSemantico {

    private ProcesadorReglas procesadorReglas;

    // Instancia única de la clase
    private static AnalizadorSemantico instancia;

    /**
     * Constructor privado del analizador semántico.
     */
    private AnalizadorSemantico() {
        this.procesadorReglas = ProcesadorReglas.getInstance();
    }

    /**
     * Devuelve la instancia única de la clase.
     * Si la instancia no ha sido creada aún, la crea.
     *
     * @return La instancia única de AnalizadorSemantico.
     */
    public static synchronized AnalizadorSemantico getInstance() {
        if (instancia == null) {
            instancia = new AnalizadorSemantico();
        }
        return instancia;
    }

    /**
     * Procesa una regla individual.
     * 
     * @param numeroRegla El número de la regla a procesar.
     */
    public void procesarRegla(Integer numeroRegla) {
        if (numeroRegla != null) {
            procesadorReglas.procesarRegla(numeroRegla);
        }
    }

    /**
     * Reinicia el analizador semantico a su estado inicial.
     */
    public void resetAnalizadorSemantico() {
        procesadorReglas.resetProcesadorReglas();
        this.procesadorReglas = ProcesadorReglas.getInstance();
    }
}
