package mantenimiento.codecounter.exceptions;

public class FileNotFoundException extends Exception {
  private static final String ERROR_MESSAGE = "No se ha encontrado el archivo: %s";

  public FileNotFoundException(String filePath) {
    super(String.format(ERROR_MESSAGE, filePath));
  }
}
