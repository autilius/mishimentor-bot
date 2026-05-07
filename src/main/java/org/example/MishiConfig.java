package org.example;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class MishiConfig {

    private static final Properties prop = new Properties();

    // Este bloque se ejecuta una sola vez cuando la clase se carga en memoria
    static {
        try (FileInputStream input = new FileInputStream("config.properties")) {
            prop.load(input);
        } catch (IOException ex) {
            System.err.println("¡Miau! No encontré el archivo config.properties. El Mishi está operando a ciegas.");
        }
    }

    public static String getApiKeyGemini() {
        return prop.getProperty("gemini.api.key");
    }

    public static String getUrlGemini() {
        return prop.getProperty("gemini.api.url");
    }

    public static String getApiKeyTavily() {
        return prop.getProperty("tavily.api.key");
    }

    public static String getUrlTavily() {
        return prop.getProperty("tavily.api.url");
    }

    public static String getSystemPrompt() {
        return prop.getProperty("mishi.system.prompt");
    }
}