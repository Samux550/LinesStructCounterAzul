package mantenimiento.codecounter.validators.format_validators;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import mantenimiento.codecounter.exceptions.InvalidFormatException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SingleAnnotationValidatorTest {
  private final SingleAnnotationValidator validator = new SingleAnnotationValidator();

  @Test
  @DisplayName("Debe devolver true para una anotación válida en una sola línea: @Override")
  void testValidOverrideAnnotation() throws InvalidFormatException {
    assertTrue(validator.isValid("@Override"));
  }

  @Test
  @DisplayName(
      "Debe devolver true para una anotación válida en una sola línea: @Rule(expected ="
          + " IllegalArgumentException.class)")
  void testValidTestAnnotation() throws InvalidFormatException {
    assertTrue(validator.isValid("@Rule(expected = IllegalArgumentException.class)"));
  }

  @Test
  @DisplayName("Debe lanzar excepción para múltiples anotaciones en la misma línea")
  void testMultipleAnnotationsThrowsException() {
    String line = "@Override @Test @Something";
    assertThrows(InvalidFormatException.class, () -> validator.isValid(line));
  }

  @Test
  @DisplayName("Debe lanzar excepción para anotación seguida de código en la misma línea")
  void testAnnotationWithCodeThrowsException() {
    String line = "@Override public void doSomething(){";
    assertThrows(InvalidFormatException.class, () -> validator.isValid(line));
  }

  @Test
  @DisplayName("Debe lanzar excepción si la anotación es mal formada")
  void testMalformedAnnotationThrowsException() {
    assertThrows(InvalidFormatException.class, () -> validator.isValid("@"));
    assertThrows(InvalidFormatException.class, () -> validator.isValid("@123Invalid"));
  }
}
