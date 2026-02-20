import java.util.Scanner;
import java.time.LocalDate;

// Importamos tus paquetes
import Comparadores.*;
import ClasesPrincipales.*;
import Archivos.*;

public class Main {
    private static ListaCompuesta<Estudiante, Entrega> listaEstudiantes = CargadorDeArchivos.cargarEstudiantes("src/Archivos/estudiantes.txt");
    private static ListaCompuesta<Actividad, Entrega> listaActividades = CargadorDeArchivos.cargarActividades("src/Archivos/actividades.txt");

    public static void main(String[] args) {
        System.out.println("=== INICIANDO SISTEMA CON CARGA DE ARCHIVOS ===");

        if (listaEstudiantes.getSize() == 0 || listaActividades.getSize() == 0) {
            System.out.println("[!] Error: Faltan datos base (estudiantes o actividades).");
            return;
        }
        System.out.println("-> Base cargada: " + listaEstudiantes.getSize() + " estudiantes, " + listaActividades.getSize() + " actividades.");

        CargadorDeArchivos.cargarEntregas("src/Archivos/entregas.txt", listaEstudiantes, listaActividades);

        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            Menu.mostrarOpciones();
            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    System.out.println("\n--- [1] ACTIVIDADES VENCIDAS Y VIGENTES ---");
                    LocalDate fechaCorte = LocalDate.of(2026, 4, 1);
                    CompararActividadesFechaEntrega compFechas = new CompararActividadesFechaEntrega();
                    Actividad actRef = new Actividad("Ref", "Ref", fechaCorte, 0, "Ref");

                    ListaCompuesta<Actividad, Entrega> vencidas = listaActividades.buscarMayoresPrincipal(compFechas, actRef);
                    System.out.println("   -> Actividades POSTERIORES al 01-Abril-2026 (Futuras): " + vencidas.getSize());

                    ListaCompuesta<Actividad, Entrega> vigentes = listaActividades.buscarMenoresPrincipal(compFechas, actRef);
                    System.out.println("   -> Actividades ANTERIORES al 01-Abril-2026 (Pasadas): " + vigentes.getSize());
                    break;

                case 2:
                    System.out.println("\n--- [2] REPORTE DE NOTAS DUPLICADAS ---");
                    CompararEntregasxNotas compNotas = new CompararEntregasxNotas();
                    ListaCompuesta<Estudiante, Entrega> repetidos = listaEstudiantes.buscarNodosConDuplicadosEnSublista(compNotas);

                    System.out.println("   Estudiantes con notas idénticas en diferentes entregas:");
                    if (!repetidos.isEmpty()) {
                        System.out.println(repetidos);
                    } else {
                        System.out.println("   -> No se encontraron duplicados.");
                    }
                    break;

                case 3:
                    System.out.println("\n--- [3] CUMPLIMIENTO (10% a 60%) ---");
                    int totalActividades = listaActividades.getSize();
                    int minEntregas = (int) Math.ceil((10.0 / 100.0) * totalActividades);
                    int maxEntregas = (int) ((60.0 / 100.0) * totalActividades);

                    ListaCompuesta<Estudiante, Entrega> cumplidos = listaEstudiantes.buscarPorRangoDeTamanoSublista(minEntregas, maxEntregas);

                    NodoCompuesto<Estudiante, Entrega> actual = cumplidos.getHeader();
                    if (actual == null) System.out.println("   -> Ningún estudiante en este rango.");

                    while (actual != null) {
                        int n = (actual.getReferenciaLista() != null) ? actual.getReferenciaLista().getSize() : 0;
                        double p = ((double) n / totalActividades) * 100;
                        System.out.println("   -> " + actual.getData().getNombre() + " " + actual.getData().getApellido()
                                + " (Entregas: " + n + " | " + String.format("%.1f", p) + "%)");
                        actual = actual.getNext();
                    }
                    break;

                case 4:
                    System.out.println("\n--- [4] ACTIVIDADES FALTANTES ---");

                    // Tomamos al primer estudiante
                    NodoCompuesto<Estudiante, Entrega> est1 = listaEstudiantes.getHeader();
                    if (est1 != null) {
                        System.out.println("\n   Faltantes de " + est1.getData().getNombre() + ":");

                        // 1. Creamos el comparador pasándole las entregas de este estudiante
                        // Es imoprtante entregarle una lista <Entrega, Entrega> la cual es la referencia lista de este estudiante
                        CompararActividadFaltante compFaltante = new CompararActividadFaltante(est1.getReferenciaLista());

                        // 2. Usamos el método genérico (pasamos una Actividad vacía de relleno).
                        // El método recorre la lista de todas las actividades del curso y, en cada iteración, el comparador verifica si dicha actividad falta en la lista del estudiante (est1).
                        ListaCompuesta<Actividad, Entrega> faltantes = listaActividades.buscarIgualesPrincipal(compFaltante, new Actividad("relleno"));

                        if (faltantes.isEmpty()) {
                            System.out.println("      - ¡Ha entregado todo!");
                        } else {

                            for (NodoCompuesto<Actividad, Entrega> p = faltantes.getHeader(); p != null; p = p.getNext()) {
                                System.out.println("      - " + p.getData().getNombre());
                            }
                        }
                    }
                    break;

                case 5:
                    System.out.println("\n--- [5] Buscar Calificaciones con calificaciones menores a 7 ---");
                    Entrega entrega = new Entrega(7);
                    CompararEntregasxNotas compararEntregasxNotas = new CompararEntregasxNotas();

                    System.out.println("\n--- Retornando estudiantes ---");
                    System.out.println(listaEstudiantes.buscarTodosMenoresEnListaSecundaria(compararEntregasxNotas, entrega));

                    System.out.println("\n--- Retornando actividades ---");
                    System.out.println(listaActividades.buscarTodosMenoresEnListaSecundaria(compararEntregasxNotas, entrega));
                    break;

                case 6:
                    System.out.println("\n--- [6] Buscar Entregas Vencidas sin nota (-1) ---");

                    // Primero necesitamos volver a calcular las vencidas porque estamos en otro "caso"
                    LocalDate corte = LocalDate.of(2026, 4, 1);
                    CompararActividadesFechaEntrega compF = new CompararActividadesFechaEntrega();
                    Actividad actR = new Actividad("Ref", "Ref", corte, 0, "Ref");
                    ListaCompuesta<Actividad, Entrega> actosVencidas = listaActividades.buscarMayoresPrincipal(compF, actR);

                    Entrega entrega1 = new Entrega(-1); // Notas con valor -1 significan nulas.
                    CompararEntregasxNotas compNotasVencidas = new CompararEntregasxNotas();

                    System.out.println(actosVencidas.buscarIgualesEnListaSecundaria(compNotasVencidas, entrega1));
                    break;

                case 0:
                    System.out.println("Guardando y saliendo del sistema... ¡Hasta luego!");
                    break;

                default:
                    System.out.println("[!] Opción no válida. Por favor, intente de nuevo.");
            }
        } while (opcion != 0);

        scanner.close();
    }
}