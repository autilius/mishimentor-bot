package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;

public class MishiClient {

    private final String apiKey;
    private final String url;
    private static final String HEADER_KEY = "x-goog-api-key";

    private final ObjectMapper mapper;
    private final HttpClient client; // Lo hacemos final para seguridad

    public MishiClient() {
        this.apiKey = MishiConfig.getApiKeyGemini();
        this.url = MishiConfig.getUrlGemini();
        this.mapper = new ObjectMapper();

        // ✅ SOLUCIÓN 2: Inicializar el cliente
        this.client = HttpClient.newHttpClient();

        if (this.apiKey == null || this.url == null) {
            System.err.println("⚠️ Error: Faltan credenciales en config.properties");
        }
    }

    public String enviarMiau(String mensajeUsuario) throws IOException, InterruptedException {

        GeminiRequest requestPayload = new GeminiRequest(MishiConfig.getSystemPrompt(), mensajeUsuario);
        String jsonBody = mapper.writeValueAsString(requestPayload);

        // ✅ SOLUCIÓN 1: Usar 'url' aquí, NO 'apiKey'
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(this.url))
                .header("Content-Type", "application/json")
                .header(HEADER_KEY, this.apiKey) // La llave va en el encabezado, no en la URL
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        // Ahora 'client' ya no es null, enviará el paquete con éxito
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        var rootNode = mapper.readTree(response.body());

        if (rootNode.has("error")) {
            return "¡Miau! Error de la API: " + rootNode.path("error").path("message").asText();
        }

        return rootNode.path("candidates").path(0)
                .path("content").path("parts").path(0)
                .path("text").asText();
    }
    // --- Clases estándar (POJOs) para máxima compatibilidad ---

    public static class GeminiRequest {
        @JsonProperty("system_instruction")
        public Instruction systemInstruction;
        public List<Content> contents;

        public GeminiRequest(String sysText, String userText) {
            this.systemInstruction = new Instruction(sysText);
            this.contents = Collections.singletonList(new Content(userText));
        }
    }

    public static class Instruction {
        public List<Part> parts;
        public Instruction(String text) {
            this.parts = Collections.singletonList(new Part(text));
        }
    }

    public static class Content {
        public List<Part> parts;
        public Content(String text) {
            this.parts = Collections.singletonList(new Part(text));
        }
    }

    public static class Part {
        public String text;
        public Part(String text) { this.text = text; }
    }
}