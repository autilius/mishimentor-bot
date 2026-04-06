package org.example;

public class Main {
    public static void main(String[] args) {

        // 1. Carga ÚNICA de configuración
        String apiKey = MishiConfig.getApiKey();
        String promptKey = MishiConfig.getSystemPrompt();

        if (apiKey == null || promptKey == null) {
            System.err.println("¡Miau! Revisa tu archivo config.properties.");
            return;
        }

        // 2. Inicializamos herramientas (UNA SOLA VEZ)
        MishiVault vault = new MishiVault();
        vault.initVault();
        MishiClient mishi = new MishiClient(apiKey, promptKey);

        try {
            // 3. Selección y lectura del archivo
            String ruta = "src/main/java/org/example/MishiClient.java";
            System.out.println("Mishi Mentor analizando: " + ruta + " 🐾");

            String codigo = MishiScanner.leerArchivo(ruta);

            // 4. Petición y Respuesta
            String peticion = "Audita este código: \n" + codigo;
            String respuesta = mishi.enviarMiau(peticion);

            System.out.println("\n--- REPORTE DEL MISHI ---");
            System.out.println(respuesta);

            // 5. Guardado del Recuerdo (Sin "TU_API_KEY" de por medio)
            MishiNode recuerdo = new MishiNode();
            recuerdo.generarIdAutomatico();
            recuerdo.setFileName(ruta);
            recuerdo.setVerdict(respuesta);
            recuerdo.setParentId(null);

            vault.guardarNodo(recuerdo);

        } catch (Exception e) {
            System.err.println("Error en la ejecución: " + e.getMessage());
        }
    }
}