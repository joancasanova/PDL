package sintactico;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import util.Token;
import util.TokenType;

public class AnalizadorSintactico {
    private TablaAnalisis tablaAnalisis;
    private Stack<Integer> pilaEstados;
    private Stack<String> pilaSimbolos;
    private List<Token> tokens;
    private List<Integer> reglasAplicadas;

    public AnalizadorSintactico(TablaAnalisis tablaAnalisis, List<Token> tokens) {
        this.tablaAnalisis = tablaAnalisis;
        this.tokens = tokens;
        this.pilaEstados = new Stack<>();
        this.pilaSimbolos = new Stack<>();
        this.reglasAplicadas = new ArrayList<>();

        // Inicializar la pila con el estado inicial 0
        pilaEstados.push(0);
        pilaSimbolos.push("FINDEFICHERO");

        procesarTokens();
    }

    private void procesarTokens() {
        // Variables para almacenar el token actual
        Token tokenActual;
        String tokenActualProcesado;

        // Variable para almacenar la acción actual
        Accion accionActual = null;

        // Iterar sobre los tokens
        int indiceTokenActual = 0;
        while (indiceTokenActual < tokens.size()) {

            tokenActual = tokens.get(indiceTokenActual);
            if (tokenActual.tipo.equals(TokenType.PALABRARESERVADA)) {
                tokenActualProcesado = String.valueOf(tokenActual.atributo).toUpperCase();
            } else {
                tokenActualProcesado = String.valueOf(tokenActual.getTipo()).toUpperCase();
            }

            Integer estadoCima = pilaEstados.peek();
            Map<String, Accion> accionesEstado = tablaAnalisis.getActionTable().get(estadoCima);
            accionActual = accionesEstado.get(tokenActualProcesado);
            if (accionActual == null) {
                accionActual = accionesEstado.get("$DEFAULT");
            }    

            if (accionActual instanceof AccionDesplazar) {
                desplazar((AccionDesplazar) accionActual, tokenActualProcesado);
                indiceTokenActual++; // Avanzar al siguiente token
            } else if (accionActual instanceof AccionReducir) {
                reducir((AccionReducir) accionActual);
                // No avanzar al siguiente token en caso de reducción, ya que el token actual necesita ser reevaluado
            } else if (accionActual instanceof AccionAceptar) {
                aceptar();
                break; // Finalizar el procesamiento en caso de aceptar
            } else {
                System.err.println("Error de análisis sintáctico en el token " + tokenActualProcesado + " en el estado " + pilaEstados.peek());
                break;
            }    
            System.out.print("\tPila Estados: ");
            for (Integer integer : pilaEstados) {
                System.out.print(integer + ", ");
            }
            System.out.println();

            System.out.print("\tPila Simbolos: ");
            for (String string : pilaSimbolos) {
                System.out.print(string + ", ");
            }
            System.out.println();
        }
    }

    private void desplazar(AccionDesplazar accion, String token) {
        System.out.println("Desplazar: " + accion.getEstado() + ", Token: " + token);

        pilaEstados.push(accion.getEstado());
        pilaSimbolos.push(token); // Apilar el simbolo terminal
    }

    private void reducir(AccionReducir accion) {
        System.out.println("Reducir: ");
        System.out.println("\tRegla: " + accion.getRegla());
        System.out.println("\tNo Terminal: " + accion.getNoTerminal());
    
        List<String> regla = tablaAnalisis.getReglas().get(accion.getRegla());

        // La cantidad de elementos a desapilar es igual a la longitud de la regla menos uno (excluyendo el lado izquierdo)
        int cantidadDesapilar = regla.size() - 1;
        // Desapila la cantidad calculada de estados de una sola vez
        while (cantidadDesapilar > 0) {
            pilaEstados.pop();
            pilaSimbolos.pop(); // Desapilar los simbolos correspondientes a la regla
            cantidadDesapilar--;
        }
    
        // Agrega el estado correspondiente al no terminal de la regla
        String noTerminal = accion.getNoTerminal();
        pilaSimbolos.push(noTerminal); // Apilar el simbolo no terminal
        try {
            int estado = tablaAnalisis.getGotoTable().get(pilaEstados.peek()).get(noTerminal);
            pilaEstados.push(estado);            
        } catch (NullPointerException e) {
                System.err.println("Error de análisis sintáctico. No existe el no terminal: " + noTerminal + " en el estado " + pilaEstados.peek() + " para la tabla de goto");
        }
        // Registro de la regla aplicada
        reglasAplicadas.add(accion.getRegla());
    }    

    private void aceptar() {
        System.out.println("Aceptar");
        // Implementar la lógica de aceptación
    }

    public List<Integer> getReglasAplicadas() {
        return reglasAplicadas;
    }
}
