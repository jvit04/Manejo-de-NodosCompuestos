import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

public class Entrega implements Serializable {
    //Atributos
    private double nota;
    private String contenido;
    private LocalDate fechaEntrega;
    private String comentarios;
    private Estudiante estudiante;
    private Actividad actividad;

    //Constructores
    public Entrega(double nota, String comentarios, Estudiante estudiante, Actividad actividad) {
        this.nota = nota;
        this.fechaEntrega = LocalDate.now();
        this.comentarios = comentarios;
        this.estudiante = estudiante;
        this.actividad = actividad;
    }


    //Getters y Setters
    public Entrega(double nota) {
        this.nota = nota;
    }
    public double getNota() {
        return nota;
    }
    public String toString(){ return "**" + this.nota + "**";}

    //Método equals para la clase Entrega, su implementación fue necesaria, ya que es la forma la que se reconoce
    //si la nota de dos Entregas son iguales.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entrega entrega = (Entrega) o;
        return nota == entrega.nota;
    }

}
