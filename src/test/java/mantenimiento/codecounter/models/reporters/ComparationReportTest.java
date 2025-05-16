package mantenimiento.codecounter.models.reporters;

import mantenimiento.codecounter.models.LineRecord;
import mantenimiento.codecounter.models.comparators.Status;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ComparationReportTest {

    @Test
    public void testMakeReportLine_Original() {
        ComparationReport report = new ComparationReport();

        report.makeReportLine(Status.ORIGINAL, "System.out.println(\"Hola\");", "System.out.println(\"Hola\");");

        List<LineRecord> current = report.getSourceContentReport();
        List<LineRecord> compare = report.getTargetContentReport();

        assertEquals(1, current.size());
        assertEquals(Status.ORIGINAL, current.get(0).status());
        assertEquals("System.out.println(\"Hola\");", current.get(0).content());

        assertEquals(1, compare.size());
        assertEquals(Status.ORIGINAL, compare.get(0).status());
    }

    @Test
    public void testMakeReportLine_Modified() {
        ComparationReport report = new ComparationReport();

        report.makeReportLine(Status.MODIFIED, "int x = 1;", "int x = 2;");

        List<LineRecord> current = report.getSourceContentReport();
        List<LineRecord> compare = report.getTargetContentReport();

        assertEquals(1, current.size());
        assertEquals(Status.ORIGINAL, current.get(0).status());

        assertEquals(1, compare.size());
        assertEquals(Status.MODIFIED, compare.get(0).status());
    }

    @Test
    public void testMakeReportLine_New() {
        ComparationReport report = new ComparationReport();

        report.makeReportLine(Status.NEW, "int z = 3;", "int z = 3;");

        List<LineRecord> current = report.getSourceContentReport();
        List<LineRecord> compare = report.getTargetContentReport();

        assertEquals(1, current.size());
        assertEquals(Status.DELETED, current.get(0).status());

        assertEquals(1, compare.size());
        assertEquals(Status.NEW, compare.get(0).status());
    }

    @Test
    public void testUpdateReport_DeletedLines() {
        ComparationReport report = new ComparationReport();

        List<String> content = Arrays.asList("line1", "line2", "line3", "line4");

        report.updateReport(content, 2);

        List<LineRecord> current = report.getSourceContentReport();

        assertEquals(2, current.size());
        assertEquals("line3", current.get(0).content());
        assertEquals(Status.DELETED, current.get(0).status());

        assertEquals("line4", current.get(1).content());
        assertEquals(Status.DELETED, current.get(1).status());
    }

    @Test
    public void testUpdateReport_NewLines() {
        ComparationReport report = new ComparationReport();

        List<String> content = Arrays.asList("line1", "line2", "line3");
        List<String> contentToCompare = Arrays.asList("line1", "line2", "line3", "line4");

        int difference = 1;

        report.updateReport(content, contentToCompare, difference);

        List<LineRecord> current = report.getSourceContentReport();

        assertEquals(1, current.size());
        assertEquals("line4", current.get(0).content());
        assertEquals(Status.NEW, current.get(0).status());
    }

    @Test
    public void testUpdateReport_NewLines_Multiple() {
        ComparationReport report = new ComparationReport();

        List<String> content = Arrays.asList("a", "b");
        List<String> contentToCompare = Arrays.asList("a", "b", "c", "d");

        int difference = 2;

        report.updateReport(content, contentToCompare, difference);

        List<LineRecord> current = report.getSourceContentReport();

        assertEquals(2, current.size());
        assertEquals("c", current.get(0).content());
        assertEquals(Status.NEW, current.get(0).status());
        assertEquals("d", current.get(1).content());
        assertEquals(Status.NEW, current.get(1).status());
    }
}
