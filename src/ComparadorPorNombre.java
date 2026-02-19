import java.util.Comparator;

public class ComparadorPorNombre implements Comparator<Actividad> {
        @Override
        public int compare(Actividad o1, Actividad o2) {
            if (o1.getNombre().equalsIgnoreCase(o2.getNombre())){
            return 0;}
            else{
                return 1;
            }
        }
    }

