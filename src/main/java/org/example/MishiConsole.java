package org.example;

import org.example.brains.GeminiBrain;
import org.example.search.TavilySearch;
import org.example.brains.GeminiBrain;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;


public class MishiConsole {
    private static final String AMARILLO = "\u001B[33m";
    private static final String RESET = "\u001B[00m";
    private final MishiOrchestrator orchestrator;
    private final Scanner scanner;

    public MishiConsole() {
        // Inicializamos los componentes usando la configuración segura
        MishiVault vault = new MishiVault();
        GeminiBrain brain = new GeminiBrain();
        TavilySearch seeker = new TavilySearch();

        // El jefe que une a todos
        this.orchestrator = new MishiOrchestrator(vault, brain, seeker);
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() throws IOException, InterruptedException {
        boolean salir = false;
        while (!salir) {
            imprimirEncabezado();
            System.out.println("1. 🔍 Auditoría Pro (Código + Web Search)");
            System.out.println("2. 🐍 Ejecutar Script Externo (Python/JS)");
            System.out.println("3. 🗄️  Consultar Baúl (MishiVault)");
            System.out.println("4. ⚙️  Ver Configuración Actual");
            System.out.println("5. ❌ Salir");
            System.out.print("\n🐾 Mishi@Terminal:~$ ");

            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    ejecutarAuditoria();
                    break;
                case "2":
                    System.out.println("🐾 Mishi: Función en desarrollo para el próximo commit...");
                    break;
                case "3":
                    System.out.println("🐾 Mishi: Abriendo el baúl de recuerdos...");
                    orchestrator.mostrarResumenVault();
                    break;
                case "4":
                    mostrarConfig();
                    break;
                case "5":
                    salir = true;
                    System.out.println("🐾 Mishi: ¡Miau! Nos vemos mañana, Autilius.");
                    break;
                default:
                    System.out.println("🐾 Mishi: ¿Eh? Esa opción no existe en mi lógica.");
            }
        }
    }

    private void imprimirEncabezado() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("    __   Mimi chan  __  ___            __  Salvador Granados          ");
        System.out.println("   /  |/  (_)____/ /_(_)___  ___  / /_____  _____");
        System.out.println("  / /|_/ / / ___/ __/ / __ \\/ _ \\/ __/ __ \\/ ___/");
        System.out.println(" / /  / / (__  ) /_/ / / / /  __/ /_/ /_/ / /    ");
        System.out.println("/_/  /_/_/____/\\__/_/_/ /_/\\___/\\__/\\____/_/     ");
        System.out.println("           [ Versión 2.0 - IA Agent ]            ");
        System.out.println("=".repeat(50));
    }


    private String obtenerRuta() {

        //Verificamos sí el sistema SOPORTA gráficos
        boolean soportaGraficos = !GraphicsEnvironment.isHeadless();


        return null;
    }

    private void ejecutarAuditoria() throws IOException, InterruptedException {
        boolean soportaGraficos = !GraphicsEnvironment.isHeadless();
        String rutaSeleccionada = null;

        if (soportaGraficos) {
            System.out.println("\n🐾 Mishi: Elige tu modo de búsqueda:");
            System.out.println("1. 🖼️ Modo Visual (Ventana)");
            System.out.println("2. ⌨️ Modo Texto (Consola)");
            System.out.print("Opcion: ");

            String opcion = scanner.nextLine();

            if (opcion.equals("1")) {
                rutaSeleccionada = abrirVentanaVisual();
            } else {
                rutaSeleccionada = pedirRutaPorConsola();
            }
        } else {
            // Si no hay monitor, saltamos directo a la consola
            rutaSeleccionada = pedirRutaPorConsola();
        }

        // --- PROCESO DE AUDITORÍA ---
        if (rutaSeleccionada != null) {
            System.out.println("🐾 Mishi: Procesando... esto puede tardar unos segundos.");

            // Ejecutamos y guardamos el resultado
            String veredicto = orchestrator.realizarAuditoriaPro(rutaSeleccionada);

            // ¡No olvides imprimirlo para verlo!
            System.out.println("\n" + "=".repeat(50));
            System.out.println("🚀 RESULTADO DE LA AUDITORÍA");
            System.out.println("=".repeat(50));
            System.out.println(veredicto);
            System.out.println("=".repeat(50));
            System.out.println("\n" + "=".repeat(50));
            System.out.println("\n▶ OPTIMIZACIÓN COMERCIAL DISPONIBLE. ¿Ejecutar Refactor y Reporte Pro? (S/N)");
            String opcionCom = scanner.nextLine();

            if (opcionCom.equalsIgnoreCase("s")) {
                System.out.println(">> [SISTEMA] Generando documentación técnica sin metadatos informales...");
                orchestrator.ejecutarFlujoComercial(rutaSeleccionada);
            }
            System.out.println("· ".repeat(30));
        } else {
            System.out.println("🐾 Mishi: Operación cancelada o ruta no válida.");
        }
    }

    private void mostrarConfig() {
        System.out.println("\n--- CONFIGURACIÓN SEGURA ---");
        System.out.println("API Gemini: " + (MishiConfig.getApiKeyGemini() != null ? "Conectado ✅" : "Error ❌"));
        System.out.println("API Tavily: " + (MishiConfig.getApiKeyTavily() != null ? "Conectado ✅" : "Error ❌"));
        System.out.println("Prompt de Sistema: " + MishiConfig.getSystemPrompt().substring(0, 20) + "...");
    }

    private String abrirVentanaVisual() {
        try {
            // Ponemos el estilo visual de tu sistema (Windows/Mac)
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());

            JFileChooser selector = new JFileChooser();
            selector.setDialogTitle("🐾 Mishi: Selecciona tu archivo");
            selector.setCurrentDirectory(new File("."));

            int resultado = selector.showOpenDialog(null);
            if (resultado == JFileChooser.APPROVE_OPTION) {
                return selector.getSelectedFile().getAbsolutePath();
            }
        } catch (Exception e) {
            System.out.println("🐾 Mishi: No pude abrir la ventana, usaremos texto.");
        }
        return null;
    }
    private String pedirRutaPorConsola() {
        System.out.println("\n" + "=".repeat(30));
        System.out.println("📂 MODO TEXTO ACTIVO");
        System.out.print("🐾 Mishi: Pega la ruta o arrastra el archivo aquí: ");
        return scanner.nextLine().trim().replace("\"", "");
    }

    public String pedirEntrada(String mensaje) {
        System.out.print("\n" + AMARILLO + "➤ " + mensaje + RESET + ": ");
        return scanner.nextLine();
    }
}