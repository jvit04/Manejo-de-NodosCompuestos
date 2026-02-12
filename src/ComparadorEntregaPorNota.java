import java.util.Comparator;

public class ComparadorEntregaPorNota implements Comparator<Entrega> {
    @Override
    public int compare(Entrega e1, Entrega e2) {
        // Usamos Double.compare para evitar problemas con decimales
        // Devuelve 0 si son iguales
        return Double.compare(e1.getNota(), e2.getNota());
    }
}