// package mantenimiento.codecounter.models.reporters;

// import mantenimiento.codecounter.models.LineRecord;
// import mantenimiento.codecounter.models.comparators.Status;
// import org.junit.jupiter.api.Test;

// import java.io.BufferedWriter;
// import java.io.IOException;
// import java.io.StringWriter;
// import java.nio.file.Path;
// import java.util.*;

// import static org.junit.jupiter.api.Assertions.*;

// public class TxtReporterIntegrationTest {

//     @Test
//     public void testLongLineIsSplitInReportButCountedOnce() throws IOException {
//         // Línea de 160 caracteres
//         String longLine = "Esta es una línea extremadamente larga que supera ampliamente los ochenta caracteres, y debería ser dividida en múltiples líneas visuales para el reporte.";

//         LineRecord record = new LineRecord(Status.NEW, longLine);
//         List<LineRecord> records = Collections.singletonList(record);
//         Map<String, List<LineRecord>> report = new HashMap<>();
//         report.put("ClasePrueba.java", records);

//         // Subclase anónima para evitar I/O real
//         TxtReporter reporter = new TxtReporter(report) {
//             protected Path requestOutputDirectory() {
//                 return Path.of("."); // Devuelve un path dummy
//             }

//             @Override
//             public void generateTxtReports() {
//                 try {
//                     StringWriter sw = new StringWriter();
//                     BufferedWriter writer = new BufferedWriter(sw);
//                     Map.Entry<String, List<LineRecord>> fileEntry = report.entrySet().iterator().next();
//                     writeSingleFileReport(writer, fileEntry);
//                     writer.flush();

//                     String output = sw.toString();
//                     // Aseguramos que se haya dividido (por lo menos dos líneas con el mismo estado)
//                     long countNewLines = output.lines().filter(line -> line.contains("[NEW]")).count();
//                     assertTrue(countNewLines >= 2, "La línea larga debe haberse dividido en varias líneas [NEW]");

//                 } catch (IOException e) {
//                     fail("No se esperaba IOException durante la prueba");
//                 }
//             }
//         };

//         // Ejecuta el test
//         reporter.generateTxtReports();
//     }
// }
