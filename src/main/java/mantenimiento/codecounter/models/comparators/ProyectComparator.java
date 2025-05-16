package mantenimiento.codecounter.models.comparators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import mantenimiento.codecounter.demo.LineRecord;
import mantenimiento.codecounter.models.JavaFile;
import mantenimiento.codecounter.models.Proyect;
import mantenimiento.codecounter.models.reporters.ComparationReport;
import mantenimiento.codecounter.utils.LineSplitter;

/**
 * Compara el contenido de dos proyectos que consisten en múltiples archivos Java.
 * Genera un reporte general con las diferencias detectadas entre los archivos.
 */
public class ProyectComparator {

    private Proyect proyectToCompare;
    private Proyect proyect;
    private List<JavaFile> contentFiles;
    private List<JavaFile> contentToCompareFiles;
    private Map<String, List<LineRecord>> generalReport = new HashMap<>();

    /**
     * Crea una nueva instancia del comparador de proyectos.
     * 
     * @param proyect          proyecto base
     * @param proyectToCompare proyecto a comparar
     */
    public ProyectComparator(Proyect proyect, Proyect proyectToCompare) {
        this.proyect = proyect;
        this.proyectToCompare = proyectToCompare;
        this.contentFiles = proyect.getFiles();
        this.contentToCompareFiles = proyectToCompare.getFiles();
    }

    /**
     * Inicia el proceso de comparación entre los archivos de los proyectos.
     */
    public void compare() {
        List<JavaFile> blackListOne = new ArrayList<>(this.proyect.getFiles());
        List<JavaFile> blackListTwo = new ArrayList<>(this.proyectToCompare.getFiles());

        for (JavaFile javaFile : this.contentFiles) {
            String name = javaFile.getFileName();
            Optional<JavaFile> fileToComOptional = findFileTocompare(name);
    
            if (fileToComOptional.isPresent()) {
                JavaFile matchedFile = fileToComOptional.get();
                compareFiles(javaFile, matchedFile);
                blackListTwo.remove(matchedFile);
                blackListOne.remove(javaFile);
            }
        }

        blackListOne.forEach(s -> generateReportForSingleFile(STATUS.DELETED, s, " [Version: A]"));
        blackListTwo.forEach(s -> generateReportForSingleFile(STATUS.NEW, s, " [Version: B]"));
    }

    /**
     * Busca un archivo en el proyecto a comparar que tenga el mismo nombre.
     * 
     * @param className nombre del archivo Java
     * @return archivo Java con nombre coincidente si existe
     */
    private Optional<JavaFile> findFileTocompare(String className) {
        return this.contentToCompareFiles.stream()
                .filter(s -> s.getFileName().equals(className))
                .findAny();
    }

    /**
     * Compara dos archivos Java y actualiza el reporte general con sus diferencias.
     * 
     * @param javaFile         archivo del proyecto base
     * @param javaFileToCompare archivo del proyecto a comparar
     */
    private void compareFiles(JavaFile javaFile, JavaFile javaFileToCompare) {
        JavaFileComparator javaFileComparator = new JavaFileComparator(javaFile.getContent(),
                javaFileToCompare.getContent());

        javaFileComparator.compareContent();

        ComparationReport comparationReport = javaFileComparator.getComparationReport();
        List<LineRecord> contentReport = comparationReport.getCurrentContentReport();
        List<LineRecord> contentToCompareReport = comparationReport.getContentToCompareReport();

        generalReport.put(javaFile.getFileName() + " [Version: A]", contentReport);
        generalReport.put(javaFile.getFileName() + " [Version: B]", contentToCompareReport);

        this.contentToCompareFiles.remove(javaFileToCompare);
    }

    /**
     * Genera un reporte para un archivo que está presente solo en uno de los proyectos.
     * 
     * @param status   estado de la línea (nuevo o eliminado)
     * @param javaFile archivo para generar el reporte
     * @param mention  etiqueta de versión
     */
    private void generateReportForSingleFile(STATUS status, JavaFile javaFile, String mention) {
        List<LineRecord> report = new ArrayList<>();
    
        javaFile.getContent().forEach(lineContent -> {
            LineRecord originalRecord = new LineRecord(status, lineContent);     
            List<LineRecord> processedLines = LineSplitter.splitLongLines(originalRecord);
            report.addAll(processedLines);
        });
    
        generalReport.put(javaFile.getFileName() + " " + mention, report);
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
