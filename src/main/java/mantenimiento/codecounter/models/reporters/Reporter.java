package mantenimiento.codecounter.models.reporters;

import java.nio.file.Path;
import java.util.List;
import mantenimiento.codecounter.models.counters.StructCounter;

/** Clase abstracta que define el comportamiento de un generador de reportes */
public abstract class Reporter {
  protected String programName;
  protected List<StructCounter> lineCounters;

  protected Reporter(Path filePath, List<StructCounter> lineCounters) {
    this.programName = filePath.getFileName().toString();
    this.lineCounters = lineCounters;
  }

  /** Genera un reporte con la información de los contadores de líneas */
  public abstract void generateReport();
}
