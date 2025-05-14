package mantenimiento.codecounter.utils;

import java.util.ArrayList;
import java.util.List;

import mantenimiento.codecounter.demo.LineRecord;
import mantenimiento.codecounter.models.comparators.STATUS;

public class LineSplitter {
    
    private static final int MAX_LINE_LENGTH = 80;
    
    public static List<LineRecord> splitLongLines(LineRecord originalRecord) {
        List<LineRecord> result = new ArrayList<>();
        String lineContent = originalRecord.content();
        STATUS originalStatus = originalRecord.status();
        
        if (lineContent.length() <= MAX_LINE_LENGTH) {
            result.add(originalRecord);
            return result;
        }

        int splitPoint = findSplitPoint(lineContent);
        String firstPart = lineContent.substring(0, splitPoint).trim();
        String secondPart = lineContent.substring(splitPoint).trim();
        
        result.add(new LineRecord(STATUS.SPLITED, firstPart));
        result.add(new LineRecord(originalStatus, secondPart));
        
        LineRecord secondRecord = new LineRecord(originalStatus, secondPart);
        if (secondPart.length() > MAX_LINE_LENGTH) {
            List<LineRecord> subSplits = splitLongLines(secondRecord);
            result.remove(result.size()-1);
            result.addAll(subSplits);
        }
        
        return result;
    }
    
    private static int findSplitPoint(String line) {
        
        if (line.length() > MAX_LINE_LENGTH && Character.isWhitespace(line.charAt(MAX_LINE_LENGTH))) {
            return MAX_LINE_LENGTH;
        }
        

        int lastSpaceBeforeMax = line.substring(0, MAX_LINE_LENGTH).lastIndexOf(' ');
        
        return lastSpaceBeforeMax > 0 ? lastSpaceBeforeMax : MAX_LINE_LENGTH;
    }
}