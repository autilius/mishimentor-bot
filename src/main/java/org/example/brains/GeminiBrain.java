package org.example.brains;

import org.example.MishiClient;
import java.io.IOException;

public class GeminiBrain implements MishiBrain {
    private final MishiClient client;

    public GeminiBrain(String apiKey, String systemPrompt) {
        this.client = new MishiClient(apiKey, systemPrompt);
    }

    @Override
    public String pensar(String peticion) throws IOException, InterruptedException {
        return client.enviarMiau(peticion);
    }

    @Override
    public String getNombreModelo() {
        return "Google Gemini 1.5 Flash";
    }
}