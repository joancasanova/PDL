package src.tablaSimbolos;

import java.util.ArrayList;
import java.util.List;


public class Simbolo {

    private String nombre;
    private Tipo tipo;
    private Tipo tipoRetorno;

    // Parametros para los simbolos normales
    private Integer desplazamiento;
    private Integer ancho;

    // Parametros para los simbolos de las funciones
    private Integer numeroParametros;
    private List<Tipo> tipoParametros;
    private List<Modo> modoPaso;

    public Simbolo(Tipo tipo, String nombre, Integer desplazamiento, Integer ancho) {
        this.tipo = tipo;
        this.nombre = nombre;
        this.desplazamiento = desplazamiento;
        this.ancho = ancho;
        this.tipoRetorno = null;
    }

    public Simbolo(Tipo tipo, String nombre, Integer numeroParametros, List<Tipo> tipoParametros, List<Modo> modoPaso, Tipo tipoRetorno) {
        this.tipo = tipo;
        this.nombre = nombre;
        this.numeroParametros = numeroParametros;
        this.tipoParametros = tipoParametros;
        this.modoPaso = modoPaso;
        this.tipoRetorno = tipoRetorno;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Tipo getTipo() {
        return this.tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
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
}
