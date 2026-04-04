package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MishiVault {

    // Usamos un nombre oculto (.mishi_vault) para que sea más "hacker"
    private static final String VAULT_NAME = ".mishi_vault";

    public Path getVaultPath() {
        // System.getProperty("user.home") es perfecto y profesional
        String home = System.getProperty("user.home");
        return Paths.get(home, VAULT_NAME);
    }

    public void initVault() {
        Path rutaVault = getVaultPath();

        try {
            if (Files.notExists(rutaVault)) {
                Files.createDirectory(rutaVault);
                System.out.println("🐾 Mishi: He cavado un agujero seguro en: " + rutaVault);
            } else {
                System.out.println("🐾 Mishi: El baúl ya existe. (Ronroneo de satisfacción)");
            }
        } catch (IOException e) {
            System.err.println("¡Miau! No pude crear el baúl: " + e.getMessage());
        }
    }
}
