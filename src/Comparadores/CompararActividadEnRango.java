// Nuevo archivo: CompararActividadEnRango.java
package Comparadores;
import ClasesPrincipales.Actividad;
import java.time.LocalDate;
import java.util.Comparator;

public class CompararActividadEnRango implements Comparator<Actividad> {
    private LocalDate inicio;
    private LocalDate fin;

    public CompararActividadEnRango(LocalDate inicio, LocalDate fin) {
        this.inicio = inicio;
        this.fin = fin;
    }

    @Override
    public int compare(Actividad o1, Actividad dummy) {
        // Asumiendo que usas fechaLimite (o fechaEnvio si la agregaste al txt)
        LocalDate fecha = o1.getFechaLimite();
        if (fecha != null && !fecha.isBefore(inicio) && !fecha.isAfter(fin)) {
            return 0; // Está en el rango
        }
        return -1; // Fuera del rango
    }
}