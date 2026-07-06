/*
 * Copyright 2026 Salvador (Autilius) Granados Godínez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */

package com.bugotruco.mishimentor;

import com.bugotruco.mishimentor.brains.MishiBrain;
import com.bugotruco.mishimentor.brains.MishiBrainFactory;
import com.bugotruco.mishimentor.brains.TipoCerebro;
import com.bugotruco.mishimentor.search.TavilySearch;
import com.bugotruco.mishimentor.util.MishiPromptLoader;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Consola Interactiva y Panel de Control de MishiMentor v3.0.
 * Permite la gestión de auditorías e inyección de cerebros polimórficos en caliente.
 */
public class MishiConsole {
    private static final String AMARILLO = "\u001B[33m";
    private static final String NARANJA_BUNKER = "\u001B[38;5;208m";
    private static final String GRIS_TACTICO = "\u001B[38;5;244m";
    private static final String VERDE_OK = "\u001B[32m";
    private static final String RESET = "\u001B[00m";
    private static final String AMARILLO_ALERTA = "\u001B[33m";

    private boolean ejecutando = true;

    private final MishiOrchestrator orchestrator;
    private final Scanner scanner;
    private final MishiVault vault;
    private final TavilySearch seeker;

    // 🎯 Agregamos la referencia al cargador de prompts
    private final MishiPromptLoader promptLoader;

    // Guardamos la referencia al cerebro actual para mutarla dinámicamente
    private MishiBrain cerebroActual;
    private MishiClient mishiClient;

    // 🎯 EL MOTOR MULTITAREA: Pool fijo de hilos para operaciones en background
    private final ExecutorService threadPool = Executors.newFixedThreadPool(2);

    public MishiConsole() {
        this.vault = new MishiVault();
        this.seeker = new TavilySearch();
        this.mishiClient = new MishiClient();
        this.scanner = new Scanner(System.in);

        // 1. Instanciamos el cargador de la mente modular
        this.promptLoader = new MishiPromptLoader();

        System.out.println("🐾 Mishi: Ejecutando auto-diagnóstico de sistemas...");

        // 2. ¡EL AJUSTE GATUNO! Cargamos los prompts desde los Markdown del búnker
        System.out.println("📄 Mishi: Cargando directivas de auditoría y prompts maestros...");
        this.promptLoader.cargarPrompts();       // Carga dudaTecnica y promptAumentado desde prompts.md
        this.promptLoader.cargarSystemPrompt();  // Carga system_analista.md desde la carpeta sistema/

        // 3. Verificamos la salud de la red del humano
        if (MishiHealth.hayInternet()) {
            System.out.println("📶 Red: Conexión establecida. Los cerebros en la nube están disponibles.");

            // Arrancamos por defecto con Gemini si está configurado
            if (MishiHealth.esMotorConfigurado("GEMINI")) {
                this.cerebroActual = MishiBrainFactory.crearCerebro(TipoCerebro.GEMINI_CLOUD, this.mishiClient);
            } else {
                System.out.println("⚠️ Advertencia: Gemini no tiene llaves. Activando motor local por defecto.");
                this.cerebroActual = MishiBrainFactory.crearCerebro(TipoCerebro.LLAMA3_LOCAL, this.mishiClient);
            }
        } else {
            System.out.println("🚨 Red: Modo desconectado (No hay internet). Activando contingencia local con Ollama.");
            // Si el humano no tiene internet, la factoría le asigna un cerebro local de inmediato para salvar el día
            this.cerebroActual = MishiBrainFactory.crearCerebro(TipoCerebro.LLAMA3_LOCAL, this.mishiClient);
        }

        // El jefe supremo recibe el cerebro ya verificado y listo para morder código
        this.orchestrator = new MishiOrchestrator(this.vault, this.cerebroActual, this.seeker);
        System.out.println("🧠 Cerebro activo inicial: " + this.cerebroActual.getNombreModelo() + "\n");
    }


    public void iniciar() throws IOException, InterruptedException {
        boolean salir = false;
        while (!salir) {
            imprimirEncabezado();
            System.out.println("🧠 Motor Actual: " + AMARILLO + cerebroActual.getNombreModelo() + RESET);
            System.out.println("=".repeat(50));
            System.out.println("1. 🔍 Auditoría Pro (Código + Web Search)");
            System.out.println("2. 🔄 Cambiar Cerebro del Mishi (7 Motores)");
            System.out.println("3. 🗄️  Consultar Baúl (MishiVault)");
            System.out.println("4. ⚙️  Ver Configuración de Credenciales");
            System.out.println("5. 🐾  Ver Créditos de MishiMentor");
            System.out.println("6. ❌ Salir");
            System.out.print("\n🐾 Mishi@Terminal:~$ ");

            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    ejecutarAuditoria();
                    break;
                case "2":
                    cambiarCerebroEnCaliente();
                    break;
                case "3":
                    System.out.println("\n🐾 Mishi: Abriendo el baúl de recuerdos...");
                    orchestrator.mostrarResumenVault();
                    break;
                case "4":
                    mostrarConfig();
                    break;
                case "5":
                    mostrarCreditosDeGala();
                    break;
                case "6":
                    salir = true;
                    System.out.println("\n🐾 Mishi: ¡Miau! Nos vemos mañana, Salvador.");
                    apagarBunker(); // 🎯 Liquidamos el pool de hilos de forma segura antes de morir
                    break;
                default:
                    System.out.println("\n🐾 Mishi: ¿Eh? Esa opción no existe en mi lógica.");
            }
        }
    }

    private void imprimirEncabezado() {
        // 🎯 Ajuste de asignación: operador jala operador, versión jala versión
        String operador = MishiConfig.getOperador();
        String version = MishiConfig.getMishiVersion();

        System.out.println(NARANJA_BUNKER + " _______________________________________________________________" + RESET);
        System.out.println(NARANJA_BUNKER + "   __  ___ _     _     _ __  __               _                 " + RESET);
        System.out.println(NARANJA_BUNKER + "  /  |/  /(_)___| |__ (_)  \\/  | ___ _ __  __| |_ ___  _ __     " + RESET);
        System.out.println(NARANJA_BUNKER + " / /|_/ / / / __| '_ \\/ /|\\/| |/ _ \\ '_ \\/ _` | __/ _ \\| '__|    " + RESET);
        System.out.println(NARANJA_BUNKER + "/ /  / / / /\\__ \\ | | / / /  | |  __/ | | | (_| | || (_) | |       " + RESET);
        System.out.println(NARANJA_BUNKER + "/_/  /_/_/_//___/_| |_/_/_/   |_|\\___|_| |_|\\__,_|\\__\\___/|_|      " + RESET);
        System.out.println(NARANJA_BUNKER + " _______________________________________________________________" + RESET);
        // 🎯 Puedes aprovechar 'version' aquí si quieres que se vea en el banner gris:
        System.out.println(GRIS_TACTICO   + " [ SECCIÓN 777 ] - HQ - " + version.toUpperCase() + " | BUNKER_EL_GATO_NEGRO_777" + RESET);
        System.out.println(GRIS_TACTICO   + " [ STATUS ] ---- : NÚCLEO CRIPTOGRÁFICO AES-GCM ACTIVO          " + RESET);
        System.out.println();
        System.out.println(VERDE_OK       + " 🐾 ¡Miau! Sistema listo. Bienvenido de vuelta, Comandante " + operador + "." + RESET);
        System.out.println(GRIS_TACTICO   + " ───────────────────────────────────────────────────────────────\n" + RESET);
    }

    private String obtenerRuta() {
        boolean soportaGraficos = !GraphicsEnvironment.isHeadless();

        if (soportaGraficos) {
            System.out.println("\n🐾 Mishi: Elige tu modo de búsqueda:");
            System.out.println("1. 🖼️ Modo Visual (Ventana JFileChooser)");
            System.out.println("2. ⌨️ Modo Texto (Consola Directa)");
            System.out.print("Opción: ");

            String opcion = scanner.nextLine();
            if (opcion.equals("1")) {
                return abrirVentanaVisual();
            }
        }
        return pedirRutaPorConsola();
    }

    private void ejecutarAuditoria() throws IOException, InterruptedException {
        String rutaSeleccionada = obtenerRuta();

        if (rutaSeleccionada != null) {
            System.out.println(AMARILLO_ALERTA + "\n⚡ [MishiMentor] Auditoría enviada al Pool de Hilos. Procesando en segundo plano..." + RESET);
            System.out.println("🐾 Puedes seguir navegando por el menú mientras Mimi-chan supervisa el background.\n");

            // 🎯 ENVIAMOS LA ACCIÓN REAL AL THREAD-POOL
            threadPool.submit(() -> {
                try {
                    // Capturamos el estado actual del cerebro para evitar colisiones si el usuario lo cambia en caliente
                    MishiBrain cerebroEnUso = this.cerebroActual;

                    // 🚀 LLAMADA REAL A LA API (Síncrona para el hilo de fondo, asíncrona para la consola)
                    String veredicto = orchestrator.realizarAuditoriaPro(rutaSeleccionada);

                    // Imprimimos el resultado de forma asíncrona cuando termine
                    System.out.println("\n\n" + VERDE_OK + "🔔 [NOTIFICACIÓN] ¡Miau! Auditoría completada para: " + rutaSeleccionada + RESET);
                    System.out.println("=".repeat(50));
                    System.out.println("🚀 RESULTADO DE LA AUDITORÍA DE FONDO (" + cerebroEnUso.getNombreModelo() + ")");
                    System.out.println("=".repeat(50));
                    System.out.println(veredicto);
                    System.out.println("=".repeat(50));

                    // Corremos el Mishiómetro en background
                    MishiEvaluador.evaluarYMostrarResultados(veredicto);

                    // Forzamos el flujo comercial base directamente al Vault para no bloquear el scanner esperando un (S/N)
                    System.out.println(">> [SISTEMA] Auto-resguardando Reporte Pro optimizado en MishiVault...");
                    orchestrator.ejecutarFlujoComercial(rutaSeleccionada);

                    System.out.print("\n🐾 Mishi@Terminal:~$ "); // Re-pintamos el prompt del menú

                } catch (Exception e) {
                    System.err.println("\n❌ Error en auditoría asíncrona: " + e.getMessage());
                }
            });

        } else {
            System.out.println("🐾 Mishi: Operación cancelada o ruta no válida.");
        }
    }

    private void mostrarConfig() {
        System.out.println("\n--- CONFIGURACIÓN DE SEGURIDAD (MISHICONFIG V3.0) ---");
        System.out.println("API Gemini:   " + (MishiConfig.getApiKeyGemini() != null ? "CONFIGURADO ✅" : "VACÍO ❌"));
        System.out.println("API OpenAI:   " + (MishiConfig.getApiKeyOpenAi() != null ? "CONFIGURADO ✅" : "VACÍO ❌"));
        System.out.println("API Tavily:   " + (MishiConfig.getApiKeyTavily() != null ? "CONFIGURADO ✅" : "VACÍO ❌"));
        System.out.println("Ollama URL:   " + MishiConfig.getUrlOllama());
        System.out.println("\n--- 🧠 ESTADO DE DIRECTIVAS DE INTELIGENCIA (MARKDOWN) ---");

        // Validamos e imprimimos una vista previa del prompt maestro analista
        String systemPrompt = promptLoader.getSystemPromptAnalista();
        System.out.println("Prompt Maestro Analista: " +
                (systemPrompt != null ? "CARGADO ✅ (" + systemPrompt.substring(0, Math.min(40, systemPrompt.length())) + "...)" : "VACÍO/NO ENCONTRADO ❌"));

        // Validamos las directivas de prompts.md
        System.out.println("Directiva dudaTecnica:   " +
                (promptLoader.getDudaTecnica() != null ? "CARGADA ✅" : "VACÍA ❌"));
        System.out.println("Directiva promptAumentado: " +
                (promptLoader.getPromptAumentado() != null ? "CARGADA ✅" : "VACÍA ❌"));
    }

    private String abrirVentanaVisual() {
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
            JFileChooser selector = new JFileChooser();
            selector.setDialogTitle("🐾 Mishi: Selecciona tu archivo de código");
            selector.setCurrentDirectory(new File("."));

            int resultado = selector.showOpenDialog(null);
            if (resultado == JFileChooser.APPROVE_OPTION) {
                return selector.getSelectedFile().getAbsolutePath();
            }
        } catch (Exception e) {
            System.out.println("🐾 Mishi: Error en UI de Swing, alternando a consola de texto.");
        }
        return null;
    }

    private String pedirRutaPorConsola() {
        System.out.println("\n📂 MODO TEXTO ACTIVO");
        System.out.print("🐾 Mishi: Pega la ruta o arrastra el archivo aquí: ");
        return scanner.nextLine().trim().replace("\"", "");
    }

    public String pedirEntrada(String mensaje) {
        System.out.print("\n" + AMARILLO + "➤ " + mensaje + RESET + ": ");
        return scanner.nextLine();
    }

    private void seleccionarMotorInteractivo() {
        List<String> motor = orchestrator.obtenerMotoresDisponibles();
        System.out.println("🐾 Mishi: Motores detectados en configuración:");

        for (int i = 0; i < motor.size(); i++) {
            System.out.println((i + 1) + ". " + motor.get(i));
        }

        // ... Aquí capturas la entrada y le pides al Orquestador que cambie
    }

    private void cambiarCerebroEnCaliente() {
        // 1. Consultamos al orquestador qué hay configurado en el .properties
        List<String> motoresValidos = orchestrator.obtenerMotoresDisponibles();
        boolean hayRed = MishiHealth.hayInternet();

        System.out.println("\n" + "=".repeat(40));
        System.out.println("🧠 PANEL DE CONTROL (MOTORES DISPONIBLES)");
        System.out.println("=".repeat(40));

        // Exclusivos de Ollama (asumiendo que Ollama local siempre está disponible si está activo)
        System.out.println("--- 💻 MOTORES LOCALES (OLLAMA) ---");
        System.out.println("1. Llama 3 (Propósito General)");
        System.out.println("2. Codestral (Especialista en Código)");
        System.out.println("3. Phi 3 (Ultra-ligero)");
        System.out.println("4. MockBrain (Local y sincero)");

        System.out.println("--- ☁️ MOTORES EN LA NUBE ---");

        // Mostramos dinámicamente según las llaves Y la conexión real a internet
        if (!hayRed) {
            System.out.println(AMARILLO + "⚠️ [MODO OFF-LINE] Motores Cloud deshabilitados por red." + RESET);
        }

        imprimirEstadoMotor("5. Google Gemini Pro", motoresValidos.contains("Gemini") && hayRed);
        imprimirEstadoMotor("6. OpenAI GPT-4o mini", motoresValidos.contains("OpenAI") && hayRed);
        imprimirEstadoMotor("7. DeepSeek Chat v3", motoresValidos.contains("DeepSeek") && hayRed);
        imprimirEstadoMotor("8. Claude Anthropic", motoresValidos.contains("Claude Anthropic") && hayRed);

        System.out.print("\n🐾 Mishi: Selecciona la nueva consciencia operativa: ");
        String seleccion = scanner.nextLine();
        TipoCerebro tipoElegido = null;

        switch (seleccion) {
            case "1" -> tipoElegido = TipoCerebro.LLAMA3_LOCAL;
            case "2" -> tipoElegido = TipoCerebro.CODESTRAL_LOCAL;
            case "3" -> tipoElegido = TipoCerebro.PHI_LOCAL;
            case "4" -> tipoElegido = TipoCerebro.MOCKBRAIN_LOCAL;
            case "5" -> {
                if (motoresValidos.contains("Gemini") && hayRed) {
                    tipoElegido = TipoCerebro.GEMINI_CLOUD;
                } else {
                    System.out.println("🐾 Mishi: Gemini no está disponible.");
                    return;
                }
            }
            case "6" -> {
                if (motoresValidos.contains("OpenAI") && hayRed) {
                    tipoElegido = TipoCerebro.GPT4O_CLOUD;
                } else {
                    System.out.println("🐾 Mishi: OpenAI no está disponible.");
                    return;
                }
            }
            case "7" -> {
                if (motoresValidos.contains("DeepSeek") && hayRed) {
                    tipoElegido = TipoCerebro.DEEPSEEK_CLOUD;
                } else {
                    System.out.println("🐾 Mishi: DeepSeek no está disponible.");
                    return;
                }
            }
            // ¡La nueva adición lista para el combate!
            case "8" -> {
                if (motoresValidos.contains("Claude") && hayRed) {
                    tipoElegido = TipoCerebro.CLAUDE_CLOUD;
                } else {
                    System.out.println("🐾 Mishi: ¡Miau! Claude no está disponible (Revisa red o API Key).");
                    return;
                }
            }
            default -> {
                System.out.println("🐾 Mishi: Selección inválida o motor bloqueado. Manteniendo motor actual.");
                return;
            }
        }

        // Mutamos el cerebro en caliente inyectando el cliente de red único. ¡Adiós fugas de memoria!
        this.cerebroActual = MishiBrainFactory.crearCerebro(tipoElegido, mishiClient);
        this.orchestrator.setCerebro(this.cerebroActual);

        System.out.println("\n✅ [SISTEMA] Sinapsis completada. Servidor conmutado a: " + cerebroActual.getNombreModelo());
    }

    /**
     * Helper estético para mantener la consola limpia y evitar repetición de código
     */
    private void imprimirEstadoMotor(String etiqueta, boolean disponible) {
        if (disponible) {
            System.out.println(etiqueta + " [DISPONIBLE ✅]");
        } else {
            System.out.println("\u001B[31m" + etiqueta + " [NO DISPONIBLE ❌]" + RESET);
        }
    }

    private void mostrarCreditosDeGala() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("🐾  MISHI MENTOR PRO " + MishiConfig.getMishiVersion() + " - CRÉDITOS DE HONOR  🐾");
        System.out.println("=".repeat(50));

        // El Arte ASCII de Mimi-chan supervisando el búnker
        System.out.println(AMARILLO +
                "       /\\_/\\\n" +
                "      ( o.o )\n" +
                "       > ^ <   [ Senior Project Manager ]\n" +
                "      /     \\ \n" +
                "     (_/\\_/\\_)" + RESET);

        System.out.println("\n--- 🏗️ ARQUITECTURA Y CÓDIGO FUENTE ---");
        System.out.println("• Lead Developer: Salvador 'Autilius' Granados Godínez");
        System.out.println("• Enfoque: Clean Code, SOLID Architecture & SecOps");
        System.out.println("• Ubicación de Operaciones: El Búnker de Zapopan");

        System.out.println("\n--- 🍊 CONTROL DE CALIDAD Y MICRO-MANAGEMENT ---");
        System.out.println("• Senior Project Manager: Mimi-chan (The Orange Boss)");
        System.out.println("• Funciones: Dormir en el teclado caliente y exigir sobres de salmón.");

        System.out.println("\n--- 🔧 COLABORADORES VIRTUALES ---");
        System.out.println("• Gemini (Tu Copiloto y Partner de Chisme Técnico)");

        System.out.println("\n--- 📚 INFRAESTRUCTURA Y PODER (NATIVO/CLOUD) ---");
        System.out.println("• Persistencia: MishiVault™ (JSON-QL Dynamic Database)");
        System.out.println("• Signos Vitales: MishiHealth™ (Anti-Paranoia Network Inspector)");
        System.out.println("• Motores Activos: Ollama (Local) | Gemini, OpenAI, DeepSeek, Claude (Cloud)");
        System.out.println("• Reportes: OpenPDF & Jackson Core");

        System.out.println("\n" + "=".repeat(50));
        System.out.println("  © 2026 BugoTruco Labs. Todos los derechos gatunos reservados.");
        System.out.println("=".repeat(50) + "\n");
    }

    private void apagarBunker() {
        System.out.println(GRIS_TACTICO + "⚙️ Deteniendo el pool de hilos de la Sección 777..." + RESET);
        threadPool.shutdown(); // No aceptamos más tareas, pero dejamos terminar las que estén corriendo
        try {
            // Esperamos un máximo de 4 segundos a que terminen las auditorías en curso
            if (!threadPool.awaitTermination(4, TimeUnit.SECONDS)) {
                System.out.println(AMARILLO_ALERTA + "⚠️ Forzando el cierre de hilos pendientes..." + RESET);
                threadPool.shutdownNow(); // Forzado si tarda demasiado
            }
            System.out.println(VERDE_OK + "🔒 Búnker apagado al 100% de forma segura." + RESET);
        } catch (InterruptedException e) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

}