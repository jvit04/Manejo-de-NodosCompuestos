
class Estudiante{
    private String nombre, apellido; 
    public Estudiante(String nombre, String apellido){
        this.nombre=nombre;
        this.apellido = apellido;
    }
    public String toString(){
        return nombre + " " + apellido;
    }
}