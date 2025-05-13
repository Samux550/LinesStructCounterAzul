package mantenimiento.codecounter.validators.format_validators;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import mantenimiento.codecounter.exceptions.InvalidFormatException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SingleDeclarationValidatorTest {

  @Test
  void testValidSingleDeclaration() throws InvalidFormatException {
    SingleDeclarationValidator validator = new SingleDeclarationValidator();
    assertTrue(validator.isValid("int x = 10;"));
  }

  @Test
  @DisplayName("Debe de lanzar la excepion al detectar incialización de varias variables")
  void testInvalidMultipleDeclarationsWithSemicolon() {
    SingleDeclarationValidator validator = new SingleDeclarationValidator();
    assertThrows(
        InvalidFormatException.class,
        () -> {
          validator.isValid("int value 1 = 10; double value2 = 12.8;");
        });
    assertThrows(
        InvalidFormatException.class,
        () -> {
          validator.isValid("value; value;");
        });
  }

  @Test
  @DisplayName("Debe de lanzar la excepion al detectar incialización de varias variables ")
  void testInvalidMultipleDeclarationsWithCommas() {
    SingleDeclarationValidator validator = new SingleDeclarationValidator();
    assertThrows(
        InvalidFormatException.class,
        () -> {
          validator.isValid("int value1, value2, value3;");
        });
  }
}
