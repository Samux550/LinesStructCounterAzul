package mantenimiento.codecounter.exceptions;

public class FolderNotFoundException extends Exception {
  private static final String ERROR_MESSAGE =
      "No se ha encontrado la carpeta o la ruta no corresponde a una carpeta %s";

  public FolderNotFoundException(String folderPath) {
    super(String.format(ERROR_MESSAGE, folderPath));
  }
}
