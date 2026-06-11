package com.bugotruco;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * El Guardián Médico del Sistema v3.0.
 * Verifica la conectividad real a internet y previene fallos catastróficos en la nube.
 */
public class MishiHealth {

    private static final String PRUEBA_CONECTIVIDAD_URL = "https://connectivitycheck.gstatic.com/generate_204";

    /**
     * Revisa el estado de salud del ecosistema completo.
     * Al invocar este método, automáticamente se forzará la carga de MishiConfig
     * disparando el bloque static y el ConsoleWizard si el archivo no existe.
     */
    public static boolean sistemaListoParaOperar() {
        System.out.println("🩺 [MishiHealth] Pasando revista a los signos vitales de la suite...");

        // Al tocar cualquier propiedad de MishiConfig por primera vez,
        // la JVM levanta el bloque static y el asistente si hace falta.
        String ollamaHost = MishiConfig.getUrlOllama();

        if (ollamaHost == null || ollamaHost.isBlank()) {
            System.err.println("❌ [MishiHealth] Error: No hay ningún motor base configurado.");
            return false;
        }

        System.out.println("✅ [MishiHealth] Signos vitales estables. Ecosistema listo.");
        return true;
    }

    /**
     * Valida de forma defensiva si hay internet abriendo un canal HTTP ligero.
     */
    public static boolean hayInternet() {
        try {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(2))
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(PRUEBA_CONECTIVIDAD_URL))
                    .GET()
                    .build();

            HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
            return response.statusCode() == 204;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Valida si un proveedor específico tiene sus credenciales válidas y cargadas.
     * Aplicamos el fix defensivo con 'esValido' para que no nos engañen textos vacíos.
     */
    public static boolean esMotorConfigurado(String proveedor) {
        if (proveedor == null) return false;

        return switch (proveedor.toUpperCase()) {
            case "GEMINI" -> esValido(MishiConfig.getApiKeyGemini()) && esValido(MishiConfig.getUrlGemini());
            case "OPENAI" -> esValido(MishiConfig.getApiKeyOpenAi()) && esValido(MishiConfig.getUrlOpenAi());
            case "DEEPSEEK" -> esValido(MishiConfig.getApiKeyDeepSeek()) && esValido(MishiConfig.getUrlDeepSeek());
            case "ANTHROPIC" -> esValido(MishiConfig.getApiKeyAnthropic()) && esValido(MishiConfig.getUrlAnthropic());
            case "OLLAMA" -> esValido(MishiConfig.getUrlOllama());
            default -> false;
        };
    }

    private static boolean esValido(String valor) {
        return valor != null && !valor.isBlank();
    }

    /**
     * Semáforo provisional para control de cuotas y tokens.
     */
    public static boolean tenemosCreditos() {
        return true;
    }
}