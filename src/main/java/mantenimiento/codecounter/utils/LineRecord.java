package mantenimiento.codecounter.utils;

import mantenimiento.codecounter.models.comparators.Status;

public record LineRecord (
    Status status,
    String content
) {
    
}
