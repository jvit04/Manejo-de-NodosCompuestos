package ClasesPrincipales;
import java.util.Stack;

//Clase que nos ayuda a realizar los calculos con las distintas actividades.
public class Calculo {
    private String nombre; // Ejemplo: "NOTA_FINAL"
    private String formula; // Ejemplo: "A01 + A02 / 2"

    public Calculo(String nombre, String formula) {
        this.nombre = nombre;
        this.formula = formula;
    }



    public double devolverResultadoPosfija() {
        Stack<Double> pila = new Stack<>(); //pila que nos permitirá evaluar la expresión
        // Aquí iremos armando los números dígito a dígito, la forma de separación será con espacios.
        String numeroTemporal = "";

        // Recorremos la fórmula caracter por caracter
        for (int i = 0; i < this.formula.length(); i++) {
            char caracter = this.formula.charAt(i);

            // Si es un espacio en blanco, significa que terminamos de leer una palabra/número
            if (caracter == ' ') {
                // Si teníamos un número armado, lo convertimos a Double y lo apilamos
                if (!numeroTemporal.isEmpty()) {
                    pila.push(Double.parseDouble(numeroTemporal));
                    numeroTemporal = ""; // Limpiamos para el siguiente número
                }
            }
            // Ejemplo visual de la Pila si la fórmula es: "10 2 /"
            // 1. Entra el "10" -> Pila: [10.0]
            // 2. Entra el "2"  -> Pila: [10.0, 2.0]
            // 3. Entra el "/"  -> Se activa este bloque:

            // Si es un operador y no estamos a mitad de armar un número negativo
            else if ((caracter == '+' || caracter == '-' || caracter == '*' || caracter == '/') && numeroTemporal.isEmpty()) {
                // El primero que sacamos es el que estaba arriba del todo (el último en entrar)
                double operandoDerecho = pila.pop();// Ejemplo: 2.0 (el divisor)
                // El segundo que sacamos es el que estaba debajo
                double operandoIzquierdo = pila.pop();// Ejemplo: 10.0 (el dividendo)
                double resultado = 0;

                switch (caracter) {
                    case '+': resultado = operandoIzquierdo + operandoDerecho; break;// 12.0 = 10.0 + 2.0
                    case '-': resultado = operandoIzquierdo - operandoDerecho; break;// 8.0 = 10.0 - 2.0
                    case '*': resultado = operandoIzquierdo * operandoDerecho; break;// 20.0 = 10.0 * 2.0
                    // Es vital el orden izquierdo / derecho para no obtener 0.2
                    case '/': resultado = operandoIzquierdo / operandoDerecho; break;// 5.0 = 10.0 / 2.0
                }

                // Apilamos el resultado de esta mini-operación
                pila.push(resultado);// Pila ahora tiene: [5.0]
            }
            // Si no es ni espacio ni operador, es parte de un número (un dígito o un punto decimal)
            else {
                numeroTemporal += caracter;
            }
        }
        // El resultado final siempre será el único número que sobreviva en la pila
        return pila.pop();
    }
}
