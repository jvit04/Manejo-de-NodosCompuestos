import java.util.Comparator;

public class CompararActividadesFechaEntrega implements Comparator<Actividad> {

    @Override
    public int compare(Actividad a1, Actividad a2) {
        if (a1.getFechaLimite() == null && a2.getFechaLimite() == null) return 0;
        if (a1.getFechaLimite() == null) return 1; // Nulos al final
        if (a2.getFechaLimite() == null) return -1;


        return a1.getFechaLimite().compareTo(a2.getFechaLimite());
    }
}