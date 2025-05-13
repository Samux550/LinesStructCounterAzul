package mantenimiento.codecounter.constants;

/**
 * Contiene constantes con expresiones regulares utilizadas para analizar estructuras y sintaxis del
 * lenguaje Java.
 */
public class JavaRegextConstants {
  public static final String ACCESS_MODIFIERS_REGEX = "((public|private|protected)\\s+)?";
  public static final String DATATYPE_DECLARATION_REGEX = "(\\s*[a-zA-Z0-9]+(<[a-zA-Z0-9]+>)?\\s+)";
  public static final String PARAMETERS_DECLARATION_REGEX = "(\\([^)]*\\)\\s*)";
  public static final String IDENTIFIER_DECLARATION_REGEX = "\\w+\\s*";
  public static final String TYPE_KEYS = "((class|enum|interface)\\s+)";
  public static final String FINAL_OR_STATIC_REGEX =
      "(?:(?:static\\s+)?(?:final\\s+)?|(?:final\\s+)?(?:static\\s+)?)?";
  public static final String ANNOTATION_REGEX = "^@[A-Za-z_]\\w*(\\(.*\\))?\\s*$";

  private JavaRegextConstants() {}
}
