import java.util.Comparator;
//Clase trabajada en clase, comparador que evalúa notas (atributo de Entrega)
class CompararEntregasxNotas implements Comparator<Entrega>{
    public int compare(Entrega a, Entrega b){
        if (a.getNota()==b.getNota()) return 0;
        if (a.getNota() > b.getNota()) return 1;
        return -1;
    }
}
