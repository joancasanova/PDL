package semantico;

import estructuras.*;
import main.Analizador;

import java.util.*;

public class AnalizadorSemantico {

    private Stack<SimboloNormal> pilaTipos;

    /**
     * Constructor del analizador semántico.
     */
    public AnalizadorSemantico() {
        this.pilaTipos = new Stack<SimboloNormal>();
    }

    /**
     * Procesa una regla individual.
     * @param numeroRegla El número de la regla a procesar.
     */
    public void procesarRegla(Integer numeroRegla) throws IllegalStateException {

        TablaSimbolos tablaActual = Analizador.gestorTablas.obtenerTablaActual();

        ArrayList<Tipo> tipoParametrosFuncion=new ArrayList<>();

        ArrayList<Modo> modoPasoParametros=new ArrayList<>();

        Integer posicion = 0;
        SimboloNormal simboloGuardado= null;

        SimboloNormal simbolo = null;

        SimboloNormal SimboloE = null;
        Tipo TipoSimboloE = null;
        SimboloNormal SimboloS = null;
        Tipo TipoRetornoS =null;

        SimboloNormal SimboloT = null;
        Tipo TipoSimboloT = null;
        SimboloNormal SimboloC = null;
        SimboloNormal SimboloB = null;
    
        switch (numeroRegla) {
            case 1:
                // Aceptar ?
            break;

            case 2:
                // P: B P
            break;

            case 3:
                // P: F P
            break;

            case 4:
                // P: lambda
            break;

            case 5:
                // B: IF ( E ) S
                SimboloS = pilaTipos.pop();
                SimboloE = pilaTipos.pop();
                Tipo tipoRetornoS = SimboloS.getTipoRetorno();
                Tipo tipoSimboloE = SimboloE.getTipo();

                if (!tipoSimboloE.equals(Tipo.BOOLEAN)){
                    throw new IllegalStateException("Error semantico: expresion deberia ser de tipo boolean");
                } else {

                    if(tipoRetornoS != null){
                        // Se ha jecutado un return
                        simbolo = new SimboloNormal(Tipo.OK, null, null, 0);
                        simbolo.setTipoRetorno(tipoRetornoS);
                        pilaTipos.add(simbolo);

                    } 
                }

            break;

            case 6:
                // B: WHILE ( E ) S -> Estos son iguales que el caso 5, Juntar todos mas adelante
            break;

            case 7: 
                // B: IF ( E ) { S }
            break;

            case 8:
                // B: WHILE ( E ) { S }
            break;

            case 9:
                // B: LET ID T
                SimboloT = pilaTipos.pop();
                TipoSimboloT = SimboloT.getTipo();

                posicion = (Integer) tablaActual.getUltimoIdentificador().getAtributo();

                simboloGuardado = (SimboloNormal) tablaActual.obtenerSimbolo(posicion);

                    if(simboloGuardado == null) {
                        throw new IllegalStateException("Error semantico: No existe identificador en expresion LET ID T;");
                    } else {

                    simboloGuardado.setDesplazamiento(tablaActual.getDesplazamiento());

                    if (TipoSimboloT.equals(Tipo.STRING)) {
                        simboloGuardado.setAncho(128);
                    } 
                    else if (TipoSimboloT.equals(Tipo.BOOLEAN)) {
                        simboloGuardado.setAncho(1);
                    }
                    else if (TipoSimboloT.equals(Tipo.INT)) {
                        simboloGuardado.setAncho(4);
                    }
                    
                    simboloGuardado.setTipo(TipoSimboloT);

                    tablaActual.agregarSimboloNormal(posicion, simboloGuardado);

                    }
            break;

            case 10:
                // B: LET ID T = E ;
                SimboloE = pilaTipos.pop();
                SimboloT = pilaTipos.pop();

                tipoSimboloE = SimboloE.getTipo();
                TipoSimboloT = SimboloT.getTipo();
                
                if (tipoSimboloE == TipoSimboloT && 
                    (TipoSimboloT.equals(Tipo.STRING) || TipoSimboloT.equals(Tipo.BOOLEAN) || TipoSimboloT.equals(Tipo.INT))) {
                    
                    posicion = (Integer) tablaActual.getUltimoIdentificador().getAtributo();

                    simboloGuardado = (SimboloNormal) tablaActual.obtenerSimbolo(posicion);
                    
                    simboloGuardado.setDesplazamiento(tablaActual.getDesplazamiento());

                    if (TipoSimboloT.equals(Tipo.STRING)) {
                        simboloGuardado.setAncho(128);
                    } 
                    else if (TipoSimboloT.equals(Tipo.BOOLEAN)) {
                        simboloGuardado.setAncho(1);
                    }
                    else if (TipoSimboloT.equals(Tipo.INT)) {
                        simboloGuardado.setAncho(4);
                    }
                    
                    simboloGuardado.setTipo(TipoSimboloT);

                    tablaActual.agregarSimboloNormal(posicion, simboloGuardado);
                }
                else {
                    throw new IllegalStateException("semantico: los tipos no coinciden");
                }

                tablaActual.setZonaDeclaracion(false);
            break;

            case 11:
                // B: S
                SimboloS = pilaTipos.pop();
                TipoRetornoS = SimboloS.getTipoRetorno();

                if(TipoRetornoS != null){
                    // Se ha jecutado un return
                simbolo = new SimboloNormal(Tipo.OK, null, null, 0);
                simbolo.setTipoRetorno(TipoRetornoS);
                pilaTipos.add(simbolo);

                } 

            break;

            case 12: 
                // T: INT
                // T.tipo = ENTERO
                // T.ancho = 4?
                simbolo = new SimboloNormal(Tipo.INT, null, null, 4);
                pilaTipos.add(simbolo);
            break;

            case 13:
                // T: BOOLEAN
                // T.tipo = BOOLEAN
                // T.ancho = 1
                simbolo = new SimboloNormal(Tipo.BOOLEAN, null, null, 1);
                pilaTipos.add(simbolo);
            break;

            case 14:
                // T: STRING
                // T.tipo = CADENA
                // T.ancho = 128
                simbolo = new SimboloNormal(Tipo.STRING, null, null, 128);
                pilaTipos.add(simbolo);

            break;

            case 15:
                // F: F1 { C }
                SimboloC = pilaTipos.pop();
                Simbolo SimboloF1 = pilaTipos.pop();
                Tipo  tipoRetornoC = SimboloC.getTipoRetorno();
                Tipo tipoSimboloF1 = SimboloF1.getTipo();

                if(!tipoRetornoC.equals(tipoSimboloF1) || ( tipoSimboloF1.equals(Tipo.VOID) && tipoRetornoC != null )){
                    throw new IllegalStateException("Error semantico: Tipo retorno de la funcion y tipo de funcion no coinciden");
                } else {
                    //  No hay problemas con los tipos devueltos
                    // Eliminar abla de simbolos local
                    Analizador.gestorTablas.destruirTabla();
                    // Actualizar la tabla de simbolos actual
                    tablaActual=Analizador.gestorTablas.obtenerTablaActual();

                    posicion = (Integer) tablaActual.getUltimoIdentificador().getAtributo();//identificador de la funcion
                    simboloGuardado = (SimboloNormal) tablaActual.obtenerSimbolo(posicion);
                    simboloGuardado.setTipoDevuelto(tipoSimboloF1);

                    tipoParametrosFuncion.clear();
                    modoPasoParametros.clear();
                }

            break;

            case 16:
                // F1: F2 ( A )
                // A no esta en la pila porque no se necesita
                SimboloNormal SimboloF2 =  pilaTipos.pop();
                Tipo tipoSimboloF2 = SimboloF2.getTipo();

                simbolo = new SimboloNormal(tipoSimboloF2, SimboloF2.getNombre(), (Integer) tipoParametrosFuncion.size(), tipoParametrosFuncion, modoPasoParametros, SimboloF2.getTipoDevuelto());
                pilaTipos.add(simbolo);

                SimboloF2.setTipoParametros(tipoParametrosFuncion);

                tablaActual.setZonaDeclaracion(false);// Ciero la zona de declaracion tras los parametros


            break;

            case 17: 
                // F2: FUNCTION ID H
                SimboloNormal SimboloH = pilaTipos.pop();
                Tipo tipoSimboloH = SimboloH.getTipo();
                // INSERTAR EN TS IDENTIFICADOR DE LA FUNCION CO TIPO FUNCION
                String nombreid = (String) tablaActual.getUltimoIdentificador().getAtributo();// Obtiene el valor del token ID
  
                simbolo = new SimboloNormal(Tipo.FUNCTION, nombreid, 0, null, null, tipoSimboloH);
                pilaTipos.add(simbolo);

                Analizador.gestorTablas.nuevaTabla();// Crea tabla
                tablaActual = Analizador.gestorTablas.obtenerTablaActual();// Al crearla se pone el desplazamiento a 0
                tablaActual.setZonaDeclaracion(true); // Poner la zona de declaracion a  true


            break;

            case 18:
                // H: T, No se hace nada, se deja el valor de T en la pila
            break;

            case 19:
                // H: VOID
                // T.tipo = VACIO
                // T.ancho = 1
                simbolo = new SimboloNormal(Tipo.VOID, null, null, 1);
                pilaTipos.add(simbolo);
            break;

            case 20:
                // A: T ID K
                SimboloT = pilaTipos.pop();
                TipoSimboloT = SimboloT.getTipo();

                posicion = (Integer) tablaActual.getUltimoIdentificador().getAtributo();
                simboloGuardado = (SimboloNormal) tablaActual.obtenerSimbolo(posicion);
                simboloGuardado.setDesplazamiento(tablaActual.getDesplazamiento());

                 if (TipoSimboloT.equals(Tipo.STRING)) {
                        simboloGuardado.setAncho(128);
                    } 
                    else if (TipoSimboloT.equals(Tipo.BOOLEAN)) {
                        simboloGuardado.setAncho(1);
                    }
                    else if (TipoSimboloT.equals(Tipo.INT)) {
                        simboloGuardado.setAncho(4);
                    }
                    
                    simboloGuardado.setTipo(TipoSimboloT);

                    // Añadir a ambas listas los tipos de los parametros
                    tipoParametrosFuncion.add(TipoSimboloT);
                    modoPasoParametros.add(Modo.VALOR);

                    tablaActual.agregarSimboloNormal(posicion, simboloGuardado);

            break;

            case 21:
                // A: VOID
                // T.tipo = VACIO
                // T.ancho = 1
                simbolo = new SimboloNormal(Tipo.VOID, null, null, 1);
                pilaTipos.add(simbolo);
            break;

            case 22: 
                // K: , T ID K
                SimboloT = pilaTipos.pop();
                TipoSimboloT = SimboloT.getTipo();

                posicion = (Integer) tablaActual.getUltimoIdentificador().getAtributo();
                simboloGuardado = (SimboloNormal) tablaActual.obtenerSimbolo(posicion);
                simboloGuardado.setDesplazamiento(tablaActual.getDesplazamiento());

                 if (TipoSimboloT.equals(Tipo.STRING)) {
                        simboloGuardado.setAncho(128);
                    } 
                    else if (TipoSimboloT.equals(Tipo.BOOLEAN)) {
                        simboloGuardado.setAncho(1);
                    }
                    else if (TipoSimboloT.equals(Tipo.INT)) {
                        simboloGuardado.setAncho(4);
                    }
                    
                    simboloGuardado.setTipo(TipoSimboloT);
                    

                    // Añadir a ambas listas los tipos de los parametros
                    tipoParametrosFuncion.add(TipoSimboloT);
                    modoPasoParametros.add(Modo.VALOR);

                    tablaActual.agregarSimboloNormal(posicion, simboloGuardado);
            break;

            case 23:
                // K: lambda
            break;

            case 24:
                // C: B C
                SimboloC = pilaTipos.pop();
                SimboloB = pilaTipos.pop();

                Tipo TipoRetornoC1 = SimboloC.getTipoRetorno();
                Tipo TipoRetornoB = SimboloB.getTipoRetorno();

                if(TipoRetornoB != null && TipoRetornoC1 != null) {
                    throw new IllegalStateException("Error semantico: Hay varios tipos de retorno en la funcion (varios return)\n");
                } else {

                simbolo = new SimboloNormal(Tipo.OK, null, null, 0);

                if(TipoRetornoB != null){
                    simbolo.setTipoRetorno(TipoRetornoB);
                    } else {
                    simbolo.setTipoRetorno(TipoRetornoC1);
                    }

                pilaTipos.add(simbolo);
                
                }

            break;

            case 25:
                // C: lambda
            break;

            case 26:
                // E: E1 == U
                Simbolo SimboloE1 = pilaTipos.pop();
                Simbolo SimboloU = pilaTipos.pop();
                
                Tipo tipoSimboloE1 = SimboloE1.getTipo();
                Tipo tipoSimboloU = SimboloU.getTipo();

                if (tipoSimboloE1 == tipoSimboloU  && tipoSimboloE1.equals(Tipo.INT)) {
                    
                simbolo = new SimboloNormal(Tipo.BOOLEAN, null, null, 1);
                pilaTipos.add(simbolo);

                } else {

                    throw new IllegalStateException("Error semantico: los tipos en la expresion E: E == U no coinciden\n");

                }

            break;

            case 27: 
                // E: U
            break;

            case 28:
                // U: U1 + V
                Simbolo SimboloU1 = pilaTipos.pop();
                Simbolo SimboloV = pilaTipos.pop();
                
                Tipo tipoSimboloU1 = SimboloU1.getTipo();
                Tipo tipoSimboloV = SimboloV.getTipo();

                if (tipoSimboloU1 == tipoSimboloV  && tipoSimboloU1.equals(Tipo.INT)) {
                    
                simbolo = new SimboloNormal(Tipo.INT, null, null, 1);
                pilaTipos.add(simbolo);

                } else {

                    throw new IllegalStateException("Error semantico: los tipos en la expresion E: E == U no coinciden\n");

                }

            break;

            case 29:
                // U: V
            break;

            case 30:
                // V: !W
                Simbolo SimboloW = pilaTipos.pop();
                
                Tipo tipoSimboloW = SimboloW.getTipo();

                if (tipoSimboloW.equals(Tipo.BOOLEAN)) {
                    
                    simbolo = new SimboloNormal(Tipo.BOOLEAN, null, null, 1);
                    pilaTipos.add(simbolo);

                } else {

                    throw new IllegalStateException("Error semantico: los tipos en la expresion E: E == U no coinciden\n");

                }

            break;

            case 31:
                // V: W
            break;

            case 32: 
                // W: ID
                posicion = (Integer) tablaActual.getUltimoIdentificador().getAtributo();
                simboloGuardado = (SimboloNormal) tablaActual.obtenerSimbolo(posicion);

                simbolo = new SimboloNormal(simboloGuardado.getTipo(), null, null, simboloGuardado.getAncho());
                pilaTipos.add(simbolo);
            break;

            case 33:
                // W: ( E )

                SimboloE = pilaTipos.pop();
                TipoSimboloE = SimboloE.getTipo();

                if ( TipoSimboloE.equals(Tipo.BOOLEAN)){

                    simbolo = new SimboloNormal(Tipo.BOOLEAN, null, null, 1);
                    pilaTipos.add(simbolo);// Añado el simbolo W a al pila

                } else if(TipoSimboloE.equals(Tipo.INT)) {

                    simbolo = new SimboloNormal(Tipo.INT, null, null, 4);// COMPROBAR SI EL ANCHO DE UN ENTERO ES 4 O 2
                    pilaTipos.add(simbolo);// Añado el simobolo W a la pila

                } else {

                     throw new IllegalStateException("Error semantico en expresion-> W: (E) tipo de E incorrecto\n");

                }

            break;

            case 34:
                // W: ID ( L ) -> IGUAL QUE W: ID JUNTAR MAS ADELANTE
                posicion = (Integer) tablaActual.getUltimoIdentificador().getAtributo();
                simboloGuardado = (SimboloNormal) tablaActual.obtenerSimbolo(posicion);

                simbolo = new SimboloNormal(simboloGuardado.getTipo(), null, null, simboloGuardado.getAncho());
                pilaTipos.add(simbolo);
            break;

            case 35:
                // W: ENTERO
                // W.tipo = ENTERO
                // W.ancho = 4
                simbolo = new SimboloNormal(Tipo.STRING, null, null, 4);
                pilaTipos.add(simbolo);
            break;

            case 36:
                // W: CADENA
                // W.tipo = CADENA
                // W.ancho = 128
                simbolo = new SimboloNormal(Tipo.STRING, null, null, 128);
                pilaTipos.add(simbolo);
            break;

            case 37:
                // S: ID = E ;
                SimboloE = pilaTipos.pop();
                TipoSimboloE = SimboloE.getTipo();

                posicion = (Integer) tablaActual.getUltimoIdentificador().getAtributo();
                simboloGuardado = (SimboloNormal) tablaActual.obtenerSimbolo(posicion);

                if (simboloGuardado.getTipo() == null)
                    throw new IllegalStateException("Error semantico: variable no inicializada en la expresion S: ID = E;\n");

                if ( TipoSimboloE.equals(simboloGuardado.getTipo()) ){
                    simbolo = new SimboloNormal(Tipo.OK, null, null, null);
                    pilaTipos.add(simbolo); // Simbolo S
                } else {

                    throw new IllegalStateException("Error semantico en expresion-> S: ID=E no coiniden tipos\n");
                }

            break;

            case 38:
                // S: ID += E ; IGUAL QUE S: ID = E; JUNTAR MAS ADELANTE
                SimboloE = pilaTipos.pop();
                TipoSimboloE = SimboloE.getTipo();

                posicion = (Integer) tablaActual.getUltimoIdentificador().getAtributo();
                simboloGuardado = (SimboloNormal) tablaActual.obtenerSimbolo(posicion);

                if (simboloGuardado.getTipo() == null) {
                    throw new IllegalStateException("Error semantico: variable no inicializada en la expresion S: ID = E;\n");
                }

                if ( TipoSimboloE.equals(simboloGuardado.getTipo()) ){
                    simbolo = new SimboloNormal(Tipo.OK, null, null, null);
                    pilaTipos.add(simbolo);
                } else {

                    throw new IllegalStateException("Error semantico en expresion-> S: ID=E no coiniden tipos\n");
                }
            break;

            case 39:
                // S: ID ( L ) ; IGUAL QUE W: ID(L) JUNTAR MAS ADELANTE
                posicion = (Integer) tablaActual.getUltimoIdentificador().getAtributo();
                simboloGuardado = (SimboloNormal) tablaActual.obtenerSimbolo(posicion);

                simbolo = new SimboloNormal(simboloGuardado.getTipo(), null, null, simboloGuardado.getAncho());
                pilaTipos.add(simbolo);

            break;

            case 40:
                // S: PUT E ;
                SimboloE = pilaTipos.pop();
                tipoSimboloE = SimboloE.getTipo();

                if ( tipoSimboloE.equals(Tipo.BOOLEAN)){

                    simbolo = new SimboloNormal(Tipo.BOOLEAN, null, null, 1);
                    pilaTipos.add(simbolo);// Añado el simbolo S a al pila

                } else if(tipoSimboloE.equals(Tipo.INT) || tipoSimboloE.equals(Tipo.BOOLEAN) || tipoSimboloE.equals(Tipo.STRING)) {

                    simbolo = new SimboloNormal(Tipo.OK, null, null, 0);// COMPROBAR SI EL ANCHO DE UN ENTERO ES 4 O 2
                    pilaTipos.add(simbolo);// Añado el simobolo S a la pila

                } else {

                    throw new IllegalStateException("Error semantico en expresion-> W: (E) tipo de E incorrecto\n");

                }

            break;

            case 41:
                // S: GET ID ;
                posicion = (Integer) tablaActual.getUltimoIdentificador().getAtributo();
                simboloGuardado = (SimboloNormal) tablaActual.obtenerSimbolo(posicion);

                if (simboloGuardado.getTipo() == null){
                    throw new IllegalStateException("Error semantico: variable no inicializada en la expresion S: GET ID;\n");
                }

                if (simboloGuardado.getTipo().equals(Tipo.INT) || simboloGuardado.getTipo().equals(Tipo.STRING)) {
                    simbolo = new SimboloNormal(simboloGuardado.getTipo(), null, null, simboloGuardado.getAncho());
                    pilaTipos.add(simbolo);// S tiene el simbolo de id
                } else {
                    throw new IllegalStateException("Error semantico en expresion-> S: GET ID; tipo de id no es valido, debe ser entero o string\n");
                }
                
            break;

            case 42: 
                // S: RETURN Z ;
                SimboloNormal SimboloZ = pilaTipos.pop();
                Tipo tipoSimboloZ = SimboloZ.getTipo();

                if (tipoSimboloZ == null){
                    throw new IllegalStateException("Error semantico: variable no inicializada en la expresion S: RETURN Z;\n");
                }

                simbolo = new SimboloNormal(Tipo.OK, null, null, 0);
                simbolo.setTipoRetorno(tipoSimboloZ);
                pilaTipos.add(simbolo);

            break;

            case 43:
                // L: E Q
                // Solo saco de l apila los valroes de la expresion  E
                // Valores de Q no se han guardado 
                pilaTipos.pop();
                
            break;

            case 44:
                // L: lambda
            break;

            case 45:
                // Q: , E Q -> IGUAL QUE L: E Q; UNIR MAS ADELANTE
                // Solo saco de la apila los valroes de la expresion  E, si ha habid error ya ha saltado y no se comprueba
                // Valores de Q no se han guardado 
                pilaTipos.pop();
                // No es necesario el valor de Q
            break;

            case 46:
                // Q: lambda
            break;

            case 47: 
                // Z: E
                // No se hace nad aporqueZ deja en la pila el mismo tipo que sea E
                
            break;

            case 48: 
                // Z: lambda
            break;

            default:
                // Manejo de reglas no implementadas o desconocidas
                throw new IllegalStateException("semantico: regla no implementada");
        }
    }
}
