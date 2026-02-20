package Comparadores;

import ClasesPrincipales.Entrega;
import java.time.LocalDate;
import java.util.Comparator;

/**
 * Comparador que evalúa dos condiciones simultáneas:
 * 1. Que la entrega haya sido enviada dentro de un rango de fechas.
 * 2. Que AÚN NO haya recibido calificación (es decir, nota == -1).
 */
public class CompararEntregaSinNotaYFecha implements Comparator<Entrega> {
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    /**
     * Constructor del comparador.
     * @param fechaInicio La fecha de inicio del rango
     * @param fechaFin    La fecha límite del rango
     */
    public CompararEntregaSinNotaYFecha(LocalDate fechaInicio, LocalDate fechaFin) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    @Override
    public int compare(Entrega o1, Entrega o2) {
        // 1. Verificamos que NO tenga nota (-1 es sin calificar)
        if (o1.getNota() == -1.0) {
            // 2. Verificamos que tenga una fecha de entrega válida
            if (o1.getFechaEntrega() != null) {
                // 3. Verificamos si la fecha está entre inicio y fin
                boolean noEsAntes = !o1.getFechaEntrega().isBefore(fechaInicio);
                boolean noEsDespues = !o1.getFechaEntrega().isAfter(fechaFin);

                if (noEsAntes && noEsDespues) {
                    return 0; //
                }
            }
        }
        return 1; // Retornamos 1 si no cumple alguna de las condiciones
    }
}