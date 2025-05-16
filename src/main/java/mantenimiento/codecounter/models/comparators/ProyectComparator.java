package mantenimiento.codecounter.models.comparators;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import mantenimiento.codecounter.models.JavaFile;
import mantenimiento.codecounter.models.LineRecord;
import mantenimiento.codecounter.models.Proyect;
import mantenimiento.codecounter.models.reporters.ComparationReport;
import mantenimiento.codecounter.models.reporters.TxtReporter;

/**
 * Compara cada uno de los archivos javas que compartan el nombre de los dos
 * proyecto provistos
 */
public class ProyectComparator {

    private Proyect targetProject;

    private Proyect sourceProject;

    private List<JavaFile> sourceProjectFiles;

    private List<JavaFile> targetProjectFiles;

    private Map<String, List<LineRecord>> generalReport = new HashMap<>();

    /**
     * Constructor
     * 
     * @param sourceProject proyecto para comparar
     * @param targetProject proyecto a comparar
     */
    public ProyectComparator(Proyect sourceProject, Proyect targetProject) {
        this.sourceProject = sourceProject;
        this.targetProject = targetProject;
        this.sourceProjectFiles = sourceProject.getFiles();
        this.targetProjectFiles = targetProject.getFiles();
    }

    /**
     * Compara cada uno de los archivos con extensión .java de los proyectos
     * provistos
     * en el constructor. Si en la comparación existen archivos que no aparezcan en
     * algún
     * proyecto, su contenido será etiquetado como nuevo o eliminado según sea el
     * caso.
     */
    public void compare() {
        List<JavaFile> blackListOne = new ArrayList<>(this.sourceProject.getFiles());
        List<JavaFile> blackListTwo = new ArrayList<>(this.targetProject.getFiles());

        for (JavaFile javaFile : this.sourceProjectFiles) {
            String name = javaFile.getFileName();
            Optional<JavaFile> fileToComOptional = findFileTocompare(name);

            if (fileToComOptional.isPresent()) {
                JavaFile matchedFile = fileToComOptional.get();
                compareFiles(javaFile, matchedFile);
                blackListTwo.remove(matchedFile);
                blackListOne.remove(javaFile);
            }
        }

        blackListOne.forEach(s -> generateReportForSingleFile(Status.DELETED, s, " [Version: A]"));
        blackListTwo.forEach(s -> generateReportForSingleFile(Status.NEW, s, " [Version: B]"));
    }

    /**
     * Realiza una busqueda de un archivo dentro de un proyecto de acuerdo con el
     * nombre del archivo
     * 
     * @param fileName nombre del archivo a buscar
     * @return estructura Optional de la instancia de la clase a buscar
     */
    private Optional<JavaFile> findFileTocompare(String fileName) {
        return this.targetProjectFiles.stream()
                .filter(s -> s.getFileName().equals(fileName))
                .findAny();
    }

    /**
     * Implementa la lógica para comparar el contenido de dos archivos con estensión
     * .java.
     * Posteriormente agrega una instancia al reporte final del proyecto
     * 
     * @param sourceJavaFile
     * @param targetJavaFile
     */
    private void compareFiles(JavaFile sourceJavaFile, JavaFile targetJavaFile) {
        JavaFileComparator javaFileComparator = new JavaFileComparator(sourceJavaFile.getContent(),
                targetJavaFile.getContent());

        javaFileComparator.compareContent();

        ComparationReport comparationReport = javaFileComparator.getComparationReport();
        List<LineRecord> contentReport = comparationReport.getSourceContentReport();
        List<LineRecord> contentToCompareReport = comparationReport.getTargetContentReport();

        generalReport.put(sourceJavaFile.getFileName() + " [Version: A]", contentReport);
        generalReport.put(sourceJavaFile.getFileName() + " [Version: B]", contentToCompareReport);

        this.targetProjectFiles.remove(targetJavaFile);
    }

    /**
 * Genera un reporte para un archivo que existe solo en una versión del proyecto (A o B),
 * agregando cada línea como un registro con el estado especificado (DELETED o NEW).
 * Las líneas se almacenan sin división para mantener el conteo preciso de líneas físicas.
 *
 * @param status   Estado que se asignará a todas las líneas del archivo (DELETED para versión A,
 *                 NEW para versión B).
 * @param javaFile Archivo Java a procesar, del cual se extraerá el contenido línea por línea.
 * @param mention  Sufijo identificador de versión (" [Version: A]" para archivos eliminados,
 *                 " [Version: B]" para archivos nuevos).
 *
 * @implNote Este método no divide líneas largas; la división se realiza posteriormente
 *           en {@link TxtReporter} para fines de visualización.
 * @see TxtReporter#writeSingleFileReport(BufferedWriter, Map.Entry)
 * @see LineRecord
 */
private void generateReportForSingleFile(Status status, JavaFile javaFile, String mention) {
    List<LineRecord> report = new ArrayList<>();
    
    javaFile.getContent().forEach(lineContent -> {
        report.add(new LineRecord(status, lineContent));
    });
    generalReport.put(javaFile.getFileName() + mention, report);
}

    /**
     * Obtiene el reporte general de comparación entre ambos proyectos.
     * 
     * @return mapa con los nombres de archivo y sus listas de diferencias
     */
    public Map<String, List<LineRecord>> getGeneralReport() {
        return generalReport;
    }
}
