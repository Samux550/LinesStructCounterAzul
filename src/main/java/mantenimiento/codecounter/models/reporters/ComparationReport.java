package mantenimiento.codecounter.models.reporters;

import java.util.ArrayList;
import java.util.List;

import mantenimiento.codecounter.models.LineRecord;
import mantenimiento.codecounter.models.comparators.Status;

/**
 * Clase encargada de construir un reporte de comparación línea por línea
 * entre dos archivos de código fuente.
 */
public class ComparationReport {

    private List<LineRecord> sourceContentReport;

    private List<LineRecord> targetContentReport;

    /**
     * Constructor que inicializa ambos reportes como listas vacías.
     */
    public ComparationReport() {
        this.sourceContentReport = new ArrayList<>();
        this.targetContentReport = new ArrayList<>();
    }

    /**
     * Realiza una instancia en el reporte de acruedo al estatus de la línea a comparar
     * @param status estado de la línea de código a comparar
     * @param sourceLine linea de código a comparar
     * @param targetLine línea de código para comparar
     */
    public void makeReportLine(Status status, String sourceLine, String targetLine) {
        switch (status) {
            case ORIGINAL:
                sourceContentReport.add(new LineRecord(Status.ORIGINAL, sourceLine));
                targetContentReport.add(new LineRecord(Status.ORIGINAL, targetLine));
                break;
            case MODIFIED:
                sourceContentReport.add(new LineRecord(Status.ORIGINAL, sourceLine));
                targetContentReport.add(new LineRecord(Status.MODIFIED, targetLine));
                break;
            case NEW:
                sourceContentReport.add(new LineRecord(Status.DELETED, sourceLine));
                targetContentReport.add(new LineRecord(Status.NEW, targetLine));
                break;
            default:
                break;
        }
    }

    /**
     * Agrega al reporte las líneas faltantes al final del contenido original,
     * marcándolas como eliminadas.
     *
     * @param content    Lista de líneas del contenido original.
     * @param difference Número de líneas de diferencia respecto al contenido comparado.
     */
    public void updateReport(List<String> content, int difference) {
        int indexToCheck = content.size() - difference;
        if (indexToCheck < 0) {
            indexToCheck = 0;
        }
        for (int i = indexToCheck; i < content.size(); i++) {
            this.sourceContentReport.add(new LineRecord(Status.DELETED, content.get(i)));
        }
    }

    /**
     * Agrega al reporte las líneas faltantes al final del contenido nuevo,
     * marcándolas como nuevas.
     *
     * @param content           Lista de líneas del contenido nuevo.
     * @param contentToCompare  Lista de líneas del contenido original.
     * @param difference        Número de líneas de diferencia respecto al contenido original.
     */
    public void updateReport(List<String> content, List<String> contentToCompare, int difference) {
        int indexToCheck = contentToCompare.size() - difference;
        if (indexToCheck < 0) {
            indexToCheck = 0;
        }
        for (int i = indexToCheck; i < contentToCompare.size(); i++) {
            this.targetContentReport.add(new LineRecord(Status.NEW, contentToCompare.get(i)));
        }
    }

    /**
     * 
     * @return retorna el reporte del contenido a comparar
     */
    public List<LineRecord> getSourceContentReport() {
        return sourceContentReport;
    }

    /**
     * 
     * @return retorna el reporte del contenido para comparar
     */
    public List<LineRecord> getTargetContentReport() {
        return targetContentReport;
    }

    /**
     * Método reservado para generar el reporte de una clase individual.
     * Actualmente no implementado.
     */
    public void makeReportSingleClass() {
        // Método aún no implementado
    }
}
