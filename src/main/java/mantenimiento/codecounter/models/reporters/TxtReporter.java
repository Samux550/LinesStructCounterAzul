package mantenimiento.codecounter.models.reporters;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import mantenimiento.codecounter.demo.LineRecord;

public class TxtReporter {
    private Map<String, List<LineRecord>> report;

    public TxtReporter(Map<String, List<LineRecord>> report) {
        this.report = report;
    }

    public void generateTxt() {
        String outputFileName = "reporte.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {
            for (Map.Entry<String, List<LineRecord>> entry : this.report.entrySet()) {
                String fileName = entry.getKey();
                List<LineRecord> records = entry.getValue();

                writer.write("Archivo: " + fileName);
                writer.newLine();

                for (LineRecord record : records) {
                    writer.write("  [" + record.status() + "] " + record.content());
                    writer.newLine();
                }

                writer.newLine(); // Línea en blanco entre archivos
            }
            System.out.println("Reporte escrito en " + outputFileName);
        } catch (IOException e) {
            System.err.println("Error al escribir el reporte: " + e.getMessage());
        }
    }
}
