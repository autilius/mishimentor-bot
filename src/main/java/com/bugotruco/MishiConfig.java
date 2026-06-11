/*
 * Copyright 2026 Salvador (Autilius) Granados Godínez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package com.bugotruco;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class MishiConfig {

    private static final Properties prop = new Properties();

    static {
        try (FileInputStream input = new FileInputStream("config.properties")) {
            prop.load(input);
        } catch (IOException ex) {
            System.err.println("¡Miau! No encontré el archivo config.properties. El Mishi te va ayudar a crear uno, comenzamos.");

            // 1. Desplegamos el asistente interactivo por consola para crear el archivo
            ConsoleWizard wizard = new ConsoleWizard();
            wizard.iniciarConfiguracion();

            // 2. ¡EL TRUCO GATUNO! Intentamos cargar el archivo recién creado en caliente
            try (FileInputStream inputNuevo = new FileInputStream("config.properties")) {
                prop.load(inputNuevo);
                System.out.println("🐾 [MishiConfig] Configuración cargada con éxito tras el setup inicial.");
            } catch (IOException exNuevo) {
                System.err.println("❌ Error crítico: El asistente no pudo guardar o leer el archivo nuevo: " + exNuevo.getMessage());
            }
        }
    }

    // --- CONFIGURACIONES BASE ---
    public static String getApiKeyTavily() { return prop.getProperty("tavily.api.key"); }
    public static String getUrlTavily() { return prop.getProperty("tavily.api.url"); }
    public static String getSystemPrompt() { return prop.getProperty("mishi.system.prompt"); }

    // --- CEREBRO BASE: GEMINI ---
    public static String getApiKeyGemini() { return prop.getProperty("gemini.api.key"); }
    public static String getUrlGemini() { return prop.getProperty("gemini.api.url"); }

    // --- MOTORES LOCALES (OLLAMA) ---
    public static String getUrlOllama() {
        return prop.getProperty("ollama.api.url", "http://localhost:11434/api/generate");
    }

    // --- NUEVOS REFUERZOS EN LA NUBE ---
    public static String getApiKeyOpenAi() { return prop.getProperty("openai.api.key"); }
    public static String getUrlOpenAi() { return prop.getProperty("openai.api.url"); }

    public static String getApiKeyAnthropic() { return prop.getProperty("anthropic.api.key"); }
    public static String getUrlAnthropic() { return prop.getProperty("anthropic.api.url"); }

    public static String getApiKeyDeepSeek() { return prop.getProperty("deepseek.api.key"); }
    public static String getUrlDeepSeek() { return prop.getProperty("deepseek.api.url"); }

}