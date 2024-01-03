package sintactico;

import java.io.*;
import java.util.*;

import estructuras.TokenType;

/**
 * Clase que se encarga de parsear un archivo de texto generado por Bison.
 * Crea estructuras de datos para ser utilizadas por un analizador sintáctico LR(1).
 */
public class ParserGramatica {
    private static final String GRAMMAR_SECTION = "Grammar";
    private static final String TERMINALS_SECTION = "Terminals";
    private static final String NONTERMINALS_SECTION = "Nonterminals";
    private static final String STATE_SECTION = "State";
    private static final String FILE_PATH = "sintactico/gramatica.txt";

    private Map<Integer, Map<String, Accion>> tablaAccion;
    private Map<Integer, Map<String, Integer>> tablaGoTo;
    private Map<Integer, List<String>> reglas;
    private Set<String> terminales;
    private Set<String> noTerminales;

    public ParserGramatica() {
        tablaAccion = new HashMap<>();
        tablaGoTo = new HashMap<>();
        reglas = new HashMap<>();
        terminales = new HashSet<>();
        noTerminales = new HashSet<>();

        String filePath = FILE_PATH;
        generateTable(filePath);
    }

    private void generateTable(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {

                if (line.contains("conflicts") || line.contains("unused") || line.contains("useless")) {
                    continue;
                }

                if (line.startsWith(GRAMMAR_SECTION)) {
                    procesarReglas(reader);
                } else if (line.startsWith(TERMINALS_SECTION)) {
                    procesarTerminales(reader);
                } else if (line.startsWith(NONTERMINALS_SECTION)) {
                    procesarNoTerminales(reader);
                } else if (line.startsWith(STATE_SECTION)) {
                    procesarEstado(reader, line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void procesarReglas(BufferedReader reader) throws IOException {
        String line;
        Integer contadorLineasVacias = 0;
        while (contadorLineasVacias < 2) {
            line = reader.readLine();
            if (line.isEmpty()) {
                contadorLineasVacias++;
                continue;
            } else {
                contadorLineasVacias = 0;
            }

            int numeroRegla = Integer.parseInt(line.split("\\s+")[1]);
            List<String> contenidoRegla = new ArrayList<>();
            for (String string : line.split("\\s+")) {
                if (!string.matches("-?\\d+") && !string.isEmpty()) {
                    if (string.equals("|")) {
                        string = reglas.get(numeroRegla - 1).get(0);
                    }
                    String terminalProcesado = procesarTerminal(string.replaceAll(":", ""));
                    terminalProcesado.replaceAll("\\?", "");
                    contenidoRegla.add(terminalProcesado);
                }
            }

            reglas.put(numeroRegla, contenidoRegla);
        }
    }

    private void procesarTerminales(BufferedReader reader) throws IOException {
        String line;
        Integer contadorLineasVacias = 0;
        while (contadorLineasVacias < 2) {
            line = reader.readLine();
            if (line.isEmpty()) {
                contadorLineasVacias++;
                continue;
            }

            String terminalSinProcesar = line.split("\\s+")[1].replace("'", "");

            terminales.add(procesarTerminal(terminalSinProcesar));

            // Añadir los terminales sin las comillas simples
        }
    }

    private String procesarTerminal(String terminalSinProcesar) {
        TokenType tipoToken = TokenType.procesarTipoToken(terminalSinProcesar);

        String terminalProcesado = "";

        if (tipoToken != null) {
            terminalProcesado = tipoToken.name();
        } else { 
            // Caso para palabra reservada
            terminalProcesado = terminalSinProcesar.toUpperCase();
        }

        return terminalProcesado;
    }

    private void procesarNoTerminales(BufferedReader reader) throws IOException {
        String line;
        Integer contadorLineasVacias = 0;
        while (contadorLineasVacias < 2) {
            line = reader.readLine();
            if (line.isEmpty()) {
                contadorLineasVacias++;
                continue;
            } else if (line.contains("left") || line.contains("right")) {
                continue;
            }

            noTerminales.add(line.split("\\s+")[1]);
        }
    }

    private void procesarEstado(BufferedReader reader, String firstLine) throws IOException {
        int stateNumber = Integer.parseInt(firstLine.split("\\s+")[1]);
        Map<String, Accion> actionMap = new HashMap<>();
        Map<String, Integer> gotoMap = new HashMap<>();

        String line;
        Integer contadorLineasVacias = 0;
        while (contadorLineasVacias < 2) {
            line = reader.readLine();
            if (line == null) {
                break;
            }
            if (line.isEmpty()) {
                contadorLineasVacias++;
                continue;
            } else {
                contadorLineasVacias = 0;
            }

            String[] parts = line.trim().split("\\s+");
            String simbolo = parts[0];
            try {
                if (parts[1].equals("shift,")) {
                    int estado = Integer.parseInt(parts[6]);
                    String token = procesarTerminal(parts[0]);
                    AccionDesplazar accionDesplazar = new AccionDesplazar(estado, token);
                    actionMap.put(procesarTerminal(simbolo), accionDesplazar);
                } else if (parts[1].equals("reduce")) {
                    int regla = Integer.parseInt(parts[4].replaceAll("[^0-9]", ""));
                    String noTerminal = parts[5].replaceAll("\\(|\\)", "");
                    Integer numeroDesapilar = reglas.get(regla).size() - 1;
                    AccionReducir accionReducir = new AccionReducir(regla, noTerminal, numeroDesapilar);
                    actionMap.put(procesarTerminal(simbolo), accionReducir);
                } else if (parts[1].equals("accept")) {
                    AccionAceptar accionAceptar = new AccionAceptar();
                    actionMap.put(procesarTerminal(simbolo), accionAceptar);
                } else if (parts[1].equals("go")) {
                    int goToState = Integer.parseInt(parts[4]);
                    gotoMap.put(simbolo, goToState);
                }
            } catch (NumberFormatException e) {
                System.out.println("Skipping invalid line: " + line);
            }
        }

        tablaAccion.put(stateNumber, actionMap);
        tablaGoTo.put(stateNumber, gotoMap);
    }

    private void printActionTable() {
        System.out.println("\nAction Table:");
        for (Map.Entry<Integer, Map<String, Accion>> entry : tablaAccion.entrySet()) {
            System.out.println("Estado " + entry.getKey() + ":");
            for (Map.Entry<String, Accion> action : entry.getValue().entrySet()) {
                System.out.println("  " + action.getKey() + " -> " + action.getValue().getTipo());
                if (action.getValue().getTipo().equals("reducir")) {
                    AccionReducir accionReducir = (AccionReducir) action.getValue();
                    System.out.println("\tNo terminal -----> " + accionReducir.getNoTerminal());
                    System.out.println("\tAplicar regla ---> " + accionReducir.getRegla());
                }
                if (action.getValue().getTipo().equals("desplazar")) {
                    AccionDesplazar accionDesplazar = (AccionDesplazar) action.getValue();
                    System.out.println("\tIr a estado -----> " + accionDesplazar.getEstado());
                }
            }
        }
    }

    private void printGotoTable() {
        System.out.println("\nGoto Table:");
        for (Map.Entry<Integer, Map<String, Integer>> entry : tablaGoTo.entrySet()) {
            System.out.println("State " + entry.getKey() + ":");
            for (Map.Entry<String, Integer> goTo : entry.getValue().entrySet()) {
                System.out.println("  " + goTo.getKey() + " -> " + goTo.getValue());
            }
        }
    }

    private void printReglas() {
        for (Map.Entry<Integer, List<String>> entry : reglas.entrySet()) {
            System.out.println("\nRegla " + entry.getKey() + ":");
            for (String contenidoRegla : entry.getValue()) {
                System.out.print(contenidoRegla + " ");
            }
            System.out.println();
        }
    }

    private void printTerminals() {
        System.out.println("\nTerminals:");
        for (String terminal : terminales) {
            System.out.println("  " + terminal);
        }
    }

    private void printNonTerminals() {
        System.out.println("\nNon-Terminals:");
        for (String nonTerminal : noTerminales) {
            System.out.println("  " + nonTerminal);
        }
    }

    public Map<Integer, Map<String, Accion>> getTablaAccion() {
        return this.tablaAccion;
    }

    public Map<Integer, Map<String, Integer>> getTablaGoTo() {
        return this.tablaGoTo;
    }

    public Map<Integer, List<String>> getReglas() {
        return this.reglas;
    }

    public Set<String> getTerminales() {
        return this.terminales;
    }

    public Set<String> getNoTerminales() {
        return this.noTerminales;
    }

    /**
     * Muestra el contenido de la tabla de analisis.
     * 
     * @param args Argumentos de la línea de comandos.
     */
    public static void main(String[] args) {
        ParserGramatica parserGramatica = new ParserGramatica();
        String filePath = FILE_PATH;
        parserGramatica.generateTable(filePath);

        System.out.println("------------");
        parserGramatica.printActionTable();
        System.out.println("------------");
        parserGramatica.printGotoTable();
        System.out.println("------------");
        parserGramatica.printTerminals();
        System.out.println("------------");
        parserGramatica.printNonTerminals();
        System.out.println("------------");
        parserGramatica.printReglas();
    }
}
