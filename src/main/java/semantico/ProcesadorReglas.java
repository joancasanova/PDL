package semantico;

import tablaSimbolos.*;
import tablaSimbolos.enums.Modo;
import tablaSimbolos.enums.Tipo;
import util.GestorErrores;

import java.util.*;

public class ProcesadorReglas {

    private GestorTablas gestorTablas;
    private Stack<Tipo> pilaTipos;
    private GestorParametros gestorParametros;
    private Boolean returnEjecutado;
    private Boolean ifWhileEjecutado;
    private Tipo tipoReturnIfWhile;

    public ProcesadorReglas() {
        this.gestorTablas = GestorTablas.getInstance();
        this.pilaTipos = new Stack<>();
        this.gestorParametros = new GestorParametros();
        this.returnEjecutado = false;
        this.ifWhileEjecutado = false;
    }

    public void procesarRegla(Integer numeroRegla) {
        switch (numeroRegla) {
            case 1: // Aceptar
                aceptar();
                break;
            case 2: // P: B P
            case 3: // P: F P
            case 4: // P: lambda
            case 16: // H: T
            case 19: // A: VOID
            case 21: // K: lambda
            case 25: // E: U
            case 27: // U: V
            case 29: // V: W
            case 42: // L: lambda
            case 44: // Q: lambda
            case 45: // Z: E
                break;
            case 5: // B: IF ( E ) S
            case 6: // B: WHILE ( E ) { C }
                procesarIfWhile();
                break;
            case 7: // B: LET ID T ;
                procesarDeclaracion();
                break;
            case 8: // B: LET ID T = E ;
                procesarAsignacion();
                break;
            case 9: // B: S
                procesarRetorno();
                break;
            case 10: // T: INT
                pilaTipos.add(Tipo.INT);
                break;
            case 11: // T: BOOLEAN
                pilaTipos.add(Tipo.BOOLEAN);
                break;
            case 12: // T: STRING
                pilaTipos.add(Tipo.STRING);
                break;
            case 13: // F: F1 { C }
                procesarFuncion();
                break;
            case 14: // F1: F2 ( A )
                procesarParametrosFuncion();
                break;
            case 15: // F2: FUNCTION ID H
                procesarFuncionID();
                break;
            case 17: // H: VOID
                pilaTipos.add(Tipo.VOID);
                break;
            case 18: // A: T ID K
            case 20: // K: , T ID K
                procesarParametros();
                break;
            case 22: // C: B C
                procesarSecuencia();
                break;
            case 23: // C: lambda
                pilaTipos.add(Tipo.OK);
                break;
            case 24: // E: E1 == U
                procesarComparacion();
                break;
            case 26: // U: U1 + V
                procesarSuma();
                break;
            case 28: // V: !W
                procesarNegacion();
                break;
            case 30: // W: ID
                procesarIdentificador();
                break;
            case 31: // W: ( E )
                procesarParentesis();
                break;
            case 32: // W: ID ( L )
                procesarLlamadaFuncion();
                break;
            case 33: // W: ENTERO
                pilaTipos.add(Tipo.INT);
                break;
            case 34: // W: CADENA
                pilaTipos.add(Tipo.STRING);
                break;
            case 35: // S: ID = E ;
            case 36: // S: ID += E ;
                procesarAsignacionConOperacion();
                break;
            case 37: // S: ID ( L ) ;
                procesarLlamadaFuncionConParametros();
                break;
            case 38: // S: PUT E ;
                procesarPut();
                break;
            case 39: // S: GET ID ;
                procesarGet();
                break;
            case 40: // S: RETURN Z ;
                procesarReturn();
                break;
            case 41: // L: E Q
            case 43: // Q: , E Q
                gestorParametros.addParametro(pilaTipos.pop());
                break;
            case 46: // Z: lambda
                pilaTipos.add(Tipo.VOID);
                break;
            default:
                GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO, GestorErrores.ERROR_REGLA_NO_IMPLEMENTADA);
        }
    }

    private void aceptar() {
        gestorTablas.destruirTabla();
    }

    private void procesarIfWhile() {
        ifWhileEjecutado = true;
        Tipo tipoSimboloS = pilaTipos.pop();
        Tipo tipoSimboloE = pilaTipos.pop();
        if (!tipoSimboloE.equals(Tipo.BOOLEAN)) {
            GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO, GestorErrores.ERROR_TIPO_BOOLEAN);
        }
        if (returnEjecutado) {
            tipoReturnIfWhile = tipoSimboloS;
        }
        pilaTipos.add(Tipo.OK);
    }

    private void procesarDeclaracion() {
        Tipo tipoSimbolo = pilaTipos.pop();
        Simbolo simbolo = gestorTablas.consumirSimboloSinTipo();
        asignarTipo(simbolo, tipoSimbolo, gestorTablas.obtenerTablaActual());
        pilaTipos.add(Tipo.OK);
    }

    private void procesarAsignacion() {
        Tipo tipoSimboloE = pilaTipos.pop();
        Tipo tipoSimboloT = pilaTipos.pop();
        if (tipoSimboloE == tipoSimboloT) {
            Simbolo simbolo = gestorTablas.consumirSimboloSinTipo();
            asignarTipo(simbolo, tipoSimboloT, gestorTablas.obtenerTablaActual());
        } else {
            GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO, GestorErrores.ERROR_TIPOS_NO_COINCIDEN);
        }
        pilaTipos.add(Tipo.OK);
    }

    private void procesarRetorno() {
        Tipo tipoRetornoS = pilaTipos.pop();
        if (tipoRetornoS != null) {
            pilaTipos.add(tipoRetornoS);
        }
    }

    private void procesarFuncion() {
        Tipo tipoRetornoC = pilaTipos.pop();
        Tipo tipoRetornoF1 = pilaTipos.pop();
        if (returnEjecutado) {
            if (!(tipoRetornoF1.equals(tipoRetornoC))) {
                if (!(tipoRetornoF1.equals(Tipo.VOID) && tipoRetornoC == Tipo.OK)) {
                    if (ifWhileEjecutado && !(tipoRetornoF1.equals(tipoReturnIfWhile))) {
                        GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO,
                                GestorErrores.ERROR_TIPO_RETORNO_FUNCION);
                    }
                }
            }
        }
        gestorTablas.destruirTabla();
        returnEjecutado = false;
        ifWhileEjecutado = false;
    }

    private void procesarParametrosFuncion() {
        gestorTablas.setZonaParametros(false);
        Simbolo simbolo = gestorTablas.getUltimoSimboloFuncion();
        simbolo.setNumeroParametros(gestorParametros.getNumeroParametrosFuncion());
        simbolo.setTipoParametros(new ArrayList<>(gestorParametros.getTipoParametrosFuncion()));
        simbolo.setModoPaso(new ArrayList<>(gestorParametros.getModoPasoParametros()));
        gestorParametros.reset();
    }

    private void procesarFuncionID() {
        gestorTablas.setZonaParametros(true);
        gestorTablas.nuevaTabla();
        returnEjecutado = false;
        ifWhileEjecutado = false;
        Tipo tipoSimbolo = pilaTipos.peek();
        Simbolo simbolo = gestorTablas.consumirSimboloSinTipo();
        simbolo.setNumeroParametros(0);
        simbolo.setTipoRetorno(tipoSimbolo);
        asignarTipo(simbolo, tipoSimbolo, gestorTablas.obtenerTablaGlobal());
    }

    private void procesarParametros() {
        Tipo tipoSimbolo = pilaTipos.pop();
        Simbolo simbolo = gestorTablas.consumirSimboloSinTipo();
        asignarTipo(simbolo, tipoSimbolo, gestorTablas.obtenerTablaActual());
        gestorParametros.addParametroFuncion(tipoSimbolo, Modo.VALOR);
    }

    private void procesarSecuencia() {
        Tipo tipoRetornoC = pilaTipos.pop();
        Tipo tipoRetornoB = pilaTipos.pop();
        if (tipoRetornoB != Tipo.OK && tipoRetornoC != Tipo.OK) {
            GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO, GestorErrores.ERROR_TIPO_RETORNO_FUNCION);
        } else {
            if (tipoRetornoB != Tipo.OK) {
                pilaTipos.add(tipoRetornoB);
            } else {
                pilaTipos.add(tipoRetornoC);
            }
        }
    }

    private void procesarComparacion() {
        Tipo tipoSimboloE1 = pilaTipos.pop();
        Tipo tipoSimboloU = pilaTipos.pop();
        if (tipoSimboloE1 == tipoSimboloU && tipoSimboloE1.equals(Tipo.INT)) {
            pilaTipos.add(Tipo.BOOLEAN);
        } else {
            GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO, GestorErrores.ERROR_TIPOS_NO_COINCIDEN);
        }
    }

    private void procesarSuma() {
        Tipo tipoSimboloU1 = pilaTipos.pop();
        Tipo tipoSimboloV = pilaTipos.pop();
        if (tipoSimboloU1 == null || tipoSimboloV == null) {
            GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO, GestorErrores.ERROR_VARIABLE_SIN_INICIALIZAR);
        }
        if (tipoSimboloU1 == tipoSimboloV && tipoSimboloU1.equals(Tipo.INT)) {
            pilaTipos.add(Tipo.INT);
        } else {
            GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO, GestorErrores.ERROR_TIPOS_NO_COINCIDEN);
        }
    }

    private void procesarNegacion() {
        Tipo tipoSimbolo = pilaTipos.pop();
        if (tipoSimbolo.equals(Tipo.BOOLEAN)) {
            pilaTipos.add(Tipo.BOOLEAN);
        } else {
            GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO, GestorErrores.ERROR_TIPO_NO_COMPATIBLE);
        }
    }

    private void procesarIdentificador() {
        Simbolo simbolo = gestorTablas.getUltimoSimbolo();
        if (simbolo.getTipo() == null) {
            GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO, GestorErrores.ERROR_VARIABLE_SIN_INICIALIZAR);
        }
        pilaTipos.add(simbolo.getTipo());
    }

    private void procesarParentesis() {
        Tipo tipoSimbolo = pilaTipos.pop();
        if (tipoSimbolo.equals(Tipo.BOOLEAN)) {
            pilaTipos.add(Tipo.BOOLEAN);
        } else if (tipoSimbolo.equals(Tipo.INT)) {
            pilaTipos.add(Tipo.INT);
        } else if (tipoSimbolo.equals(Tipo.STRING)) {
            pilaTipos.add(Tipo.STRING);
        } else {
            GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO, GestorErrores.ERROR_TIPO_NO_COMPATIBLE);
        }
    }

    private void procesarLlamadaFuncion() {
        Simbolo simboloFuncion = gestorTablas.getUltimoSimboloFuncion();
        verificarLlamadaFuncion(simboloFuncion);
        if (simboloFuncion.getTipo() == null) {
            pilaTipos.add(pilaTipos.peek());
        } else {
            pilaTipos.add(simboloFuncion.getTipo());
        }
    }

    private void procesarLlamadaFuncionConParametros() {
        Simbolo simboloFuncion = gestorTablas.getUltimoSimboloFuncion();
        verificarLlamadaFuncion(simboloFuncion);
        if (simboloFuncion.getTipo() == null) {
            pilaTipos.add(pilaTipos.peek());
        } else {
            pilaTipos.add(simboloFuncion.getTipo());
        }
        pilaTipos.pop();
        pilaTipos.add(Tipo.OK);
    }

    private void procesarAsignacionConOperacion() {
        Tipo tipoSimboloE = pilaTipos.pop();
        Simbolo simbolo = gestorTablas.verPrimerSimboloSinTipo() == null
                ? gestorTablas.getUltimoSimbolo()
                : gestorTablas.verPrimerSimboloSinTipo();
        if (simbolo.getTipo() == null) {
            simbolo = gestorTablas.consumirSimboloSinTipo();
            asignarTipo(simbolo, Tipo.INT, gestorTablas.obtenerTablaActual());
        }
        if (!tipoSimboloE.equals(simbolo.getTipo())) {
            GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO, GestorErrores.ERROR_TIPOS_NO_COINCIDEN);
        }
        pilaTipos.add(Tipo.OK);
    }

    private void procesarPut() {
        Tipo tipoSimboloE = pilaTipos.pop();
        if (tipoSimboloE.equals(Tipo.INT) || tipoSimboloE.equals(Tipo.STRING)) {
            pilaTipos.add(Tipo.OK);
        } else {
            GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO, GestorErrores.ERROR_TIPO_NO_COMPATIBLE);
        }
    }

    private void procesarGet() {
        Simbolo simbolo = gestorTablas.getUltimoSimbolo();
        if (simbolo.getTipo() == null) {
            GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO, GestorErrores.ERROR_VARIABLE_SIN_INICIALIZAR);
        }
        if (simbolo.getTipo().equals(Tipo.INT) || simbolo.getTipo().equals(Tipo.STRING)) {
            pilaTipos.add(simbolo.getTipo());
        } else {
            GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO, GestorErrores.ERROR_TIPO_NO_COMPATIBLE);
        }
    }

    private void procesarReturn() {
        Tipo tipoSimbolo = pilaTipos.peek();
        returnEjecutado = true;
        ifWhileEjecutado = true;
        if (tipoSimbolo == null) {
            GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO, GestorErrores.ERROR_VARIABLE_SIN_INICIALIZAR);
        }
    }

    private void verificarLlamadaFuncion(Simbolo simboloFuncion) {
        if (simboloFuncion == null) {
            GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO,
                    GestorErrores.ERROR_LLAMADA_FUNCION_NO_DECLARADA);
        }
        List<Tipo> parametrosFuncion = obtenerParametrosFuncion(simboloFuncion);
        if (parametrosFuncion.size() != gestorParametros.getListaDeParametros().size()) {
            GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO, GestorErrores.ERROR_NUMERO_PARAMETROS);
        }
        verificarTipoParametros(parametrosFuncion);
        gestorParametros.reset();
    }

    private List<Tipo> obtenerParametrosFuncion(Simbolo simboloFuncion) {
        if (gestorTablas.obtenerTablaActual().getNumeroTabla() > 0 && simboloFuncion.getNumeroParametros() == null) {
            return new ArrayList<>(gestorParametros.getTipoParametrosFuncion());
        } else {
            return simboloFuncion.getTipoParametros();
        }
    }

    private void verificarTipoParametros(List<Tipo> parametrosFuncion) {
        List<Tipo> listaDeParametros = new ArrayList<>(gestorParametros.getListaDeParametros());
        for (int i = 0; i < listaDeParametros.size(); i++) {
            Tipo tipoParametro = listaDeParametros.get(i);
            if (tipoParametro == null || !tipoParametro.equals(parametrosFuncion.get(i))) {
                GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO, GestorErrores.ERROR_TIPO_PARAMETROS);
            }
        }
    }

    private void asignarTipo(Simbolo simbolo, Tipo tipoSimbolo, TablaSimbolos tabla) {
        if (simbolo.getTipo() != null) {
            GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO, GestorErrores.ERROR_VARIABLE_REDECLARADA);
        }
        simbolo.setDesplazamiento(tabla.getDesplazamiento());
        simbolo.setTipo(tipoSimbolo);
        simbolo.setAncho(calcularAncho(tipoSimbolo));
        tabla.setDesplazamiento(tabla.getDesplazamiento() + simbolo.getAncho());
    }

    private int calcularAncho(Tipo tipo) {
        switch (tipo) {
            case STRING:
                return 128;
            case BOOLEAN:
            case INT:
                return 2;
            case VOID:
                return 0;
            default:
                GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO, GestorErrores.ERROR_TIPO_NO_COMPATIBLE);
                return -1; // no se alcanza
        }
    }

    public void resetProcesadorReglas() {
        this.gestorTablas = GestorTablas.getInstance();
        this.pilaTipos = new Stack<>();
        this.gestorParametros = new GestorParametros();
        this.returnEjecutado = false;
        this.ifWhileEjecutado = false;
    }
}
