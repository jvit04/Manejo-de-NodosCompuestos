public class Estudiante {
    //Atributos
    private String codigo;
    private String cedula;
    private String nombre;
    private String apellido;

//Constructor
    public Estudiante(String nombre, String apellido) {
        CodigoEstudiantil codigoEstudiantil = new CodigoEstudiantil();
        this.codigo = codigoEstudiantil.generarCodigoNuevo();
        this.nombre = nombre;
        this.apellido = apellido;
    }

//Método toString
    public String toString(){
        return "Código de estudiante: " + codigo + "\nNombre: " + nombre + "\nApellido: " + apellido;
    }

//Getters y Setters
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


