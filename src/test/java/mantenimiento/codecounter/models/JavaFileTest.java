package mantenimiento.codecounter.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import mantenimiento.codecounter.exceptions.FileNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JavaFileTest {

  private Path tempFile;

  @BeforeEach
  void setUp() throws IOException {
    tempFile = Paths.get("testFile.java");
    Files.write(
        tempFile,
        new ArrayList<String>(
            List.of(
                "public class Test {",
                "    // Esto es un comentario en línea",
                "    /** ",
                "     *",
                "     * que ocupa varias líneas",
                "     */",
                "    public void method() {",
                "                             ",
                "        System.out.println(\"Hello World\");",
                "    }",
                "                             ",
                "}")));
  }

  @AfterEach
  void tearDown() throws IOException {
    Files.deleteIfExists(tempFile);
  }

  @Test
  @DisplayName("Debe de cargar el contenido correctamente")
  void testLoadFileContent() throws FileNotFoundException {
    JavaFile javaFile = new JavaFile(tempFile);
    List<String> content = javaFile.getContent();

    assertNotNull(content);
    assertFalse(content.isEmpty());
    assertEquals(12, content.size());
  }

  @Test
  @DisplayName("Debe de eliminar comentarios en bloque y de linea correctamente")
  void testRemoveComments() throws FileNotFoundException {
    JavaFile javaFile = new JavaFile(this.tempFile).removeComments();
    List<String> content = javaFile.getContent();

    assertFalse(content.contains("// Esto es un comentario en línea"));
    assertFalse(content.contains("/**"));
    assertFalse(content.contains("*"));
    assertFalse(content.contains("* Comentario de bloque"));
    assertFalse(content.contains("* que ocupa varias líneas"));
    assertFalse(content.contains("*/"));
  }

  @Test
  @DisplayName("Debe de eliminar lineas en blanco correctamente")
  void testRemoveBlankLines() throws FileNotFoundException {
    JavaFile javaFile = new JavaFile(this.tempFile).removeBlankLines();
    List<String> content = javaFile.getContent();

    assertFalse(content.contains(""));
  }

  @Test
  @DisplayName("Debe de remover lineas en blanco y comentarios")
  void testRemoveCommentsAndBlankLines() throws FileNotFoundException {
    JavaFile javaFile = new JavaFile(tempFile).removeComments().removeBlankLines();
    List<String> content = javaFile.getContent();

    assertFalse(content.contains("// Esto es un comentario en línea"));
    assertFalse(content.contains("/* Comentario de bloque"));
    assertFalse(content.contains("que ocupa varias líneas */"));
    assertFalse(content.contains(""));

    assertEquals(5, content.size());
  }
}
