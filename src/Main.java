import java.time.LocalDate;

public class Main {
    // 1. Cargar Estudiantes y Actividades
    private static ListaCompuesta<Estudiante, Entrega> listaEstudiantes = CargadorDeArchivos.cargarEstudiantes("src/estudiantes.txt");
    private static ListaCompuesta<Actividad, Entrega> listaActividades = CargadorDeArchivos.cargarActividades("src/actividades.txt");

    public static void main(String[] args) {

        System.out.println("=== INICIANDO SISTEMA CON CARGA DE ARCHIVOS ===");


        if (listaEstudiantes.getSize() == 0 || listaActividades.getSize() == 0) {
            System.out.println("[!] ERROR: Faltan datos base (estudiantes o actividades).");
            return;
        }
        System.out.println("-> Base cargada: " + listaEstudiantes.getSize() + " estudiantes, " + listaActividades.getSize() + " actividades.");

        // 2. CARGAR ENTREGAS
        // Esto leerá entregas.txt y llenará las sublistas de los estudiantes automáticamente
        CargadorDeArchivos.cargarEntregas("src/entregas.txt", listaEstudiantes, listaActividades);


        // =============================================================
        //              ZONA DE PRUEBAS DE TUS FUNCIONES
        // =============================================================

        // PRUEBA A: Actividades Vencidas / Vigentes
        System.out.println("\n[A] PRUEBA DE FECHAS (Corte: 01-Abril-2026)");
        LocalDate fechaCorte = LocalDate.of(2026, 4, 1);
        CompararActividadesFechaEntrega compFechas = new CompararActividadesFechaEntrega();
        Actividad actRef = new Actividad("Ref", "Ref", fechaCorte, 0, "Ref");

        ListaCompuesta<Actividad, Entrega> vencidas = listaActividades.buscarMayoresPrincipal(compFechas, actRef);
        System.out.println("   - Actividades POSTERIORES a la fecha (Futuras): " + vencidas.getSize());

        ListaCompuesta<Actividad, Entrega> vigentes = listaActividades.buscarMenoresPrincipal(compFechas, actRef);
        System.out.println("   - Actividades ANTERIORES a la fecha (Pasadas): " + vigentes.getSize());


        // PRUEBA B: Estudiantes con Entregas Duplicadas
        // (En el txt puse a Juan Perez y Ana Gomez con notas repetidas a propósito)
        System.out.println("\n[B] PRUEBA DE DUPLICADOS");
        CompararEntregasxNotas compNotas = new CompararEntregasxNotas();

        ListaCompuesta<Estudiante, Entrega> repetidos = listaEstudiantes.buscarEstudiantesConDuplicadosEnSecundaria(compNotas);

        System.out.println("   Estudiantes con notas idénticas en sus entregas:");
        if (!repetidos.isEmpty()) {
            System.out.println(repetidos); // Debería salir Juan Perez y Ana Gomez según mi txt
        } else {
            System.out.println("   No se encontraron duplicados.");
        }


        // PRUEBA C: Rendimiento por Porcentaje
        System.out.println("\n[C] PRUEBA DE PORCENTAJE DE CUMPLIMIENTO");
        // En el txt, Maria Lopez tiene 5 entregas. Si hay 10 actividades en total, tiene 50%.
        int totalActividades = listaActividades.getSize();
        double min = 10.0;
        double max = 60.0; // Buscamos gente que tenga entre 10% y 60% de avance

        System.out.println("   Buscando estudiantes con avance entre " + min + "% y " + max + "%:");

        ListaCompuesta<Estudiante, Entrega> cumplidos = listaEstudiantes.buscarPorPorcentajeEntrega(totalActividades, min, max);

        NodoCompuesto<Estudiante, Entrega> actual = cumplidos.getHeader();
        while (actual != null) {
            int n = actual.getReferenciaLista().getSize();
            double p = ((double) n / totalActividades) * 100;
            System.out.println("   -> " + actual.getData().getNombre() + " " + actual.getData().getApellido()
                    + " (Entregas: " + n + " | " + String.format("%.1f", p) + "%)");
            actual = actual.getNext();
        }

        System.out.println("------------------Buscar Calificaciones con calificaciones menores---------------------------");
        Entrega entrega = new Entrega(7);
        CompararEntregasxNotas compararEntregasxNotas = new CompararEntregasxNotas();
        System.out.println("\n---Retornando estudiantes---");
        System.out.println(listaEstudiantes.buscarTodosMenoresEnListaSecundaria(compararEntregasxNotas, entrega));
        System.out.println("\n---Retornando actividades---");
        System.out.println(listaActividades.buscarTodosMenoresEnListaSecundaria(compararEntregasxNotas, entrega));


        System.out.println("------------------Buscar Entregas Vencidas sin nota---------------------------");
        Entrega entrega1 = new Entrega(-1);//Notas con valor -1 significan nulas.
        System.out.println(vencidas.buscarIgualesEnListaSecundaria(compararEntregasxNotas, entrega1));

        System.out.println("------------------Diferencia---------------------------");
        ComparadorPorNombre comparadorPorNombre = new ComparadorPorNombre();
        NodoCompuesto<Estudiante, Entrega> estudianteActual = listaEstudiantes.getHeader();
        // 1. Obtenemos su lista de entregas (Tipo Entrega)
        ListaCompuesta<Entrega, Entrega> entregasDelEstudiante = estudianteActual.getReferenciaLista();

// 2. Creamos una lista TEMPORAL de Actividades (para igualar los tipos)
        ListaCompuesta<Actividad, Entrega> actividadesQueSiEntrego = new ListaCompuesta<>();

        NodoCompuesto<Entrega, Entrega> nodoEnt = entregasDelEstudiante.getHeader();
        while (nodoEnt != null) {
            entrega = nodoEnt.getData();

            // Creamos una "Actividad fantasma" usando el nombre que viene en la entrega.
            // Para esto se ajusto el constructor para que se pueda crear actividades con solo el nombre de la actividad.
            Actividad actTemporal = new Actividad(entrega.getActividad().getNombre());

            actividadesQueSiEntrego.add(new NodoCompuesto<>(actTemporal));
            nodoEnt = nodoEnt.getNext();
        }

// 3. Como ambas listas son de tipo <Actividad, Entrega>, el método diferencia funcionará perfecto
        ListaCompuesta<Actividad, Entrega> actividadesFaltantes = listaActividades.diferencia(actividadesQueSiEntrego, comparadorPorNombre);

// 4. Imprimir el resultado
        System.out.println("Al estudiante " + estudianteActual.getData().getNombre() + " le faltan estas actividades:");
        NodoCompuesto<Actividad, Entrega> nodoFaltante = actividadesFaltantes.getHeader();
        while (nodoFaltante != null) {
            System.out.println("- " + nodoFaltante.getData().getNombre());
            nodoFaltante = nodoFaltante.getNext();
        }

        System.out.println("\n-Diferencia del segundo estudiante de la lista-");
        //Ahora probemos la diferencia con otro estudiante, tomaremos el ultimo de la lista de estudiantes
        NodoCompuesto<Estudiante, Entrega> estudianteActual2 = listaEstudiantes.getHeader().getNext();
        // 1. Obtenemos su lista de entregas (Tipo Entrega)
        ListaCompuesta<Entrega, Entrega> entregasDelEstudiante2 = estudianteActual2.getReferenciaLista();

// 2. Creamos una lista TEMPORAL de Actividades (para igualar los tipos)
        ListaCompuesta<Actividad, Entrega> actividadesQueSiEntrego2 = new ListaCompuesta<>();

        NodoCompuesto<Entrega, Entrega> nodoEnt2 = entregasDelEstudiante2.getHeader();
        while (nodoEnt2 != null) {
            entrega = nodoEnt2.getData();

            // Creamos una "Actividad fantasma" usando el nombre que viene en la entrega.
            // Para esto se ajusto el constructor para que se pueda crear actividades con solo el nombre de la actividad.
            Actividad actTemporal2 = new Actividad(entrega.getActividad().getNombre());

            actividadesQueSiEntrego2.add(new NodoCompuesto<>(actTemporal2));
            nodoEnt2 = nodoEnt2.getNext();
        }

// 3. Como ambas listas son de tipo <Actividad, Entrega>, el método diferencia funcionará perfecto
        ListaCompuesta<Actividad, Entrega> actividadesFaltantes2 = listaActividades.diferencia(actividadesQueSiEntrego2, comparadorPorNombre);

// 4. Imprimir el resultado
        System.out.println("Al estudiante " + estudianteActual2.getData().getNombre() + " le faltan estas actividades:");
        NodoCompuesto<Actividad, Entrega> nodoFaltante2 = actividadesFaltantes2.getHeader();
        while (nodoFaltante2 != null) {
            System.out.println("- " + nodoFaltante2.getData().getNombre());
            nodoFaltante2 = nodoFaltante2.getNext();
        }
    }


    }