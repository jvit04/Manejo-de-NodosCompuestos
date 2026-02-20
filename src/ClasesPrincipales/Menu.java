package ClasesPrincipales;

/**
 * Clase encargada de manejar la interfaz de texto del sistema (Vista).
 */
public class Menu {

    public static void mostrarOpciones() {
        System.out.println("\n=== SISTEMA DE CALIFICACIONES ===");
        System.out.println("1. Ver actividades vencidas y vigentes");
        System.out.println("2. Reporte de notas duplicadas");
        System.out.println("3. Reporte de cumplimiento (10% a 60%)");
        System.out.println("4. Ver actividades faltantes por estudiante (Diferencia)");
        System.out.println("5. Buscar estudiantes y actividades con calificaciones menores a 7");
        System.out.println("6. Buscar entregas vencidas sin calificar (-1)");
        System.out.println("0. Salir");
        System.out.print("Elija una opción: ");
    }
}