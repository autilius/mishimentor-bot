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

import com.bugotruco.mishimentor.util.MishiPromptLoader;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

public class MishiConfig {

    private static final Properties prop = new Properties();
    private static final MishiVault vault = new MishiVault();
    private static final MishiPromptLoader promptLoader = new MishiPromptLoader();
    private static String systemPromptCache = null;

    static {
        // 1. Aseguramos que existan las carpetas (~/.mishi_vault/sistema, prompts, json)
        vault.initVault();

        // 🎯 [EL TRUCO CRUCIAL]: Despertamos la mente del Loader al arrancar la clase
        promptLoader.cargarPrompts();
        promptLoader.cargarSystemPrompt();

        Path rutaConfig = vault.getSistemaPath().resolve("config.properties");

        try (FileInputStream input = new FileInputStream(rutaConfig.toFile())) {
            prop.load(input);
        } catch (IOException ex) {
            System.err.println("¡Miau! No encontré el archivo config.properties en el baúl. El Mishi te va a ayudar a crear uno, comenzamos.");

            // 2. Desplegamos el asistente interactivo por consola para crear el archivo
            ConsoleWizard wizard = new ConsoleWizard();
            wizard.iniciarConfiguracion();

            // 3. ¡EL TRUCO GATUNO RECARGADO! Cargamos en caliente desde la ruta del Vault
            try (FileInputStream inputNuevo = new FileInputStream(rutaConfig.toFile())) {
                prop.load(inputNuevo);
                System.out.println("🐾 [MishiConfig] Configuración cargada con éxito tras el setup inicial.");
            } catch (IOException exNuevo) {
                System.err.println("❌ Error crítico: El asistente no pudo guardar o leer el archivo nuevo: " + exNuevo.getMessage());
            }
        }
    }

    // --- CONFIGURACIONES BASE ---
    public static String getApiKeyTavily() {
        String apiKeyTavily = prop.getProperty("apiKeyTavily");
        return MishiCrypto.decrypt(apiKeyTavily);
    }
    public static String getUrlTavily() { return prop.getProperty("tavily.api.url"); }

    // --- 🎯 EL NUEVO RADAR DE PROMPTS DESDE MD ---
    public static String getSystemPrompt() {
        if (systemPromptCache == null) {
            // Si el caché está vacío, lo llenamos leyendo el loader una única vez
            systemPromptCache = promptLoader.getSystemPromptAnalista();
        }
        return systemPromptCache;
    }

    // --- CEREBRO BASE: GEMINI ---
    public static String getApiKeyGemini() {
        String apiKeyGemini = prop.getProperty("gemini.api.key");
        return MishiCrypto.decrypt(apiKeyGemini);
    }
    public static String getUrlGemini() { return prop.getProperty("gemini.api.url"); }

    // --- MOTORES LOCALES (OLLAMA) ---
    public static String getUrlOllama() {
        return prop.getProperty("ollama.api.url", "http://localhost:11434/api/generate");
    }

    // --- NUEVOS REFUERZOS EN LA NUBE ---
    public static String getApiKeyOpenAi() {
        String apiKeyOpenAi = prop.getProperty("openAi.api.key");
        return MishiCrypto.decrypt(apiKeyOpenAi);
    }
    public static String getUrlOpenAi() { return prop.getProperty("openai.api.url"); }

    public static String getApiKeyAnthropic() {
        String apiKeyAnthropic = prop.getProperty("anthropic.api.key");
        return MishiCrypto.decrypt(apiKeyAnthropic);
    }
    public static String getUrlAnthropic() { return prop.getProperty("anthropic.api.url"); }

    public static String getApiKeyDeepSeek() {
        String apiKeyDeepSeek = prop.getProperty("deepSeek.api.key");
        return MishiCrypto.decrypt(apiKeyDeepSeek);
    }
    public static String getUrlDeepSeek() { return prop.getProperty("deepseek.api.url"); }

    public static String getOperador() { return prop.getProperty("mishi.operador", "Agente_777"); }

    public static String getMishiVersion() { return prop.getProperty("mishi.version", "v4.0-SNAPSHOT"); }

}