package org.example;

public class Main {
    public static void main(String[] args) {

        // 1. Cargamos la configuración (Seguridad ante todo)
        String apiKey = MishiConfig.getApiKey();
        String promptKey = MishiConfig.getSystemPrompt();

        if (apiKey == null || promptKey == null) {
            System.err.println("¡Miau! Faltan datos en config.properties. Revisa tu API Key y tu System Prompt.");
            return;
        }

        // 2. Inicializamos al Mishi con su personalidad de Hacker
        MishiClient mishi = new MishiClient(apiKey, promptKey);

        try {
            // --- INICIO DE LA AUDITORÍA (Día 6) ---

            // 3. Definimos qué archivo queremos que el Mishi "olfatee"
            // Puedes probar con esta misma clase o con MishiClient.java
            String ruta = "src/main/java/org/example/MishiClient.java";

            System.out.println("Mishi Mentor activando Ojo de Lince sobre: " + ruta + " 🐾");

            // 4. Usamos la clase MishiScanner para obtener el código
            // (El método en MishiScanner se llama leerArchivo)
            String codigoParaRevisar = MishiScanner.leerArchivo(ruta);

            if (codigoParaRevisar.startsWith("¡Miau! Error")) {
                System.err.println(codigoParaRevisar);
                return;
            }

            // 5. Preparamos la petición con el toque de BugoTruco
            String peticion = """
                Mishi, actúa como el consultor de seguridad de élite que eres. 
                Audita el siguiente código buscando:
                1. Vulnerabilidades de seguridad (especialmente manejo de llaves).
                2. Malas prácticas de Clean Code.
                3. Sugerencias de optimización.
                
                Aquí tienes el código:
                %s
                """.formatted(codigoParaRevisar);

            // 6. Enviamos al Mishi y esperamos su sabiduría
            System.out.println("Mishi Mentor analizando... (esto puede tardar unos segundos) 🐈‍⬛");
            String respuesta = mishi.enviarMiau(peticion);

            System.out.println("\n--- 🛡️ REPORTE DE SEGURIDAD DEL MISHI ---");
            System.out.println(respuesta);
            System.out.println("------------------------------------------");

        } catch (Exception e) {
            System.err.println("¡El Mishi se enredó con un cable!: " + e.getMessage());
            e.printStackTrace();
        }
    }
}