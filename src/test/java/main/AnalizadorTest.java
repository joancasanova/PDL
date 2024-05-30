package main;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.stream.Stream;
import java.io.IOException;

public class AnalizadorTest {

    private static final String TEST_DIR = "src/test/java/archivosTest/";

    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    private void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    private void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    private void runTest(String inputFile, boolean esCorrecto) throws IOException {
        setUpStreams();
        try {
            Analizador.main(new String[] { inputFile });
            String output = outContent.toString();
            String errorOutput = errContent.toString();
            if (!esCorrecto) {
                if (errorOutput.isEmpty()) {
                    fail("Se esperaba una excepción para el archivo: " + inputFile
                            + ", pero el análisis se completó sin errores: " + output);
                } else {
                    System.out.println("Error output for file " + inputFile + ": " + errorOutput);
                }
            } else {
                if (!errorOutput.isEmpty()) {
                    fail("No se esperaba una excepción para el archivo: " + inputFile + ", pero se encontró error: "
                            + errorOutput);
                }
                assertTrue(output.contains("completo"),
                        "Esperado analisis completo para el archivo: " + inputFile + ", pero se obtuvo: " + output);
            }
        } catch (Exception e) {
            if (esCorrecto) {
                fail("\n\nTest: " + inputFile + "\nNo se esperaba una excepción, pero se lanzó: "
                        + e.getLocalizedMessage());
            }
        } finally {
            restoreStreams();
        }
    }

    static Stream<Arguments> provideFiles() throws IOException {
        return Files.list(new File(TEST_DIR).toPath())
                .filter(Files::isRegularFile)
                .map(path -> {
                    String filename = path.getFileName().toString();
                    boolean esCorrecto = filename.contains("correcto");
                    return arguments(TEST_DIR + filename, esCorrecto);
                });
    }

    @ParameterizedTest
    @MethodSource("provideFiles")
    public void testFiles(String inputFile, boolean esCorrecto) throws IOException {
        runTest(inputFile, esCorrecto);
    }

}
