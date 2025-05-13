package mantenimiento.codecounter.demo;

import mantenimiento.codecounter.models.comparators.STATUS;

public record LineRecord (
    STATUS status,
    String content
) {
    
}
