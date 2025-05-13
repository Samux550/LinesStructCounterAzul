package mantenimiento.codecounter.validators.format_validators;

import static mantenimiento.codecounter.constants.JavaRegextConstants.ANNOTATION_REGEX;
import static mantenimiento.codecounter.constants.ReasonInvalidFormat.INVALID_ANOTATION_STATEMENT;

import mantenimiento.codecounter.exceptions.InvalidFormatException;
import mantenimiento.codecounter.templates.FormatValidator;

/**
 * Clase encargada de verificar si una línea contiene una anotación válida en el formato correcto,
 * Una anotación válida debe comenzar con '@', seguida de un nombre que cumpla con las reglas de
 * identificadores de Java
 */
public class SingleAnnotationValidator extends FormatValidator {

  /**
   * Valida si la línea proporcionada contiene una anotación bien formada. Si la línea comienza con
   * '@', se verifica su formato; si está mal formada, lanza una excepción.
   *
   * @param lineOfFile Línea de código a validar.
   * @return {@code true} si la línea es una anotación válida
   * @throws InvalidFormatException Si la línea es una anotación pero está mal formada.
   */
  @Override
  public boolean isValid(String lineOfFile) throws InvalidFormatException {
    if (isAnnotation(lineOfFile)) {
      if (!isWellFormedAnnotation(lineOfFile)) {
        throw new InvalidFormatException(INVALID_ANOTATION_STATEMENT, lineOfFile);
      }
      return true;
    }
    return validateNext(lineOfFile);
  }

  /**
   * Determina si la línea corresponde a una anotación, verificando si comienza con '@'.
   *
   * @param lineOfFile Línea de código a evaluar.
   * @return {@code true} si la línea comienza con '@', {@code false} en caso contrario.
   */
  private boolean isAnnotation(String lineOfFile) {
    return lineOfFile.trim().startsWith("@");
  }

  /**
   * Verifica si una anotación está bien formada según la expresión regular definida en
   *
   * @param lineOfFile Línea de código a validar.
   * @return {@code true} si la anotación cumple con el formato definido, {@code false} si no.
   */
  private boolean isWellFormedAnnotation(String lineOfFile) {
    return lineOfFile.matches(ANNOTATION_REGEX);
  }
}
