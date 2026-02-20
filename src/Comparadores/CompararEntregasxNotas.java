package Comparadores;
import ClasesPrincipales.Entrega;

import java.util.Comparator;
//Clase trabajada en clase, comparador que evalúa notas (atributo de ClasesPrincipales.Entrega)
public class CompararEntregasxNotas implements Comparator<Entrega>{
    public int compare(Entrega a, Entrega b){
        if (a.getNota()==b.getNota()) return 0;
        if (a.getNota() > b.getNota()) return 1;
        return -1;
    }
}
