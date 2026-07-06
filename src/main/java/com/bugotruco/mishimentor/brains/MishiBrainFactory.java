package com.bugotruco.mishimentor.brains;

import com.bugotruco.mishimentor.MishiConfig;
import com.bugotruco.mishimentor.MishiClient;

public class MishiBrainFactory {

    /**
     * El creador polimórfico.
     * Recibe el cliente compartido para inyectarlo defensivamente en los adaptadores Cloud.
     */
    public static MishiBrain crearCerebro(TipoCerebro tipo, MishiClient mishiClient) {
        switch (tipo) {

            // === 💻 CAPA LOCAL (OLLAMA) ===
            case LLAMA3_LOCAL:
                return new OllamaBrain(MishiConfig.getUrlOllama(), "llama3");

            case CODESTRAL_LOCAL:
                return new OllamaBrain(MishiConfig.getUrlOllama(), "codestral");

            case PHI_LOCAL:
                return new OllamaBrain(MishiConfig.getUrlOllama(), "phi3");

            case MOCKBRAIN_LOCAL:
                return new MockBrain();

            // === ☁️ CAPA EN LÍNEA (CLOUD) ===
            case GEMINI_CLOUD:
                // Le pasamos el cliente directamente si tu GeminiBrain lo requiere,
                // o si GeminiBrain ya usa internamente MishiConfig, lo dejas limpio.
                return new GeminiBrain();

            case GPT4O_CLOUD:
                // ¡Adiós OpenAiBrain! Usamos el StandardCloudBrain configurado para OpenAI
                return new StandardCloudBrain(mishiClient, "OPENAI");

            case DEEPSEEK_CLOUD:
                // ¡Misma clase, diferente configuración! Reutilización Senior
                return new StandardCloudBrain(mishiClient, "DEEPSEEK");

            case CLAUDE_CLOUD:
                // El cerebro exclusivo de Anthropic
                return new AnthropicBrain(mishiClient);

            default:
                System.out.println("🐾 Mishi: Motor no reconocido. Redirigiendo a Gemini por seguridad.");
                return new GeminiBrain();
        }
    }
}