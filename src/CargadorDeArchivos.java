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
    public static ListaCompuesta<Actividad,Entrega> cargarActividades(String rutaArchivo) {
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

    // Método para cargar Entregas y asignarlas a los estudiantes correspondientes
    public static void cargarEntregas(String rutaArchivo,
                                      ListaCompuesta<Estudiante, Entrega> listaEstudiantes,
                                      ListaCompuesta<Actividad, Entrega> listaActividades) {

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                // Formato: Cedula,NombreActividad,Nota,Comentario
                String[] datos = linea.split(",");

                if(datos.length >= 4){
                    String cedulaBusq = datos[0].trim();
                    String nombreActBusq = datos[1].trim();
                    double nota = Double.parseDouble(datos[2].trim());
                    String comentario = datos[3].trim();

                    // 1. Buscar al Estudiante por Cédula
                    NodoCompuesto<Estudiante, Entrega> nodoEst = null;
                    for(NodoCompuesto<Estudiante, Entrega> p = listaEstudiantes.getHeader(); p != null; p = p.getNext()){
                        if(p.getData().getCedula().equals(cedulaBusq)){
                            nodoEst = p;
                            break;
                        }
                    }

                    // 2. Buscar la Actividad por Nombre
                    NodoCompuesto<Actividad,Entrega> nodoActividad = null; //nodo para guardar el valor de la actividad a asociar con la entrega
                    Actividad actEncontrada = null;
                    for(NodoCompuesto<Actividad, Entrega> a = listaActividades.getHeader(); a != null; a = a.getNext()){
                        if(a.getData().getNombre().equalsIgnoreCase(nombreActBusq)){
                            actEncontrada = a.getData();
                            nodoActividad = a;
                            break;
                        }
                    }

                    // 3. Si ambos existen, creamos la entrega y la vinculamos
                    if (nodoEst != null && actEncontrada != null) {
                        Estudiante est = nodoEst.getData();
                        Entrega nuevaEntrega = new Entrega(nota, comentario, est, actEncontrada);

                        // Agregamos a la lista secundaria del estudiante
                        listaEstudiantes.addElementInSecondaryList(nodoEst, nuevaEntrega);
                        listaActividades.addElementInSecondaryList(nodoActividad, nuevaEntrega);
                    }
                }
            }
            System.out.println(">> Entregas cargadas desde archivo correctamente.");
        } catch (IOException e) {
            System.out.println("Error leyendo entregas: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error procesando datos de entregas: " + e.getMessage());
        }
    }
}