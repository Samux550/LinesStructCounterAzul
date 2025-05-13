package mantenimiento.codecounter.validators.logical_validators;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TypeDeclarationValidatorTest {

  private TypeDeclarationValidator validator;

  @BeforeEach
  void setUp() {
    validator = new TypeDeclarationValidator();
  }

  @Test
  @DisplayName("Debe de validar correctamente una declaración de clase")
  void testValidateTypeWithValidClassDeclaration() {
    String validClassDeclaration = "public class MyClass {";
    assertTrue(validator.validateType(validClassDeclaration));
  }

  @Test
  @DisplayName("Debe de validar correctamente una declaración de interfaz")
  void testValidateTypeWithValidInterfaceDeclaration() {
    String validInterfaceDeclaration = "public interface MyInterface {";
    assertTrue(validator.validateType(validInterfaceDeclaration));
  }

  @Test
  @DisplayName("Debe de validar correctamente una declaración de enum")
  void testValidateTypeWithValidEnumDeclaration() {
    String validEnumDeclaration = "public enum MyEnum {";
    assertTrue(validator.validateType(validEnumDeclaration));
  }

  @Test
  @DisplayName("Debe de validar correctamente una declaración de método")
  void testValidateTypeWithInvalidDeclaration() {
    String invalidDeclaration = "public void myMethod() {";
    assertFalse(validator.validateType(invalidDeclaration));
  }

  @Test
  @DisplayName("Debe de validar correctamente una declaración con linea vacía")
  void testValidateTypeWithEmptyLine() {
    String emptyLine = "";
    assertFalse(validator.validateType(emptyLine));
  }

  @Test
  @DisplayName("Debe de validar correctamente una declaración con espacios en blanco")
  void testValidateTypeWithWhitespaceOnly() {
    String whitespaceOnly = "    ";
    assertFalse(validator.validateType(whitespaceOnly));
  }

  @Test
  @DisplayName("Debe de validar correctamente una declaración con comentario de bloque")
  void testValidateTypeWithCommentLine() {
    String commentLine = "// This is a comment";
    assertFalse(validator.validateType(commentLine));
  }

  @Test
  @DisplayName("Debe de validar correctamente una declaración parcial")
  void testValidateTypeWithPartialDeclaration() {
    String partialDeclaration = "class";
    assertFalse(validator.validateType(partialDeclaration));
  }
}
