package mantenimiento.codecounter.models;

import mantenimiento.codecounter.models.comparators.Status;

/**
 * Representación de un registro de la línea de código posterior a una comparación
 * @param status estado de la línea posterior a la evaluación
 * @param content contenido de la línea de código
 */
public record LineRecord (Status status, String content) {}
