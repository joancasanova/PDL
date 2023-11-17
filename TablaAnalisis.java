import java.io.*;
import java.util.*;

import AnalizadorSintactico.*;

public class TablaAnalisis {
    private Map<Integer, Map<String, Accion>> actionTable;
    private Map<Integer, Map<String, Integer>> gotoTable;
    private Map<Integer, List<String>> reglas;
    private Set<String> terminals;
    private Set<String> nonTerminals;

    public TablaAnalisis() {
        actionTable = new HashMap<>();
        gotoTable = new HashMap<>();
        reglas = new HashMap<>();
        terminals = new HashSet<>();
        nonTerminals = new HashSet<>();
    }

    public void generateTable(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {

                if (line.contains("conflicts") || line.contains("unused") || line.contains("useless")) {
                    continue;
                }

                if (line.startsWith("Grammar")) {
                    processGrammar(reader);
                } else if (line.startsWith("Terminals")) {
                    processTerminals(reader);
                } else if (line.startsWith("Nonterminals")) {
                    processNonTerminals(reader);
                } else if (line.startsWith("State")) {
                    processState(reader, line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processGrammar(BufferedReader reader) throws IOException  {
        String line;
        Integer contador_lineas_vacias = 0;
        while (contador_lineas_vacias < 2) {
            line = reader.readLine();
            if (line.isEmpty()) {
                contador_lineas_vacias++;
                continue;
            } else {
                contador_lineas_vacias = 0;
            }

            int numeroRegla = Integer.parseInt(line.split("\\s+")[1]);
            List<String> contenidoRegla = new ArrayList<>();
            for (String string : line.split("\\s+")) {
                if (!string.matches("-?\\d+") && !string.isEmpty()) {
                    if (string.equals("|")) {
                        string = reglas.get(numeroRegla - 1).get(0);
                    }
                    contenidoRegla.add(string.replaceAll(":", ""));
                }
            }

            reglas.put(numeroRegla, contenidoRegla);
        }
    }

    private void processTerminals(BufferedReader reader) throws IOException {
        String line;
        Integer contador_lineas_vacias = 0;
        while (contador_lineas_vacias < 2) {
            line = reader.readLine();
            if (line.isEmpty()) {
                contador_lineas_vacias++;
                continue;
            }
            terminals.add(line.split("\\s+")[1]);
        }
    }

    private void processNonTerminals(BufferedReader reader) throws IOException {
        String line;
        Integer contador_lineas_vacias = 0;
        while (contador_lineas_vacias < 2) {
            line = reader.readLine();
            if (line.isEmpty()) {
                contador_lineas_vacias++;
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
        Integer contador_lineas_vacias = 0;
        while (contador_lineas_vacias < 5) {
            line = reader.readLine();
            if (line == null) {
                break;
            }
            if (line.isEmpty()) {
                contador_lineas_vacias++;
                continue;
            }
            
            String[] parts = line.trim().split("\\s+");
            String simbolo = parts[0];
            try {
                if (parts[1].equals("shift,")) {
                    int estado = Integer.parseInt(parts[6]);
                    AccionDesplazar accionDesplazar = new AccionDesplazar(estado);
                    actionMap.put(simbolo, accionDesplazar);
                } else if (parts[1].equals("reduce")) {
                    int regla = Integer.parseInt(parts[4].replaceAll("[^0-9]", ""));
                    String noTerminal = parts[5].replaceAll("\\(|\\)", "");
                    AccionReducir accionReducir = new AccionReducir(regla, noTerminal);
                    actionMap.put(simbolo, accionReducir);
                } else if (parts[1].equals("accept")) {
                    AccionAceptar accionAceptar = new AccionAceptar();
                    actionMap.put(simbolo, accionAceptar);
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

    public static void main(String[] args) {
        TablaAnalisis generator = new TablaAnalisis();
        String filePath = "gramatica.txt";
        generator.generateTable(filePath);
        generator.printActionTable();
        generator.printGotoTable();
        generator.printTerminals();
        generator.printNonTerminals();
        generator.printReglas();
    }
}
