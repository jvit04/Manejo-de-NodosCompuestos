import java.time.LocalDate;

public class Estudiante {
    private String codigo;
    private String cedula;
    private String nombre;
    private String apellido;


    public Estudiante(String nombre, String apellido) {
        CodigoEstudiantil codigoEstudiantil = new CodigoEstudiantil();
        this.codigo = codigoEstudiantil.generarCodigoNuevo();
        this.nombre = nombre;
        this.apellido = apellido;
    }
















    public String toString(){
        return "Código de estudiante: " + codigo + "\nNombre: " + nombre + "\nApellido: " + apellido;
    }


    public String getCodigo() {
        return codigo;
    }

    public String getApellido() {
        return apellido;
    }

    public String getNombre() {
        return nombre;
    }
}


