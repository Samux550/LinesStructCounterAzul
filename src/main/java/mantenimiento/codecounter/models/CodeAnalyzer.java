package mantenimiento.codecounter.models;

import java.util.ArrayDeque;
import java.util.Deque;
import mantenimiento.codecounter.exceptions.InvalidFormatException;
import mantenimiento.codecounter.models.counters.StructCounter;
import mantenimiento.codecounter.templates.LogicalValidator;
import mantenimiento.codecounter.validators.ValidatorManager;
import mantenimiento.codecounter.validators.logical_validators.MethodDeclarationValidator;
import mantenimiento.codecounter.validators.logical_validators.TypeDeclarationValidator;

/**
 * Analiza líneas de código Java para contar clases (anidadas incluidas), métodos y líneas físicas.
 */
public class CodeAnalyzer {

  private final StructCounter counter;
  private final Deque<String> classContextStack;
  private final Deque<Integer> classStartBraceLevelStack;
  private int currentBraceLevel = 0;

  /**
   * Constructor.
   *
   * @param counter El contador donde se almacenarán los resultados. No puede ser nulo.
   */
  public CodeAnalyzer(StructCounter counter) {
    this.counter = counter;
    this.classContextStack = new ArrayDeque<>();
    this.classStartBraceLevelStack = new ArrayDeque<>();
  }

  /**
   * Procesa una línea de código fuente. Hace el análisis llamando a métodos auxiliares.
   *
   * @param line La línea de código a procesar.
   * @throws InvalidFormatException Si los validadores detectan un formato inválido.
   */
  public void processLine(String line) throws InvalidFormatException {
    String trimmedLine = preprocessLine(line);
    countLineForCurrentContext();
    DeclarationInfo declInfo = detectDeclarations(trimmedLine);
    processBracesAndUpdateContext(line, declInfo.potentialClassName());
    countMethodIfDeclared(declInfo.isMethod());
  }

  /**
   * Preprocesa la línea: recorta espacios y verifica si debe ignorarse.
   *
   * @param line Línea original.
   * @return La línea recortada, o null si debe ignorarse.
   */
  String preprocessLine(String line) {
    String trimmedLine = line.trim();
    if (trimmedLine.isEmpty() || isCommentLine(trimmedLine)) {
      return null;
    }
    return trimmedLine;
  }

  /** Llama a StructCounter para añadir una línea a todas las clases en el contexto actual. */
  private void countLineForCurrentContext() {
    if (!classContextStack.isEmpty()) {
      for (String classNameInStack : classContextStack) {
        counter.addLineToClass(classNameInStack);
      }
    }
  }

  private record DeclarationInfo(boolean isType, boolean isMethod, String potentialClassName) {}

  /**
   * Determina si la línea contiene una declaración de tipo o método.
   *
   * @param trimmedLine La línea de código recortada.
   * @return Un objeto DeclarationInfo con los resultados.
   * @throws InvalidFormatException Si los validadores lanzan la excepción.
   */
  private DeclarationInfo detectDeclarations(String trimmedLine) {
    LogicalValidator validator = ValidatorManager.getLogicalValidators(trimmedLine);
    boolean isType = validator instanceof TypeDeclarationValidator;
    boolean isMethod = validator instanceof MethodDeclarationValidator;
    String className = null;

    if (isType) {
      className = ((TypeDeclarationValidator) validator).getTypeName(trimmedLine);
      if (className == null || className.trim().isEmpty()) {
        isType = false;
        className = null;
      }
    }
    return new DeclarationInfo(isType, isMethod, className);
  }

  /**
   * Itera sobre los caracteres de la línea, actualiza el nivel de llaves, y llama a push/pop del
   * contexto de clase cuando corresponde.
   *
   * @param line La línea de código original (para iterar caracteres).
   * @param potentialClassNameForLine El nombre de la clase declarada en ESTA línea (si aplica),
   *     null si no.
   */
  private void processBracesAndUpdateContext(String line, String potentialClassNameForLine) {
    int levelBeforeProcessingLine = currentBraceLevel;
    boolean classAddedOnThisLine = false;

    for (char c : line.toCharArray()) {
      if (c == '{') {
        if (potentialClassNameForLine != null && !classAddedOnThisLine) {
          pushClassContext(potentialClassNameForLine, levelBeforeProcessingLine);
          classAddedOnThisLine = true;
        }
        currentBraceLevel++;
      } else if (c == '}') {
        tryPopClassContext();
        if (currentBraceLevel > 0) {
          currentBraceLevel--;
        }
      }
    }
    if (potentialClassNameForLine != null && !classAddedOnThisLine) {
      pushClassContext(potentialClassNameForLine, currentBraceLevel);
    }
  }

  /**
   * Intenta sacar una clase del contexto si el nivel de llaves actual coincide con el cierre
   * esperado de la clase más interna.
   */
  private void tryPopClassContext() {
    if (!classContextStack.isEmpty()
        && !classStartBraceLevelStack.isEmpty()
        && currentBraceLevel == classStartBraceLevelStack.peek() + 1) {
      popClassContext();
    }
  }

  /**
   * Añade una clase al contexto y actualiza los contadores.
   *
   * @param className Nombre de la clase.
   * @param braceLevel Nivel de llaves antes de la apertura de esta clase.
   */
  private void pushClassContext(String className, int braceLevel) {
    if (classContextStack.isEmpty() || !classContextStack.peek().equals(className)) {
      classContextStack.push(className);
      classStartBraceLevelStack.push(braceLevel);
      counter.addClass(className);
      counter.addLineToClass(className);
    }
  }

  /** Saca la clase más interna del contexto (de ambas pilas). */
  private void popClassContext() {
    if (!classContextStack.isEmpty()) {
      classContextStack.pop();
    }
    if (!classStartBraceLevelStack.isEmpty()) {
      classStartBraceLevelStack.pop();
    }
  }

  /**
   * Llama al contador para incrementar el número de métodos si aplica.
   *
   * @param isMethodDeclaration true si la línea actual declaró un método.
   */
  private void countMethodIfDeclared(boolean isMethodDeclaration) {
    if (!classContextStack.isEmpty() && isMethodDeclaration) {
      counter.addMethodToLastClass();
    }
  }

  /**
   * Verifica si la línea es un comentario simple de una línea.
   *
   * @param trimmedLine Línea recortada.
   * @return true si es comentario, false si no.
   */
  private boolean isCommentLine(String trimmedLine) {
    return trimmedLine.startsWith("//");
  }

  /**
   * Devuelve el contador asociado a este analizador.
   *
   * @return El objeto StructCounter.
   */
  public StructCounter getCounter() {
    return counter;
  }
}
