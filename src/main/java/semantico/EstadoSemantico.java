package semantico;

import tablaSimbolos.enums.Tipo;

public class EstadoSemantico {
    private Boolean returnEjecutado;
    private Boolean ifWhileEjecutado;
    private Tipo tipoReturnIfWhile;

    public EstadoSemantico() {
        this.returnEjecutado = false;
        this.ifWhileEjecutado = false;
    }

    public Boolean getReturnEjecutado() {
        return returnEjecutado;
    }

    public void setReturnEjecutado(Boolean returnEjecutado) {
        this.returnEjecutado = returnEjecutado;
    }

    public Boolean getIfWhileEjecutado() {
        return ifWhileEjecutado;
    }

    public void setIfWhileEjecutado(Boolean ifWhileEjecutado) {
        this.ifWhileEjecutado = ifWhileEjecutado;
    }

    public Tipo getTipoReturnIfWhile() {
        return tipoReturnIfWhile;
    }

    public void setTipoReturnIfWhile(Tipo tipoReturnIfWhile) {
        this.tipoReturnIfWhile = tipoReturnIfWhile;
    }

    public void reset() {
        this.returnEjecutado = false;
        this.ifWhileEjecutado = false;
        this.tipoReturnIfWhile = null;
    }
}
