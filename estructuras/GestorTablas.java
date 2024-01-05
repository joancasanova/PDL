package estructuras;

import java.util.List;
import java.util.ArrayList;

public class GestorTablas {
    private List<TablaSimbolos> tablas;
    private Integer numeroTabla;

    public GestorTablas(){
        this.tablas = new ArrayList<>();
        this.numeroTabla = -1;
        nuevaTabla();
    }

    public void nuevaTabla() {
        tablas.add(new TablaSimbolos(numeroTabla));
        numeroTabla++;
    }

    public void destruirTabla() throws IllegalStateException {
        if (numeroTabla > 0) {
            tablas.remove(numeroTabla);
            numeroTabla--;
        } else {
            throw new IllegalStateException("Se ha intentado destruir la tabla de simbolos global");
        }
    }

    public TablaSimbolos obtenerTablaActual() {
        return tablas.get(numeroTabla);
    }

    public TablaSimbolos obtenerTablaGlobal() {
        return tablas.get(0);
    }
}
