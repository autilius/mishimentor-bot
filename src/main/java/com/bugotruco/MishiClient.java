package com.bugotruco;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
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

    /**
     * NUEVO MOTOR: Puente unificado para OpenAI y DeepSeek
     * Aprovechamos que ambos comparten el estándar de Chat Completions de la industria.
     */
    public String enviarMiauEstandar(String mensajeUsuario, String proveedor) throws IOException, InterruptedException {
        String urlEndpoint;
        String apiKey;
        String modelo;

        if ("DEEPSEEK".equalsIgnoreCase(proveedor)) {
            urlEndpoint = MishiConfig.getUrlDeepSeek();
            apiKey = MishiConfig.getApiKeyDeepSeek();
            modelo = "deepseek-chat";
        } else {
            urlEndpoint = MishiConfig.getUrlOpenAi();
            apiKey = MishiConfig.getApiKeyOpenAi();
            modelo = "gpt-4o-mini"; // Tu elección económica y masiva de hoy
        }

        if (apiKey == null || urlEndpoint == null) {
            return "¡Miau! Error: Faltan credenciales o URL en config.properties para " + proveedor;
        }

        // Ensamblado usando el POJO estándar
        StandardChatRequest requestPayload = new StandardChatRequest(modelo, MishiConfig.getSystemPrompt(), mensajeUsuario);
        String jsonBody = mapper.writeValueAsString(requestPayload);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlEndpoint))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey) // Estándar Bearer Token
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        var rootNode = mapper.readTree(response.body());

        if (rootNode.has("error")) {
            return "¡Miau! Error de la API (" + proveedor + "): " + rootNode.path("error").path("message").asText();
        }

        // Navegación en el árbol JSON estándar: choices[0].message.content
        return rootNode.path("choices").path(0)
                .path("message").path("content").asText();
    }

    /**
     * NUEVO MOTOR: Conexión exclusiva para Anthropic (Claude)
     * Este requiere su propio método porque estructuran el prompt de sistema fuera del arreglo de mensajes.
     */
    public String enviarMiauAnthropic(String mensajeUsuario) throws IOException, InterruptedException {
        String urlEndpoint = MishiConfig.getUrlAnthropic();
        String apiKey = MishiConfig.getApiKeyAnthropic();
        String modelo = "claude-3-5-sonnet-20241022"; // O el modelo de tu preferencia para el portafolio

        if (apiKey == null || urlEndpoint == null) {
            return "¡Miau! Error: Faltan credenciales o URL de Anthropic en config.properties";
        }

        // Ensamblado usando el POJO específico de Anthropic
        AnthropicChatRequest requestPayload = new AnthropicChatRequest(modelo, MishiConfig.getSystemPrompt(), mensajeUsuario);
        String jsonBody = mapper.writeValueAsString(requestPayload);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlEndpoint))
                .header("Content-Type", "application/json")
                .header("x-api-key", apiKey) // Encabezado exclusivo de Anthropic
                .header("anthropic-version", "2023-06-01") // Encabezado de protocolo obligatorio
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        var rootNode = mapper.readTree(response.body());

        if (rootNode.has("error")) {
            return "¡Miau! Error de la API de Anthropic: " + rootNode.path("error").path("message").asText();
        }

        // Estructura de respuesta de Anthropic: content[0].text
        return rootNode.path("content").path(0).path("text").asText();
    }

    // --- POJOs ESTÁNDICOS PARA APIS EXTERNAS ---

    // Estructura Estándar (OpenAI / DeepSeek)
    public static class StandardChatRequest {
        public String model;
        public List<Message> messages;
        public double temperature = 0.2;

        public StandardChatRequest(String model, String sysText, String userText) {
            this.model = model;
            this.messages = new ArrayList<>();
            this.messages.add(new Message("system", sysText));
            this.messages.add(new Message("user", userText));
        }
    }

    public static class Message {
        public String role;
        public String content;
        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }

    // Estructura Exclusiva (Anthropic)
    public static class AnthropicChatRequest {
        public String model;
        public String system; // Anthropic pide el system prompt en la raíz
        public List<Message> messages;
        @JsonProperty("max_tokens")
        public int maxTokens = 4000;
        public double temperature = 0.2;

        public AnthropicChatRequest(String model, String sysText, String userText) {
            this.model = model;
            this.system = sysText;
            this.messages = Collections.singletonList(new Message("user", userText));
        }
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