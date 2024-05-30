package semantico;

import tablaSimbolos.*;
import tablaSimbolos.enums.Modo;
import tablaSimbolos.enums.Tipo;
import util.GestorErrores;

import java.util.*;

public class AnalizadorSemantico {

    // Gestor de tablas de simbolos
    private GestorTablas gestorTablas;

    private ArrayList<Tipo> tipoParametrosFuncion = new ArrayList<>();
    private ArrayList<Modo> modoPasoParametros = new ArrayList<>();
    private ArrayList<Tipo> listaDeParametros = new ArrayList<>();
    private Stack<Tipo> pilaTipos;
    private Boolean returnEjecutado;
    private Boolean ifWhileEjecutado;
    private Tipo tipoReturnIfWhile;

    // Instancia única de la clase
    private static AnalizadorSemantico instancia;

    /**
     * Constructor privado del analizador semántico.
     */
    private AnalizadorSemantico() {
        this.gestorTablas = GestorTablas.getInstance();
        this.pilaTipos = new Stack<>();
        this.returnEjecutado = false;
        this.ifWhileEjecutado = false;
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
    public void procesarRegla(Integer numeroRegla) throws IllegalStateException, NullPointerException {

        TablaSimbolos tablaActual = gestorTablas.obtenerTablaActual();

        Simbolo simbolo;
        Tipo tipoSimbolo, tipoSimboloE, tipoSimboloT, tipoSimboloS, tipoSimboloE1, tipoSimboloU, tipoSimboloV,
                tipoSimboloU1, TipoRetornoC, TipoRetornoB, TipoRetornoS, tipoRetornoC, tipoRetornoF1;

        switch (numeroRegla) {
            case 1: // Aceptar
                gestorTablas.destruirTabla();
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
                ifWhileEjecutado = true;

                tipoSimboloS = pilaTipos.pop();
                tipoSimboloE = pilaTipos.pop();

                if (!tipoSimboloE.equals(Tipo.BOOLEAN)) {
                    GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO, GestorErrores.ERROR_TIPO_BOOLEAN);
                }

                if (returnEjecutado) {
                    tipoReturnIfWhile = tipoSimboloS;
                }

                pilaTipos.add(Tipo.OK);

                break;

            case 7: // B: LET ID T ;
                tipoSimbolo = pilaTipos.pop();

                simbolo = gestorTablas.consumirSimboloSinTipo();
                asignarTipo(simbolo, tipoSimbolo, tablaActual);
                pilaTipos.add(Tipo.OK);

                break;

            case 8: // B: LET ID T = E ;
                tipoSimboloE = pilaTipos.pop();
                tipoSimboloT = pilaTipos.pop();

                if (tipoSimboloE == tipoSimboloT) {
                    simbolo = gestorTablas.consumirSimboloSinTipo();
                    asignarTipo(simbolo, tipoSimboloT, tablaActual);
                } else {
                    GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO,
                            GestorErrores.ERROR_TIPOS_NO_COINCIDEN);
                }
                pilaTipos.add(Tipo.OK);

                break;

            case 9: // B: S
                TipoRetornoS = pilaTipos.pop();

                if (TipoRetornoS != null) {
                    // Se ha ejecutado un return
                    pilaTipos.add(TipoRetornoS);
                }

                break;

            case 10: // T: INT
                pilaTipos.add(Tipo.INT);
                break;

            case 11: // T: BOOLEAN
                pilaTipos.add(Tipo.BOOLEAN);
                break;

            case 12: // T: STRING
                pilaTipos.push(Tipo.STRING);
                break;

            case 13: // F: F1 { C }
                tipoRetornoC = pilaTipos.pop();
                tipoRetornoF1 = pilaTipos.pop();

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

                // No hay problemas con los tipos devueltos
                // Eliminar tabla de simbolos local
                gestorTablas.destruirTabla();
                returnEjecutado = false;
                ifWhileEjecutado = false;
                break;

            case 14: // F1: F2 ( A )
                gestorTablas.setZonaParametros(false);

                // Obtener el simbolo de la funcion y asignar el numero de parametros y tipos
                simbolo = gestorTablas.getUltimoSimboloFuncion();
                simbolo.setNumeroParametros((Integer) tipoParametrosFuncion.size());

                Collections.reverse(tipoParametrosFuncion);
                Collections.reverse(modoPasoParametros);
                ArrayList<Tipo> copiaTipoParametros = new ArrayList<>(tipoParametrosFuncion);
                ArrayList<Modo> copiaModoPasoParametros = new ArrayList<>(modoPasoParametros);
                simbolo.setTipoParametros(copiaTipoParametros);
                simbolo.setModoPaso(copiaModoPasoParametros);

                tipoParametrosFuncion.clear();
                modoPasoParametros.clear();
                break;

            case 15: // F2: FUNCTION ID H
                gestorTablas.setZonaParametros(true);
                gestorTablas.nuevaTabla();
                returnEjecutado = false;
                ifWhileEjecutado = false;

                tipoSimbolo = pilaTipos.peek();
                simbolo = gestorTablas.consumirSimboloSinTipo();
                simbolo.setNumeroParametros(0);
                simbolo.setTipoRetorno(tipoSimbolo);
                asignarTipo(simbolo, tipoSimbolo, gestorTablas.obtenerTablaGlobal());
                break;

            case 17: // H: VOID
                pilaTipos.add(Tipo.VOID);
                break;

            case 18: // A: T ID K
            case 20: // K: , T ID K

                tipoSimbolo = pilaTipos.pop();

                simbolo = gestorTablas.consumirSimboloSinTipo();
                asignarTipo(simbolo, tipoSimbolo, tablaActual);

                // Añadir a ambas listas los tipos de los parametros
                tipoParametrosFuncion.add(tipoSimbolo);
                modoPasoParametros.add(Modo.VALOR);

                break;

            case 22: // C: B C
                TipoRetornoC = pilaTipos.pop();
                TipoRetornoB = pilaTipos.pop();

                if (TipoRetornoB != Tipo.OK && TipoRetornoC != Tipo.OK) {
                    GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO,
                            GestorErrores.ERROR_TIPO_RETORNO_FUNCION);
                } else {
                    if (TipoRetornoB != Tipo.OK) {
                        pilaTipos.add(TipoRetornoB);
                    } else {
                        pilaTipos.add(TipoRetornoC);
                    }
                }
                break;

            case 23: // C: lambda
                pilaTipos.add(Tipo.OK);
                break;

            case 24: // E: E1 == U
                tipoSimboloE1 = pilaTipos.pop();
                tipoSimboloU = pilaTipos.pop();

                if (tipoSimboloE1 == tipoSimboloU && tipoSimboloE1.equals(Tipo.INT)) {
                    pilaTipos.add(Tipo.BOOLEAN);
                } else {
                    GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO,
                            GestorErrores.ERROR_TIPOS_NO_COINCIDEN);
                }
                break;

            case 26: // U: U1 + V
                tipoSimboloU1 = pilaTipos.pop();
                tipoSimboloV = pilaTipos.pop();

                if (tipoSimboloU1 == null || tipoSimboloV == null) {
                    GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO,
                            GestorErrores.ERROR_VARIABLE_SIN_INICIALIZAR);
                }
                if (tipoSimboloU1 == tipoSimboloV && tipoSimboloU1.equals(Tipo.INT)) {
                    pilaTipos.add(Tipo.INT);
                } else {
                    GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO,
                            GestorErrores.ERROR_TIPOS_NO_COINCIDEN);
                }
                break;

            case 28: // V: !W
                tipoSimbolo = pilaTipos.pop();

                if (tipoSimbolo.equals(Tipo.BOOLEAN)) {
                    pilaTipos.add(Tipo.BOOLEAN);
                } else {
                    GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO,
                            GestorErrores.ERROR_TIPO_NO_COMPATIBLE);
                }
                break;

            case 30: // W: ID
                simbolo = gestorTablas.getUltimoSimbolo();
                if (simbolo.getTipo() == null) {
                    GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO,
                            GestorErrores.ERROR_VARIABLE_SIN_INICIALIZAR);
                }
                pilaTipos.add(simbolo.getTipo());
                break;

            case 31: // W: ( E )
                tipoSimbolo = pilaTipos.pop();

                if (tipoSimbolo.equals(Tipo.BOOLEAN)) {
                    pilaTipos.add(Tipo.BOOLEAN);
                } else if (tipoSimbolo.equals(Tipo.INT)) {
                    pilaTipos.add(Tipo.INT);
                } else if (tipoSimbolo.equals(Tipo.STRING)) {
                    pilaTipos.add(Tipo.STRING);
                } else {
                    GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO,
                            GestorErrores.ERROR_TIPO_NO_COMPATIBLE);
                }
                break;

            case 32: // W: ID ( L )
                int numeroParametros;
                List<Tipo> parametrosFuncion;
                Simbolo simboloFuncion = null;

                // Buscar el simbolo de la funcion en la tabla anterior
                simboloFuncion = gestorTablas.getUltimoSimboloFuncion();

                // Estamos llamando a una funcion no declarada
                if (simboloFuncion == null) {
                    GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO,
                            GestorErrores.ERROR_LLAMADA_FUNCION_NO_DECLARADA);
                }

                // Dentro de la misma funcion
                if (tablaActual.getNumeroTabla() > 0 && simboloFuncion.getNumeroParametros() == null) {
                    numeroParametros = tipoParametrosFuncion.size();
                    parametrosFuncion = tipoParametrosFuncion;
                }

                // Fuera de función o función diferente
                else {
                    numeroParametros = simboloFuncion.getNumeroParametros();
                    parametrosFuncion = simboloFuncion.getTipoParametros();
                }

                // Comparar que el numero de parametros coinciden
                if (numeroParametros != listaDeParametros.size()) {
                    GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO,
                            GestorErrores.ERROR_NUMERO_PARAMETROS);
                }

                // Comparar que el tipo de parametros coinciden
                if (tablaActual.getNumeroTabla() == -1) {
                    Collections.reverse(listaDeParametros);
                }
                int i = 0;
                for (Tipo tipo : listaDeParametros) {
                    if (tipo == null) {
                        GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO,
                                GestorErrores.ERROR_TIPO_PARAMETROS);
                    }
                    if (!tipo.equals(parametrosFuncion.get(i))) {
                        GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO,
                                GestorErrores.ERROR_TIPO_PARAMETROS);
                    }
                    i++;
                }
                listaDeParametros.clear();

                if (simboloFuncion.getTipo() == null) {
                    pilaTipos.add(pilaTipos.peek());
                } else {
                    pilaTipos.add(simboloFuncion.getTipo());
                }

                break;

            case 37: // S: ID ( L ) ;

                simboloFuncion = null;

                // Buscar el simbolo de la funcion en la tabla anterior
                simboloFuncion = gestorTablas.getUltimoSimboloFuncion();

                // Estamos llamando a una funcion no declarada
                if (simboloFuncion == null) {
                    GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO,
                            GestorErrores.ERROR_LLAMADA_FUNCION_NO_DECLARADA);
                }

                // Dentro de la misma funcion
                if (tablaActual.getNumeroTabla() > 0 && simboloFuncion.getNumeroParametros() == null) {
                    numeroParametros = tipoParametrosFuncion.size();
                    parametrosFuncion = tipoParametrosFuncion;
                }

                // Fuera de función o función diferente
                else {
                    numeroParametros = simboloFuncion.getNumeroParametros();
                    parametrosFuncion = simboloFuncion.getTipoParametros();
                }

                // Comparar que el numero de parametros coinciden
                if (numeroParametros != listaDeParametros.size()) {
                    GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO,
                            GestorErrores.ERROR_NUMERO_PARAMETROS);
                }

                // Comparar que el tipo de parametros coinciden
                if (tablaActual.getNumeroTabla() == -1) {
                    Collections.reverse(listaDeParametros);
                }
                if (tablaActual.getNumeroTabla() != -1) {
                    i = listaDeParametros.size() - 1;
                } else {
                    i = 0;
                }
                for (Tipo tipo : listaDeParametros) {
                    if (tipo == null) {
                        GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO,
                                GestorErrores.ERROR_TIPO_PARAMETROS);
                    }
                    if (!tipo.equals(parametrosFuncion.get(i))) {
                        GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO,
                                GestorErrores.ERROR_TIPO_PARAMETROS);
                    }
                    if (tablaActual.getNumeroTabla() != -1) {
                        i--;
                    } else {
                        i++;
                    }
                }
                listaDeParametros.clear();

                if (simboloFuncion.getTipo() == null) {
                    pilaTipos.add(pilaTipos.peek());
                } else {
                    pilaTipos.add(simboloFuncion.getTipo());
                }
                pilaTipos.pop();
                pilaTipos.add(Tipo.OK);

                break;

            case 33: // W: ENTERO
                pilaTipos.add(Tipo.INT);
                break;

            case 34: // W: CADENA
                pilaTipos.add(Tipo.STRING);
                break;

            case 35: // S: ID = E ;
            case 36: // S: ID += E ;
                tipoSimboloE = pilaTipos.pop();

                if (gestorTablas.verPrimerSimboloSinTipo() == null) {
                    simbolo = gestorTablas.getUltimoSimbolo();
                } else {
                    simbolo = gestorTablas.verPrimerSimboloSinTipo();
                }

                // Asignar el tipo INT al identificador sin inicializar explicitamente
                // (inicialización implícita)
                if (simbolo.getTipo() == null) {
                    simbolo = gestorTablas.consumirSimboloSinTipo();
                    asignarTipo(simbolo, Tipo.INT, tablaActual);
                }
                if (!tipoSimboloE.equals(simbolo.getTipo())) {
                    GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO,
                            GestorErrores.ERROR_TIPOS_NO_COINCIDEN);
                }

                pilaTipos.add(Tipo.OK);

                break;

            case 38: // S: PUT E ;
                tipoSimboloE = pilaTipos.pop();

                if (tipoSimboloE.equals(Tipo.INT)
                        || tipoSimboloE.equals(Tipo.STRING)) {
                    pilaTipos.add(Tipo.OK);// Añado el simbolo S a la pila
                } else {
                    GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO,
                            GestorErrores.ERROR_TIPO_NO_COMPATIBLE);
                }
                break;

            case 39: // S: GET ID ;
                simbolo = gestorTablas.getUltimoSimbolo();

                if (simbolo.getTipo() == null) {
                    GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO,
                            GestorErrores.ERROR_VARIABLE_SIN_INICIALIZAR);
                }

                if (simbolo.getTipo().equals(Tipo.INT) || simbolo.getTipo().equals(Tipo.STRING)) {
                    pilaTipos.add(simbolo.getTipo());
                } else {
                    GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO,
                            GestorErrores.ERROR_TIPO_NO_COMPATIBLE);
                }
                break;

            case 40: // S: RETURN Z ;
                tipoSimbolo = pilaTipos.peek();

                returnEjecutado = true;
                ifWhileEjecutado = true;

                if (tipoSimbolo == null) {
                    GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO,
                            GestorErrores.ERROR_VARIABLE_SIN_INICIALIZAR);
                }

                break;

            case 41: // L: E Q
            case 43: // Q: , E Q
                listaDeParametros.add(pilaTipos.pop());
                break;

            case 46: // Z: lambda
                pilaTipos.add(Tipo.VOID);
                break;

            default:
                // Manejo de reglas no implementadas o desconocidas
                GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO, GestorErrores.ERROR_REGLA_NO_IMPLEMENTADA);
        }
    }

    private void asignarTipo(Simbolo simbolo, Tipo tipoSimbolo, TablaSimbolos tabla) throws IllegalStateException {
        try {
            if (simbolo.getTipo() != null) {
                GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO, GestorErrores.ERROR_VARIABLE_REDECLARADA);
            }

            // Asignar atributos al simbolo
            simbolo.setDesplazamiento(tabla.getDesplazamiento());
            simbolo.setTipo(tipoSimbolo);
            if (tipoSimbolo.equals(Tipo.STRING)) {
                simbolo.setAncho(128);
            } else if (tipoSimbolo.equals(Tipo.BOOLEAN)) {
                simbolo.setAncho(2);
            } else if (tipoSimbolo.equals(Tipo.INT)) {
                simbolo.setAncho(2);
            } else if (tipoSimbolo.equals(Tipo.VOID)) {
                simbolo.setAncho(0);
            } else {
                GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO, GestorErrores.ERROR_TIPO_NO_COMPATIBLE);
            }

            tabla.setDesplazamiento(tabla.getDesplazamiento() + simbolo.getAncho());
        } catch (Exception e) {
            GestorErrores.lanzarError(GestorErrores.TipoError.SEMANTICO, e.getLocalizedMessage());
        }
    }

    /**
     * Reinicia el analizador semantico a su estado inicial.
     */
    public void resetAnalizadorSemantico() {
        this.gestorTablas = GestorTablas.getInstance();
        this.pilaTipos = new Stack<>();
        this.returnEjecutado = false;
        this.ifWhileEjecutado = false;
    }
}
