package mantenimiento.codecounter.templates;

/*
 * Interfaz que define el comportamiento de una fábrica de validadores lógicos
 */
public interface LogicalValidator {
  /**
   * Valida si la línea de código corresponde a una declaración de tipo
   *
   * @param linesOfCode
   * @return
   */
  public boolean validateType(String linesOfCode);
}
