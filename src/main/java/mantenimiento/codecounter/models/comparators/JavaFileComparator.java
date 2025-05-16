package mantenimiento.codecounter.models.comparators;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import mantenimiento.codecounter.constants.JavaRegextConstants;
import mantenimiento.codecounter.models.reporters.ComparationReport;

/**
 * Compara el contenido de dos archivos Java línea por línea y genera un
 * reporte de comparación que incluye líneas originales, modificadas y nuevas.
 */
public class JavaFileComparator {

    private ComparationReport comparationReport;
    private List<String> content;
    private List<String> contentToCompare;

    /**
     * Crea una nueva instancia del comparador de archivos Java.
     * 
     * @param content          contenido del archivo base
     * @param contentToCompare contenido del archivo a comparar
     */
    public JavaFileComparator(List<String> content, List<String> contentToCompare) {
        this.comparationReport = new ComparationReport();
        this.content = content;
        this.contentToCompare = contentToCompare;
    }

    /**
     * Inicia el proceso de comparación entre los contenidos de los archivos.
     */
    public void compareContent() {
        compareSameLines();
        compareDifferentLines();
    }

    /**
     * Compara dos líneas específicas y actualiza el reporte con su estado.
     * 
     * @param line          línea del archivo original
     * @param lineToCompare línea del archivo a comparar
     * @param i             índice de la línea
     */
    private void compareLine(String line, String lineToCompare, int i) {
        if (line.equals(lineToCompare)) {
            this.comparationReport.makeReportLine(STATUS.ORIGINAL, line, lineToCompare);
        } else if (getSimplifiedContent(line) == getSimplifiedContent(lineToCompare)) {
            this.comparationReport.makeReportLine(STATUS.MODIFIED, line, lineToCompare);
        } else if (!line.equals(lineToCompare)) {
            this.comparationReport.makeReportLine(STATUS.NEW, line, lineToCompare);
        }
    }

    /**
     * Compara las líneas en las posiciones correspondientes de ambos archivos.
     */
    private void compareSameLines() {
        for (int i = 0, j = 0; i < content.size() && j < contentToCompare.size(); i++, j++) {
            String currentLine = content.get(i);
            String lineToCompare = contentToCompare.get(j);
            compareLine(currentLine, lineToCompare, i);
        }
    }

    /**
     * Simplifica una línea removiendo modificadores de acceso y otras palabras
     * clave.
     * 
     * @param line línea a simplificar
     * @return línea simplificada sin modificadores
     */
    private String getSimplifiedContent(String line) {
        String simplified = Stream.of(line.split(" "))
                .filter(s -> !s.matches(JavaRegextConstants.ACCESS_MODIFIERS_REGEX))
                .filter(s -> !s.matches(JavaRegextConstants.IDENTIFIER_DECLARATION_REGEX))
                .filter(s -> !s.matches(JavaRegextConstants.FINAL_OR_STATIC_REGEX))
                .collect(Collectors.joining());
        return simplified;
    }

    /**
     * Maneja las líneas adicionales si los archivos tienen diferentes longitudes.
     */
    private void compareDifferentLines() {
        int difference = Math.abs(this.content.size() - this.contentToCompare.size());
        if (this.content.size() < this.contentToCompare.size()) {
            this.comparationReport.updateReport(this.content, this.contentToCompare, difference);
        } else if (this.content.size() > this.contentToCompare.size()) {
            this.comparationReport.updateReport(this.content, difference);
        }
    }

    /**
     * Retorna el reporte generado después de la comparación.
     * 
     * @return reporte de comparación
     */
    public ComparationReport getComparationReport() {
        return comparationReport;
    }
}