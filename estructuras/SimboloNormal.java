package estructuras;

import java.util.ArrayList;
import java.util.List;

public class SimboloNormal extends Simbolo {

        // Parametros para los simbolos normales
    private Integer desplazamiento;
    private Integer ancho;
    private Tipo tipoRetorno;
    // Parametros para los simbolos de las funciones
    private Integer numeroParametros;
    private List<Tipo> tipoParametros;
    private List<Modo> modoPaso;
    private Tipo tipoDevuelto;

    public SimboloNormal(Tipo tipo, String nombre, Integer desplazamiento, Integer ancho) {
        super(tipo, nombre);

        this.desplazamiento = desplazamiento;
        this.ancho = ancho;
        this.tipoRetorno = null;
    }

     public SimboloNormal(Tipo tipo, String nombre, Integer numeroParametros, List<Tipo> tipoParametros, List<Modo> modoPaso, Tipo tipoDevuelto) {
        super(tipo, nombre);
        
        this.numeroParametros = numeroParametros;
        this.tipoParametros = tipoParametros;
        this.modoPaso = modoPaso;
        this.tipoDevuelto = tipoDevuelto;
    }

    public Integer getAncho() {
        return this.ancho;
    }

    public void setAncho(Integer ancho) {
        this.ancho = ancho;
    }

    public Integer getDesplazamiento() {
        return this.desplazamiento;
    }

    public void setDesplazamiento(Integer desplazamiento) {
        this.desplazamiento = desplazamiento;
    }

    public Tipo getTipoRetorno() {
        return this.tipoRetorno;
    }

    public void setTipoRetorno( Tipo retorno) {
        this.tipoRetorno = retorno;
    }

        // GETTERS Y SETTEERS DE LA FUNCION
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
