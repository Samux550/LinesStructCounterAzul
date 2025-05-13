package mantenimiento.codecounter.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;
import mantenimiento.codecounter.exceptions.FolderNotFoundException;
import mantenimiento.codecounter.exceptions.JavaFilesNotFoundException;

public class JavaFilesScanner {
  /**
   * Método para obtener los archivos .java dentro de un directorio y sus subdirectorios.
   *
   * @param folderPath Ruta del directorio raíz.
   * @return Lista de rutas de archivos .java encontrados.
   * @throws FolderNotFoundException Si la carpeta no existe o no es válida.
   * @throws NoJavaFilesFoundException Si no se encuentran archivos .java en la carpeta.
   */
  public static List<Path> getJavaFiles(String folderPath)
      throws FolderNotFoundException, JavaFilesNotFoundException {
    Path path = Paths.get(folderPath);
    try (Stream<Path> stream = Files.walk(path)) {
      List<Path> javaFiles = stream.filter(Files::isRegularFile).filter(isJavaFile()).toList();

      if (javaFiles.isEmpty()) {
        throw new JavaFilesNotFoundException();
      }

      return javaFiles;

    } catch (IOException e) {
      throw new FolderNotFoundException(folderPath);
    }
  }

  /**
   * Método para verificar si un archivo es un archivo .java.
   *
   * @return Predicate que verifica si el archivo termina en ".java".
   */
  private static Predicate<Path> isJavaFile() {
    return fileName -> fileName.toString().endsWith(".java");
  }

  private JavaFilesScanner() {}
}
