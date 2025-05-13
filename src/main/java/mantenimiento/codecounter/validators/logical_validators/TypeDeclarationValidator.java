package mantenimiento.codecounter.validators.logical_validators;

import static mantenimiento.codecounter.constants.JavaRegextConstants.ACCESS_MODIFIERS_REGEX;
import static mantenimiento.codecounter.constants.JavaRegextConstants.TYPE_KEYS;

import mantenimiento.codecounter.templates.LogicalValidator;

/**
 * Clase que verifica si existe una declaración de tipo (class, interface, enum) para poder
 * contabilizarlo como línea lógica
 */
public class TypeDeclarationValidator implements LogicalValidator {

  private static final String TYPE_DECLARATION =
      "^(\\s*" + ACCESS_MODIFIERS_REGEX + ".*\\s*" + TYPE_KEYS + ".*)";

  /**
   * Determina si una línea de código corresponde a una declaración de tipo para considerarla como
   * línea lógica
   *
   * @param linesOfCode
   * @return {@code true} si la corresponde a una declaración de tipo, {@code false} en caso
   *     contrario
   */
  @Override
  public boolean validateType(String linesOfCode) {
    return isTypeDeclaration(linesOfCode);
  }

  /**
   * Verifica si la sentencia es una declaración de tipo
   *
   * @param lineOfCode sentencia de codigo por analizar
   * @return {@code true} si coincide con la declaracion, {@code false} en caso contrario
   */
  private boolean isTypeDeclaration(String line) {
    return line.matches(TYPE_DECLARATION) && !line.contains("\"");
  }

  /*
   * Obtiene el nombre del tipo declarado en la línea de código
   */

  public String getTypeName(String line) {
    if (line.contains("\"")) return null;

    String[] tokens = line.trim().split("\\s+");
    for (int i = 0; i < tokens.length - 1; i++) {
      if (tokens[i].equals("class") || tokens[i].equals("interface") || tokens[i].equals("enum")) {
        return tokens[i + 1];
      }
    }
    return null;
  }
}
