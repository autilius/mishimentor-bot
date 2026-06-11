/*
 * Copyright 2026 Salvador (Autilius) Granados Godínez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */

package com.bugotruco.brains;

import com.bugotruco.MishiClient;
import java.io.IOException;

/**
 * Cerebro basado en la nube que utiliza la API de Google Gemini
 * para el procesamiento avanzado de auditorías.
 */
public class GeminiBrain implements MishiBrain {

    // El guardia estricto (final) que maneja la comunicación HTTP
    private final MishiClient client;
    private final String nombreModelo;

    // Constructor por defecto
    public GeminiBrain() {
        // Inicializamos el cliente aquí mismo.
        // Como MishiClient ya usa MishiConfig internamente, todo fluye de forma nativa.
        this.client = new MishiClient();
        this.nombreModelo = "Google Gemini Pro (Cloud)";
    }

    // Constructor con parámetros por si en el futuro necesitas inyectar un cliente mockeado en pruebas
    public GeminiBrain(MishiClient client, String nombreModelo) {
        this.client = client;
        this.nombreModelo = nombreModelo;
    }

    @Override
    public String pensar(String prompt) throws IOException, InterruptedException {
        // Conectamos directamente con el cliente especializado para enviar el prompt aumentado
        return client.enviarMiau(prompt);
    }

    @Override
    public String getNombreModelo() {
        // Ahora sí, devolvemos el identificador real del cerebro para que luzca en los reportes v3.0
        return this.nombreModelo;
    }
}