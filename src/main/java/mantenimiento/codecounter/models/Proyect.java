package mantenimiento.codecounter.models;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import mantenimiento.codecounter.exceptions.FileNotFoundException;

/**
 * Representa un proyecto de código fuente Java, incluyendo su ruta de origen
 * y los archivos Java contenidos dentro de él.
 */
public class Proyect {

    /** Ruta del proyecto. */
    private String path;

    /** Lista de rutas a archivos Java dentro del proyecto. */
    private List<Path> pathFiles;

    /** Lista de objetos JavaFile representando cada archivo Java del proyecto. */
    private List<JavaFile> files;

    /**
     * Crea una nueva instancia de {@code Proyect} con la ruta y los archivos especificados.
     * Convierte las rutas a objetos {@link JavaFile}.
     *
     * @param path       Ruta del proyecto.
     * @param pathFiles  Lista de rutas a archivos Java dentro del proyecto.
     */
    public Proyect(String path, List<Path> pathFiles) {
        this.path = path;
        this.pathFiles = pathFiles;
        this.files = new ArrayList<>();
        makeFiles();
    }

    /**
     * Obtiene la ruta del proyecto.
     *
     * @return Ruta como cadena.
     */
    public String getPath() {
        return path;
    }

    /**
     * Establece una nueva ruta para el proyecto.
     *
     * @param path Nueva ruta.
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Devuelve la lista de archivos Java procesados del proyecto.
     *
     * @return Lista de {@link JavaFile}.
     */
    public List<JavaFile> getFiles() {
        return files;
    }

    /**
     * Reemplaza la lista de archivos Java del proyecto.
     *
     * @param files Nueva lista de objetos {@link JavaFile}.
     */
    public void setFiles(List<JavaFile> files) {
        this.files = files;
    }

    /**
     * Crea objetos {@link JavaFile} a partir de las rutas especificadas
     * en {@code pathFiles} y los agrega a la lista {@code files}.
     * Si algún archivo no se encuentra, imprime el error.
     */
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

