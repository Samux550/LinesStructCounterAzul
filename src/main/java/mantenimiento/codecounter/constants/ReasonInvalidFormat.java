package mantenimiento.codecounter.constants;

/** Clase que representa razones de formato inválido en el código fuente. */
public class ReasonInvalidFormat {
  public static final ReasonInvalidFormat INVALID_STYLE_K_AND_R =
      new ReasonInvalidFormat("Se debe seguir el estilo K&R para el uso de bloques.");
  public static final ReasonInvalidFormat INVALID_IMPORT_STATEMENT =
      new ReasonInvalidFormat(
          "Las importaciones de paquete deben de ser explicitas y con comodin *.");
  public static final ReasonInvalidFormat INVALID_ANOTATION_STATEMENT =
      new ReasonInvalidFormat("Cada anotación debe estar en una sola línea y bien formada.");
  public static final ReasonInvalidFormat INVALID_SINGLE_DECLARATION_STATEMENT =
      new ReasonInvalidFormat("No se permite realizar multiples declaraciones por linea");

  private String reason;

  private ReasonInvalidFormat(String reason) {
    this.reason = reason;
  }

  @Override
  public String toString() {
    return this.reason;
  }
}
