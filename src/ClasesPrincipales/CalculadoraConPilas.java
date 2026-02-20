package ClasesPrincipales;

import java.util.Stack;

/**
 * Servicio encargado de procesar cálculos agregados.
 * Utiliza la estructura de datos Pila (Stack) de Java para evaluar
 * expresiones matemáticas en notación postfija.
 */
public class CalculadoraConPilas {

    /**
     * Evalúa la fórmula postfija de un objeto Calculo.
     * @param calculo El cálculo que contiene la fórmula a evaluar.
     * @param entregasDelEstudiante Sublista con las entregas de un estudiante para buscar sus notas.
     * @return El resultado matemático (double) de la evaluación.
     */
    public static double evaluar(Calculo calculo, ListaCompuesta<Entrega, Entrega> entregasDelEstudiante) {
        Stack<Double> pila = new Stack<>();

        // 1. Separamos la fórmula por espacios.
        // Así podemos leer "Tarea_1" entero en lugar de leer "T", "a", "r"...
        String[] elementos = calculo.getFormula().split(" ");

        for (String elemento : elementos) {
            elemento = elemento.trim();
            if (elemento.isEmpty()) continue;

            // 2. Si es un operador matemático, sacamos los dos últimos valores (POP) y operamos
            if (elemento.equals("+") || elemento.equals("-") || elemento.equals("*") || elemento.equals("/")) {
                if (pila.size() < 2) return 0.0; // Evitar errores si la fórmula está mal escrita

                double operandoDerecho = pila.pop();
                double operandoIzquierdo = pila.pop();
                double resultado = 0;

                switch (elemento) {
                    case "+": resultado = operandoIzquierdo + operandoDerecho; break;
                    case "-": resultado = operandoIzquierdo - operandoDerecho; break;
                    case "*": resultado = operandoIzquierdo * operandoDerecho; break;
                    case "/": resultado = operandoIzquierdo / operandoDerecho; break;
                }
                pila.push(resultado); // Metemos el resultado de vuelta a la pila (PUSH)
            }
            // 3. Si es un número puro (Ej. para dividir para 2)
            else if (esNumero(elemento)) {
                pila.push(Double.parseDouble(elemento));
            }
            // 4. Si es texto, asumimos que es el nombre de una Actividad y buscamos su nota real
            else {
                double notaObtenida = buscarNota(elemento, entregasDelEstudiante);
                pila.push(notaObtenida);
            }
        }

        // El resultado final es lo único que debe quedar en la pila
        return pila.isEmpty() ? 0.0 : pila.pop();
    }

    // --- Métodos Ayudantes Privados ---

    /**
     * Revisa si el texto es un número.
     */
    private static boolean esNumero(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Busca la nota de una actividad específica dentro de la lista de entregas del estudiante.
     */
    private static double buscarNota(String nombreActividadBuscada, ListaCompuesta<Entrega, Entrega> entregas) {
        if (entregas == null) return 0.0;

        NodoCompuesto<Entrega, Entrega> actual = entregas.getHeader();
        while (actual != null) {
            Entrega entrega = actual.getData();
            // Reemplazamos los guiones bajos por espacios (Ej: "Tarea_1" -> "Tarea 1")
            String nombreLimpio = nombreActividadBuscada.replace("_", " ");

            if (entrega.getActividad() != null && entrega.getActividad().getNombre().equalsIgnoreCase(nombreLimpio)) {
                return entrega.getNota() == -1 ? 0.0 : entrega.getNota();
            }
            actual = actual.getNext();
        }
        return 0.0; // Si no tiene la entrega, su nota es 0
    }


    /**
     * Construye una fórmula postfija automáticamente basada en una operación.
     * * @param operacion El tipo de cálculo ("Suma" o "Promedio").
     * @param nombres   Arreglo con los nombres de las actividades a evaluar.
     * @return El string en notación postfija listo para la CalculadoraConPilas.
     */
    public static String generar(String operacion, String[] nombres) {
        if (nombres == null || nombres.length == 0) return "0";
        if (nombres.length == 1) return nombres[0].replace(" ", "_");

        StringBuilder formula = new StringBuilder();

        // Agregamos la primera actividad (reemplazando espacios por guiones bajos)
        formula.append(nombres[0].replace(" ", "_"));

        // Agregamos las demás actividades seguidas del signo "+"
        for (int i = 1; i < nombres.length; i++) {
            formula.append(" ").append(nombres[i].replace(" ", "_")).append(" +");
        }

        // Si es un promedio, añadimos la división para la cantidad total al final
        if (operacion.equalsIgnoreCase("Promedio")) {
            formula.append(" ").append(nombres.length).append(" /");
        }

        return formula.toString();
    }
}