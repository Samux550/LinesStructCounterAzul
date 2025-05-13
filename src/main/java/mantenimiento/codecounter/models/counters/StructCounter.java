package mantenimiento.codecounter.models.counters;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import mantenimiento.codecounter.models.JavaClass;

/**
 * Clase que representa un contador de estructuras: Clases y métodos. Mantiene una lista de objetos
 * JavaClass y proporciona métodos para incrementar sus contadores.
 */
public class StructCounter {
  private final List<JavaClass> javaClasses;

  public StructCounter() {
    this.javaClasses = new ArrayList<>();
  }

  /**
   * Añade una nueva clase a la lista si no existe una con el mismo nombre.
   *
   * @param className El nombre de la clase a añadir.
   */
  public void addClass(String className) {
    if (findClassByName(className).isEmpty()) {
      javaClasses.add(new JavaClass(className));
    }
  }

  /**
   * Busca una clase en la lista por su nombre.
   *
   * @param className El nombre de la clase a buscar.
   * @return Un Optional conteniendo la JavaClass si se encuentra, o un Optional vacío si no.
   */
  private Optional<JavaClass> findClassByName(String className) {
    if (className == null) return Optional.empty();
    for (JavaClass jc : javaClasses) {
      if (className.equals(jc.getClassName())) {
        return Optional.of(jc);
      }
    }
    return Optional.empty();
  }

  /** Añade un método a la última clase registrada en la lista. */
  public void addMethodToLastClass() {
    if (!javaClasses.isEmpty()) {
      javaClasses.get(javaClasses.size() - 1).incrementMethodsAmount();
    }
  }

  /**
   * Añade una línea física de código a la clase especificada por su nombre.
   *
   * @param className El nombre de la clase a la que se debe añadir la línea.
   */
  public void addLineToClass(String className) {
    Optional<JavaClass> targetClass = findClassByName(className);
    if (targetClass.isPresent()) {
      targetClass.get().incrementLinesOfCode();
    } else {
      throw new IllegalStateException("Clase no encontrada al intentar añadir línea: " + className);
    }
  }

  /** Devuelve el total de líneas de código sumando las de todas las clases registradas. */
  public int getTotalLinesOfCode() {
    int totalLines = 0;
    for (JavaClass javaClass : javaClasses) {
      totalLines += javaClass.getLinesOfCode();
    }
    return totalLines;
  }

  /** Devolver el número total de clases registradas. */
  public int getClassesCount() {
    return javaClasses.size();
  }

  /** Devolver el total de métodos sumando los de todas las clases registradas. */
  public int getTotalMethodsCount() {
    int totalMethods = 0;
    for (JavaClass javaClass : javaClasses) {
      totalMethods += javaClass.getMethodsAmount();
    }
    return totalMethods;
  }

  /** Devuelve la lista completa de objetos JavaClass registrados. */
  public List<JavaClass> getClasses() {
    return new ArrayList<>(javaClasses);
  }
}
