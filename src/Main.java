
import java.util.Comparator;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("       SISTEMA DE GESTIÓN ACADÉMICA (TEST)       ");
        System.out.println("=================================================");

        // ---------------------------------------------------------------
        // 1. CARGA DE DATOS DESDE LOS ARCHIVOS TXT
        // ---------------------------------------------------------------
        // Asegúrate de que los archivos estén en la raíz del proyecto o ajusta la ruta (ej: "src/estudiantes.txt")
        ListaCompuesta<Estudiante, Entrega> listaCurso = CargadorDeArchivos.cargarEstudiantes("estudiantes.txt");

        // Obtenemos la lista de nombres de actividades reales de tu archivo
        ListaCompuesta<Actividad,Entrega> listaActividades = CargadorDeArchivos.cargarActividades("actividades.txt");
        int totalActividadesCurso = listaActividades.getSize(); // Deberían ser 10 según tu archivo

        if (listaCurso.isEmpty() || totalActividadesCurso == 0) {
            System.out.println("[ERROR] No se cargaron datos. Revisa las rutas de los archivos.");
            return;
        }

        // ---------------------------------------------------------------
        // 2. SIMULACIÓN DE ENTREGAS (ASIGNAR TAREAS A LOS ESTUDIANTES)
        // ---------------------------------------------------------------
        System.out.println("\n[INFO] Simulando entregas para los estudiantes cargados...");

        // Vamos a recorrer la lista y asignar entregas aleatorias para probar
        NodoCompuesto<Estudiante, Entrega> actual = listaCurso.getHeader();
        int contador = 0;

        while (actual != null) {
            Estudiante est = actual.getData();

            // CASO A: Los primeros 2 estudiantes (Juan y Maria) son APLICADOS (Entregan TODO)
            if (contador < 2) {
                for (NodoCompuesto<Actividad,Entrega> i = listaActividades.getHeader(); i!=null;i=i.getNext() ) {
                    listaCurso.addElementInSecondaryList(actual, new Entrega(nombreTarea));
                }
            }
            // CASO B: El estudiante "Pedro Ramirez" (Índice 5 en tu txt) hará TRAMPA (Duplicados)
            else if (est.toString().contains("Pedro") && est.toString().contains("Ramirez")) {
                listaCurso.addToNodesList(actual, new Entrega(listaActividades.get(0))); // Taller 1
                listaCurso.addToNodesList(actual, new Entrega(listaActividades.get(0))); // Taller 1 (REPETIDO)
                listaCurso.addToNodesList(actual, new Entrega(listaActividades.get(1))); // Leccion 1
            }
            // CASO C: El resto entrega tareas al azar (entre 0 y 5 tareas)
            else {
                Random rand = new Random();
                int cuantasVaAEntregar = rand.nextInt(6); // 0 a 5 entregas
                for (int i = 0; i < cuantasVaAEntregar; i++) {
                    listaCurso.addToNodesList(actual, new Entrega(listaActividades.get(i)));
                }
            }

            actual = actual.getNext();
            contador++;
        }
        System.out.println(">> Entregas asignadas exitosamente.\n");

        // ---------------------------------------------------------------
        // 3. PRUEBA DE FUNCIONES (VISUALIZACIÓN)
        // ---------------------------------------------------------------

        // --- PRUEBA A: PORCENTAJE DE ENTREGAS ---
        System.out.println(":::::::::::::::::::::::::::::::::::::::::::::::::::");
        System.out.println(" PRUEBA 1: FILTRAR POR CUMPLIMIENTO (100%)");
        System.out.println(" (Buscamos estudiantes que entregaron las " + totalActividadesCurso + " actividades)");
        System.out.println(":::::::::::::::::::::::::::::::::::::::::::::::::::");

        ListaCompuesta<Estudiante, Entrega> perfectos =
                listaCurso.buscarPorPorcentajeEntrega(totalActividadesCurso, 100, 100);

        imprimirReporte(perfectos, totalActividadesCurso);


        System.out.println("\n:::::::::::::::::::::::::::::::::::::::::::::::::::");
        System.out.println(" PRUEBA 2: FILTRAR POR BAJO RENDIMIENTO (0% - 40%)");
        System.out.println(":::::::::::::::::::::::::::::::::::::::::::::::::::");

        ListaCompuesta<Estudiante, Entrega> vagos =
                listaCurso.buscarPorPorcentajeEntrega(totalActividadesCurso, 0, 40);

        imprimirReporte(vagos, totalActividadesCurso);


        // --- PRUEBA B: DUPLICADOS ---
        System.out.println("\n:::::::::::::::::::::::::::::::::::::::::::::::::::");
        System.out.println(" PRUEBA 3: DETECTAR DUPLICADOS (TRAMPOSOS)");
        System.out.println(":::::::::::::::::::::::::::::::::::::::::::::::::::");

        Comparator<Entrega> comp = new Comparator<Entrega>() {
            @Override
            public int compare(Entrega o1, Entrega o2) {
                return o1.toString().compareTo(o2.toString());
            }
        };

        ListaCompuesta<Estudiante, Entrega> tramposos =
                listaCurso.buscarEstudiantesConDuplicadosEnSecundaria(comp);

        if (tramposos != null && !tramposos.isEmpty()) {
            System.out.println(">> ¡ALERTA! Se encontraron entregas repetidas:");
            imprimirReporte(tramposos, totalActividadesCurso);
        } else {
            System.out.println(">> No se encontraron duplicados.");
        }
    }

    // ========================================================
    // MÉTODO AUXILIAR PARA VER LOS RESULTADOS BONITOS
    // ========================================================
    public static void imprimirReporte(ListaCompuesta<Estudiante, Entrega> lista, int total) {
        if (lista == null || lista.isEmpty()) {
            System.out.println("   (Lista vacía: No hubo coincidencias)");
            return;
        }

        NodoCompuesto<Estudiante, Entrega> nodo = lista.getHeader();
        while (nodo != null) {
            Estudiante e = nodo.getData();
            int entregas = 0;
            String detalle = "";

            if (nodo.getReferenciaLista() != null) {
                entregas = nodo.getReferenciaLista().getSize();
                detalle = nodo.getReferenciaLista().toString(); // Asume que tu toString de lista es limpio
            }

            double porc = ((double)entregas/total) * 100;

            System.out.printf("-> %-20s | Cumplimiento: %5.1f%% (%d/%d)\n",
                    e.toString(), porc, entregas, total);
            // Si quieres ver qué entregaron, descomenta la siguiente linea:
            // System.out.println("     Entregas: " + detalle);

            nodo = nodo.getNext();
        }
    }
}