package tablaSimbolos;

import java.util.List;
import java.util.ArrayList;

public class GestorTablas {

    private List<TablaSimbolos> tablas;
    private int numeroTabla;

    private StringBuilder impresionTabla;

    public GestorTablas() {
        this.tablas = new ArrayList<>();
        this.impresionTabla = new StringBuilder();
        this.numeroTabla = -1;
        nuevaTabla();
    }

    public void nuevaTabla() {
        tablas.add(new TablaSimbolos(numeroTabla));
        numeroTabla++;
    }

    public void destruirTabla() throws IllegalStateException {
        impresionTabla.append(tablas.get(numeroTabla).imprimirTabla());
        tablas.remove(numeroTabla);
        numeroTabla--;
    }

    public StringBuilder getImpresionTabla() {
        return this.impresionTabla;
    }

    public TablaSimbolos obtenerTablaActual() {
        return tablas.get(numeroTabla);
    }

    public TablaSimbolos obtenerTablaGlobal() {
        return tablas.get(0);
    }
}
