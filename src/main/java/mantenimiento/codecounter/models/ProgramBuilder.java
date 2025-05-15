package mantenimiento.codecounter.models;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import mantenimiento.codecounter.demo.LineRecord;
import mantenimiento.codecounter.exceptions.FileNotFoundException;
import mantenimiento.codecounter.exceptions.FolderNotFoundException;
import mantenimiento.codecounter.exceptions.InvalidFormatException;
import mantenimiento.codecounter.exceptions.JavaFilesNotFoundException;
import mantenimiento.codecounter.models.comparators.ProyectComparator;
import mantenimiento.codecounter.models.counters.StructCounter;
import mantenimiento.codecounter.models.reporters.Reporter;
import mantenimiento.codecounter.models.reporters.TerminalReporter;
import mantenimiento.codecounter.utils.JavaFilesScanner;
import mantenimiento.codecounter.models.reporters.TxtReporter;

/**
 * Clase encargada de analizar archivos Java dentro de una carpeta, contando
 * líneas de código
 * físicas y lógicas, y generando un reporte con los resultados.
 */
public class ProgramBuilder {
  /**
   * Analiza los archivos Java dentro de la carpeta especificada, contando líneas
   * de código y
   * generando un reporte con los resultados.
   *
   * @param folderPath Ruta de la carpeta que contiene los archivos Java.
   * @throws JavaFilesNotFoundException
   * @throws FolderNotFoundException
   */
  public static void buildProgram(String folderPath) {
    String[] paths = folderPath.split(" ");
    Stream.of(paths).forEach(ProgramBuilder::countWorkflow);
    if (paths.length == 2) {
      compareWorkflow(paths);
    }
  }

  private static void countWorkflow(String folderPath) {
    try {
      List<Path> javaFilePaths = JavaFilesScanner.getJavaFiles(folderPath);
      List<StructCounter> lineCounters = processFiles(javaFilePaths);
      generateTerminalReport(folderPath, lineCounters);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Procesa los archivos Java proporcionados, validando su formato y lógica antes
   * de contar sus
   * líneas de código.
   *
   * @param javaFilePaths Lista de rutas de archivos Java a procesar.
   * @return Un objeto {@link LineCounter} con el conteo de líneas físicas y
   *         lógicas.
   * @throws FileNotFoundException  Si alguno de los archivos no se encuentra.
   * @throws InvalidFormatException Si se encuentra un error de formato en algún
   *                                archivo.
   */
  private static List<StructCounter> processFiles(List<Path> javaFilePaths)
      throws FileNotFoundException, InvalidFormatException {

    List<StructCounter> lineCounters = new ArrayList<>();

    for (Path filePath : javaFilePaths) {
      JavaFile javaFile = new JavaFile(filePath);

      lineCounters.add(processLines(javaFile));
    }
    return lineCounters;
  }

  /**
   * Cuenta las líneas físicas y lógicas de un archivo Java validando su formato y
   * contenido lógico.
   *
   * @param fileContent      Contenido del archivo sin comentarios ni líneas en
   *                         blanco.
   * @param formatValidator  Validador de formato de líneas.
   * @param logicalValidator Validador de lógica de líneas.
   * @param lineCounter      Contador de líneas donde se almacenan los resultados.
   * @throws InvalidFormatException Si alguna línea tiene un formato incorrecto.
   */
  private static StructCounter processLines(JavaFile javaFile) throws InvalidFormatException {

    List<String> fileContent = javaFile.removeComments().removeBlankLines().getContent();
    StructCounter lineCounter = new StructCounter();
    CodeAnalyzer analyzer = new CodeAnalyzer(lineCounter);

    for (String line : fileContent) {
      analyzer.processLine(line);
    }

    return lineCounter;
  }

  private static void compareWorkflow(String[] folderPaths) {
    try {
      Proyect proyect = makeProyect(folderPaths[0]);
      Proyect proyectToCompare = makeProyect(folderPaths[1]);
      ProyectComparator proyectComparator = new ProyectComparator(proyect, proyectToCompare);
      proyectComparator.compare();
      generateTxtReport(proyectComparator);
    } catch (FolderNotFoundException | JavaFilesNotFoundException e) {
      System.out.println(e.getMessage());
    }
  }

  private static Proyect makeProyect(String folderPath) throws FolderNotFoundException, JavaFilesNotFoundException {
    List<Path> javaPathFiles = JavaFilesScanner.getJavaFiles(folderPath);
    Proyect proyect = new Proyect(folderPath, javaPathFiles);
    return proyect;
  }

  /**
   * Genera un reporte con los resultados del análisis de líneas de código.
   *
   * @param folderPath  Ruta de la carpeta analizada.
   * @param lineCounter Contador de líneas de código con los resultados del
   *                    análisis.
   */
  private static void generateTerminalReport(String folderPath, List<StructCounter> lineCounters) {
    Reporter reporter = new TerminalReporter(Paths.get(folderPath), lineCounters);
    reporter.generateReport();
  }

  private static void generateTxtReport(ProyectComparator proyectComparator) {
    Map<String, List<LineRecord>> report = proyectComparator.getGeneralReport();
    TxtReporter txtReporter = new TxtReporter(report);
    txtReporter.generateTxtReports();
  }

  private ProgramBuilder() {
  }
}