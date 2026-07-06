/*
 * Copyright 2026 Salvador (Autilius) Granados Godínez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package com.bugotruco.mishimentor.util;

import com.bugotruco.mishimentor.MishiVault;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MishiPromptLoader {

    private static final MishiVault vault = new MishiVault();

    // Variables de estado para los prompts de usuario
    private String dudaTecnica = null;
    private String promptAumentado = null;

    // 🎯 Nueva variable de estado para el Prompt Maestro Analista
    private String systemPromptAnalista = null;

    /**
     * Carga y parsea el archivo de interacción con el usuario (prompts.md)
     */
    public void cargarPrompts() {
        Path rutaPrompts = vault.getPromptsPath().resolve("prompts.md");

        try {
            if (Files.notExists(rutaPrompts)) {
                System.err.println("⚠️ [MishiPromptLoader] El archivo prompts.md no existe en: " + rutaPrompts);
                return;
            }

            String contenidoCompleto = Files.readString(rutaPrompts);

            String claveDuda = "dudaTecnica=";
            String clavePrompt = "promptAumentado=";

            int indexDuda = contenidoCompleto.indexOf(claveDuda);
            int indexPrompt = contenidoCompleto.indexOf(clavePrompt);

            if (indexDuda != -1 && indexPrompt != -1) {
                this.dudaTecnica = contenidoCompleto.substring(
                        indexDuda + claveDuda.length(), indexPrompt
                ).trim();

                this.promptAumentado = contenidoCompleto.substring(
                        indexPrompt + clavePrompt.length()
                ).trim();

                System.out.println("🐾 [MishiPromptLoader] Mente modular cargada con éxito desde el búnker.");
            }

        } catch (IOException e) {
            System.err.println("❌ [MishiHealth-Alert] Error crítico: No se pudo leer el archivo prompts.md");
            System.err.println("💡 Por favor, verifica que el archivo exista en la ruta correcta: " + e.getMessage());
        }
    }

    /**
     * 👁️‍🗨️ NUEVO MÉTODO: Carga el prompt maestro desde la carpeta SISTEMA.
     * Como este archivo contiene el prompt completo directamente, no requiere substrings.
     */
    public void cargarSystemPrompt() {
        // Resolvemos usando la carpeta sistema/: ~/.mishi_vault/sistema/system_analista.md
        Path rutaSystemAnalista = vault.getSistemaPath().resolve("system_analista.md");

        try {
            if (Files.notExists(rutaSystemAnalista)) {
                System.err.println("⚠️ [MishiPromptLoader] El archivo maestro system_analista.md no existe en: " + rutaSystemAnalista);
                return;
            }

            // Al ser un archivo dedicado, leemos todo su contenido directo a la variable
            this.systemPromptAnalista = Files.readString(rutaSystemAnalista).trim();
            System.out.println("🧠 [MishiPromptLoader] Prompt maestro analista inyectado con éxito.");

        } catch (IOException e) {
            System.err.println("❌ [MishiHealth-Alert] Error crítico: No se pudo leer el prompt maestro analista.");
            System.err.println("💡 Verifica que system_analista.md esté en la carpeta sistema/: " + e.getMessage());
        }
    }

    // --- GETTERS SEGUROS ---
    public String getDudaTecnica() { return dudaTecnica; }
    public String getPromptAumentado() { return promptAumentado; }
    public String getSystemPromptAnalista() { return systemPromptAnalista; }
}