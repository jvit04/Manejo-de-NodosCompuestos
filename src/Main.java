import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {

        System.out.println("=== INICIANDO SISTEMA CON CARGA DE ARCHIVOS ===");

        // 1. Cargar Estudiantes y Actividades
        ListaCompuesta<Estudiante, Entrega> listaEstudiantes = CargadorDeArchivos.cargarEstudiantes("src/estudiantes.txt");
        ListaCompuesta<Actividad, Entrega> listaActividades = CargadorDeArchivos.cargarActividades("src/actividades.txt");

        if (listaEstudiantes.getSize() == 0 || listaActividades.getSize() == 0) {
            System.out.println("[!] ERROR: Faltan datos base (estudiantes o actividades).");
            return;
        }
        System.out.println("-> Base cargada: " + listaEstudiantes.getSize() + " estudiantes, " + listaActividades.getSize() + " actividades.");

        // 2. CARGAR ENTREGAS (Usando el método de tu compañera)
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

        ListaCompuesta<Actividad, Entrega> vencidas = listaActividades.buscarActividadesVencidas(compFechas, actRef);
        System.out.println("   - Actividades POSTERIORES a la fecha (Futuras): " + vencidas.getSize());

        ListaCompuesta<Actividad, Entrega> vigentes = listaActividades.buscarActividadesVigentes(compFechas, actRef);
        System.out.println("   - Actividades ANTERIORES a la fecha (Pasadas): " + vigentes.getSize());


        // PRUEBA B: Estudiantes con Entregas Duplicadas
        // (En el txt puse a Juan Perez y Ana Gomez con notas repetidas a propósito)
        System.out.println("\n[B] PRUEBA DE DUPLICADOS");
        CompararEntregasxNotas compNotas = new CompararEntregasxNotas();

        ListaCompuesta<Estudiante, Entrega> repetidos = listaEstudiantes.buscarEstudiantesConDuplicadosEnSecundaria(compNotas);

        System.out.println("   Estudiantes con notas idénticas en sus entregas:");
        if(!repetidos.isEmpty()){
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
        while(actual != null) {
            int n = actual.getReferenciaLista().getSize();
            double p = ((double)n / totalActividades) * 100;
            System.out.println("   -> " + actual.getData().getNombre() + " " + actual.getData().getApellido()
                    + " (Entregas: " + n + " | " + String.format("%.1f", p) + "%)");
            actual = actual.getNext();
        }
    }
}