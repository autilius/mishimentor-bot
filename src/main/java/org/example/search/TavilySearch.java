package org.example.search;

import org.example.MishiConfig;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.ArrayList;


public class TavilySearch implements MishiSeeker {

    private final String API_URL = MishiConfig.getUrlTavily();
    private final String apiKey;

    // Constructor por defecto para uso normal
    public TavilySearch() {
        this.apiKey = MishiConfig.getApiKeyTavily();
    }

    // Constructor con parámetros (útil para pruebas unitarias)
    public TavilySearch(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public List<String> buscar(String query) {
        List<String> resultados = new ArrayList<>();
        try {
            // Cuerpo de la petición en JSON
            String jsonBody = """
                {
                  "api_key": "%s",
                  "query": "%s",
                  "search_depth": "advanced",
                  "max_results": 3
                }
                """.formatted(apiKey, query);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // Aquí podrías usar una librería JSON, pero para no complicarte hoy:
                resultados.add(extraerContenidoSimple(response.body()));
            }
        } catch (Exception e) {
            System.err.println("🐾 Mishi: Falló el rastreo web: " + e.getMessage());
        }
        return resultados;
    }

    // Un método rápido para sacar el texto importante sin librerías pesadas
    private String extraerContenidoSimple(String json) {
        // En una app real, aquí usarías Jackson. Por ahora, devolvemos el crudo:
        return json;
    }
}