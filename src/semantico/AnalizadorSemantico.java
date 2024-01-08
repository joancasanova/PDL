package semantico;

import main.Analizador;
import tablaSimbolos.*;

import java.util.*;

public class AnalizadorSemantico {

    private ArrayList<Tipo> tipoParametrosFuncion = new ArrayList<>();
    private ArrayList<Modo> modoPasoParametros = new ArrayList<>();

    private ArrayList<Tipo> listaDeParametros = new ArrayList<>();

    private Stack<Tipo> pilaTipos;

    /**
     * Constructor del analizador semántico.
     */
    public AnalizadorSemantico() {
        this.pilaTipos = new Stack<Tipo>();
    }

    /**
     * Procesa una regla individual.
     * 
     * @param numeroRegla El número de la regla a procesar.
     */
    public void procesarRegla(Integer numeroRegla) throws IllegalStateException, NullPointerException {

        TablaSimbolos tablaActual = Analizador.gestorTablas.obtenerTablaActual();

        Integer posicion = 0;

        // System.out.println(numeroRegla);

        switch (numeroRegla) {
            case 1:
                // Aceptar
                Analizador.gestorTablas.destruirTabla();
                break;

            case 2: // P: B P
            case 3: // P: F P
            case 4: // P: lambda
            case 18: // H: T
            case 23: // K: lambda
            case 27: // E: U
            case 29: // U: V
            case 31: // V: W
            case 44: // L: lambda
            case 46: // Q: lambda
            case 47: // Z: E
            case 48: // Z: lambda
                break;

            case 5: // B: IF ( E ) S
            case 6: // B: WHILE ( E ) S
            case 7: // B: IF ( E ) { S }
            case 8: // B: WHILE ( E ) { S }

                Tipo tipoRetornoS = pilaTipos.pop();
                Tipo tipoSimboloE = pilaTipos.pop();

                if (!tipoSimboloE.equals(Tipo.BOOLEAN)) {
                    throw new IllegalStateException("semantico: expresion deberia ser de tipo boolean");
                } else {
                    if (tipoRetornoS != null) {
                        pilaTipos.add(tipoRetornoS);
                    }
                }

                break;

            case 9: // B: LET ID T
                Tipo TipoSimboloT = pilaTipos.pop();
                asignarTipo(TipoSimboloT, tablaActual);
                break;

            case 10: // B: LET ID T = E ;
                tipoSimboloE = pilaTipos.pop();
                TipoSimboloT = pilaTipos.pop();

                if (tipoSimboloE == TipoSimboloT) {
                    asignarTipo(TipoSimboloT, tablaActual);
                } else {
                    throw new IllegalStateException("semantico: los tipos no coinciden");
                }

                pilaTipos.add(Tipo.OK);

                break;

            case 11:
                // B: S
                Tipo TipoRetornoS = pilaTipos.pop();

                if (TipoRetornoS != null) {
                    // Se ha ejecutado un return
                    pilaTipos.add(TipoRetornoS);
                }

                break;

            case 12: // T: INT
                pilaTipos.add(Tipo.INT);
                break;

            case 13: // T: BOOLEAN
                pilaTipos.add(Tipo.BOOLEAN);
                break;

            case 14: // T: STRING
                pilaTipos.push(Tipo.STRING);
                break;

            case 15: // F: F1 { C }
                Tipo tipoRetornoC = pilaTipos.pop();
                Tipo tipoRetornoF1 = pilaTipos.pop();

                if (!(tipoRetornoF1.equals(tipoRetornoC))) {
                    if (!(tipoRetornoF1.equals(Tipo.VOID) && tipoRetornoC == Tipo.OK)) {
                        throw new IllegalStateException(
                            "semantico: Tipo retorno de la funcion y tipo de funcion no coinciden");
                    }
                } else {
                    // No hay problemas con los tipos devueltos
                    // Eliminar tabla de simbolos local
                    Analizador.gestorTablas.destruirTabla();

                    // Actualizar la tabla de simbolos actual
                    tablaActual = Analizador.gestorTablas.obtenerTablaActual();
                    posicion = (Integer) tablaActual.getUltimoIdentificador().getAtributo();
                    Simbolo simboloGuardado = tablaActual.obtenerSimbolo(posicion);
                    simboloGuardado.setTipo(Tipo.FUNCTION);
                    simboloGuardado.setNumeroParametros((Integer) tipoParametrosFuncion.size());
                    simboloGuardado.setTipoRetorno(tipoRetornoF1);

                    ArrayList<Tipo> copiaTipoParametros = new ArrayList<>(tipoParametrosFuncion);
                    ArrayList<Modo> copiaModoPasoParametros = new ArrayList<>(modoPasoParametros);
                    simboloGuardado.setTipoParametros(copiaTipoParametros);
                    simboloGuardado.setModoPaso(copiaModoPasoParametros);

                    tipoParametrosFuncion.clear();
                    modoPasoParametros.clear();
                }

                break;

            case 16: // F1: F2 ( A )
                // A no esta en la pila porque no se necesita
                Tipo tipoSimboloF2 = pilaTipos.pop();
                pilaTipos.push(tipoSimboloF2);
                break;

            case 17: // F2: FUNCTION ID H
                Tipo tipoSimboloH = pilaTipos.pop();
                pilaTipos.add(tipoSimboloH);
                Analizador.gestorTablas.nuevaTabla();
                break;

            case 19: // H: VOID
                pilaTipos.add(Tipo.VOID);
                break;

            case 20: // A: T ID K
            case 22: // K: , T ID K

                TipoSimboloT = pilaTipos.pop();
                asignarTipo(TipoSimboloT, tablaActual);

                // Añadir a ambas listas los tipos de los parametros
                tipoParametrosFuncion.add(TipoSimboloT);
                modoPasoParametros.add(Modo.VALOR);

                break;

            case 21: // A: VOID
                pilaTipos.add(Tipo.VOID);
                break;

            case 24: // C: B C
                Tipo TipoRetornoC = pilaTipos.pop();
                Tipo TipoRetornoB = pilaTipos.pop();

                if (TipoRetornoB != Tipo.OK && TipoRetornoC != Tipo.OK) {
                    throw new IllegalStateException(
                            "semantico: Hay un error con el tipo de retorno de la función");
                } else {
                    if (TipoRetornoB != Tipo.OK) {
                        pilaTipos.add(TipoRetornoB);
                    } else {
                        pilaTipos.add(TipoRetornoC);
                    }
                }
                break;

            case 25: // C: lambda
                pilaTipos.add(Tipo.OK);
                break;

            case 26: // E: E1 == U
                Tipo tipoSimboloE1 = pilaTipos.pop();
                Tipo tipoSimboloU = pilaTipos.pop();

                if (tipoSimboloE1 == tipoSimboloU && tipoSimboloE1.equals(Tipo.INT)) {
                    pilaTipos.add(Tipo.BOOLEAN);
                } else {
                    throw new IllegalStateException("semantico: los tipos en la expresion E: E == U no coinciden");
                }
                break;

            case 28: // U: U1 + V
                Tipo tipoSimboloV = pilaTipos.pop();
                Tipo tipoSimboloU1 = pilaTipos.pop();

                if (tipoSimboloU1 == tipoSimboloV && tipoSimboloU1.equals(Tipo.INT)) {
                    pilaTipos.add(Tipo.INT);
                } else {
                    throw new IllegalStateException("semantico: los tipos en la expresion no coinciden");

                }
                break;

            case 30: // V: !W
                Tipo tipoSimboloW = pilaTipos.pop();

                if (tipoSimboloW.equals(Tipo.BOOLEAN)) {
                    pilaTipos.add(Tipo.BOOLEAN);
                } else {
                    throw new IllegalStateException("semantico: los tipos en la expresion E: E == U no coinciden");
                }
                break;

            case 32: // W: ID

                if (tablaActual.getZonaAsignacion()) {
                    posicion = (Integer) tablaActual.getIdentificadorEnZonaAsignacion().getAtributo();
                } else {
                    posicion = (Integer) tablaActual.getUltimoIdentificador().getAtributo();
                }

                Simbolo simboloGuardado = (Simbolo) tablaActual.obtenerSimbolo(posicion);
                pilaTipos.add(simboloGuardado.getTipo());
                break;

            case 33: // W: ( E )

                Tipo TipoSimboloE = pilaTipos.pop();

                if (TipoSimboloE.equals(Tipo.BOOLEAN)) {
                    pilaTipos.add(Tipo.BOOLEAN);// Añado el simbolo W a al pila
                } else if (TipoSimboloE.equals(Tipo.INT)) {
                    pilaTipos.add(Tipo.INT);// Añado el simobolo W a la pila
                } else {
                    throw new IllegalStateException("semantico en expresion-> W: (E) tipo de E incorrecto");
                }
                break;

            case 34: // W: ID ( L )
            case 39: // S: ID ( L ) ;
                if (tablaActual.getZonaAsignacion()) {
                    posicion = (Integer) tablaActual.getIdentificadorEnZonaAsignacion().getAtributo();
                } else {
                    posicion = (Integer) tablaActual.getUltimoIdentificador().getAtributo();
                }
                simboloGuardado = (Simbolo) tablaActual.obtenerSimbolo(posicion);

                // Comprobar que cada parametro de L coincide con los de la funcion
                List<Tipo> parametrosFuncion = simboloGuardado.getTipoParametros();
                int numParam = simboloGuardado.getNumeroParametros();

                int i = 0;
                for (Tipo tipo : parametrosFuncion) {
                    if(numParam<i) {
                        throw new IllegalStateException("semantico: el numero de parametros no corresponde con los de la funcion");
                    }
                    if(!tipo.equals(listaDeParametros.get(i))) {
                        throw new IllegalStateException("semantico: los tipos de los parametros no coinciden con los de la funcion");
                    }
                    i++;
                }

                pilaTipos.add(simboloGuardado.getTipo());
                break;

            case 35: // W: ENTERO
                pilaTipos.add(Tipo.INT);
                break;

            case 36: // W: CADENA
                pilaTipos.add(Tipo.STRING);
                break;

            case 37: // S: ID = E ;
            case 38: // S: ID += E ;
                TipoSimboloE = pilaTipos.pop();

                posicion = (Integer) tablaActual.getUltimoIdentificador().getAtributo();
                simboloGuardado = (Simbolo) tablaActual.obtenerSimbolo(posicion);

                if (simboloGuardado.getTipo() == null) {
                    throw new IllegalStateException("semantico: variable no inicializada en la expresion S: ID = E;");
                }

                if (TipoSimboloE.equals(simboloGuardado.getTipo())) {
                    pilaTipos.add(Tipo.OK); // Simbolo S
                } else {
                    throw new IllegalStateException("semantico en expresion-> S: ID=E no coiniden tipos");
                }
                break;

            case 40: // S: PUT E ;
                tipoSimboloE = pilaTipos.pop();

                if (tipoSimboloE.equals(Tipo.BOOLEAN)) {
                    pilaTipos.add(Tipo.BOOLEAN);// Añado el simbolo S a al pila
                } else if (tipoSimboloE.equals(Tipo.INT) || tipoSimboloE.equals(Tipo.BOOLEAN)
                        || tipoSimboloE.equals(Tipo.STRING)) {
                    pilaTipos.add(Tipo.OK);// Añado el simobolo S a la pila
                } else {
                    throw new IllegalStateException("semantico en expresion-> W: (E) tipo de E incorrecto");
                }
                break;

            case 41: // S: GET ID ;
                posicion = (Integer) tablaActual.getUltimoIdentificador().getAtributo();
                simboloGuardado = (Simbolo) tablaActual.obtenerSimbolo(posicion);

                if (simboloGuardado.getTipo() == null) {
                    throw new IllegalStateException("semantico: variable no inicializada en la expresion S: GET ID;");
                }

                if (simboloGuardado.getTipo().equals(Tipo.INT) || simboloGuardado.getTipo().equals(Tipo.STRING)) {
                    pilaTipos.add(simboloGuardado.getTipo());// S tiene el simbolo de id
                } else {
                    throw new IllegalStateException(
                            "semantico en expresion-> S: GET ID; tipo de id no es valido, debe ser entero o string");
                }
                break;

            case 42: // S: RETURN Z ;
                Tipo tipoSimboloZ = pilaTipos.pop();

                if (tipoSimboloZ == null) {
                    throw new IllegalStateException(
                            "semantico: variable no inicializada en la expresion S: RETURN Z;");
                }
                pilaTipos.add(tipoSimboloZ);
                break;

            case 43: // L: E Q 
            case 45: // Q: , E Q
                listaDeParametros.add(pilaTipos.pop());
                break;      

            default:
                // Manejo de reglas no implementadas o desconocidas
                throw new IllegalStateException("semantico: regla no implementada");
        }

        // System.out.println("CONTENIDO PILA: ");
        // for (Tipo tipo : pilaTipos) {
        //    System.out.println("-" + tipo);
        //}
    }

    private void asignarTipo(Tipo TipoSimboloT, TablaSimbolos tablaActual) {

        int posicion = (Integer) tablaActual.getUltimoIdentificador().getAtributo();
        Simbolo simboloGuardado = (Simbolo) tablaActual.obtenerSimbolo(posicion);
        simboloGuardado.setDesplazamiento(tablaActual.getDesplazamiento());
        simboloGuardado.setTipo(TipoSimboloT);

        if (TipoSimboloT.equals(Tipo.STRING)) {
            simboloGuardado.setAncho(128);
        } else if (TipoSimboloT.equals(Tipo.BOOLEAN)) {
            simboloGuardado.setAncho(2);
        } else if (TipoSimboloT.equals(Tipo.INT)) {
            simboloGuardado.setAncho(2);
        } else {
            throw new IllegalStateException("semantico: tipo no aceptado");
        }
        
        tablaActual.setDesplazamiento(tablaActual.getDesplazamiento() + simboloGuardado.getAncho());

    }

}
