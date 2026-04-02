package org.example;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class MishiConfig {

    public static String getApiKey() {
        Properties prop = new Properties();
        try (FileInputStream input = new FileInputStream("config.properties")) {
            prop.load(input);
            return prop.getProperty("mishi.api.key");
        } catch (IOException ex) {
            System.err.println("¡Miau! No encontré el archivoconfig.properties");
            return null;
        }
    }

    public static String getSystemPrompt() {
        Properties prop = new Properties();
        try (FileInputStream input = new FileInputStream("config.properties")) {
            prop.load(input);
            return prop.getProperty("mishi.system.prompt");
        } catch (IOException e) {
            System.err.println("¡Miau! No encontré el archivoconfig.properties");
            return null;
        }
    }
}
