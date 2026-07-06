package com.bugotruco.mishimentor;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        try {
            // 1. Inicializar infraestructura y seguridad transparentemente
            MishiArranque.prepararBunker();

            System.out.println("✅ ¡Búnker descifrado con éxito!");
            System.out.println("🐾 Despertando al Mishi de la consola...\n");

            // 2. Ejecutar la consola real de la app
            MishiConsole console = new MishiConsole();
            console.iniciar();

        } catch (SecurityException e) {
            System.err.println("❌ " + e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.err.println("❌ Error de Acceso: El token es incorrecto o el archivo está corrupto.");
            System.exit(1);
        }
    }
}