package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class MishiClient {

    private final String apiKey;
    private final HttpClient client;
    private final ObjectMapper mapper = new ObjectMapper();

    public MishiClient(String apiKey) {
        this.apiKey = apiKey;
        this.client = HttpClient.newHttpClient();
    }

    public String enviarMiau(String mensaje) throws Exception {
        // El endpoint oficial de Gemini
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + apiKey;

        // DEFINIMOS LA PERSONALIDAD AQUÍ
        String personalidad = "Eres Mishi Mentor, un gato programador y hacker (Gray team, blue team, read team y a veces  purple team) experto,  sabio y un poco cínico. " +
                "Usas analogías de gatos (rasguños, bolas de pelo, cajas de cartón) " +
                "para explicar conceptos de Java, Shell, Java Script, Type Scrip, Scripting en general, seguridad informática, seguridad defensiva y ofensiva, Python y Clean Code. Eres breve y directo.";

        // NUEVO JSON CON SYSTEM_INSTRUCTION
        String jsonBody = """
            {
              "system_instruction": {
                "parts": [{"text": "%s"}]
              },
              "contents": [{
                "parts":[{"text": "%s"}]
              }]
            }
            """.formatted(personalidad, mensaje);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // AQUÍ ESTÁ EL TRUCO DEL DÍA 3:
        System.out.println("DEBUG - Respuesta de Google: " + response.body());
        JsonNode rootNode = mapper.readTree(response.body());

        // 1. ¿Hay un error explícito de la API?
        if (rootNode.has("error")) {
            return "¡Miau! Error de API: " + rootNode.path("error").path("message").asText();
        }

        // 2. ¿Hay candidatos?
        JsonNode candidates = rootNode.path("candidates");
        if (candidates.isMissingNode() || candidates.isEmpty()) {
            // Si no hay candidatos, es probable que el filtro de seguridad bloqueó la respuesta
            return "Mishi se quedó mudo. Revisa si el prompt o la respuesta activaron los filtros de seguridad.";
        }

        // 3. Navegamos con seguridad usando .path() en lugar de .get()
        String textoLimpio = candidates.path(0)
                .path("content")
                .path("parts")
                .path(0)
                .path("text")
                .asText();

        if (textoLimpio.isEmpty()) {
            return "Mishi recibió una respuesta vacía del servidor.";
        }

        return textoLimpio;
    }
}
