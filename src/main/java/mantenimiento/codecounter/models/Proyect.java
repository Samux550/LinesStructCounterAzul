package mantenimiento.codecounter.models;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import mantenimiento.codecounter.exceptions.FileNotFoundException;

public class Proyect {

    private String path;

    private List<Path> pathFiles;

    private List<JavaFile> files;

    public Proyect(String path, List<Path> pathFiles) {
        this.path = path;
        this.pathFiles = pathFiles;
        this.files = new ArrayList<>();
        makeFiles();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<JavaFile> getFiles() {
        return files;
    }

    public void setFiles(List<JavaFile> files) {
        this.files = files;
    }

    private void makeFiles() {
        for (Path path : pathFiles) {
            try {
                this.files.add(new JavaFile(path));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}
