import java.io.*;
import java.util.*;

public class TablaAnalisis {
    private Map<Integer, Map<String, String>> actionTable;
    private Map<Integer, Map<String, Integer>> gotoTable;
    private Set<String> terminals;
    private Set<String> nonTerminals;

    public TablaAnalisis() {
        actionTable = new HashMap<>();
        gotoTable = new HashMap<>();
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

                if (line.startsWith("Terminals")) {
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
        Map<String, String> actionMap = new HashMap<>();
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
            String symbol = parts[0];
            try {
                if (parts[1].equals("shift,")) {
                    int goToState = Integer.parseInt(parts[6]);
                    actionMap.put(symbol, "s" + goToState);
                } else if (parts[1].equals("reduce")) {
                    int ruleNumber = Integer.parseInt(parts[4].replaceAll("[^0-9]", ""));
                    actionMap.put(symbol, "r" + ruleNumber);
                } else if (parts[1].equals("go")) {
                    int goToState = Integer.parseInt(parts[4]);
                    gotoMap.put(symbol, goToState);
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
        for (Map.Entry<Integer, Map<String, String>> entry : actionTable.entrySet()) {
            System.out.println("State " + entry.getKey() + ":");
            for (Map.Entry<String, String> action : entry.getValue().entrySet()) {
                System.out.println("  " + action.getKey() + " -> " + action.getValue());
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
    }
}
