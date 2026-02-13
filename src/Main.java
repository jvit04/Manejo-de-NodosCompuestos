import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {

        System.out.println("=== INICIANDO CARGA DE DATOS ===");

        // 1. Cargar Estudiantes desde archivo
        // Asegúrate de que el nombre del archivo coincida con el que creaste
        ListaCompuesta<Estudiante, Entrega> listaEstudiantes = CargadorDeArchivos.cargarEstudiantes("src/estudiantes.txt");

        if (listaEstudiantes.getSize() > 0) {
            System.out.println("\n--> Estudiantes cargados exitosamente (" + listaEstudiantes.getSize() + "):");
            System.out.println(listaEstudiantes); // Usa tu método toString() de la lista
        } else {
            System.out.println("\n[!] No se pudieron cargar estudiantes o la lista está vacía.");
        }

        // 2. Cargar Actividades desde archivo
        ListaCompuesta<Actividad, Entrega> listaActividades = CargadorDeArchivos.cargarActividades("src/actividades.txt");

        if (listaActividades.getSize() > 0) {
            System.out.println("\n--> Actividades cargadas exitosamente (" + listaActividades.getSize() + "):");
            // Iteramos manualmente para ver que los datos (incluido el TIPO) se cargaron bien
            NodoCompuesto<Actividad, Entrega> actual = listaActividades.getHeader();
            while(actual != null) {
                Actividad act = actual.getData();
                System.out.println(" - " + act.getNombre() + " | Fecha: " + act.getFechaLimite() + " | Nota: " + act.getNotaMaxima());
                // Si tienes un getter para tipo, podrías imprimirlo también:
                // System.out.println("   Tipo: " + act.getTipo());
                actual = actual.getNext();
            }
        } else {
            System.out.println("\n[!] No se pudieron cargar actividades o la lista está vacía.");
        }

        // 3. Prueba de funcionalidad con los datos cargados (Ejemplo: Buscar actividades vigentes)
        System.out.println("\n=== PRUEBA DE FUNCIONALIDAD CON DATOS CARGADOS ===");
        if (listaActividades.getSize() > 0) {
            LocalDate hoy = LocalDate.now();
            System.out.println("Buscando actividades posteriores a HOY (" + hoy + ")...");

            CompararActividadesFechaEntrega comparador = new CompararActividadesFechaEntrega();
            // Creamos una actividad dummy solo para pasar la fecha de corte
            Actividad referencia = new Actividad("Ref", "Ref", hoy, 0, "Ref");

            ListaCompuesta<Actividad, Entrega> futuras = listaActividades.buscarActividadesVencidas(comparador, referencia);
            System.out.println(futuras);
        }
    }
}