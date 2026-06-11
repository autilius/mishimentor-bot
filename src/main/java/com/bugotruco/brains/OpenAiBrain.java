/*
 * Copyright 2026 Salvador (Autilius) Granados Godínez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package com.bugotruco.brains;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bugotruco.MishiConfig;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Cerebro basado en la nube que utiliza la API de OpenAI (GPT-4o)
 * para auditorías con un fuerte cumplimiento de directivas estructuradas.
 */
public class OpenAiBrain implements MishiBrain {

    private final String url;
    private final String apiKey;
    private final String modelo;
    private final ObjectMapper mapper;
    private final HttpClient client;

    public OpenAiBrain() {
        this.url = MishiConfig.getUrlOpenAi();
        this.apiKey = MishiConfig.getApiKeyOpenAi();
        this.modelo = "gpt-4o"; // Modelo estándar corporativo
        this.mapper = new ObjectMapper();
        this.client = HttpClient.newHttpClient();

        if (this.apiKey == null || this.url == null) {
            System.err.println("⚠️ Error: Faltan credenciales de OpenAI en config.properties");
        }
    }

    @Override
    public String pensar(String promptAumentado) throws IOException, InterruptedException {
        // Construimos el payload estándar de OpenAI (System + User)
        OpenAiRequest requestPayload = new OpenAiRequest(
                this.modelo,
                MishiConfig.getSystemPrompt(),
                promptAumentado
        );

        String jsonBody = mapper.writeValueAsString(requestPayload);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(this.url))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + this.apiKey) // OpenAI usa Bearer Token
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonNode rootNode = mapper.readTree(response.body());

        if (rootNode.has("error")) {
            return "¡Miau! Error de la API de OpenAI: " + rootNode.path("error").path("message").asText();
        }

        // Navegamos el árbol JSON estándar de OpenAI: choices[0].message.content
        return rootNode.path("choices").path(0)
                .path("message").path("content").asText();
    }

    @Override
    public String getNombreModelo() {
        return "OpenAI Cloud (Model: " + this.modelo + ")";
    }

    // --- POJOs de Mapeo Interno Estilo MishiClient para OpenAI ---

    public static class OpenAiRequest {
        public String model;
        public List<Message> messages;

        public OpenAiRequest(String model, String sysText, String userText) {
            this.model = model;
            this.messages = new ArrayList<>();
            this.messages.add(new Message("system", sysText));
            this.messages.add(new Message("user", userText));
        }
    }

    public static class Message {
        public String role;
        public String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }
}