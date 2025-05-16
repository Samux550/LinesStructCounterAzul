package mantenimiento.codecounter.models.reporters;

import java.util.ArrayList;
import java.util.List;

import mantenimiento.codecounter.models.comparators.Status;
import mantenimiento.codecounter.utils.LineRecord;

public class ComparationReport {

    private List<LineRecord> sourceContentReport;

    private List<LineRecord> targetContentReport;

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
     * Genera un reporte de estado de eliminado para un javaFile
     * @param content contenido con el cual se realizará el reporte
     * @param difference valor utilizado para iterar sobre el contenido
     */
    public void updateReport(List<String> content, int difference) {
        int indexToCheck = content.size() - difference - 1;
        for (int i = indexToCheck; i < content.size() - 1; i++) {
            this.sourceContentReport.add(new LineRecord(Status.DELETED, content.get(i)));
        }
    }

    /**
     * Genera un reporte de estado de estado nuevo para un javaFile
     * @param sourceContent contenido requerido para realizar el reporte
     * @param targetContent contenido con el cual se realizará el reporte
     * @param difference valor utilizado para iterar sobre el contenido
     */
    public void updateReport(List<String> sourceContent, List<String> targetContent, int difference) {
        int indexToCheck = targetContent.size() - difference - 1;
        for (int i = indexToCheck; i < targetContent.size() - 1; i++) {
            this.sourceContentReport.add(new LineRecord(Status.NEW, sourceContent.get(i)));
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

    public void makeReportSingleClass() {

    }

}
