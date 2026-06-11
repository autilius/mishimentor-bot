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
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.bugotruco.MishiConfig;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Cerebro basado en el entorno local que utiliza la API de Ollama
 * para auditorías privadas y con soberanía de datos absoluta.
 */
public class OllamaBrain implements MishiBrain {

    private final String url;
    private final String modelo;
    private final ObjectMapper objectMapper;
    private final HttpClient client; // Centralizamos el cliente de red local

    // Constructor por defecto alineado con MishiConfig v3.0
    public OllamaBrain() {
        this.url = MishiConfig.getUrlOllama();
        this.modelo = "llama3"; // Modelo por defecto
        this.objectMapper = new ObjectMapper();
        this.client = HttpClient.newHttpClient(); // Instancia única para este cerebro
    }

    // Constructor parametrizado para la inyección dinámica de la Factoría
    public OllamaBrain(String url, String modelo) {
        this.url = url;
        this.modelo = modelo;
        this.objectMapper = new ObjectMapper();
        this.client = HttpClient.newHttpClient(); // Evitamos fugas de sockets reutilizando este cliente
    }

    @Override
    public String pensar(String promptAumentado) throws IOException, InterruptedException {
        // Construimos el cuerpo JSON estructurado con Jackson
        ObjectNode jsonBodyNode = objectMapper.createObjectNode();
        jsonBodyNode.put("model", this.modelo);
        jsonBodyNode.put("prompt", promptAumentado);
        jsonBodyNode.put("stream", false);

        // 🐾 INYECCIÓN CRUCIAL: Le recordamos a Ollama que es MishiMentor mediante el prompt de sistema centralizado
        if (MishiConfig.getSystemPrompt() != null) {
            jsonBodyNode.put("system", MishiConfig.getSystemPrompt());
        }

        String jsonBody = objectMapper.writeValueAsString(jsonBodyNode);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(this.url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        // El Orchestrator atrapa centralizadamente los Exceptions
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JsonNode rootNode = objectMapper.readTree(response.body());
            return rootNode.path("response").asText();
        } else {
            throw new IOException("🐾 Mishi Error: Ollama respondió con código de estado " + response.statusCode());
        }
    }

    @Override
    public String getNombreModelo() {
        return "Ollama Local (Model: " + this.modelo + ")";
    }
}