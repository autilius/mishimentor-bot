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

    // Cambiamos a v1beta para soporte total de system_instruction
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?";
    private static final String HEADER_KEY = "x-goog-api-key";

    private final String apiKey;
    private final String systemPrompt;
    private final HttpClient client;
    private final ObjectMapper mapper;

    public MishiClient(String apiKey, String systemPrompt) {
        this.apiKey = apiKey;
        this.systemPrompt = systemPrompt;
        this.client = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
    }

    public String enviarMiau(String mensajeUsuario) throws IOException, InterruptedException {

        GeminiRequest requestPayload = new GeminiRequest(this.systemPrompt, mensajeUsuario);
        String jsonBody = mapper.writeValueAsString(requestPayload);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header(HEADER_KEY, apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

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