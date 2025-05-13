package mantenimiento.codecounter.templates;

import mantenimiento.codecounter.exceptions.InvalidFormatException;

/** Proporciona una implementación base para la validación de formato del código. */
public abstract class FormatValidator {

  private FormatValidator nextValidator;

  /**
   * Establece el siguiente validador en la cadena de validación lógica.
   *
   * @param nextLogicalValidator Siguiente validador lógico en la cadena.
   */
  public void setNextValidator(FormatValidator nextFormatValidator) {
    this.nextValidator = nextFormatValidator;
  }

  public abstract boolean isValid(String lineOfFile) throws InvalidFormatException;

  /**
   * Valida la siguiente regla en la cadena de responsabilidad.
   *
   * @param linesOfFile Lista de líneas de código a validar.
   * @return {@code true} si la validación es exitosa, {@code false} en caso contrario.
   */
  protected boolean validateNext(String lineOfFile) throws InvalidFormatException {
    if (nextValidator != null) {
      return nextValidator.isValid(lineOfFile);
    }

    return true;
  }
}
