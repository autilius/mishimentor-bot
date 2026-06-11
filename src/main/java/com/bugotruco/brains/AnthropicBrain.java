package com.bugotruco.brains;

import com.bugotruco.MishiClient;

public class AnthropicBrain implements MishiBrain {

    private final MishiClient client;

    public AnthropicBrain(MishiClient client) {
        this.client = client;
    }

    @Override
    public String pensar(String promptAumentado) {
        try {
            // Llama al método exclusivo de Anthropic en tu cliente
            return client.enviarMiauAnthropic(promptAumentado);
        } catch (Exception e) {
            throw new RuntimeException("Error en sinapsis de Anthropic: " + e.getMessage(), e);
        }
    }

    @Override
    public String getNombreModelo() {
        return "Claude 3.5 Sonnet (Anthropic)";
    }
}