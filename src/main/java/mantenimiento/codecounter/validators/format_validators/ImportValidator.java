package mantenimiento.codecounter.validators.format_validators;

import static mantenimiento.codecounter.constants.ReasonInvalidFormat.INVALID_IMPORT_STATEMENT;

import mantenimiento.codecounter.exceptions.InvalidFormatException;
import mantenimiento.codecounter.templates.FormatValidator;

/**
 * Clase que valida si la importacion de un paquete tiene un comodin y lanza una excepcion en caso
 * de haber
 *
 * <p>ej: import java.util*
 */
public class ImportValidator extends FormatValidator {

  /**
   * Realiza la validacion del formato verificando si es un import y si utiliza un comodin
   *
   * @param lineOfFile linea de texto (sentencia de codigo) a validar
   * @return {@code true} si el import es sin comodin
   * @throws InvalidFormatException si el import se realiza con comodin
   */
  @Override
  public boolean isValid(String lineOfFile) throws InvalidFormatException {
    if (isImportDeclaration(lineOfFile)) {
      if (isImportWithWildcard(lineOfFile)) {
        throw new InvalidFormatException(INVALID_IMPORT_STATEMENT, lineOfFile);
      }

      return true;
    }

    return this.validateNext(lineOfFile);
  }

  /**
   * Valida si la sentencia corresponde a una declaracion de import
   *
   * @param lineOfCode sentencia a validar
   * @return {@code true} si es una declaracion de import, {@code false} si no corresponde
   */
  private boolean isImportDeclaration(String lineOfCode) {
    String importDeclarationRegex = "^import\\s+.*;$";
    return lineOfCode.matches(importDeclarationRegex);
  }

  /**
   * Valida si la declaracion de import, incluso un import statico, tiene comodín
   *
   * @param lineOfCode sentencia a validar
   * @return {@code true} si el import es con comodin, {@code false} si no corresponde
   */
  private boolean isImportWithWildcard(String lineOfCode) {
    String wildcardImportRegex = "^import\\s+(static\\s+)?(?:\\w+\\.\\s*){1,10}\\*\\s*;";
    return lineOfCode.matches(wildcardImportRegex);
  }
}
