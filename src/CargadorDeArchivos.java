import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class CargadorDeArchivos {

    public static ListaCompuesta<Estudiante, Entrega> cargarEstudiantes(String rutaArchivo) {
        ListaCompuesta<Estudiante, Entrega> lista = new ListaCompuesta<>();
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            int numLinea = 1;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue; // Ignorar líneas en blanco
                try {
                    String[] datos = linea.split(",");
                    if(datos.length < 3) throw new FormatoArchivoException("Faltan datos en la línea " + numLinea);
                    Estudiante e = new Estudiante(datos[0].trim(), datos[1].trim(), datos[2].trim());
                    lista.add(new NodoCompuesto<>(e));
                } catch (FormatoArchivoException e) {
                    System.err.println("[Aviso Estudiantes] " + e.getMessage() + ". Se omitió esta línea.");
                }
                numLinea++;
            }
        } catch (IOException e) {
            System.err.println("[Error] No se pudo leer el archivo de estudiantes: " + e.getMessage());
        }
        return lista;
    }

    public static ListaCompuesta<Actividad,Entrega> cargarActividades(String rutaArchivo) {
        ListaCompuesta<Actividad, Entrega> lista = new ListaCompuesta<>();
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            int numLinea = 1;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                try {
                    String[] datos = linea.split(",");
                    if (datos.length < 5) throw new FormatoArchivoException("Faltan datos en la línea " + numLinea);

                    String nombre = datos[0].trim();
                    String descripcion = datos[1].trim();
                    LocalDate fechaLimite = LocalDate.parse(datos[2].trim()); // Puede fallar si la fecha está mal
                    int notaMaxima = Integer.parseInt(datos[3].trim());       // Puede fallar si no es número
                    String tipo = datos[4].trim();

                    Actividad a = new Actividad(nombre, descripcion, fechaLimite, notaMaxima, tipo);
                    lista.add(new NodoCompuesto<>(a));
                } catch (DateTimeParseException e) {
                    System.err.println("[Aviso Actividades L" + numLinea + "] Formato de fecha incorrecto.");
                } catch (NumberFormatException e) {
                    System.err.println("[Aviso Actividades L" + numLinea + "] La nota máxima debe ser un número entero.");
                } catch (FormatoArchivoException e) {
                    System.err.println("[Aviso Actividades] " + e.getMessage());
                }
                numLinea++;
            }
        } catch (IOException e) {
            System.err.println("[Error] No se pudo leer el archivo de actividades: " + e.getMessage());
        }
        return lista;
    }

    public static void cargarEntregas(String rutaArchivo, ListaCompuesta<Estudiante, Entrega> listaEstudiantes, ListaCompuesta<Actividad, Entrega> listaActividades) {
        // Validación de seguridad antes de intentar asignar entregas
        if (listaEstudiantes == null || listaEstudiantes.isEmpty() || listaActividades == null || listaActividades.isEmpty()) {
            System.err.println("[Error] No se pueden cargar entregas porque las listas base están vacías.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            int numLinea = 1;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                try {
                    String[] datos = linea.split(",");
                    if(datos.length < 4) throw new FormatoArchivoException("Faltan datos en la línea " + numLinea);

                    String cedulaBusq = datos[0].trim();
                    String nombreActBusq = datos[1].trim();
                    double nota = Double.parseDouble(datos[2].trim());
                    String comentario = datos[3].trim();

                    // Buscar Estudiante
                    NodoCompuesto<Estudiante, Entrega> nodoEst = null;
                    for(NodoCompuesto<Estudiante, Entrega> e = listaEstudiantes.getHeader(); e != null; e = e.getNext()){
                        if(e.getData().getCedula().equals(cedulaBusq)){
                            nodoEst = e;
                            break;
                        }
                    }
                    if (nodoEst == null) throw new EstudianteNoEncontradoException("No existe estudiante con cédula " + cedulaBusq);

                    // Buscar Actividad
                    NodoCompuesto<Actividad, Entrega> nodoActividad = null;
                    Actividad actEncontrada = null;
                    for(NodoCompuesto<Actividad, Entrega> a = listaActividades.getHeader(); a != null; a = a.getNext()){
                        if(a.getData().getNombre().equalsIgnoreCase(nombreActBusq)){
                            actEncontrada = a.getData();
                            nodoActividad = a;
                            break;
                        }
                    }
                    if (actEncontrada == null) throw new ActividadNoEncontradaException("No existe la actividad '" + nombreActBusq + "'");

                    // Crear entrega de forma segura
                    Estudiante est = nodoEst.getData();
                    Entrega nuevaEntrega = new Entrega(nota, comentario, est, actEncontrada);
                    listaEstudiantes.addElementInSecondaryList(nodoEst, nuevaEntrega);
                    listaActividades.addElementInSecondaryList(nodoActividad, nuevaEntrega);

                } catch (NumberFormatException e) {
                    System.err.println("[Aviso Entregas L" + numLinea + "] La nota debe ser un número. Se omitió.");
                } catch (EstudianteNoEncontradoException | ActividadNoEncontradaException | FormatoArchivoException e) {
                    System.err.println("[Aviso Entregas L" + numLinea + "] " + e.getMessage() + ". Se omitió.");
                }
                numLinea++;
            }
        } catch (IOException e) {
            System.err.println("[Error] No se pudo leer el archivo de entregas: " + e.getMessage());
        }
    }
}