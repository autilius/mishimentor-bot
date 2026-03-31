package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        String apiKey = MishiConfig.getApiKey();
        if (apiKey == null) {
            System.out.println("Por favor, configura tu config.properties");
            return;
        }

        MishiClient mishi = new MishiClient(apiKey);

        try {
            System.out.println("Mishi Mentor pensando... 🐾");
            String respuesta = mishi.enviarMiau("Hola Mishi, dame un consejo corto de programación.");
            System.out.println("Mishi dice: " + respuesta);
        } catch (Exception e) {
            System.err.println("¡El Mishi se enredó con un cable!: " + e.getMessage());
        }
    }
}