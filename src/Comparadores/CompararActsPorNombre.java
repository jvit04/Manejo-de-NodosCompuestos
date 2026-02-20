package Comparadores;
import ClasesPrincipales.Actividad;

import java.util.Comparator;
public class CompararActsPorNombre implements Comparator<Actividad> {
    @Override
    public int compare(Actividad o1, Actividad o2) {
        if (o1.getNombre().equalsIgnoreCase(o2.getNombre())){
            return 0;} // Si de nombre son iguales entonces retorna 0
        else{
            return 1; // Si no lo son, retorna 1
        }
    }
}
