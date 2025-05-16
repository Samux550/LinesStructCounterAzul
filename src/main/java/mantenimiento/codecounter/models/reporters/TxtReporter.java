package mantenimiento.codecounter.models.reporters;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import static mantenimiento.codecounter.models.comparators.STATUS.*;

import mantenimiento.codecounter.demo.LineRecord;
import mantenimiento.codecounter.models.comparators.STATUS;
import mantenimiento.codecounter.utils.LineSplitter;

/**
 * Genera reportes en formato TXT con los resultados de la comparación de archivos Java.
 * Crea un archivo por cada clase analizada y proporciona estadísticas globales de cambios.
 */
public class TxtReporter {
    private Map<String, List<LineRecord>> report;
    private Path outputDirectory;
    private Map<STATUS, Integer> globalChangeCounts;

    /**
     * Constructor que inicializa el generador de reportes.
     * @param report Mapa con los datos de comparación de archivos (nombre → lista de cambios)
     */
    public TxtReporter(Map<String, List<LineRecord>> report) {
        this.report = report;
        this.outputDirectory = requestOutputDirectory();
        this.globalChangeCounts = initializeChangeCounts();
    }

    /**
     * Inicializa el contador de cambios con valores en cero para cada tipo de estado.
     * @return Mapa con los contadores inicializados
     */
    private Map<STATUS, Integer> initializeChangeCounts() {
        Map<STATUS, Integer> counts = new HashMap<>();
        for (STATUS status : values()) {
            counts.put(status, 0);
        }
        return counts;
    }

    /**
     * Solicita al usuario el directorio donde se guardarán los reportes.
     * Crea el directorio si no existe.
     * @return Ruta absoluta del directorio de salida
     */
    private Path requestOutputDirectory() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nIngrese la ruta del directorio para guardar los reportes:");
            String dirPath = scanner.nextLine().trim();
            
            try {
                Path dir = Paths.get(dirPath);
                if (!Files.exists(dir)) {
                    Files.createDirectories(dir);
                }
                return dir.toAbsolutePath();
            } catch (IOException e) {
                System.err.println("Error al acceder al directorio: " + e.getMessage());
                System.err.println("Por favor, ingrese una ruta válida.");
            }
        }
    }

    /**
     * Genera los reportes en formato TXT para todos los archivos analizados.
     * Crea un archivo por cada clase y muestra estadísticas globales al finalizar.
     */
    public void generateTxtReports() {
        Map<String, List<Map.Entry<String, List<LineRecord>>>> groupedFiles = groupFilesByName();

        for (Map.Entry<String, List<Map.Entry<String, List<LineRecord>>>> group : groupedFiles.entrySet()) {
            String baseFileName = group.getKey();
            List<Map.Entry<String, List<LineRecord>>> versions = group.getValue();

            Path outputFile = outputDirectory.resolve(baseFileName + "_report.txt");

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile.toFile()))) {
                if (versions.size() == 1) {
                    writeSingleFileReport(writer, versions.get(0));
                    countChanges(versions.get(0).getValue());
                } else {
                    writeComparisonReport(writer, versions);
                    versions.forEach(version -> countChanges(version.getValue()));
                }
                System.out.println("Reporte generado: " + outputFile);
            } catch (IOException e) {
                System.err.println("Error al escribir reporte para " + baseFileName + ": " + e.getMessage());
            }
        }

        printGlobalChangeSummary();
    }

    /**
     * Actualiza los contadores globales de cambios con las líneas procesadas.
     * @param records Lista de registros de líneas a contar
     */
    private void countChanges(List<LineRecord> records) {
        records.forEach(record -> {
            globalChangeCounts.compute(record.status(), (k, v) -> v + 1);
        });
    }

    /**
     * Imprime en consola un resumen con las estadísticas globales de cambios.
     */
    private void printGlobalChangeSummary() {
        System.out.println("\n=== RESUMEN GLOBAL DE CAMBIOS ===");
        System.out.println("Total de cambios en todo el proyecto:");
        
        System.out.printf("Nuevas líneas (NEW): %d%n", globalChangeCounts.getOrDefault(NEW, 0));
        System.out.printf("Líneas modificadas (MODIFIED): %d%n", globalChangeCounts.getOrDefault(MODIFIED, 0));
        System.out.printf("Líneas eliminadas (DELETED): %d%n", globalChangeCounts.getOrDefault(DELETED, 0));
        System.out.printf("Líneas sin cambios (ORIGINAL): %d%n", globalChangeCounts.getOrDefault(ORIGINAL, 0));
        
        long totalChanges = globalChangeCounts.values().stream().mapToLong(Integer::longValue).sum();
        System.out.printf("\nTotal de líneas procesadas: %d%n", totalChanges);
    }

    /**
     * Agrupa los archivos por su nombre base (eliminando indicadores de versión).
     * @return Mapa con los archivos agrupados por nombre
     */
    private Map<String, List<Map.Entry<String, List<LineRecord>>>> groupFilesByName() {
        return report.entrySet().stream()
            .collect(Collectors.groupingBy(entry -> {
                String fileName = entry.getKey();
                return fileName.replaceAll(" \\[Version: [AB]\\]", "");
            }));
    }

    /**
     * Genera el reporte para un archivo único (sin versión comparada).
     * @param writer BufferedWriter para escribir el archivo
     * @param fileEntry Entrada con los datos del archivo
     * @throws IOException Si ocurre un error al escribir el archivo
     */
    private void writeSingleFileReport(BufferedWriter writer, 
                                 Map.Entry<String, List<LineRecord>> fileEntry) throws IOException {
    String fileName = fileEntry.getKey();
    List<LineRecord> records = fileEntry.getValue();

    writer.write("=== ARCHIVO " + fileName + " ===");
    writer.newLine();
    writer.newLine();

    for (LineRecord record : records) {
        // Dividir aquí (solo para visualización)
        List<LineRecord> displayLines = LineSplitter.splitLongLines(record);
        for (LineRecord displayLine : displayLines) {
            writer.write(String.format("[%s] %s", displayLine.status(), displayLine.content()));
            writer.newLine();
        }
    }
}

    /**
     * Genera un reporte comparativo entre dos versiones del mismo archivo.
     * @param writer BufferedWriter para escribir el archivo
     * @param versions Lista con ambas versiones del archivo
     * @throws IOException Si ocurre un error al escribir el archivo
     */
    private void writeComparisonReport(BufferedWriter writer,
                                 List<Map.Entry<String, List<LineRecord>>> versions) throws IOException {
    versions.sort((a, b) -> a.getKey().contains("Version: A") ? -1 : 1);
    String baseName = versions.get(0).getKey().replaceAll(" \\[Version: [AB]\\]", "");
    
    writer.write("=== COMPARACIÓN PARA " + baseName + " ===");
    writer.newLine();
    writer.newLine();

    for (Map.Entry<String, List<LineRecord>> version : versions) {
        String versionLabel = version.getKey().contains("Version: A") ? "VERSIÓN ANTIGUA" : "VERSIÓN NUEVA";
        writer.write("--- " + versionLabel + " ---");
        writer.newLine();

        for (LineRecord record : version.getValue()) {
            // Aplica LineSplitter aquí también
            List<LineRecord> displayLines = LineSplitter.splitLongLines(record);
            for (LineRecord displayLine : displayLines) {
                writer.write(String.format("[%s] %s", displayLine.status(), displayLine.content()));
                writer.newLine();
            }
        }
        writer.newLine();
    }
}
}