package mantenimiento.codecounter.utils;

import java.util.ArrayList;
import java.util.List;

import mantenimiento.codecounter.models.LineRecord;
import mantenimiento.codecounter.models.comparators.Status;

/**
 * Clase utilitaria para dividir líneas de código que exceden un número máximo de caracteres.
 * Facilita la visualización de líneas extensas al dividirlas de forma controlada.
 */
public class LineSplitter {

    /** Longitud máxima permitida para una línea antes de ser dividida. */
    private static final int MAX_LINE_LENGTH = 80;

    /**
     * Divide una línea larga en múltiples objetos {@link LineRecord} si excede la longitud máxima.
     * El primer fragmento se marca como SPLITED y los siguientes conservan el estado original.
     *
     * @param originalRecord La línea original a dividir.
     * @return Lista de {@link LineRecord} resultantes de la división.
     */
    public static List<LineRecord> splitLongLines(LineRecord originalRecord) {
        List<LineRecord> result = new ArrayList<>();
        String lineContent = originalRecord.content();
        Status originalStatus = originalRecord.status();
        
        if (lineContent.length() <= MAX_LINE_LENGTH) {
            result.add(originalRecord);
            return result;
        }

        // Se determina el punto óptimo para dividir
        int splitPoint = findSplitPoint(lineContent);
        String firstPart = lineContent.substring(0, splitPoint).trim();
        String secondPart = lineContent.substring(splitPoint).trim();

        // Se agrega la primera parte marcada como SPLITED
        result.add(new LineRecord(Status.SPLITED, firstPart));
        result.add(new LineRecord(originalStatus, secondPart));

        // Si la segunda parte aún es muy larga, se divide recursivamente
        LineRecord secondRecord = new LineRecord(originalStatus, secondPart);
        if (secondPart.length() > MAX_LINE_LENGTH) {
            List<LineRecord> subSplits = splitLongLines(secondRecord);
            result.remove(result.size() - 1); // Remueve la línea larga sin dividir
            result.addAll(subSplits); // Agrega las divisiones más pequeñas
        }

        return result;
    }

    /**
     * Encuentra el punto más adecuado para dividir una línea larga,
     * intentando partir en un espacio antes del límite, si es posible.
     *
     * @param line La línea original.
     * @return Índice donde se debe dividir la línea.
     */
    private static int findSplitPoint(String line) {
        // Si justo en el límite hay un espacio, se divide en ese punto
        if (line.length() > MAX_LINE_LENGTH && Character.isWhitespace(line.charAt(MAX_LINE_LENGTH))) {
            return MAX_LINE_LENGTH;
        }

        // Busca el último espacio antes del límite
        int lastSpaceBeforeMax = line.substring(0, MAX_LINE_LENGTH).lastIndexOf(' ');

        // Si no hay espacio, parte en el límite directamente
        return lastSpaceBeforeMax > 0 ? lastSpaceBeforeMax : MAX_LINE_LENGTH;
    }
}
