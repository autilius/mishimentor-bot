package org.example;

import javax.swing.*;
import java.io.File;
import java.util.Scanner;

public class MishiConsole {

    // Colores ANSI para el toque "Hacker"
    private static final String RESET = "\u001B[00m";
    static final String VERDE = "\u001B[32m";
    static final String ROJO = "\u001B[31m";
    static final String AMARILLO = "\u001B[33m";
    private static final String CIAN = "\u001B[36m";

    private final Scanner scanner = new Scanner(System.in);

    public void mostrarBienvenida() {
        limpiarPantalla();
        System.out.println(VERDE + """
                 |\\__/,|   (`\\
               _.|o o  |_   ) )  MISHI MENTOR v1.0
             -(((---(((--------  "Auditoría con Garras"
            """ + RESET);
        escribirLento(CIAN + "🐾 Mishi: " + RESET + "Miau... Hola humano. ¿Qué código quieres que destroce hoy?\n", 30);
    }

    public void limpiarPantalla() {
        // Truco para limpiar la consola en Linux
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public void escribirLento(String texto, int velocidad) {
        for (char c : texto.toCharArray()) {
            System.out.print(c);
            try {
                Thread.sleep(velocidad);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public String pedirEntrada(String mensaje) {
        System.out.print("\n" + AMARILLO + "➤ " + mensaje + RESET + ": ");
        return scanner.nextLine();
    }

    public void mostrarMenu() {
        System.out.println("\n" + VERDE + "--- MENÚ PRINCIPAL ---" + RESET);
        System.out.println("1. Auditar un archivo (.java)");
        System.out.println("2. Ver historial de recuerdos (Vault)");
        System.out.println("3. Salir (y dejarme dormir)");
    }

    public String seleccionarArchivoGUI() {
        // Esto abre la ventana de "Abrir archivo" de tu sistema
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("🐾 Mishi: Elige el código que quieres que olfatee");

        // Abrir en el directorio actual del proyecto
        chooser.setCurrentDirectory(new File("."));

        int resultado = chooser.showOpenDialog(null);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile().getAbsolutePath();
        }

        return null; // El usuario canceló
    }

    public String seleccionarArchivoVintage() {
        JFileChooser selector = new JFileChooser();
        selector.setDialogTitle("🐾 Mishi: Elige el código para la auditoría");
        selector.setCurrentDirectory(new File("."));

        int resultado = selector.showOpenDialog(null);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            // Retorna la ruta completa (String)
            return selector.getSelectedFile().getAbsolutePath();
        }
        return null;
    }
}
