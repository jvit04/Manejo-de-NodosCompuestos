import java.time.LocalDate;

public class Actividad {
    //Atributos
    private String nombre;
    private String descripcion;
    private String tipo;
    private LocalDate fechaEnvio;
    private LocalDate fechaLimite;
    private int notaMaxima;

//Constructores
    public Actividad(String nombre, String descripcion, LocalDate fechaLimite, int notaMaxima) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaEnvio = LocalDate.now();
        this.fechaLimite = fechaLimite;
        this.notaMaxima = notaMaxima;
    }

    public Actividad(int notaMaxima) {
        this.nombre = "Actividad Sin Nombre ";
        this.descripcion = "Sin Descripción";
        this.fechaEnvio = LocalDate.now();
        this.fechaLimite = null;
        this.notaMaxima = notaMaxima;
    }

    //Getters y Setters
    public String getNombre() {
        return nombre;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public LocalDate getFechaEnvio() {
        return fechaEnvio;
    }
    public LocalDate getFechaLimite() {
        return fechaLimite;
    }
    public int getNotaMaxima() {
        return notaMaxima;
    }

    //Método toString
    @Override
    public String toString() {
        return " -> Actividad: " + this.getNombre() + " | Fecha: " + this.getFechaLimite();
    }
}
