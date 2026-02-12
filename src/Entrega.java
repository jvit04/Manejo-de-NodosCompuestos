import java.io.Serializable;

class Entrega implements Serializable {
    private int nota;
    public Entrega(int nota){ this.nota =nota;}
    public int getNota(){return this.nota;}
    public String toString(){ return "#" + this.nota + "";}
}