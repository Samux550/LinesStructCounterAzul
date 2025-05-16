package mantenimiento.codecounter.models.comparators;

import java.util.List;
import mantenimiento.codecounter.models.reporters.ComparationReport;

/**
 * Compara el contenido de dos archivos con la extención .java
 * Sirve como clase intermedia para incorporar la lógica de comparación de
 * dos contenidos y la elaboración del reporte.
 */
public class JavaFileComparator {

    private ComparationReport comparationReport;
    private List<String> content;
    private List<String> contentToCompare;

    /**
     * Constructor
     * @param content contenido de un archivo con extensión .java 
     * @param contentToCompare contenido de un archivo .java para comparar
     */
    public JavaFileComparator(List<String> content, List<String> contentToCompare) {
        this.comparationReport = new ComparationReport();
        this.content = content;
        this.contentToCompare = contentToCompare;
    }
    
    /**
     * Compara el contenido de ambos archivos
     */
    public void compareContent() {
        compareSameLines();
        compareDifferentLines();
    }

    /**
     * Ejecuta la lógica de comparación de dos líneas provistas como parámetros
     * y añade una nueva entrada al reporte de comparación
     * @param line línea de código a comparar
     * @param lineToCompare línea de código para comparar
     */
    private void compareLine(String line, String lineToCompare) {
        LineComparator lineComparator = new LineComparator(line, lineToCompare);
        Status status = lineComparator.compare();
        this.comparationReport.makeReportLine(status, line, lineToCompare);
    }

    /**
     * Compara aquellas líneas de código en ambos contenidos que compartan el mismo índice
     */
    private void compareSameLines() {
        for (int i = 0, j = 0; i < content.size() && j < contentToCompare.size(); i++, j++) {
            String currentLine = content.get(i);
            String lineToCompare = contentToCompare.get(j);
            compareLine(currentLine, lineToCompare);
        }
    }


    /**
     * Genera un reporte de comparación de aquellas líneas que no se encuentren en un contenido u 
     * otro a razón de que tener una mayor extensión. Dichos reportes empiezan a partir del índice
     * del contenido con menos longitud.
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
     * Método que retorna el reporte final de los contenidos del archivo java en cuestión
     * @return Retorna el reporte de cada línea de código de ambos contenidos
     */
    public ComparationReport getComparationReport() {
        return comparationReport;
    }
}