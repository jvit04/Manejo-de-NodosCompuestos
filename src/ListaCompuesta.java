import java.util.Comparator;

public class ListaCompuesta <E,F> {
    //Atributos
    private NodoCompuesto<E, F> header;
    private NodoCompuesto<E, F> tail;
    private int size;

    //Constructor vacío
    public ListaCompuesta() {
        this.header = null;
        this.tail = null;
        this.size = 0;
    }

//Getters y setters
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

    //Metodo toString
    public String toString() {
        String texto = "";
        for (NodoCompuesto<E, F> nodo = this.getHeader(); nodo != null; nodo = nodo.getNext()) {
            texto = texto + "\n" + nodo.getData() + " ";
            if (!nodo.getReferenciaLista().isEmpty())
                texto += "---->" + nodo.getReferenciaLista().toString();
        }
        return texto;
    }

    //Metodo que devuelve boolean verificando si la lista está vacía o no.
    public boolean isEmpty() {
        return this.size == 0 && this.header == null;
    }

    //Metodo que permite agregar nodos a la lista principal ya sea de estudiantes o actividades
    public void addToMainList(NodoCompuesto<E, F> nuevo) {
        if (this.header == null) {
            this.header = nuevo;
        } else {
            this.tail.setNext(nuevo);
        }
        this.tail = nuevo;
        this.size++;
    }

    //Metodo que permite agregar en la lista secundaria, es decir, la lista que poseen los nodos de actividades o estudiantes.
    //Estas listas contienen objetos tipo entrega.
    public void addToSecondaryList(NodoCompuesto<E, F> nodo, F elemento) {
        if (nodo.getReferenciaLista() != null) {
        } else {
            nodo.SetListaCompuesta(new ListaCompuesta());
        }
        nodo.getReferenciaLista().addToMainList(new NodoCompuesto(elemento));
    }


//Este metodo devuelve el primer elemento que cumpla la condición favorecida por el comparador creado "c".
    //Se realizó en clase con los estudiantes con alguna nota inferior a un puntaje dado "data".
    public NodoCompuesto<E, F> searchFirstMinor(Comparator<E> c, E data) {
        E data1;
        for (NodoCompuesto<E, F> p = this.header; p != null; p = p.getNext()) {
            data1 = p.getData();
            if (c.compare(data1, data) < 0) return p;
        }
        return null;
    }


    //Función que toma el comparador<F> y F data para tomar los elementos de una lista secundaria.
    //Es útil porque me permite acceder a las entregas compararlas y devolver el resultado
    //que puede ser la misma entrega o el nodo principal sea Estudiante o Actividad.
    public ListaCompuesta<E, F> searchMinorsSecondaryList(Comparator<F> c, F data) {
        ListaCompuesta<E, F> nueva = new ListaCompuesta();
        for (NodoCompuesto<E, F> p = this.header; p != null; p = p.getNext()) {
            ListaCompuesta sublista = p.getReferenciaLista();
            if (sublista.searchFirstMinor(c, data) != null)
                nueva.addToMainList(new NodoCompuesto<>(p.getData()));
        }
        return nueva;
    }

//Este metodo está diseñado para funcionar con el comparador de la clase CompararActividadesFechaEntrega
    //Su función es devolver una lista de las actividades que sean anteriores a una fecha dada.
    public ListaCompuesta<E, F> buscarActividadesVigentes(Comparator<E> c, E data) {
        ListaCompuesta<E, F> fechasMenores = new ListaCompuesta<>();
        E data1;
        for (NodoCompuesto<E, F> p = header; p != null; p = p.getNext()) {
            data1 = p.getData();
            if (c.compare(data1, data) < 0) {
                fechasMenores.addToMainList(new NodoCompuesto<>(data1));
            }
        }
        return fechasMenores;
    }

    //Este metodo está diseñado para funcionar con el comparador de la clase CompararActividadesFechaEntrega
    //Su función es devolver una lista de las actividades que sean posteriores a una fecha dada.
    public ListaCompuesta<E, F> buscarActividadesVencidas(Comparator<E> c, E data) {
        ListaCompuesta<E, F> fechasMayores = new ListaCompuesta<>();
        E data1;
        for (NodoCompuesto<E, F> p = header; p != null; p = p.getNext()) {
            data1 = p.getData();
            if (c.compare(data1, data) > 0) {
                fechasMayores.addToMainList(new NodoCompuesto<>(data1));
            }
        }
        return fechasMayores;
    }


    //Método trabajado en clase que devuelve el nodo si lo encuentra en una lista.
    public NodoCompuesto<E, F> searchNode(NodoCompuesto<E, F> elemento) {
        for (NodoCompuesto<E, F> i = this.header; i != null; i = i.getNext()) {
            if (i.getData().equals(elemento.getData())) return i;
        }
        return null;
    }

    //Método que devuelve boolean si detecta que un elemento esta en una lista o no.
    public boolean existeDato(ListaCompuesta<E,F> listaCompuesta,E data){
        for (NodoCompuesto<E,F> i =listaCompuesta.getHeader();i!=null;i=i.getNext()){
            if(i.getData().equals(data)){
                return true;
            }
        }
        return false;
    }


//Método que retorna una lista de la unión de dos listas pero SIN repetidos.
    public ListaCompuesta<E, F> unirListasSinRepetidos(ListaCompuesta<E,F> lista) {
        if (this.isEmpty()) return lista;
        if (lista.isEmpty()) return this;
        ListaCompuesta<E, F> nuevaLista = new ListaCompuesta<>();
        for (NodoCompuesto<E, F> i = this.getHeader(); i != null; i = i.getNext()) {
            if (!existeDato(nuevaLista,i.getData())) {
                nuevaLista.addToMainList(new NodoCompuesto<>(i.getData()));
                nuevaLista.size++;
            }
        }
        for (NodoCompuesto<E, F> i = lista.getHeader(); i != null; i = i.getNext()) {
            if (!existeDato(nuevaLista,i.getData())) {
                nuevaLista.addToMainList(new NodoCompuesto<>(i.getData()));
                nuevaLista.size++;
            }
        }
        return nuevaLista;
    }

    //Método que devuelve la interseccion de dos listas, es decir, une dos listas en una de retorno,
    //pero esta solo cuenta con los elementos que se encuentran en ambas listas.
    public ListaCompuesta<E,F> devolverInsterseccion(ListaCompuesta<E,F> lista){
        ListaCompuesta<E,F> unidaSinRepeticion = this.unirListasSinRepetidos(lista);
        ListaCompuesta <E,F> interserccion = new ListaCompuesta<>();
        for(NodoCompuesto <E,F> i = unidaSinRepeticion.header; i!=null; i=i.getNext()){
            if(unidaSinRepeticion.existeDato(this,i.getData())&& unidaSinRepeticion.existeDato(lista, i.getData())){
                interserccion.addToMainList(i);
            }
        }
        return interserccion;
    }

    //Método trabajado en clase, devuelve el nodo anterior a un nodo dado como parametro EN la lista PRINCIPAL
    public NodoCompuesto <E,F> getAnteriorListaPrincipal(NodoCompuesto <E,F> elemento) {
        for (NodoCompuesto <E,F> i = this.getHeader(); i != null; i = i.getNext()) {
            if(i.getNext() == null) return null;
            if (i.getNext().getData().equals(elemento.getData())) {
                return i;
            }
        }
        return null;
    }


    //Método trabajado en clase, devuelve el nodo anterior a un nodo dado como parametro EN la lista SECUNDARIA
    public NodoCompuesto <E,F> getAnteriorListaSecundaria(ListaCompuesta<E,F> lista, NodoCompuesto <E,F> nodo){
        return lista.getAnteriorListaPrincipal(nodo);
    }


    public ListaCompuesta<E, F> buscarPorPorcentajeEntrega(int totalActividades, double minPorcentaje, double maxPorcentaje) {
        ListaCompuesta<E, F> listaResultado = new ListaCompuesta<>();
        NodoCompuesto<E, F> actual = this.header;
        while (actual != null) {
            int entregasRealizadas = 0;
            if (actual.getReferenciaLista() != null) {
                entregasRealizadas = actual.getReferenciaLista().getSize();}
            double porcentaje = ((double) entregasRealizadas / totalActividades) * 100.0;
            if (porcentaje >= minPorcentaje && porcentaje <= maxPorcentaje) {
                NodoCompuesto<E, F> nodoEstudiante = new NodoCompuesto<>(actual.getData());
                nodoEstudiante.SetListaCompuesta(actual.getReferenciaLista());
                listaResultado.addToMainList(nodoEstudiante);}
            actual = actual.getNext();}
        return listaResultado;}



    public ListaCompuesta<E, F> buscarEstudiantesConDuplicadosEnSecundaria(Comparator<F> criterioDeIgualdad) {
        if(this.isEmpty())return null;
        ListaCompuesta<E, F> resultados = new ListaCompuesta<>();

        // 1. Recorremos la lista principal la cual es 'Estudiantes'
        for (NodoCompuesto<E,F> i = this.header; i!=null; i=i.getNext()) {
            if(i.getReferenciaLista().isEmpty()) continue;
            for (NodoCompuesto<F,F> j = i.getReferenciaLista().getHeader(); j!=null; j=j.getNext()) {
                if (!i.equals(j) && criterioDeIgualdad.compare(i,j))
            }
        }

        return resultados;
    }


    }


