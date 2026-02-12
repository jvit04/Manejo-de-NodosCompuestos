public class Main {
    public static void main(String[] args) {

        ListaCompuesta<Estudiante, Entrega> L = new ListaCompuesta<>();
        NodoCompuesto<Estudiante, Entrega> p;

        // Estudiante 1 y sus entregas
        L.add(p = new NodoCompuesto<>(new Estudiante("Gabriel", "Andrade")));
        L.addElementInSecondaryList(p, new Entrega(30));
        L.addElementInSecondaryList(p, new Entrega(50));
        
        // Estudiante 2 y sus entregas
        L.add(p = new NodoCompuesto<>(new Estudiante("Camila", "Aguirre")));
        // esto significa al nodo p: le agrego una nueva entrega. a una lista que crea el metodo automaticamente
        L.addElementInSecondaryList(p, new Entrega(20));
        L.addElementInSecondaryList(p, new Entrega(90));
        L.addElementInSecondaryList(p, new Entrega(20)); 

        L.add(p = new NodoCompuesto<>(new Estudiante("María", "Vélez")));
        L.addElementInSecondaryList(p, new Entrega(10));
        L.addElementInSecondaryList(p, new Entrega(40));
        L.addElementInSecondaryList(p, new Entrega(10)); 

        System.out.println("Lista Completa:");
        System.out.println(L);

        // Retornar todos los elementos de la lista principal cuyos elementos de l listas secundarias cumplan con un criterio dado (
        // Ej., dada una lista de Estudiantes, retornar una sublista de Estudiantes cuyas entregas tengan calificacion debajo de un cierto valor).
        Entrega ebuscar = new Entrega (50);
        ListaCompuesta<Estudiante, Entrega> Lnueva = L.buscarTodosMenoresEnListaSecundaria (new compararEntregasxNotas(), ebuscar);
        System.out.println("Buscar todos las entragas con notas menores que 50:");
        System.out.println(Lnueva);

        ListaCompuesta<Entrega, Estudiante> listaNotas1 = new ListaCompuesta<>();
        listaNotas1.add(new NodoCompuesto<>(new Entrega(10)));
        listaNotas1.add(new NodoCompuesto<>(new Entrega(20)));
        listaNotas1.add(new NodoCompuesto<>(new Entrega(30)));

        ListaCompuesta<Entrega, Estudiante> listaNotas2 = new ListaCompuesta<>();
        listaNotas2.add(new NodoCompuesto<>(new Entrega(20))); 
        listaNotas2.add(new NodoCompuesto<>(new Entrega(30))); 
        listaNotas2.add(new NodoCompuesto<>(new Entrega(40))); 

        compararEntregasxNotas compNotas = new compararEntregasxNotas();

        System.out.println("\nUnion de listas de notas (10,20,30) y (20,30,40)");
        System.out.println(listaNotas1.union(listaNotas2, compNotas));

        System.out.println("\nInterseccion de listas de notas (10,20,30) con (20,30,40)");
        System.out.println(listaNotas1.interseccion(listaNotas2, compNotas));

        System.out.println("\nEstudiantes con notas duplicadas:");
            
        ListaCompuesta<Estudiante, Entrega> repetidos = L.buscarEstudiantesConDuplicadosEnSecundaria(compNotas);
        
        System.out.println(repetidos);
    }
}
        
