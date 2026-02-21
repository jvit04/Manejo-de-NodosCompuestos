import java.io.Serializable;
import java.util.Comparator;

class ListaCompuesta <E, F> implements Serializable {

    private NodoCompuesto<E, F> header;
    private NodoCompuesto<E, F> tail;
    private int size;

    public void setHeader(NodoCompuesto<E, F> nodo){this.header = nodo;}
    public void setTail(NodoCompuesto<E, F> nodo){this.tail = nodo;}
    public NodoCompuesto<E, F> getHeader(){return this.header;}
    public NodoCompuesto<E, F> getTail(){return this.tail;}
    public int getSize(){return this.size;}

    public ListaCompuesta(){
        setHeader(null);
        setTail(null);
        this.size = 0;

    }

    // Metodo que permita inserar elementos al final de una ListaCompuesta. 
    public void add(NodoCompuesto<E, F> nodo){
        if (size == 0){
            setHeader(nodo);
            setTail(nodo);
        }
        else {
            getTail().setNext(nodo);
            setTail(nodo);
        }
        size++;
    }


    // Metodo que permita insertar elementos al final de una lista secundaria de un nodo dentro de una ListaCompuesta
    public void addElementInSecondaryList(NodoCompuesto<E, F> nodo, F elemento){
    // verificamos si existe la lista
    if (nodo.getReferenciaLista() != null){
    }
    else {
        // si no existe, entonces le creamos una referencia a una nueva lista de tipo f
        nodo.SetListaCompuesta(new ListaCompuesta<F, F>());          
    }
    // como nuestra lista tiene de tipo principal f entonces el nodo debera ser de tipo F
    nodo.getReferenciaLista().add(new NodoCompuesto<F, F>(elemento));
    }

    @Override
    public String toString(){
        String texto = "";
        // Esto tomara el elemento principal.
        for (NodoCompuesto<E, F> nodo = this.getHeader(); nodo != null; nodo = nodo.getNext()){
            texto = texto + "\n" + nodo.getData() + " ";
            if (nodo.getReferenciaLista()!=null) // si es que no tiene una sublista vacia enotnces:
                texto += ":" + nodo.getReferenciaLista().toString();
        }
        return texto;
    }

    // nos devuelve la primera aparicion de un nodo que contenga la E data
    public NodoCompuesto<E,F> buscarPrimero (Comparator<E> c, E data){
        E data1;
        for (NodoCompuesto<E, F> p = header; p!=null; p=p.getNext()){
            data1 = p.getData();
            // if 
            if(c.compare(data1, data)< 0) return p;
        }
        return null;
    }
        
    public ListaCompuesta<E, F> buscarTodosMenoresEnListaSecundaria(Comparator<F> c, F data){        
        ListaCompuesta<E, F> nueva = new ListaCompuesta<>();
        for (NodoCompuesto<E, F>p = this.header; p!=null; p=p.getNext()) {
            ListaCompuesta<F, F> sublista = p.getReferenciaLista();
            if(sublista!=null){
                if(sublista.buscarPrimero(c, data)!=null)
                    nueva.add(new NodoCompuesto<>(p.getData()));
            }
        }
        return nueva;}

    public ListaCompuesta<F,F> buscarIgualesEnListaSecundaria(Comparator<F> c, F data){
        ListaCompuesta<F, F> nueva = new ListaCompuesta<>();
        for (NodoCompuesto<E, F>p = this.header; p!=null; p=p.getNext()) {
            ListaCompuesta<F, F> sublista = p.getReferenciaLista();
            if(sublista!=null){
                for (NodoCompuesto<F,F> i = sublista.header;i!=null;i=i.getNext()){
                    if(c.compare(i.getData(), data)== 0) {
                        nueva.add(new NodoCompuesto<>(i.getData()));
                    }
                }

            }
        }
        return nueva;
    }


//metodo que devuelve valores menores de la lista principal
    public ListaCompuesta<E,F> buscarMenoresPrincipal(Comparator<E> c,E data){
        ListaCompuesta<E,F> nueva = new ListaCompuesta<>();
        for (NodoCompuesto<E, F> p = header; p!=null; p=p.getNext()){
            // if que busca valores menores
            if(c.compare(p.getData(), data)< 0) nueva.add(new NodoCompuesto<>(p.getData()));
        }
        return nueva;
    }
    //metodo que devuelve valores mayores de la lista principal
    public ListaCompuesta<E,F> buscarMayoresPrincipal(Comparator<E> c,E data){
        ListaCompuesta<E,F> nueva = new ListaCompuesta<>();
        for (NodoCompuesto<E, F> p = header; p!=null; p=p.getNext()){
            // if que busca valores mayores
            if(c.compare(p.getData(), data)> 0) {
                NodoCompuesto<E, F> nodoNuevo = new NodoCompuesto<>(p.getData());
                //Le pasamos la referencia de la sublista del nodo original
                nodoNuevo.SetListaCompuesta(p.getReferenciaLista());
                //lo agregamos
                nueva.add(nodoNuevo);
            }

        }
        return nueva;
    }

    // retornar todos los elementos de la lista principal que coincidan con
    public ListaCompuesta<E, F> buscarIgualesPrincipal(Comparator<E> c, E data) {
        ListaCompuesta<E, F> resultado = new ListaCompuesta<>(); // la que vamos a devolver
        for (NodoCompuesto<E, F> p = this.header; p != null; p = p.getNext()) {
            // comparamos el dato principal
            if (c.compare(p.getData(), data) == 0) {
                // Si coincide, lo agregamos a la lista resultado
                resultado.add(new NodoCompuesto<>(p.getData()));
            }
        }
        return resultado;
    }

    // Metodo buscarExacto MEJORADO (a prueba de nulos)
    public NodoCompuesto<E,F> buscarExacto(Comparator<E> comparador, E elementoBusqueda){
        if (this.isEmpty() || elementoBusqueda == null) {
            return null; // Si la lista está vacía o me pasan un nulo, devuelvo nulo directamente
        }
        for (NodoCompuesto<E,F> i = this.getHeader(); i != null; i = i.getNext()) {
            if(comparador.compare(i.getData(), elementoBusqueda) == 0){
                return i;
            }
        }
        return null;
    }

    // retornar la union de dos lsitas sin elementos repetidos
    public ListaCompuesta<E, F> union(ListaCompuesta<E, F> lista2, Comparator<E> c) {
        ListaCompuesta<E, F> resultado = new ListaCompuesta<>();
        // primero agregamos todo de la lista actual
        for (NodoCompuesto<E, F> p = this.header; p != null; p = p.getNext()) {
            resultado.add(new NodoCompuesto<>(p.getData()));
        }
        // agregamos lo de la otra lista solo si NO está ya en resultado
        if (lista2 != null) {
            for (NodoCompuesto<E, F> q = lista2.getHeader(); q != null; q = q.getNext()) {
                // usamos buscarPrimero para ver si ya existe
                if (resultado.buscarExacto(c, q.getData()) == null) {
                    // solo si buscar primero no nos devuelve un nodo, es decir no se encuentra en la lista, lo agregamos
                    resultado.add(new NodoCompuesto<>(q.getData()));
                }
            }
        }
        return resultado;
    }
    
    // la interseccion seria los elementos que coincicen
    public ListaCompuesta<E, F> interseccion(ListaCompuesta<E, F> lista2, Comparator<E> c) {
        ListaCompuesta<E, F> resultado = new ListaCompuesta<>();

        if (lista2 == null) return resultado; // Si una es nula, no hay intersección

        // recorremos la lista actual
        for (NodoCompuesto<E, F> p = this.header; p != null; p = p.getNext()) {
            // si el elemento 'p' existen en la otra lista entonces se intersecta, y lo agregamos
            if (lista2.buscarExacto(c, p.getData()) != null) {
                resultado.add(new NodoCompuesto<>(p.getData()));
            }
        }
        return resultado;
    }
    public ListaCompuesta<E, F> buscarEstudiantesConDuplicadosEnSecundaria(Comparator<F> criterioDeIgualdad) {
        ListaCompuesta<E, F> resultados = new ListaCompuesta<>();
        // Recorro los estudiantes
        for (NodoCompuesto<E, F> actualEstudiante = this.header; actualEstudiante != null; actualEstudiante = actualEstudiante.getNext()) {
            // creo una lista de las entregas de ESE estudiante
            ListaCompuesta<F, F> listaEntregas = actualEstudiante.getReferenciaLista();
            boolean tieneDuplicado = false;
            if (listaEntregas != null) {
                // 2. recorro sus entregas 
                // Se detiene si llegamos al final O si ya encontramos un duplicado 
                for (NodoCompuesto<F, F> nodoA = listaEntregas.getHeader(); nodoA != null && !tieneDuplicado; nodoA = nodoA.getNext()) {
                    // tengo que comparar ese nodo a con TODOS los demas nodos, por eso otro for.
                    for (NodoCompuesto<F, F> nodoB = nodoA.getNext(); nodoB != null; nodoB = nodoB.getNext()) {
                        if (criterioDeIgualdad.compare(nodoA.getData(), nodoB.getData()) == 0) {
                            tieneDuplicado = true;
                            break; // rompe el for interno
                        }
                    }
                }
            }
            // si hubo duplicado, agregamos al estudiante al resultado
            if (tieneDuplicado) {
                resultados.add(new NodoCompuesto<>(actualEstudiante.getData()));
            }
        }
        return resultados;
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
                listaResultado.add(nodoEstudiante);}
            actual = actual.getNext();}
        return listaResultado;}


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


    public boolean isEmpty() {
        return this.size == 0 && this.header == null;}
        //Este metodo está diseñado para funcionar con el comparador de la clase CompararActividadesFechaEntrega
        //Su función es devolver una lista de las actividades que sean posteriores a una fecha dada.


    // Metodo diferencia MEJORADO (a prueba de listas vacías)
    //Metodo que devuelve la diferencia entre 2 listas. Servirá para encontrar las Entregas Incompletas.
    public ListaCompuesta<E,F> diferencia(ListaCompuesta<E,F> lista2, Comparator<E> comparator){
        ListaCompuesta<E,F> diferencia = new ListaCompuesta<>();

        // VALIDACIÓN 1: Si mi lista principal está vacía, no hay nada que comparar.
        if (this.isEmpty()) {
            return diferencia; // Retorna lista vacía
        }

        // VALIDACIÓN 2: Si la lista2 es nula o está vacía, la diferencia es TODA mi lista principal.
        if (lista2 == null || lista2.isEmpty()) {
            for (NodoCompuesto<E,F> i = this.getHeader(); i != null; i = i.getNext()) {
                diferencia.add(new NodoCompuesto<>(i.getData()));
            }
            return diferencia;
        }

        // Recorrido normal si ambas listas tienen datos
        for (NodoCompuesto<E,F> i = this.getHeader(); i != null; i = i.getNext()) {
            E datoActual = i.getData();
            if(lista2.buscarExacto(comparator, datoActual) == null){
                diferencia.add(new NodoCompuesto<>(datoActual));
            }
        }
        return diferencia;
    }

 }







