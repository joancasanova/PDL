import java.io.*;
import java.util.*;

public class TablaAnalisis {
    private Map<Integer, Map<String, String>> actionTable;
    private Map<Integer, Map<String, Integer>> gotoTable;
    private List<String> terminals;
    private List<String> nonTerminals;

    public TablaAnalisis() {
        actionTable = new HashMap<>();
        gotoTable = new HashMap<>();
        terminals = new ArrayList<>();
        nonTerminals = new ArrayList<>();
    }

public void generateTable(String filePath) {
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("Terminals")) {
                processTerminals(reader);
            } else if (line.startsWith("Nonterminals")) {
                processNonTerminals(reader);
            } else if (line.startsWith("State")) {
                processState(reader, line);
            }
            // Continuar con otras secciones si las hay
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}

private void processTerminals(BufferedReader reader) throws IOException {
    String line;
    while (!(line = reader.readLine()).isEmpty()) {
        terminals.add(line.trim());
    }
}

private void processNonTerminals(BufferedReader reader) throws IOException {
    String line;
    while (!(line = reader.readLine()).isEmpty()) {
        nonTerminals.add(line.trim());
    }
}

private void processState(BufferedReader reader, String firstLine) throws IOException {
    // Extraemos el número del estado de la primera línea
    int stateNumber = Integer.parseInt(firstLine.split(" ")[1]);
    Map<String, String> actionMap = new HashMap<>();
    Map<String, Integer> gotoMap = new HashMap<>();

    String line;
    while (!(line = reader.readLine()).trim().isEmpty()) {
        // Divide la línea en partes para analizarlas
        String[] parts = line.split("\\s+");
        String symbol = parts[1]; // El símbolo es la segunda parte (shift, reduce, etc.)

        if (symbol.equals("$default")) {
            // Maneja el caso especial del $default
            String action = parts[3];
            actionMap.put("$default", action);
        } else if (parts.length >= 5 && (parts[3].equals("shift,") || parts[3].equals("shift"))) {
            // Si es una acción de desplazamiento
            int goToState = Integer.parseInt(parts[parts.length - 1]);
            actionMap.put(symbol, "s" + goToState);
        } else if (parts.length >= 3 && (parts[2].equals("reduce") || parts[2].equals("reduce,"))) {
            // Si es una acción de reducción
            int ruleNumber = Integer.parseInt(parts[parts.length - 1].replaceAll("[()]", ""));
            actionMap.put(symbol, "r" + ruleNumber);
        } else {
            // Para las entradas de la tabla GOTO
            int goToState = Integer.parseInt(parts[parts.length - 1]);
            gotoMap.put(symbol, goToState);
        }
    }

    // Agregar los mapas a las tablas generales
    actionTable.put(stateNumber, actionMap);
    gotoTable.put(stateNumber, gotoMap);
}
}