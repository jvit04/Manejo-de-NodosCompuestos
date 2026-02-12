public class NodoCompuesto<E,F> {
    //Atributos del nodo compuesto
    private E data; //En este caso serán objetos Estudiantes o Actividades
    private NodoCompuesto<E,F> next; //Contiene la información del siguiente nodo
    private ListaCompuesta<F,F> referenciaLista; //Lista secundaria del Nodo, que contendrá datos tipo Entrega

    //Constructor
    public NodoCompuesto(E data) {
        this.data = data;
        this.next = null;
        this.referenciaLista = new ListaCompuesta<>();
    }

    //Getters y Setters
    public NodoCompuesto<E, F> getNext(){return this.next;}
    public void setNext(NodoCompuesto<E, F> nodo){this.next = nodo;}
    public void SetListaCompuesta(ListaCompuesta<F, F> lista){this.referenciaLista = lista;}
    public ListaCompuesta<F, F> getReferenciaLista(){return this.referenciaLista;}
    public E getData(){return this.data;}



}
