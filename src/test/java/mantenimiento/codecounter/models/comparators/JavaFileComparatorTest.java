package mantenimiento.codecounter.models.comparators;

import mantenimiento.codecounter.models.reporters.ComparationReport;
import mantenimiento.codecounter.models.LineRecord;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JavaFileComparatorTest {

    @Test
    public void testCompareContent_identicalContent() {
        List<String> contentA = Arrays.asList(
                "public class Test {",
                "    int x = 5;",
                "}"
        );
        List<String> contentB = Arrays.asList(
                "public class Test {",
                "    int x = 5;",
                "}"
        );

        JavaFileComparator comparator = new JavaFileComparator(contentA, contentB);
        comparator.compareContent();

        ComparationReport report = comparator.getComparationReport();

        for (LineRecord line : report.getSourceContentReport()) {
            assertEquals(Status.ORIGINAL, line.status());
        }

        for (LineRecord line : report.getTargetContentReport()) {
            assertEquals(Status.ORIGINAL, line.status());
        }
    }

    @Test
    public void testCompareContent_modifiedLine() {
        List<String> contentA = Arrays.asList(
                "public class Test {",
                "    int x = 4;",
                "}"
        );
        List<String> contentB = Arrays.asList(
                "public class Test {",
                "    int x = 5;",
                "}"
        );

        JavaFileComparator comparator = new JavaFileComparator(contentA, contentB);
        comparator.compareContent();

        ComparationReport report = comparator.getComparationReport();
        assertTrue(report.getSourceContentReport().stream().anyMatch(r -> r.status() == Status.ORIGINAL));
        assertTrue(report.getTargetContentReport().stream().anyMatch(r -> r.status() == Status.MODIFIED));
    }

    @Test
    public void testCompareContent_newLine() {
        List<String> contentA = Arrays.asList(
                "public class Test {"
        );
        List<String> contentB = Arrays.asList(
                "public class Test {",
                "    int y = 10;"
        );

        JavaFileComparator comparator = new JavaFileComparator(contentA, contentB);
        comparator.compareContent();

        ComparationReport report = comparator.getComparationReport();
        assertTrue(report.getTargetContentReport().stream().anyMatch(r -> r.status() == Status.NEW));
    }

    @Test
    public void testCompareContent_deletedLine() {
        List<String> contentA = Arrays.asList(
                "public class Test {",
                "    int z = 9;"
        );
        List<String> contentB = Arrays.asList(
                "public class Test {"
        );

        JavaFileComparator comparator = new JavaFileComparator(contentA, contentB);
        comparator.compareContent();

        ComparationReport report = comparator.getComparationReport();
        assertTrue(report.getSourceContentReport().stream().anyMatch(r -> r.status() == Status.DELETED));
    }

    @Test
    public void testCompareContent_mixedChanges() {
        List<String> contentA = Arrays.asList(
                "public class Test {",
                "    int a = 1;",
                "    int b = 2;",
                "}"
        );
        List<String> contentB = Arrays.asList(
                "public class Test {",
                "    int a = 1;",
                "    int c = 3;",
                "    int b = 2;",
                "}"
        );

        JavaFileComparator comparator = new JavaFileComparator(contentA, contentB);
        comparator.compareContent();

        ComparationReport report = comparator.getComparationReport();

        assertTrue(report.getTargetContentReport().stream().anyMatch(r -> r.status() == Status.NEW));
        assertTrue(report.getSourceContentReport().stream().anyMatch(r -> r.status() == Status.DELETED));
        assertTrue(report.getTargetContentReport().stream().anyMatch(r -> r.status() == Status.ORIGINAL));
    }
}
