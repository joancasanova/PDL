package sintactico;
import java.io.*;
import java.util.*;

/**
 * Clase TablaAnalisis que se encarga de parsear un archivo de texto generado por Bison.
 * Crea estructuras de datos para ser utilizadas por un analizador sintáctico LR(1).
 */
public class TablaAnalisis {
    private static final String GRAMMAR_SECTION = "Grammar";
    private static final String TERMINALS_SECTION = "Terminals";
    private static final String NONTERMINALS_SECTION = "Nonterminals";
    private static final String STATE_SECTION = "State";
    private static final String FILE_PATH = "sintactico/gramatica.txt";

    private Map<Integer, Map<String, Accion>> actionTable;
    private Map<Integer, Map<String, Integer>> gotoTable;
    private Map<Integer, List<String>> reglas;
    private Set<String> terminals;
    private Set<String> nonTerminals;

    /**
     * Constructor de TablaAnalisis.
     * Inicializa las estructuras de datos y genera la tabla a partir de un archivo.
     */
    public TablaAnalisis() {
        actionTable = new HashMap<>();
        gotoTable = new HashMap<>();
        reglas = new HashMap<>();
        terminals = new HashSet<>();
        nonTerminals = new HashSet<>();

        String filePath = FILE_PATH;
        generateTable(filePath);
    }

    /**
     * Genera las tablas de análisis sintáctico a partir de un archivo de gramática dado.
     * Lee y procesa el archivo para construir las tablas de acción y salto (goto).
     *
     * @param filePath Ruta del archivo de gramática a procesar.
     */
    private void generateTable(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {

                if (line.contains("conflicts") || line.contains("unused") || line.contains("useless")) {
                    continue;
                }

                if (line.startsWith(GRAMMAR_SECTION)) {
                    processGrammar(reader);
                } else if (line.startsWith(TERMINALS_SECTION)) {
                    processTerminals(reader);
                } else if (line.startsWith(NONTERMINALS_SECTION)) {
                    processNonTerminals(reader);
                } else if (line.startsWith(STATE_SECTION)) {
                    processState(reader, line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Procesa la sección de gramática del archivo.
     * Lee y analiza las reglas gramaticales definidas en el archivo.
     *
     * @param reader BufferedReader para leer el archivo.
     * @throws IOException Si ocurre un error de lectura.
     */
    private void processGrammar(BufferedReader reader) throws IOException  {
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
                    String terminalProcesado = processTerminal(string.replaceAll(":", ""));
                    terminalProcesado.replaceAll("\\?", "");
                    contenidoRegla.add(terminalProcesado);
                }
            }

            reglas.put(numeroRegla, contenidoRegla);
        }
    }

    private void processTerminals(BufferedReader reader) throws IOException {
        String line;
        Integer contadorLineasVacias = 0;
        while (contadorLineasVacias < 2) {
            line = reader.readLine();
            if (line.isEmpty()) {
                contadorLineasVacias++;
                continue;
            }

            String terminal = line.split("\\s+")[1];
            terminals.add(processTerminal(terminal));
        }
    }

    private String processTerminal(String terminalSinProcesar) {
        Terminal terminal = Terminal.procesarTerminal(terminalSinProcesar);

        String terminalProcesado = "";

        if (terminal != null) {
            terminalProcesado = terminal.name();
        } else {
            terminalProcesado = terminalSinProcesar.toUpperCase();
        }

        return terminalProcesado;
    }
    

    private void processNonTerminals(BufferedReader reader) throws IOException {
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

            nonTerminals.add(line.split("\\s+")[1]);
        }
    }

    private void processState(BufferedReader reader, String firstLine) throws IOException {
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
                    AccionDesplazar accionDesplazar = new AccionDesplazar(estado);
                    actionMap.put(processTerminal(simbolo), accionDesplazar);
                } else if (parts[1].equals("reduce")) {
                    int regla = Integer.parseInt(parts[4].replaceAll("[^0-9]", ""));
                    String noTerminal = parts[5].replaceAll("\\(|\\)", "");
                    AccionReducir accionReducir = new AccionReducir(regla, noTerminal);
                    actionMap.put(processTerminal(simbolo), accionReducir);
                } else if (parts[1].equals("accept")) {
                    AccionAceptar accionAceptar = new AccionAceptar();
                    actionMap.put(processTerminal(simbolo), accionAceptar);
                } else if (parts[1].equals("go")) {
                    int goToState = Integer.parseInt(parts[4]);
                    gotoMap.put(simbolo, goToState);
                }
            } catch (NumberFormatException e) {
                System.out.println("Skipping invalid line: " + line);
            }
        }

        actionTable.put(stateNumber, actionMap);
        gotoTable.put(stateNumber, gotoMap);
    }

    private void printActionTable() {
        System.out.println("-------------");
        System.out.println("-------------");
        System.out.println("Action Table:");
        for (Map.Entry<Integer, Map<String, Accion>> entry : actionTable.entrySet()) {
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
        System.out.println("-----------");
        System.out.println("-----------");
        System.out.println("Goto Table:");
        for (Map.Entry<Integer, Map<String, Integer>> entry : gotoTable.entrySet()) {
            System.out.println("State " + entry.getKey() + ":");
            for (Map.Entry<String, Integer> goTo : entry.getValue().entrySet()) {
                System.out.println("  " + goTo.getKey() + " -> " + goTo.getValue());
            }
        }
    }

    private void printReglas() {
        System.out.println("--------------");
        System.out.println("--------------");
        for (Map.Entry<Integer, List<String>> entry : reglas.entrySet()) {
            System.out.println("Regla " + entry.getKey() + ":");
            for (String contenidoRegla : entry.getValue()) {
                System.out.print(contenidoRegla + " ");
            }
                System.out.println();
        }
    }

    private void printTerminals() {
        System.out.println("----------");
        System.out.println("----------");
        System.out.println("Terminals:");
        for (String terminal : terminals) {
            System.out.println("  " + terminal);
        }
    }

    private void printNonTerminals() {
        System.out.println("--------------");
        System.out.println("--------------");
        System.out.println("Non-Terminals:");
        for (String nonTerminal : nonTerminals) {
            System.out.println("  " + nonTerminal);
        }
    }

    public Map<Integer,Map<String,Accion>> getActionTable() {
        return this.actionTable;
    }

    public Map<Integer,Map<String,Integer>> getGotoTable() {
        return this.gotoTable;
    }

    public Map<Integer,List<String>> getReglas() {
        return this.reglas;
    }

    public Set<String> getTerminals() {
        return this.terminals;
    }

    public Set<String> getNonTerminals() {
        return this.nonTerminals;
    }

    /**
     * Método principal para probar la clase TablaAnalisis.
     * @param args Argumentos de la línea de comandos.
     */
    public static void main(String[] args) {
        TablaAnalisis generator = new TablaAnalisis();
        String filePath = FILE_PATH;
        generator.generateTable(filePath);
        generator.printActionTable();
        generator.printGotoTable();
        generator.printTerminals();
        generator.printNonTerminals();
        generator.printReglas();
    }
}
