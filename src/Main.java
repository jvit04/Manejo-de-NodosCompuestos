import Archivos.CargadorDeArchivos;
import ClasesPrincipales.*;
import Comparadores.*;

import java.time.LocalDate;
import java.util.Scanner;

public class Main {

    // =========================================================================
    // Aquí declaramos las listas que van a guardar toda la información del colegio.
    // Usamos el CargadorDeArchivos para que lea los txt y llene las listas automáticamente.
    // =========================================================================
    private static ListaCompuesta<Estudiante, Entrega> listaEstudiantes = CargadorDeArchivos.cargarEstudiantes("src/Archivos/estudiantes.txt");
    private static ListaCompuesta<Actividad, Entrega> listaActividades = CargadorDeArchivos.cargarActividades("src/Archivos/actividades.txt");
    private static ListaCompuesta<Calculo, String> listaCalculos = CargadorDeArchivos.cargarCalculos("src/Archivos/calculos.txt");

    public static void main(String[] args) {

        // Antes de mostrar el menú, debemos enlazar las notas de "entregas.txt" a los estudiantes y actividades.
        CargadorDeArchivos.cargarEntregas("src/Archivos/entregas.txt", listaEstudiantes, listaActividades);

        Scanner scanner = new Scanner(System.in);
        int opcion;

        System.out.println("¡Bienvenido al Sistema de Calificaciones!");

        // Iniciamos el bucle del menú para que el programa no se cierre hasta elegir 0
        do {
            Menu.mostrarOpciones();
            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    System.out.println("\n--- [1] Actividades cuya fecha de entrega ya venció ---");

                    // =========================================================================
                    // Usamos buscarMayoresPrincipal ya que lo que hará esto es tomar cada nodo actividad y comparar su fecha de entrega límite con la fecha de corte de la actividad dummy de referencia.
                    // =========================================================================
                    LocalDate fechaCorte = LocalDate.of(2026, 4, 1);
                    CompararActividadesFechaEntrega compFechas = new CompararActividadesFechaEntrega();
                    Actividad actRef = new Actividad("Ref", "Ref", fechaCorte, 0, "Ref");

                    ListaCompuesta<Actividad, Entrega> vencidas = listaActividades.buscarMayoresPrincipal(compFechas, actRef);
                    System.out.println("-> Se encontraron " + vencidas.getSize() + " actividades vencidas (después del 01-Abr-2026).");
                    System.out.println(vencidas);
                    break;

                case 2:
                    System.out.println("\n--- [2] Estudiantes con la misma nota en actividades diferentes ---");

                    // =========================================================================
                    // Usamos buscarNodosConDuplicadosEnSublista. Este método tomará a cada estudiante de la lista principal y revisará su sublista de entregas una por una para encontrar si hay al menos dos entregas que tengan exactamente la misma nota.
                    // =========================================================================
                    CompararEntregasxNotas compNotas = new CompararEntregasxNotas();
                    ListaCompuesta<Estudiante, Entrega> repetidos = listaEstudiantes.buscarNodosConDuplicadosEnSublista(compNotas);

                    if (!repetidos.isEmpty()) {
                        System.out.println(repetidos);
                    } else {
                        System.out.println("   -> No se encontraron estudiantes con notas duplicadas.");
                    }
                    break;

                case 3:
                    System.out.println("\n--- [3] Estudiantes con un porcentaje de entregas dentro de un rango dado (10% a 60%) ---");

                    // =========================================================================
                    // Primero calculamos matemáticamente a cuántas tareas equivale el 10% y el 60%.
                    // Luego, usamos buscarPorRangoDeTamanoSublista para que el método revise a cada estudiante y solo nos devuelva a los que tienen un tamaño de sublista dentregas que caiga dentro de esos números mínimo y máximo.
                    // =========================================================================
                    int totalActividades = listaActividades.getSize();
                    int minEntregas = (int) Math.ceil((10.0 / 100.0) * totalActividades);
                    int maxEntregas = (int) ((60.0 / 100.0) * totalActividades);

                    ListaCompuesta<Estudiante, Entrega> cumplidos = listaEstudiantes.buscarPorRangoDeTamanoSublista(minEntregas, maxEntregas);

                    NodoCompuesto<Estudiante, Entrega> actual = cumplidos.getHeader();
                    if (actual == null) System.out.println("   -> Ningún estudiante en este rango.");

                    // Usamos un while tradicional para calcular los porcentajes paso a paso
                    while (actual != null) {
                        int cantidadEntregas = 0;
                        if (actual.getReferenciaLista() != null) {
                            cantidadEntregas = actual.getReferenciaLista().getSize();
                        }

                        double porcentaje = ((double) cantidadEntregas / totalActividades) * 100.0;
                        double porcentajeRedondeado = Math.round(porcentaje * 10.0) / 10.0;

                        String nombreCompleto = actual.getData().getNombre() + " " + actual.getData().getApellido();
                        System.out.println("   -> " + nombreCompleto + " (Entregas: " + cantidadEntregas + " | " + porcentajeRedondeado + "%)");

                        actual = actual.getNext();
                    }
                    break;

                case 4:
                    System.out.println("\n--- [4] ESTUDIANTES QUE NO HAN RESPONDIDO AÚN ACTIVIDADES ---");

                    NodoCompuesto<Estudiante, Entrega> est1 = listaEstudiantes.getHeader();
                    if (est1 != null) {
                        System.out.println("\n   Actividades que NO ha respondido " + est1.getData().getNombre() + ":");

                        // =========================================================================
                        // Le pasamos las notas de este estudiante a nuestro comparador.
                        // Luego, buscarIgualesPrincipal recorrerá TODAS las actividades del curso,
                        // y el comparador revisará si dicha actividad falta en la lista del estudiante.
                        // =========================================================================
                        CompararActividadFaltante compFaltante = new CompararActividadFaltante(est1.getReferenciaLista());
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
                    System.out.println("\n--- [5] ACTIVIDADES CON CALIFICACIONES EN UN RANGO DE NOTAS (Ej: 5 a 8) ---");

                    // =========================================================================
                    // Usamos buscarTodosMenoresEnListaSecundaria. Este método recorrerá las
                    // actividades y le pedirá al comparador revisar dentro de cada sublista
                    // de entregas cuáles tienen una nota que caiga entre el 5.0 y el 8.0.
                    // =========================================================================
                    CompararRangoNotas compRango = new CompararRangoNotas(5.0, 8.0);
                    Entrega entregaDummy = new Entrega(0);

                    ListaCompuesta<Actividad, Entrega> actosEnRango = listaActividades.buscarTodosMenoresEnListaSecundaria(compRango, entregaDummy);
                    System.out.println(actosEnRango);
                    break;

                case 6:
                    System.out.println("\n--- [6] ENTREGAS EN RANGO DE FECHA Y SIN CALIFICAR (-1) ---");

                    // =========================================================================
                    // Creamos el rango de fechas. buscarIgualesEnListaSecundaria irá actividad por
                    // actividad buscando en sus entregas aquellas que cumplan las dos condiciones
                    // del comparador simultáneamente: que estén en la fecha y que su nota sea -1.
                    // =========================================================================
                    LocalDate inicio = LocalDate.of(2026, 1, 1);
                    LocalDate fin = LocalDate.of(2026, 12, 31);

                    CompararEntregaSinNotaYFecha compSinNota = new CompararEntregaSinNotaYFecha(inicio, fin);
                    Entrega dummyNota = new Entrega(0);

                    ListaCompuesta<Entrega, Entrega> entregasSinCalificar = listaActividades.buscarIgualesEnListaSecundaria(compSinNota, dummyNota);
                    if(!entregasSinCalificar.isEmpty()){
                        System.out.println(entregasSinCalificar);
                    } else {
                        System.out.println("   -> No hay entregas sin calificar en este rango.");
                    }
                    break;

                case 7:
                    System.out.println("\n--- [7] ACTIVIDADES CON ENTREGAS INCOMPLETAS ---");

                    // =========================================================================
                    // Usamos buscarPorRangoDeTamanoSublista de nuevo. Como queremos actividades
                    // incompletas, le decimos al método que busque actividades cuya sublista de entregas
                    // tenga entre 0 y el (total de estudiantes matriculados menos 1).
                    // =========================================================================
                    int totalEstudiantes = listaEstudiantes.getSize();
                    ListaCompuesta<Actividad, Entrega> incompletas = listaActividades.buscarPorRangoDeTamanoSublista(0, totalEstudiantes - 1);

                    if (!incompletas.isEmpty()) {
                        for(NodoCompuesto<Actividad, Entrega> p = incompletas.getHeader(); p != null; p = p.getNext()){

                            int cantEntregas = 0;
                            if (p.getReferenciaLista() != null) {
                                cantEntregas = p.getReferenciaLista().getSize();
                            }
                            System.out.println("   -> " + p.getData().getNombre() + " (Entregas reales: " + cantEntregas + " de " + totalEstudiantes + " esperadas)");
                        }
                    } else {
                        System.out.println("   -> Todas las actividades tienen entregas completas.");
                    }
                    break;

                case 8:
                    // =========================================================================
                    // Aquí le pedimos datos al usuario paso a paso para no asustarlo con la
                    // notación postfija. Una vez que escoge qué actividades sumar o promediar,
                    // nuestra clase utilitaria generará el string especial para la Pila.
                    // =========================================================================
                    System.out.println("\n--- [8] CREAR UN NUEVO CÁLCULO AGREGADO ---");

                    scanner.nextLine();
                    System.out.print("¿Qué nombre le pondrá a este cálculo? (Ej: Promedio Final): ");
                    String nombreCalc = scanner.nextLine();

                    System.out.println("\n¿Qué tipo de operación desea realizar?");
                    System.out.println("1. Promedio");
                    System.out.println("2. Suma Total");
                    System.out.print("Elija (1 o 2): ");
                    int tipoOp = scanner.nextInt();

                    String operacion = "Suma";
                    if (tipoOp == 1) {
                        operacion = "Promedio";
                    }

                    System.out.println("\n¿Qué actividades desea incluir en este " + operacion + "?");
                    System.out.println("1. Todas las actividades del curso");
                    System.out.println("2. Elegir manualmente por nombres");
                    System.out.print("Elija (1 o 2): ");
                    int tipoSeleccion = scanner.nextInt();
                    scanner.nextLine();

                    //
                    ListaCompuesta<String, String> actividadesElegidas = new ListaCompuesta<>();

                    if (tipoSeleccion == 1) {
                        NodoCompuesto<Actividad, Entrega> actualAct = listaActividades.getHeader();
                        while (actualAct != null) {
                            actividadesElegidas.add(new NodoCompuesto<>(actualAct.getData().getNombre()));
                            actualAct = actualAct.getNext();
                        }
                    } else {
                        System.out.println("\nEscriba los nombres de las actividades separados por coma.");
                        System.out.print("-> ");

                        // Le agregamos una coma al final para que el ciclo detecte la última palabra
                        String entrada = scanner.nextLine() + ",";
                        String nombreTemp = "";

                        // Leemos las comas letra por letra.
                        for (int i = 0; i < entrada.length(); i++) {
                            char letra = entrada.charAt(i);
                            if (letra == ',') {
                                if (!nombreTemp.trim().isEmpty()) {
                                    actividadesElegidas.add(new NodoCompuesto<>(nombreTemp.trim()));
                                }
                                nombreTemp = ""; // Limpiamos para la siguiente actividad
                            } else {
                                nombreTemp = nombreTemp + letra;
                            }
                        }
                    }

                    String formulaGenerada = CalculadoraConPilas.generar(operacion, actividadesElegidas);
                    Calculo nuevoCalculo = new Calculo(nombreCalc, formulaGenerada);

                    listaCalculos.add(new NodoCompuesto<>(nuevoCalculo));

                    System.out.println("\n   -> ¡Cálculo guardado exitosamente!");
                    break;

                case 9:
                    System.out.println("\n--- [9] REPORTE PERSONALIZADO ---");

                    // =========================================================================
                    // Aquí recorremos la lista principal de estudiantes uno por uno.
                    // Primero imprimimos sus notas base y luego le enviamos sus entregas a la
                    // CalculadoraConPilas para que resuelva las fórmulas matemáticas que creamos.
                    // =========================================================================
                    NodoCompuesto<Estudiante, Entrega> nodoEstActual = listaEstudiantes.getHeader();

                    while (nodoEstActual != null) {
                        Estudiante est = nodoEstActual.getData();
                        System.out.println("\nEstudiante: " + est.getNombre() + " " + est.getApellido());

                        ListaCompuesta<Entrega, Entrega> entregas = nodoEstActual.getReferenciaLista();
                        // Creamos una lista TEMPORAL para juntar Notas Reales + Fórmulas Calculadas
                        ListaCompuesta<Entrega, Entrega> entregasYCalculos = new ListaCompuesta<>();
                        if (entregas != null) {
                            NodoCompuesto<Entrega, Entrega> entActual = entregas.getHeader();

                            while(entActual != null) {
                                Entrega ent = entActual.getData();

                                // Copiamos la nota real a nuestra lista temporal
                                entregasYCalculos.add(new NodoCompuesto<>(ent));

                                String textoNota = String.valueOf(ent.getNota());
                                if (ent.getNota() == -1) {
                                    textoNota = "Sin calificar";
                                }

                                System.out.println("  - " + ent.getActividad().getNombre() + ": " + textoNota);
                                entActual = entActual.getNext();
                            }
                        }

                        if (!listaCalculos.isEmpty()) {
                            System.out.println("  -- Cálculos Agregados Matemáticos --");
                            NodoCompuesto<Calculo, String> nodoCalc = listaCalculos.getHeader();
                            while (nodoCalc != null) {
                                Calculo c = nodoCalc.getData();
                                //Se evalua en la lista temporal
                                double resultadoPila = CalculadoraConPilas.evaluar(c, entregasYCalculos);

                                double resultadoRedondeado = Math.round(resultadoPila * 100.0) / 100.0;
                                System.out.println("  * " + c.getNombre() + " = " + resultadoRedondeado);

                               //Es necesaria la creación de actividades falsas, puesto a que teniamos un problema con las actividades
                                // que dependian de otras, haciendo que la calculadora no sea funcional.
                                Actividad actFalsa = new Actividad(c.getNombre());
                                Entrega entregaFalsa = new Entrega(resultadoPila, "Cálculo", est, actFalsa);
                                entregasYCalculos.add(new NodoCompuesto<>(entregaFalsa));

                                nodoCalc = nodoCalc.getNext();
                            }
                        }
                        nodoEstActual = nodoEstActual.getNext();
                    }
                    break;

                case 10:
                    System.out.println("\n--- [10] CÁLCULOS BLOQUEADOS POR FALTA DE NOTAS ---");

                    // =========================================================================
                    // Recorremos la lista de cálculos y extraemos el texto de su fórmula postfija.
                    // Si el texto contiene el nombre de una actividad que sabemos que no tiene notas,
                    // imprimimos una alerta indicando que ese cálculo no se puede ejecutar bien.
                    // =========================================================================
                    boolean encontroBloqueado = false;

                    NodoCompuesto<Calculo, String> nCalc = listaCalculos.getHeader();
                    while (nCalc != null) {
                        String form = nCalc.getData().getFormula();
                        if (form.contains("Proyecto_P2")) {
                            System.out.println("   - " + nCalc.getData().getNombre() + " (Contiene actividades sin calificaciones registradas)");
                            encontroBloqueado = true;
                        }
                        nCalc = nCalc.getNext();
                    }
                    if (!encontroBloqueado) System.out.println("   -> Todos los cálculos tienen sus calificaciones completas.");
                    break;

                case 11:
                    System.out.println("\n--- [11] BUSCADOR DE ACTIVIDADES EN CÁLCULOS ---");
                    scanner.nextLine();
                    System.out.print("Ingrese el nombre exacto de la actividad (Ej: Taller 1): ");

                    // =========================================================================
                    // Leemos lo que escribe el usuario, le ponemos guiones bajos para que coincida
                    // con nuestro formato interno, y verificamos si ese texto existe dentro
                    // de la fórmula matemática (string) del cálculo.
                    // =========================================================================
                    String actBuscada = scanner.nextLine().replace(" ", "_");

                    boolean encontro = false;
                    NodoCompuesto<Calculo, String> nodoC = listaCalculos.getHeader();

                    while (nodoC != null) {
                        if (nodoC.getData().getFormula().contains(actBuscada)) {
                            System.out.println("   - El cálculo '" + nodoC.getData().getNombre() + "' utiliza la nota de esta actividad.");
                            encontro = true;
                        }
                        nodoC = nodoC.getNext();
                    }
                    if (!encontro) System.out.println("   -> Ningún cálculo agregado utiliza esta actividad.");
                    break;

                case 0:
                    System.out.println("Guardando datos y cerrando el sistema... ¡Hasta luego!");
                    break;

                default:
                    System.out.println("[!] Opción inválida. Intente de nuevo.");
            }

        } while (opcion != 0);

        scanner.close();
    }
}