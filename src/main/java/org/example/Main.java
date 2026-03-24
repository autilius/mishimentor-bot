package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        String miApiKey = "API_KEY"; // como variable de entorno
        MishiClient mishi = new MishiClient(miApiKey);

        try {
            System.out.println("Mishi Mentor pensando... 🐾");
            String respuesta = mishi.enviarMiau("Hola Mishi, dame un consejo corto de programación.");
            System.out.println("Mishi dice: " + respuesta);
        } catch (Exception e) {
            System.err.println("¡El Mishi se enredó con un cable!: " + e.getMessage());
        }
    }
}