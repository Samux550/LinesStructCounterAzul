package mantenimiento.codecounter.models.comparators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import mantenimiento.codecounter.models.JavaFile;
import mantenimiento.codecounter.models.LineRecord;
import mantenimiento.codecounter.models.Proyect;
import mantenimiento.codecounter.models.reporters.ComparationReport;
import mantenimiento.codecounter.utils.LineSplitter;

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
     * Genera un reporte de un archivo java que se encuentre en un proyecto pero en
     * otro no.
     * 
     * @param status   status de la comparación de la comparación de un archivo con
     *                 extensión .java.
     * @param javaFile archivo java al cual generar un reporte.
     * @param mention  nomenclatura para registrar en el reporte final.
     */
    private void generateReportForSingleFile(Status status, JavaFile javaFile, String mention) {
        List<LineRecord> report = new ArrayList<>();

        javaFile.getContent().forEach(lineContent -> {

            LineRecord originalRecord = new LineRecord(status, lineContent);
            List<LineRecord> processedLines = LineSplitter.splitLongLines(originalRecord);
            report.addAll(processedLines);
        });

        generalReport.put(javaFile.getFileName() + " " + mention, report);
    }

    /**
     * Retorna el reporte final de comparar dos proyectos
     * 
     * @return retorna el reporte de general de dos proyectos provistos
     */
    public Map<String, List<LineRecord>> getGeneralReport() {
        return generalReport;
    }

}
