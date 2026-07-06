package com.bugotruco.mishimentor;

import java.nio.file.Files;
import java.util.Scanner;

public class MishiArranque {
    private static final Scanner scanner = new Scanner(System.in);
    private static final MishiVault vault = new MishiVault();
    private static final ConsoleWizard wizard = new ConsoleWizard();

    public static void prepararBunker() {
        String token = "";

        // 1. Si no existe configuración, el Wizard se encarga y nos devuelve el token creado
        if (Files.notExists(vault.getSistemaPath().resolve("config.properties"))) {
            ConsoleWizard wizard = new ConsoleWizard();
            token = wizard.iniciarConfiguracion();
        } else {
            // 2. Si ya existía, se lo pedimos formalmente aquí para desbloquear
            System.out.print("🔑 Introduce tu Token del Búnker para desbloquear MishiMentor: ");
            token = scanner.nextLine().trim();
        }

        if (token.isEmpty()) {
            throw new SecurityException("El token de seguridad no puede estar vacío.");
        }

        // Inicializamos el motor criptográfico en memoria de forma definitiva
        MishiCrypto.inicializarBunker(token);
    }
}