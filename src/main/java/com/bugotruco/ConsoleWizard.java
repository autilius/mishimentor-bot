package com.bugotruco;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import java.util.Scanner;

public class ConsoleWizard {
    private final Scanner scanner = new Scanner(System.in);
    private final Properties props = new Properties();
    private final String CONFIG_FILE = "config.properties";

    public void iniciarConfiguracion() {
        System.out.println("\n🐾 --- MISHIMENTOR v3.0 - CONFIGURACIÓN DEL SISTEMA --- 🐾");
        System.out.println("Vamos a preparar tus cerebros de IA. Deja vacío si no usas esa opción.\n");

        // 1. Configuración de APIs en la Nube
        configurarDeepSeek();

        // 2. Configuración e Inspección de IA Local (¡Sin dar nada por sentado!)
        configurarOllama();

        // 3. Prompt de Sistema Personalizado
        configurarSystemPrompt();

        // 4. Guardar los resultados
        guardarArchivo();
    }

    private void configurarDeepSeek() {
        System.out.print("🔑 Introduce tu DeepSeek API Key: ");
        String key = scanner.nextLine().trim();
        if (!key.isEmpty()) {
            props.setProperty("DEEPSEEK_API_KEY", key);
            System.out.println("-> API Key registrada (Se validará al iniciar las auditorías).");
        }
    }

    private void configurarOllama() {
        System.out.print("🌐 URL de Ollama Local [Por defecto: http://localhost:11434]: ");
        String host = scanner.nextLine().trim();
        if (host.isEmpty()) {
            host = "http://localhost:11434";
        }

        System.out.println("🔍 Confirmando conexión con Ollama en " + host + "...");
        if (verificarConexionPing(host)) {
            System.out.println("✅ ¡Miau! Conexión exitosa con el servicio local de Ollama.");
            props.setProperty("OLLAMA_HOST", host);

            System.out.print("🤖 ¿Qué modelo local vas a usar? (ej. llama3, qwen2.5, deepseek-coder): ");
            String modelo = scanner.nextLine().trim();
            props.setProperty("OLLAMA_MODEL", modelo.isEmpty() ? "llama3" : modelo);
        } else {
            System.out.println("⚠️ ALERTA: No se detectó Ollama corriendo en esa dirección.");
            System.out.println("Se guardará la configuración, pero asegúrate de encenderlo antes de auditar.");
            props.setProperty("OLLAMA_HOST", host);
            props.setProperty("OLLAMA_MODEL", "fallback-local");
        }
    }

    private void configurarSystemPrompt() {
        System.out.println("\n🧠 --- CONFIGURACIÓN DEL PROMPT DE SISTEMA ---");
        System.out.println("Define la personalidad de tu auditor. Ejemplo: 'Eres un experto senior en seguridad...'");
        System.out.print("Introduce el System Prompt para el Mishi: ");
        String prompt = scanner.nextLine().trim();

        if (prompt.isEmpty()) {
            // Un prompt genérico para no revelar tu secreto de estado corporativo
            prompt = "Eres un asistente de auditoría de código en Java enfocado en Clean Code y OWASP Top 10.";
        }
        props.setProperty("SYSTEM_PROMPT", prompt);
    }

    private boolean verificarConexionPing(String urlTarget) {
        try {
            // Intentamos pegarle al endpoint básico de Ollama para ver si responde
            URL url = new URL(urlTarget);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(2000); // 2 segundos máximo para no colgar la consola
            connection.connect();
            int responseCode = connection.getResponseCode();
            return (responseCode == 200);
        } catch (Exception e) {
            return false;
        }
    }

    private void guardarArchivo() {
        try (FileOutputStream out = new FileOutputStream(CONFIG_FILE)) {
            props.store(out, "MishiMentor v3.0 - Configuración de Consola");
            System.out.println("\n💾 ¡Configuración inmutable guardada con éxito en 'config.properties'! 🐈‍⬛🚀\n");
        } catch (IOException e) {
            System.out.println("\n❌ Error crítico de E/S al escribir el archivo: " + e.getMessage());
        }
    }
}