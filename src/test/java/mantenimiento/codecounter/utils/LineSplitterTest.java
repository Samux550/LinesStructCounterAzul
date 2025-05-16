package mantenimiento.codecounter.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import mantenimiento.codecounter.models.LineRecord;
import mantenimiento.codecounter.models.comparators.Status;

public class LineSplitterTest {
    @Test
    public void testSplitLongLine_ShouldSplitAndPreserveLogicalLine() {
        // Línea de 160 caracteres (doble del máximo permitido)
        String longLine = "Esto es una línea muy larga que definitivamente excede los ochenta caracteres, y debe ser dividida en múltiples partes para facilitar la lectura.";
        LineRecord original = new LineRecord(Status.ORIGINAL, longLine);

        List<LineRecord> result = LineSplitter.splitLongLines(original);

        // Debe haber más de un fragmento
        assertTrue(result.size() > 1, "La línea debería haber sido dividida en múltiples fragmentos");

        // El primer fragmento debe tener el estado SPLITED
        assertEquals(Status.SPLITED, result.get(0).status(), "El primer fragmento debe tener estado SPLITED");

        // Los fragmentos siguientes deben tener estado ORIGINAL
        for (int i = 1; i < result.size(); i++) {
            assertEquals(Status.ORIGINAL, result.get(i).status(), "Los fragmentos siguientes deben mantener el estado ORIGINAL");
        }

        // Validamos que al unir el contenido dividido sea igual al original (sin espacios extra)
        StringBuilder reconstructed = new StringBuilder();
        for (LineRecord part : result) {
            reconstructed.append(part.content()).append(" ");
        }
        String reconstructedLine = reconstructed.toString().trim().replaceAll("\\s+", " ");
        String originalNormalized = longLine.trim().replaceAll("\\s+", " ");
        assertEquals(originalNormalized, reconstructedLine, "El contenido reconstruido debe ser igual al original");
    }
}
