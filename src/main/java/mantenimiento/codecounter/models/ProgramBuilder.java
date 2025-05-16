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
 * líneas de código físicas y lógicas, y generando un reporte con los resultados.
 */
public class ProgramBuilder {

  /**
   * Analiza los archivos Java dentro de la carpeta especificada, contando líneas
   * de código y generando un reporte con los resultados.
   *
   * @param folderPath Ruta de la carpeta que contiene los archivos Java.
   * @throws JavaFilesNotFoundException Si no se encuentran archivos Java.
   * @throws FolderNotFoundException    Si la carpeta no existe.
   */
  public static void buildProgram(String folderPath) throws FolderNotFoundException, JavaFilesNotFoundException {
    String[] paths = folderPath.split(" ");
    Stream.of(paths).forEach(ProgramBuilder::countWorkflow);
    if (paths.length == 2) {
      compareWorkflow(paths);
    }
  }

  /**
   * Flujo de trabajo para el conteo de líneas, clases y metodos en los archivos Java dentro de una carpeta.
   *
   * @param folderPath Ruta de la carpeta a procesar.
   */
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
   * de contar sus líneas de código.
   *
   * @param javaFilePaths Lista de rutas de archivos Java a procesar.
   * @return Lista de objetos {@link StructCounter} con el conteo de líneas físicas y lógicas.
   * @throws FileNotFoundException  Si alguno de los archivos no se encuentra.
   * @throws InvalidFormatException Si se encuentra un error de formato en algún archivo.
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
   * Cuenta las líneas físicas y lógicas de un archivo Java validando su formato y contenido lógico.
   *
   * @param javaFile Archivo Java a procesar.
   * @return Contador de líneas con los resultados.
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

  /**
   * Flujo de trabajo para comparar los cambios entre 2 proyectos
   *
   * @param folderPaths Arreglo con dos rutas de carpetas que contienen los proyectos a comparar.
   */
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

  /**
   * Crea un objeto {@link Proyect} a partir de la carpeta que contiene archivos Java.
   *
   * @param folderPath Ruta de la carpeta del proyecto.
   * @return Objeto {@link Proyect} inicializado.
   * @throws FolderNotFoundException    Si la carpeta no existe.
   * @throws JavaFilesNotFoundException Si no se encuentran archivos Java.
   */
  private static Proyect makeProyect(String folderPath) throws FolderNotFoundException, JavaFilesNotFoundException {
    List<Path> javaPathFiles = JavaFilesScanner.getJavaFiles(folderPath);
    Proyect proyect = new Proyect(folderPath, javaPathFiles);
    return proyect;
  }

  /**
   * Genera un reporte en la terminal con los resultados del análisis de líneas de código.
   *
   * @param folderPath   Ruta de la carpeta analizada.
   * @param lineCounters Lista de contadores de líneas con los resultados del análisis.
   */
  private static void generateTerminalReport(String folderPath, List<StructCounter> lineCounters) {
    Reporter reporter = new TerminalReporter(Paths.get(folderPath), lineCounters);
    reporter.generateReport();
  }

  /**
   * Genera un reporte en archivo de texto con los resultados de la comparación entre proyectos.
   *
   * @param proyectComparator Comparador que contiene los resultados de la comparación.
   */
  private static void generateTxtReport(ProyectComparator proyectComparator) {
    Map<String, List<LineRecord>> report = proyectComparator.getGeneralReport();
    TxtReporter txtReporter = new TxtReporter(report);
    txtReporter.generateTxtReports();
  }

  /**
   * Constructor privado para evitar la instanciación de la clase {@code ProgramBuilder}
   * ya que contiene únicamente métodos estáticos.
   */
  private ProgramBuilder() {
  }
}

/*
 * Changelog de cambios respecto a la versión anterior de ProgramBuilder:
 *
 * [✔] Refactorización de método principal:
 *     - El método buildProgram(String folderPath) fue refactorizado para soportar múltiples rutas separadas por espacio.
 *     - Se agregó la lógica para bifurcar entre análisis individual (countWorkflow) y comparación entre dos carpetas (compareWorkflow).

 * [✔] División en flujos de trabajo separados:
 *     - Se extrajo el análisis de una sola carpeta a countWorkflow(String folderPath).
 *     - Se agregó compareWorkflow(String[] folderPaths) para manejar comparación entre dos proyectos.

 * [✔] Nueva funcionalidad de comparación entre proyectos:
 *     - Se incorporó la clase ProyectComparator para detectar diferencias entre dos conjuntos de archivos Java.
 *     - Se agregó soporte para generar reportes de comparación en archivo de texto mediante TxtReporter.

 * [✔] Nuevas excepciones:
 *     - Se manejan nuevas excepciones específicas: FolderNotFoundException y JavaFilesNotFoundException.
 *     - Estas excepciones mejoran la validación y el control de errores cuando no se encuentran carpetas o archivos Java.

 * [✔] Mejora en generación de reportes:
 *     - Se mantiene TerminalReporter para el análisis individual.
 *     - Se agrega TxtReporter como nueva opción para el resultado de comparaciones.

 * [✔] Mejora en documentación y Javadoc:
 *     - Se amplió la documentación de los métodos y se actualizaron las descripciones para reflejar mejor sus responsabilidades.

 * [✔] Buenas prácticas:
 *     - Se mantuvo el constructor privado para prevenir instanciación.
 *     - Se reorganizó el orden de los métodos para mejorar la legibilidad (buildProgram al inicio, utilitarios al final).
 *
 */
