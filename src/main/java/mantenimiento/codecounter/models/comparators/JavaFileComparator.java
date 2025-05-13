package mantenimiento.codecounter.models.comparators;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import mantenimiento.codecounter.constants.JavaRegextConstants;
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

        if (line.equals(lineToCompare)) {
            this.comparationReport.makeReportLine(STATUS.ORIGINAL, line, lineToCompare);
        } else if (getSimplifiedContent(line) == getSimplifiedContent(lineToCompare)) {
            this.comparationReport.makeReportLine(STATUS.MODIFIED, line, lineToCompare);
        } else if (!line.equals(lineToCompare)) {
            this.comparationReport.makeReportLine(STATUS.NEW, line, lineToCompare);
        }

    }

    private void compareSameLines() {
        for (int i = 0, j = 0; i < content.size() && j < contentToCompare.size(); i++, j++) {
            String currentLine = content.get(i);
            String lineToCompare = contentToCompare.get(j);
            compareLine(currentLine, lineToCompare, i);
        }
    }

    private String getSimplifiedContent(String line) {
        String simplified = Stream.of(line.split(" "))
                .filter(s -> !s.matches(JavaRegextConstants.ACCESS_MODIFIERS_REGEX))
                .filter(s -> !s.matches(JavaRegextConstants.IDENTIFIER_DECLARATION_REGEX))
                .filter(s -> !s.matches(JavaRegextConstants.FINAL_OR_STATIC_REGEX))
                .collect(Collectors.joining());
        System.out.println(simplified);
        return simplified;
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