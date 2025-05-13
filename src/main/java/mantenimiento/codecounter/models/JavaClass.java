package mantenimiento.codecounter.models;

/** Representa una clase Java individual con sus contadores de líneas y métodos. */
public class JavaClass {
  private int methodsAmount;
  private int linesOfCode;
  private String className;

  public JavaClass(String className) {
    if (className == null || className.trim().isEmpty()) {
      throw new IllegalArgumentException("El nombre de la clase no puede ser nulo o vacío.");
    }
    this.className = className;
    this.methodsAmount = 0;
    this.linesOfCode = 0;
  }

  /** Devolver el total de métodos encontrados en esta clase. */
  public int getMethodsAmount() {
    return methodsAmount;
  }

  /** Devolver el nombre de esta clase. */
  public String getClassName() {
    return className;
  }

  /** Incrementar en uno el contador de métodos para esta clase. */
  public void incrementMethodsAmount() {
    this.methodsAmount++;
  }

  /** Incrementar en uno el contador de líneas físicas de código para esta clase. */
  public void incrementLinesOfCode() {
    this.linesOfCode++;
  }

  /** Devolver el total de líneas físicas de código contadas para esta clase. */
  public int getLinesOfCode() {
    return linesOfCode;
  }

  @Override
  public String toString() {
    // Útil para depuración
    return "JavaClass{"
        + "className='"
        + className
        + '\''
        + ", linesOfCode="
        + linesOfCode
        + ", methodsAmount="
        + methodsAmount
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    JavaClass javaClass = (JavaClass) o;
    return className.equals(javaClass.className);
  }
}
