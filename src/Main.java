import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {

        ListaCompuesta<Estudiante, Entrega> L = new ListaCompuesta<>();
        NodoCompuesto <Estudiante, Entrega> p;
        ListaCompuesta<Actividad,Entrega> listaActividades = new ListaCompuesta<>();
        NodoCompuesto<Actividad,Entrega> actividadEntregaNodo;

        L.addToMainList(p = new NodoCompuesto<>(new Estudiante("Pepito", "Abad")));
        L.addToSecondaryList(p, new Entrega (30));
        L.addToSecondaryList(p, new Entrega (10));
        L.addToSecondaryList(p, new Entrega (100));
        L.addToMainList(p = new NodoCompuesto<>(new Estudiante("Luisito", "Comunica")));
        L.addToSecondaryList(p, new Entrega (20));
        L.addToSecondaryList(p, new Entrega (10));
        L.addToSecondaryList(p, new Entrega (6));
        L.addToMainList(p = new NodoCompuesto<>(new Estudiante("Josecito", "Noboa")));
        L.addToSecondaryList(p, new Entrega (6));
        L.addToSecondaryList(p, new Entrega (10));
        L.addToSecondaryList(p, new Entrega (100));
        System.out.println(L);
        Entrega ebuscar = new Entrega (15);
        ListaCompuesta<Estudiante, Entrega> Lnueva = L.searchMinorsSecondaryList(new compararEntregasxNotas(), ebuscar);
        System.out.println(Lnueva);


        // --- CREACIÓN DE DATOS ---

        // Actividad 1: Fecha pasada (Enero 2025)
        Actividad act1 = new Actividad("Taller 1", "Intro", LocalDate.of(2025, 1, 15), 50);
        listaActividades.addToMainList(actividadEntregaNodo = new NodoCompuesto<>(act1));
        // (Opcional) Agregar entregas a esta actividad...
        // listaActividades.addToSecondaryList(p, new Entrega(50));

        // Actividad 2: Fecha futura lejana (Diciembre 2026)
        Actividad act2 = new Actividad("Proyecto Final", "Java", LocalDate.of(2026, 12, 20), 100);
        listaActividades.addToMainList(actividadEntregaNodo = new NodoCompuesto<>(act2));

        // Actividad 3: Fecha cercana (Marzo 2026)
        Actividad act3 = new Actividad("Examen", "Teoría", LocalDate.of(2026, 3, 10), 20);
        listaActividades.addToMainList(actividadEntregaNodo = new NodoCompuesto<>(act3));


        System.out.println("=== LISTA COMPLETA ===");
        System.out.println(listaActividades);


        // Actividades anteriores al 1 de Junio de 2026
        LocalDate fechaCorte = LocalDate.of(2026, 6, 1);

        Actividad actividadReferencia = new Actividad("Referencia", "", fechaCorte, 0);

        System.out.println("\n=== BUSCANDO ACTIVIDADES ANTERIORES A: " + fechaCorte + " ===");

        CompararActividadesFechaEntrega comparador = new CompararActividadesFechaEntrega();

        ListaCompuesta<Actividad, Entrega> listaFiltrada = listaActividades.buscarActividadesVigentes(comparador, actividadReferencia);
        System.out.println(listaFiltrada);


        System.out.println("\n=== BUSCANDO ACTIVIDADES POSTERIORES A: " + fechaCorte + " ===");
        listaFiltrada = listaActividades.buscarActividadesVencidas(comparador, actividadReferencia);
        System.out.println(listaFiltrada);


        System.out.println(L.getHeader().getReferenciaLista().unirListasSinRepetidos(L.getTail().getReferenciaLista()));
        System.out.println(L.getHeader().getReferenciaLista().devolverInsterseccion(L.getTail().getReferenciaLista()));

    }
}

