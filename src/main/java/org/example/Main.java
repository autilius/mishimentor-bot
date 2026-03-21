package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.println("MishiMentor v0.1 - Inicializando sistemas felinos...");

        // Aquí vivirá la lógica
        if (args.length > 0) {
            System.out.println("Mishi analizando tu petición: " + args[0]);
        } else {
            System.out.println("¿En qué puedo ayudarte hoy, humano?");
        }
    }
}