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
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + apiKey;

        // Cuerpo de la petición en formato JSON (muy simplificado para hoy)
        String jsonBody = """
                {
                  "contents": [{
                    "parts":[{"text": "%s"}]
                  }]
                }
                """.formatted(mensaje);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // AQUÍ ESTÁ EL TRUCO DEL DÍA 3:
        JsonNode rootNode = mapper.readTree(response.body());

        // Navegamos por el árbol del JSON de Gemini:
        // candidates[0] -> content -> parts[0] -> text
        String textoLimpio = rootNode.path("candidates")
                .get(0)
                .path("content")
                .path("parts")
                .get(0)
                .path("text")
                .asText();

        return textoLimpio;
    }
}
