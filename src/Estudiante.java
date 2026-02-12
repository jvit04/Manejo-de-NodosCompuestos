import java.io.Serializable;

class Estudiante implements Serializable {
    private String nombre, apellido; 
    public Estudiante(String nombre, String apellido){
        this.nombre=nombre;
        this.apellido = apellido;
    }
    public String toString(){
        return nombre + " " + apellido;
    }
}