package mantenimiento.codecounter.exceptions;

public class JavaFilesNotFoundException extends Exception {
  private static final String ERROR_MESSAGE =
      "No se ha encontrado archivos Java por analizar en la carpeta del proyecto";

  public JavaFilesNotFoundException() {
    super(ERROR_MESSAGE);
  }
}
