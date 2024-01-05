package semantico;

import estructuras.*;
import main.Analizador;

import java.util.*;

public class AnalizadorSemantico {

    private Stack<Simbolo> pilaTipos;

    /**
     * Constructor del analizador semántico.
     */
    public AnalizadorSemantico() {
        this.pilaTipos = new Stack<Simbolo>();
    }

    /**
     * Procesa una regla individual.
     * @param numeroRegla El número de la regla a procesar.
     */
    public void procesarRegla(Integer numeroRegla) throws IllegalStateException {

        TablaSimbolos tablaActual = Analizador.gestorTablas.obtenerTablaActual();

        Simbolo simbolo = null;
    
        switch (numeroRegla) {
            case 1:
            case 2:
            case 4:
            case 27:
            case 29:
            case 31:
                break;

            case 3:
                // Poner la zona de declarion a true
                tablaActual.setZonaDeclaracion(true);
                break;

            case 5: 
            case 7:
                // B: IF '(' E ')' S

            case 6:
            case 8:
            case 14:
                // T.tipo = CADENA
                // T.ancho = 128
                simbolo = new SimboloNormal(Tipo.STRING, null, null, 128);
                pilaTipos.add(simbolo);
                break;

            case 36:
                // W.tipo = CADENA
                // W.ancho = 128
                simbolo = new SimboloNormal(Tipo.STRING, null, null, 128);
                pilaTipos.add(simbolo);

                break;

            case 10:
                // B: LET ID T '=' E ';'
                Simbolo SimboloE = pilaTipos.pop();
                Simbolo SimboloT = pilaTipos.pop();

                Tipo tipoSimboloE = SimboloE.getTipo();
                Tipo tipoSimboloT = SimboloT.getTipo();
                
                if (tipoSimboloE == tipoSimboloT && 
                    (tipoSimboloT.equals(Tipo.STRING) || tipoSimboloT.equals(Tipo.BOOLEAN) || tipoSimboloT.equals(Tipo.INT))) {
                    
                    Integer posicion = (Integer) tablaActual.getUltimoIdentificador().getAtributo();

                    SimboloNormal simboloGuardado = (SimboloNormal) tablaActual.obtenerSimbolo(posicion);
                    
                    simboloGuardado.setDesplazamiento(tablaActual.getDesplazamiento());

                    if (tipoSimboloT.equals(Tipo.STRING)) {
                        simboloGuardado.setAncho(128);
                    } 
                    else if (tipoSimboloT.equals(Tipo.BOOLEAN)) {
                        simboloGuardado.setAncho(1);
                    }
                    else if (tipoSimboloT.equals(Tipo.INT)) {
                        simboloGuardado.setAncho(2);
                    }
                    
                    simboloGuardado.setTipo(tipoSimboloT);

                    tablaActual.agregarSimboloNormal(posicion, simboloGuardado);
                }
                else {
                    throw new IllegalStateException("semantico: los tipos no coinciden");
                }

                tablaActual.setZonaDeclaracion(false);
                break;

            default:
                // Manejo de reglas no implementadas o desconocidas
                throw new IllegalStateException("semantico: regla no implementada");
        }
    }
}
