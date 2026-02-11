import java.io.Flushable;
import java.util.Comparator;

public class ListaCompuesta <E,F> {
    private NodoCompuesto<E, F> header;
    private NodoCompuesto<E, F> tail;
    private int size;

    public ListaCompuesta() {
        this.header = null;
        this.tail = null;
        this.size = 0;
    }

    public void setHeader(NodoCompuesto<E, F> nodo) {
        this.header = nodo;
    }

    public void setTail(NodoCompuesto<E, F> nodo) {
        this.tail = nodo;
    }

    public NodoCompuesto<E, F> getHeader() {
        return this.header;
    }

    public NodoCompuesto<E, F> getTail() {
        return this.tail;
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void addToList(NodoCompuesto<E, F> nuevo) {
        if (this.header == null) {
            this.header = nuevo;
        } else {
            this.tail.setNext(nuevo);
        }
        this.tail = nuevo;
        this.size++;
    }

    public void addToNodesList(NodoCompuesto<E, F> nodo, F elemento) {

        if (nodo.getReferenciaLista() != null) {

        } else {
            nodo.SetListaCompuesta(new ListaCompuesta());
            // System.out.println("Este nodo no tiene una lista conectada");

        }
        nodo.getReferenciaLista().addToList(new NodoCompuesto(elemento));
    }

    public String toString() {
        String texto = "";
        for (NodoCompuesto<E, F> nodo = this.getHeader(); nodo != null; nodo = nodo.getNext()) {
            texto = texto + "\n" + nodo.getData() + " ";
            if (nodo.getReferenciaLista() != null)
                texto += "---->" + nodo.getReferenciaLista().toString();
        }
        return texto;
    }

    public NodoCompuesto<E, F> buscarPrimero(Comparator<E> c, E data) {
        E data1;
        for (NodoCompuesto<E, F> p = header; p != null; p = p.getNext()) {
            data1 = p.getData();
            if (c.compare(data1, data) < 0) return p;
        }
        return null;
    }

    public ListaCompuesta<E, F> buscarTodosMenoresEnListaSecundaria(Comparator<F> c, F data) {
        ListaCompuesta<E, F> nueva = new ListaCompuesta();
        for (NodoCompuesto<E, F> p = this.header; p != null; p = p.getNext()) {
            ListaCompuesta sublista = p.getReferenciaLista();
            if (sublista.buscarPrimero(c, data) != null)
                nueva.addToList(new NodoCompuesto<>(p.getData()));
        }
        return nueva;
    }

    public ListaCompuesta<E, F> buscarListaPrincipal(Comparator<E> c, E data) {
        ListaCompuesta<E, F> fechasMenores = new ListaCompuesta<>();
        E data1;
        for (NodoCompuesto<E, F> p = header; p != null; p = p.getNext()) {
            data1 = p.getData();
            if (c.compare(data1, data) < 0) {
                fechasMenores.addToList(new NodoCompuesto<>(data1));
            }
        }
        return fechasMenores;
    }

    public boolean isEmpty() {
        return this.size == 0 && this.header == null;
    }

    public NodoCompuesto<E, F> searchNode(NodoCompuesto<E, F> elemento) {
        for (NodoCompuesto<E, F> i = this.header; i != null; i = i.getNext()) {
            if (i.getData().equals(elemento.getData())) return i;
        }
        return null;
    }
    public boolean existeDato(ListaCompuesta<E,F> listaCompuesta,E data){
        for (NodoCompuesto<E,F> i =listaCompuesta.getHeader();i!=null;i=i.getNext()){
            if(i.getData().equals(data)){
                return true;
            }
        }
        return false;
    }



    public ListaCompuesta<E, F> unirListasSinRepetidos(ListaCompuesta<E,F> lista) {
        if (this.isEmpty()) return lista;
        if (lista.isEmpty()) return this;
        ListaCompuesta<E, F> nuevaLista = new ListaCompuesta<>();
        for (NodoCompuesto<E, F> i = this.getHeader(); i != null; i = i.getNext()) {
            if (!existeDato(nuevaLista,i.getData())) {
                nuevaLista.addToList(new NodoCompuesto<>(i.getData()));
                nuevaLista.size++;
            }
        }
        for (NodoCompuesto<E, F> i = lista.getHeader(); i != null; i = i.getNext()) {
            if (!existeDato(nuevaLista,i.getData())) {
                nuevaLista.addToList(new NodoCompuesto<>(i.getData()));
                nuevaLista.size++;
            }
        }
        return nuevaLista;
    }

    public ListaCompuesta<E,F> devolverInsterseccion(ListaCompuesta<E,F> lista){
        ListaCompuesta<E,F> unidaSinRepeticion = this.unirListasSinRepetidos(lista);
        ListaCompuesta <E,F> interserccion = new ListaCompuesta<>();
        for(NodoCompuesto <E,F> i = unidaSinRepeticion.header; i!=null; i=i.getNext()){
            if(unidaSinRepeticion.existeDato(this,i.getData())&& unidaSinRepeticion.existeDato(lista, i.getData())){
                interserccion.addToList(i);
            }
        }
        return interserccion;
    }


    }


