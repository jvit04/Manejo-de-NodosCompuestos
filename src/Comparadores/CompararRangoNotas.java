package Comparadores;

import ClasesPrincipales.Entrega;
import java.util.Comparator;

/**
 * Comparador que evalúa si la nota de una entrega se encuentra dentro de un rango numérico específico. Utiliza el constructor para recibir los límites del rango y devuelve 0 (que el TDA interpreta como una coincidencia) si la nota entra en dicho rango.
 */
public class CompararRangoNotas implements Comparator<Entrega> {
    private double min;
    private double max;

    /**
     * Constructor del comparador.
     * @param min La nota mínima esperada
     * @param max La nota máxima esperada
     */
    public CompararRangoNotas(double min, double max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public int compare(Entrega o1, Entrega o2) {
        // Ignoramos "o2"
        if (o1.getNota() >= min && o1.getNota() <= max) {
            return 0; // (Está dentro del rango)
        }
        return -1; // No está en el rango
    }
}