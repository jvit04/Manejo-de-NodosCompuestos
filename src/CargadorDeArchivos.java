import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;

public class CargadorDeArchivos {

    // Método estático para no tener que instanciar la clase
    public static ListaCompuesta<Estudiante, Entrega> cargarEstudiantes(String rutaArchivo) {
        ListaCompuesta<Estudiante, Entrega> lista = new ListaCompuesta<>();

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                // Asumiendo que el archivo dice: Nombre,Apellido,Cedula
                // Ejemplo: Pau,Pau,099999
                String[] datos = linea.split(",");
                if(datos.length >= 3){
                    Estudiante e = new Estudiante(datos[0].trim(), datos[1].trim(), datos[2].trim());
                    lista.add(new NodoCompuesto<>(e));
                }
            }
        } catch (IOException e) {
            System.out.println("Error leyendo archivo: " + e.getMessage());
        }
        return lista;
    }

    // Método estático para no tener que instanciar la clase
    public static ListaCompuesta<Actividad, Entrega> cargarActividades(String rutaArchivo) {
        ListaCompuesta<Actividad, Entrega> lista = new ListaCompuesta<>();

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                // Formato esperado en el archivo: Nombre,Descripcion,Fecha,NotaMaxima, Tipo
                // Ejemplo: Taller 1,Ejercicios de pilas,2026-02-20,10, Taller
                String[] datos = linea.split(",");
                if(datos.length >= 5){
                    Actividad a = new Actividad(datos[0].trim(), datos[1].trim(), LocalDate.parse(datos[2].trim()),Integer.parseInt(datos[3].trim()),datos[4].trim());
                    lista.add(new NodoCompuesto<>(a));
                }
            }
        } catch (IOException e) {
            System.out.println("Error leyendo archivo: " + e.getMessage());
        }
        return lista;
    }
}