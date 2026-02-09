public class NodoCompuesto<E,F> {
    private E data;
    private NodoCompuesto<E,F> next;
    private ListaCompuesta<E,F> referenciaLista;

    public NodoCompuesto(E data) {
        this.data = data;
        this.next = null;
        this.referenciaLista = new ListaCompuesta<>();
    }

    public NodoCompuesto<E, F> getNext(){return this.next;}
    public void setNext(NodoCompuesto<E, F> nodo){this.next = nodo;}
    public void SetListaCompuesta(ListaCompuesta<E, F> lista){this.referenciaLista = lista;}
    public ListaCompuesta<E, F> getReferenciaLista(){return this.referenciaLista;}
    public E getData(){return this.data;}


    public ListaCompuesta<E, F>  unirListasSecundarias(NodoCompuesto<E, F> nodo) {
        ListaCompuesta<E, F> nuevaLista = new ListaCompuesta<>();
        if (this.getReferenciaLista().isEmpty()) return nodo.getReferenciaLista();
        if (nodo.getReferenciaLista().isEmpty()) return this.getReferenciaLista();
        NodoCompuesto<E,F> actual1 = this.referenciaLista.getHeader();
        NodoCompuesto<E,F> actual2 = this.referenciaLista.getHeader();

        while(actual1!=null && actual2!=null){
            if (actual1!=null){
                nuevaLista.addToList(actual1);
                actual1 =  actual1.getNext();
            }
            if (actual2!=null){
                nuevaLista.addToList(actual2);
                actual2 =  actual2.getNext();
            }
        }

        return nuevaLista;
    }


}
