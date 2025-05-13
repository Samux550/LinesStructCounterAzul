package mantenimiento.codecounter.models;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import mantenimiento.codecounter.exceptions.FileNotFoundException;

/**
 * Representacion de un archivo Java que provee metodos de acceso para su contenido y eliminar
 * informacion innecesaria para el conteno de lineas logicas y fisicas
 */
public class JavaFile {
  private List<String> content;
  private String fileName;

  /**
   * @param filePath Ruta del archivo
   * @throws FileNotFoundException Si la ruta es invalida
   */
  public JavaFile(Path filePath) throws FileNotFoundException {
    try {
      this.content = Files.readAllLines(filePath);
      this.fileName = filePath.getFileName().toString();
    } catch (Exception e) {
      throw new FileNotFoundException(filePath.toString());
    }
  }

  /**
   * Remueve comentarios en linea y de bloque
   *
   * @return Devuelve la instancia misma del objeto
   */
  public JavaFile removeComments() {
    String commentRegext = "(?s)/\\*.*?\\*/|//[^\n]*";

    String fileContent = String.join("\n", this.content);
    String codeWithOutComments = fileContent.replaceAll(commentRegext, "");

    this.content = new ArrayList<>(List.of(codeWithOutComments.split("\n")));

    return this;
  }

  /**
   * Remueve lineas en blanco
   *
   * @return Devuelve la instancia misma del objeto
   */
  public JavaFile removeBlankLines() {
    this.content.removeIf(String::isBlank);
    return this;
  }

  /**
   * @return Contenido del archivo java
   */
  public List<String> getContent() {
    return this.content.stream().map(String::trim).toList();
  }

  /**
   * @return Nombre del archivo
   */
  public String getFileName() {
    return this.fileName;
  }
}
