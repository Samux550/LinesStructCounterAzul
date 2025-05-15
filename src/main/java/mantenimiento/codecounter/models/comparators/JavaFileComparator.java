package mantenimiento.codecounter.models.comparators;

import java.util.List;
import mantenimiento.codecounter.models.reporters.ComparationReport;

public class JavaFileComparator {

    private ComparationReport comparationReport;

    private List<String> content;

    private List<String> contentToCompare;

    public JavaFileComparator(List<String> content, List<String> contentToCompare) {
        this.comparationReport = new ComparationReport();
        this.content = content;
        this.contentToCompare = contentToCompare;
    }

    public void compareContent() {
        compareSameLines();
        compareDifferentLines();
    }

    private void compareLine(String line, String lineToCompare, int i) {
        LineComparator lineComparator = new LineComparator(line, lineToCompare);
        STATUS status = lineComparator.compare();
        this.comparationReport.makeReportLine(status, line, lineToCompare);
    }

    private void compareSameLines() {
        for (int i = 0, j = 0; i < content.size() && j < contentToCompare.size(); i++, j++) {
            String currentLine = content.get(i);
            String lineToCompare = contentToCompare.get(j);
            compareLine(currentLine, lineToCompare, i);
        }
    }



    private void compareDifferentLines() {
        int difference = Math.abs(this.content.size() - this.contentToCompare.size());
        if (this.content.size() < this.contentToCompare.size()) {
            this.comparationReport.updateReport(this.content, difference);
        } else if (this.content.size() > this.contentToCompare.size()) {
            this.comparationReport.updateReport(this.content, this.contentToCompare, difference);
        }
    }

    public ComparationReport getComparationReport() {
        return comparationReport;
    }

}