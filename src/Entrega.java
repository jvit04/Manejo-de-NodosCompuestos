import java.time.LocalDate;

public class Entrega {
    private int nota;
    private LocalDate fechaEntrega;
    private String comentarios;
    private Estudiante estudiante;
    private Actividad actividad;

    public Entrega(int nota, String comentarios, Estudiante estudiante, Actividad actividad) {
        this.nota = nota;
        this.fechaEntrega = LocalDate.now();
        this.comentarios = comentarios;
        this.estudiante = estudiante;
        this.actividad = actividad;
    }

    public Entrega(int nota, Estudiante estudiante, Actividad actividad) {
        this.nota = nota;
        this.fechaEntrega = LocalDate.now();
        this.estudiante = estudiante;
        this.actividad = actividad;
        this.comentarios = "Sin comentarios";
    }

    public Entrega(int nota) {
        this.nota = nota;
    }

    public int getNota() {
        return nota;
    }

    public String toString(){ return "**" + this.nota + "**";}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entrega entrega = (Entrega) o;
        return nota == entrega.nota;
    }

}
