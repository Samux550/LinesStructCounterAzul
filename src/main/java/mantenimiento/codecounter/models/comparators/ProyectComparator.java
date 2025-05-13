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

public class ProyectComparator {

    private Proyect proyectToCompare;

    private Proyect proyect;

    private List<JavaFile> contentFiles;

    private List<JavaFile> contentToCompareFiles;

    private Map<String, List<LineRecord>> generalReport = new HashMap<>();

    public ProyectComparator(Proyect proyect, Proyect proyectToCompare) {
        this.proyect = proyect;
        this.proyectToCompare = proyectToCompare;
        this.contentFiles = proyect.getFiles();
        this.contentToCompareFiles = proyectToCompare.getFiles();
    }

    public void compare() {
        List<JavaFile> blackListOne = new ArrayList<>(this.proyect.getFiles());
        List<JavaFile> blackListTwo = new ArrayList<>(this.proyectToCompare.getFiles());

        for (JavaFile javaFile : this.contentFiles) {
            String name = javaFile.getFileName();
            Optional<JavaFile> fileToComOptional = findFileTocompare(name);
            fileToComOptional.ifPresent(s -> compareFiles(javaFile, s));
            //fileToComOptional.ifPresentOrElse(s -> compareFiles(javaFile, fileToComOptional.get()), null);
            fileToComOptional.ifPresent(blackListTwo::remove);
            blackListOne.remove(javaFile);
        }

        blackListOne.forEach(s -> generateReportForSingleFile(STATUS.DELETED, s, " [Version: A]"));
        blackListTwo.forEach(s -> generateReportForSingleFile(STATUS.NEW, s, " [Version: B]"));
    }

    private Optional<JavaFile> findFileTocompare(String className) {
        return this.contentToCompareFiles.stream()
                .filter(s -> s.getFileName().equals(className))
                .findAny();
    }

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

    private void generateReportForSingleFile(STATUS status, JavaFile javaFile, String mention) {
        List<LineRecord> report = new ArrayList<>();
        javaFile.getContent().stream().forEach(s -> {

            report.add(new LineRecord(status, s));
            
        });
        ;
        generalReport.put(javaFile.getFileName() + " " + mention, report);
    }

    public Map<String, List<LineRecord>> getGeneralReport() {
        return generalReport;
    }

}
