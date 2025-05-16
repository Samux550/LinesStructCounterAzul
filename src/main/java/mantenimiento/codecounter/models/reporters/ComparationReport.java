package mantenimiento.codecounter.models.reporters;

import java.util.ArrayList;
import java.util.List;

import mantenimiento.codecounter.demo.LineRecord;
import mantenimiento.codecounter.models.comparators.STATUS;

/**
 * Clase encargada de construir un reporte de comparación línea por línea
 * entre dos archivos de código fuente.
 */
public class ComparationReport {

    /** Reporte correspondiente al contenido original o base. */
    private List<LineRecord> currentContentReport;

    /** Reporte correspondiente al contenido con el que se compara. */
    private List<LineRecord> contentToCompareReport;

    /**
     * Constructor que inicializa ambos reportes como listas vacías.
     */
    public ComparationReport() {
        this.currentContentReport = new ArrayList<>();
        this.contentToCompareReport = new ArrayList<>();
    }

    /**
     * Agrega una línea al reporte según el estado comparativo detectado entre dos versiones de código.
     *
     * @param status        Estado de la línea (ORIGINAL, MODIFIED, NEW).
     * @param line          Línea del contenido original.
     * @param lineToCompare Línea del contenido comparado.
     */
    public void makeReportLine(STATUS status, String line, String lineToCompare) {
        switch (status) {
            case ORIGINAL:
                currentContentReport.add(new LineRecord(STATUS.ORIGINAL, line));
                contentToCompareReport.add(new LineRecord(STATUS.ORIGINAL, lineToCompare));
                break;
            case MODIFIED:
                currentContentReport.add(new LineRecord(STATUS.ORIGINAL, line));
                contentToCompareReport.add(new LineRecord(STATUS.MODIFIED, lineToCompare));
                break;
            case NEW:
                currentContentReport.add(new LineRecord(STATUS.DELETED, line));
                contentToCompareReport.add(new LineRecord(STATUS.NEW, lineToCompare));
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
            this.currentContentReport.add(new LineRecord(STATUS.DELETED, content.get(i)));
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
        int indexToCheck = content.size() - difference;
        if (indexToCheck < 0) {
            indexToCheck = 0;
        }
        for (int i = indexToCheck; i < contentToCompare.size(); i++) {
            this.currentContentReport.add(new LineRecord(STATUS.NEW, content.get(i)));
        }
    }

    /**
     * Devuelve el reporte del contenido original.
     *
     * @return Lista de objetos {@link LineRecord} del contenido original.
     */
    public List<LineRecord> getCurrentContentReport() {
        return currentContentReport;
    }

    /**
     * Devuelve el reporte del contenido comparado.
     *
     * @return Lista de objetos {@link LineRecord} del contenido comparado.
     */
    public List<LineRecord> getContentToCompareReport() {
        return contentToCompareReport;
    }

    /**
     * Método reservado para generar el reporte de una clase individual.
     * Actualmente no implementado.
     */
    public void makeReportSingleClass() {
        // Método aún no implementado
    }
}
