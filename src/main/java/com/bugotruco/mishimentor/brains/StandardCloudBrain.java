package com.bugotruco.mishimentor.brains;

import com.bugotruco.mishimentor.MishiClient;

public class StandardCloudBrain implements MishiBrain {

    private final MishiClient client;
    private final String proveedor; // "OPENAI" o "DEEPSEEK"

    public StandardCloudBrain(MishiClient client, String proveedor) {
        this.client = client;
        this.proveedor = proveedor;
    }

    @Override
    public String pensar(String promptAumentado) {
        try {
            // Llama al método unificado que creamos en el MishiClient
            return client.enviarMiauEstandar(promptAumentado, this.proveedor);
        } catch (Exception e) {
            throw new RuntimeException("Error en sinapsis de " + proveedor + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String getNombreModelo() {
        return "DEEPSEEK".equalsIgnoreCase(proveedor) ? "DeepSeek-Chat (Cloud)" : "GPT-4o-mini (OpenAI)";
    }
}