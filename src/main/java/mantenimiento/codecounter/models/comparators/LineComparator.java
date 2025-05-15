package mantenimiento.codecounter.models.comparators;
import mantenimiento.codecounter.utils.LevenshteinDistanceComputer;

public class LineComparator {

    private final double threshold = .80;

    private String line;

    private String lineToCompare;

    public LineComparator(String line, String lineToCompare) {
        this.line = line;
        this.lineToCompare = lineToCompare;
    }

    private int computeDistance() {
        return LevenshteinDistanceComputer.calculate(lineToCompare, line);
    }

    private double computeSimilarity() {
        int distance = computeDistance();
        int length = Math.max(this.line.length(), this.lineToCompare.length());
        return 1 - ((double) distance / length);
    }

    private boolean isModified() {
        System.out.printf("Similarity: %.2f%%\n", computeSimilarity());
        return computeSimilarity() > this.threshold;
    }


    public STATUS compare() {
        if (line.equals(lineToCompare)) {
            return STATUS.ORIGINAL;
        } else if (isModified()) {
            return STATUS.MODIFIED;
        } else {
            return STATUS.NEW;
        }
    }

}
