package mantenimiento.codecounter.validators.format_validators;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import mantenimiento.codecounter.exceptions.InvalidFormatException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StyleKAndRValidatorTest {

  private final StyleKAndRValidator validator = new StyleKAndRValidator();

  @Test
  @DisplayName(
      "Debe lanzar InvalidFormatException si la línea tiene un corchete de apertura inválido")
  void testInvalidOpeningBracketThrowsException() {
    String invalidLine = " {";
    assertThrows(InvalidFormatException.class, () -> validator.isValid(invalidLine));
  }

  @Test
  @DisplayName(
      "Debe lanzar InvalidFormatException si la línea tiene un corchete de cierre inválido")
  void testInvalidClosingBracketThrowsException() {
    String invalidLine = "} if";
    assertThrows(InvalidFormatException.class, () -> validator.isValid(invalidLine));
  }

  @Test
  @DisplayName("Debe lanzar InvalidFormatException si la línea contiene corchetes vacíos")
  void testEmptyBracketsThrowsException() {
    String invalidLine = "{}";
    assertThrows(InvalidFormatException.class, () -> validator.isValid(invalidLine));
  }

  @Test
  @DisplayName(
      "Debe devolver lanzar InvalidFormatException si existen {} en una sola línea con o sin"
          + " contenido")
  void testValidLineReturnsTrue() {
    String validLine = "if (x > 0) { doSomething(); }";
    assertThrows(InvalidFormatException.class, () -> assertTrue(validator.isValid(validLine)));
  }
}
