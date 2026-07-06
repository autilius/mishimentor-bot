/*
 * Copyright 2026 Salvador (Autilius) Granados Godínez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package com.bugotruco.mishimentor;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.Scanner;

public class ConsoleWizard {
    private final Scanner scanner = new Scanner(System.in);
    private final Properties props = new Properties();
    private final MishiVault vault = new MishiVault(); // 🐾 Conectamos al baúl para saber dónde guardar

    // 🔄 Cambiamos de 'void' a 'String'
    public String iniciarConfiguracion() {
        System.out.println("\n🐾 --- MISHIMENTOR v4.0 - CONFIGURACIÓN DEL BÚNKER --- 🐾");

        String token = "";
        while (token.isEmpty()) {
            System.out.print("🔐 Define una Frase de Seguridad/Token para cifrar tus llaves: ");
            token = scanner.nextLine().trim();
            if (token.isEmpty()) {
                System.out.println("⚠️ Por seguridad, necesitas definir un token para proteger el búnker.");
            }
        }

        MishiCrypto.inicializarBunker(token);

        // 🎯 [NUEVO V4.0] Clavamos la versión del sistema automáticamente
        props.setProperty("mishi.version", "v4.0-SNAPSHOT");

        // 🎯 [NUEVO V4.0] Capturamos la identidad del Comandante
        System.out.print("👨‍💻 Introduce tu nombre de Operador [Por defecto: Agente_777]: ");
        String operador = scanner.nextLine().trim();
        if (operador.isEmpty()) {
            operador = "Agente_777";
        }
        props.setProperty("mishi.operador", operador);

        System.out.println("\nVamos a preparar tus credenciales de IA...\n");

        configurarGemini();
        configurarTavily();
        configurarDeepSeek();
        configurarOpenAi();
        configurarAnthropic();
        configurarOllama();
        verificarEstructuraPrompts();
        guardarArchivoEnVault();

        return token;
    }

    private void configurarGemini() {
        System.out.print("🔑 Introduce tu Gemini API Key (Cerebro Base): ");
        String key = scanner.nextLine().trim();
        if (!key.isEmpty()) {
            props.setProperty("gemini.api.key", key);
            props.setProperty("gemini.api.url", "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent");
        }
    }

    private void configurarTavily() {
        System.out.print("🔑 Introduce tu Tavily API Key (Radar Web): ");
        String key = scanner.nextLine().trim();
        if (!key.isEmpty()) {
            props.setProperty("tavily.api.key", key);
            props.setProperty("tavily.api.url", "https://api.tavily.com/search");
        }
    }

    private void configurarDeepSeek() {
        System.out.print("🔑 Introduce tu DeepSeek API Key: ");
        String key = scanner.nextLine().trim();
        if (!key.isEmpty()) {
            props.setProperty("deepseek.api.key", key);
            props.setProperty("deepseek.api.url", "https://api.deepseek.com/v1/chat/completions");
        }
    }

    private void configurarOpenAi() {
        System.out.print("🔑 Introduce tu OpenAI API Key: ");
        String key = scanner.nextLine().trim();
        if (!key.isEmpty()) {
            props.setProperty("openai.api.key", key);
            props.setProperty("openai.api.url", "https://api.openai.com/v1/chat/completions");
        }
    }

    private void configurarAnthropic() {
        System.out.print("🔑 Introduce tu Anthropic API Key: ");
        String key = scanner.nextLine().trim();
        if (!key.isEmpty()) {
            props.setProperty("anthropic.api.key", key);
            props.setProperty("anthropic.api.url", "https://api.anthropic.com/v1/messages");
        }
    }

    private void configurarOllama() {
        System.out.print("🌐 URL de Ollama Local [Por defecto: http://localhost:11434]: ");
        String host = scanner.nextLine().trim();
        if (host.isEmpty()) {
            host = "http://localhost:11434";
        }

        // Ajustamos al endpoint por defecto que maneja MishiConfig
        String fullUrl = host + "/api/generate";

        System.out.println("🔍 Confirmando conexión con Ollama en " + host + "...");
        if (verificarConexionPing(host)) {
            System.out.println("✅ ¡Miau! Conexión exitosa con el servicio local de Ollama.");
            props.setProperty("ollama.api.url", fullUrl);
        } else {
            System.out.println("⚠️ ALERTA: No se detectó Ollama corriendo. Se guardará la URL por defecto.");
            props.setProperty("ollama.api.url", fullUrl);
        }
    }

    /**
     * 🧠 NUEVO CONTROL V4.0: Asegura que existan las plantillas maestras en Markdown
     * si es la primera vez que se monta el búnker.
     */
    private void verificarEstructuraPrompts() {
        System.out.println("\n🧠 --- VERIFICANDO MENTE MODULAR (PROMPTS) ---");
        Path rutaSystem = vault.getSistemaPath().resolve("system_analista.md");
        Path rutaPrompts = vault.getPromptsPath().resolve("prompts.md");

        try {
            if (Files.notExists(rutaSystem)) {
                String plantillaMaestraDefault =
                        "Eres un auditor Senior experto en seguridad de software (SecOps) y Clean Code.\n" +
                                "Tu misión es analizar el código proporcionado buscando vulnerabilidades críticas.\n\n" +
                                "=== REPORTE DE INTELIGENCIA TAVILY ===\n" +
                                "${CONTEXTO_WEB}\n\n" +
                                "=== CÓDIGO FUENTE A AUDITAR ===\n" +
                                "${CODIGO}\n\n" +
                                "Genera el veredicto usando los marcadores MISHIFINDING|ID|Título|Gravedad|Contexto|Descripción.";

                Files.writeString(rutaSystem, plantillaMaestraDefault);
                System.out.println("📄 [MishiVault] Creado archivo por defecto: system_analista.md");
            }

            if (Files.notExists(rutaPrompts)) {
                String promptsDefault =
                        "dudaTecnica=Analizar vulnerabilidades y exploits recientes relacionados con las librerías:\n\n" +
                                "promptAumentado=Usa la plantilla del analista maestro.";

                Files.writeString(rutaPrompts, promptsDefault);
                System.out.println("📄 [MishiVault] Creado archivo por defecto: prompts.md");
            }
            System.out.println("✅ Estructura de Markdown lista y alineada en el Baúl.");
        } catch (IOException e) {
            System.err.println("⚠️ No se pudieron generar las plantillas Markdown por defecto: " + e.getMessage());
        }
    }

    private boolean verificarConexionPing(String urlTarget) {
        try {
            URL url = new URL(urlTarget);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(1500);
            connection.connect();
            return (connection.getResponseCode() == 200);
        } catch (Exception e) {
            return false;
        }
    }

    private void guardarArchivoEnVault() {
        Path rutaConfig = vault.getSistemaPath().resolve("config.properties");

        // Creamos un clon de Properties para no ensuciar los valores en memoria si la app sigue corriendo
        Properties encryptedProps = new Properties();

        props.forEach((key, val) -> {
            String stringKey = (String) key;
            String stringVal = (String) val;

            // Encriptamos únicamente los campos que contengan ".key" o tokens sensibles
            if (stringKey.contains(".key") || stringKey.contains("api.url")) {
                encryptedProps.setProperty(stringKey, MishiCrypto.encrypt(stringVal));
            } else {
                encryptedProps.setProperty(stringKey, stringVal); // Hosts o configuraciones simples van plano
            }
        });

        try (FileOutputStream out = new FileOutputStream(rutaConfig.toFile())) {
            encryptedProps.store(out, "MishiMentor v4.0 - Configuración Encriptada del Búnker");
            System.out.println("\n💾 ¡Configuración ENCRIPTADA guardada con éxito en el Baúl! 🐈‍⬛🚀\n");
        } catch (IOException e) {
            System.out.println("\n❌ Error crítico de E/S al escribir en el Baúl: " + e.getMessage());
        }
    }
}