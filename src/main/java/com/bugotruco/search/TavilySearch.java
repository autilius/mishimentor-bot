package com.bugotruco.search;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bugotruco.MishiConfig;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.ArrayList;

public class TavilySearch implements MishiSeeker {

    private final String API_URL = MishiConfig.getUrlTavily();
    private final String apiKey;
    private final ObjectMapper objectMapper = new ObjectMapper(); // Reutilizamos Jackson

    public TavilySearch() {
        this.apiKey = MishiConfig.getApiKeyTavily();
    }

    public TavilySearch(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public List<String> buscar(String query) {
        List<String> resultados = new ArrayList<>();
        try {
            // Escapamos las comillas de la query para que no rompan el JSON manual
            String queryEscapada = query.replace("\"", "\\\"");

            String jsonBody = """
                {
                  "api_key": "%s",
                  "query": "%s",
                  "search_depth": "advanced",
                  "max_results": 3,
                  "include_answer": false
                }
                """.formatted(apiKey, queryEscapada);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                resultados = extraerContenidoEstructurado(response.body());
            } else {
                System.err.println("🐾 Mishi: Tavily respondió con código: " + response.statusCode());
            }
        } catch (Exception e) {
            System.err.println("🐾 Mishi: Falló el rastreo web: " + e.getMessage());
        }
        return resultados;
    }

    private List<String> extraerContenidoEstructurado(String json) {
        List<String> fragmentosLimpios = new ArrayList<>();
        try {
            JsonNode rootNode = objectMapper.readTree(json);
            JsonNode resultsNode = rootNode.get("results");

            if (resultsNode != null && resultsNode.isArray()) {
                for (JsonNode result : resultsNode) {
                    String title = result.path("title").asText();
                    String url = result.path("url").asText();
                    String content = result.path("content").asText();

                    // Creamos un formato súper limpio para el prompt inyectado
                    String formatoLimpio = """
                        - FUENTE: %s (%s)
                          HALLAZGO: %s
                        """.formatted(title, url, content);

                    fragmentosLimpios.add(formatoLimpio);
                }
            }
        } catch (Exception e) {
            System.err.println("🐾 Mishi: Error al procesar JSON de Tavily: " + e.getMessage());
        }
        return fragmentosLimpios;
    }
}