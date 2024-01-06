package estructuras;

import java.util.*;

public class SimboloFuncion extends Simbolo {
    private Integer numeroParametros;
    private List<Tipo> tipoParametros;
    private List<Modo> modoPaso;
    private Tipo tipoDevuelto;

    public SimboloFuncion(Tipo tipo, String nombre, Integer numeroParametros, ArrayList<Tipo> tipoParametros, ArrayList<Modo> modoPaso, Tipo tipoDevuelto) {
        super(tipo, nombre);
        
        this.numeroParametros = numeroParametros;
        this.tipoParametros = tipoParametros;
        this.modoPaso = modoPaso;
        this.tipoDevuelto = tipoDevuelto;
    }

    public Integer getNumeroParametros() {
        return this.numeroParametros;
    }

    public void setNumeroParametros(Integer numeroParametros) {
        this.numeroParametros = numeroParametros;
    }

    public List<Tipo> getTipoParametros() {
        return this.tipoParametros;
    }

    public void setTipoParametros(ArrayList<Tipo> tipoParametros) {
        this.tipoParametros = tipoParametros;
    }

    public List<Modo> getModoPaso() {
        return this.modoPaso;
    }

    public void setModoPaso(ArrayList<Modo> modoPaso) {
        this.modoPaso = modoPaso;
    }

    public Tipo getTipoDevuelto() {
        return this.tipoDevuelto;
    }

    public void setTipoDevuelto(Tipo tipoDevuelto) {
        this.tipoDevuelto = tipoDevuelto;
    }
    

}
