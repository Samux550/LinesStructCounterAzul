package mantenimiento.codecounter.models.reporters;

import java.util.ArrayList;
import java.util.List;

import mantenimiento.codecounter.demo.LineRecord;
import mantenimiento.codecounter.models.comparators.STATUS;

public class ComparationReport {

    private List<LineRecord> currentContentReport;

    private List<LineRecord> contentToCompareReport;

    public ComparationReport() {
        this.currentContentReport = new ArrayList<>();
        this.contentToCompareReport = new ArrayList<>();
    }

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

    public void updateReport(List<String> content, int difference) {
        int indexToCheck = content.size() - difference; 
        if (indexToCheck < 0) {
            indexToCheck = 0; 
        }
        for (int i = indexToCheck; i < content.size(); i++) {
            this.currentContentReport.add(new LineRecord(STATUS.DELETED, content.get(i)));
        }
    }

    public void updateReport(List<String> content, List<String> contentToCompare, int difference) {
        int indexToCheck = content.size() - difference;
        if (indexToCheck < 0) {
            indexToCheck = 0;
        }
        for (int i = indexToCheck; i < content.size(); i++) {
            this.currentContentReport.add(new LineRecord(STATUS.NEW, content.get(i)));
        }
    }

    public List<LineRecord> getCurrentContentReport() {
        return currentContentReport;
    }

    public List<LineRecord> getContentToCompareReport() {
        return contentToCompareReport;
    }

    public void makeReportSingleClass() {

    }

}
