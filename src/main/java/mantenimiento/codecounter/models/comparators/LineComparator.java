package mantenimiento.codecounter.models.comparators;

import mantenimiento.codecounter.utils.LevenshteinDistanceComputer;

/*
 * Compara dos líneas de código por medio de la similitud la formula de similitud de 
 * LevenshtinDistance. Para que una línea se considere como modificada la similitud 
 * debe de ser superior a .80.
 */
public class LineComparator {

    private final double threshold = .80;

    private String sourceLine;

    private String targetLine;

    /**
     * Constructor
     * 
     * @param sourceLine    linea de código
     * @param targetLine línea de código con la cual se quiere comparar
     */
    public LineComparator(String sourceLine, String targetLine) {
        this.sourceLine = sourceLine;
        this.targetLine = targetLine;
    }

    /**
     * Calcula la distancia Levenshtein de las dos líneas de código
     * 
     * @return la distancia Levenshtein para las dos líneas de código
     */
    private int computeDistance() {
        return LevenshteinDistanceComputer.calculate(targetLine, sourceLine);
    }

    /**
     * Calcula la similaridad entre dos líneas de código
     * utilizando la distancia Levenshtein
     * 
     * @return el valor de similaridad que tienen las palabras
     */
    private double computeSimilarity() {
        int distance = computeDistance();
        int length = Math.max(this.sourceLine.length(), this.targetLine.length());
        return 1 - ((double) distance / length);
    }

    /**
     * Determina si dos líneas de código son lo suficientemente
     * similares como para considerarse como modificadas.
     * 
     * @return true si la similaridad entre dos palabras supera .80
     */
    private boolean isModified() {
        return computeSimilarity() > this.threshold;
    }

    /**
     * Compara las líneas de código determinando si son iguales, modificadas
     * o completamente nuevas.
     * 
     * @return el estatus de la línea de código
     */
    public Status compare() {
        if (sourceLine.equals(targetLine)) {
            return Status.ORIGINAL;
        } else if (isModified()) {
            return Status.MODIFIED;
        } else {
            return Status.NEW;
        }
    }

}
