package mantenimiento.codecounter.validators.format_validators;

import static mantenimiento.codecounter.constants.ReasonInvalidFormat.INVALID_SINGLE_DECLARATION_STATEMENT;

import mantenimiento.codecounter.exceptions.InvalidFormatException;
import mantenimiento.codecounter.templates.FormatValidator;

/** Clase que valida si existe una sola variable declarada por línea ejemplo: int value = 10; */
public class SingleDeclarationValidator extends FormatValidator {
  private static final String MULTIPLE_DECLARATIONS_REGEX =
      "^\\s*\\w+[\\s.=*&%+\\-/^\\w]*[;,][\\s.=*&%+\\-/^\\w]*[;,].*$";

  /**
   * Realiza la validacion del formato verificando si existe una sola declaración por línea
   *
   * @param lineOfFile linea de texto (sentencia de codigo) a validar
   * @return {@code true} si la línea cuenta con una sola declaración, {@code false} en caso
   *     contrario
   * @throws InvalidFormatException si existen más variables declaradas
   */
  @Override
  public boolean isValid(String lineOfFile) throws InvalidFormatException {
    if (hasMultipleDeclarations(lineOfFile)) {
      throw new InvalidFormatException(INVALID_SINGLE_DECLARATION_STATEMENT, lineOfFile);
    }

    return this.validateNext(lineOfFile);
  }

  /**
   * Verifica si la línea contiene múltiples declaraciones separadas por punto y coma o solo por
   * comas.
   *
   * @param lineOfFile La línea del archivo a validar.
   * @return {@code true} si hay múltiples declaraciones separadas por comas o punto y coma, {@code
   *     false} en caso contrario.
   */
  public boolean hasMultipleDeclarations(String lineOfFile) {
    return lineOfFile.matches(MULTIPLE_DECLARATIONS_REGEX);
  }
}
