package org.example.brains;

import org.example.MishiClient;
import java.io.IOException;

public class GeminiBrain implements MishiBrain {

    // El guardia estricto (final)
    private final MishiClient client;
        // Constructor vacío que soluciona el problema
    public GeminiBrain() {
            // Inicializamos el cliente aquí mismo.
            // Como MishiClient ya usa MishiConfig internamente, todo fluye.
            this.client = new MishiClient();
        }

        @Override
        public String pensar(String prompt) throws IOException, InterruptedException {
            // Tu lógica de envío a Gemini...
            return client.enviarMiau(prompt);
        }

    @Override
    public String getNombreModelo() {
        return "";
    }
}