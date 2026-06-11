/*
 * Copyright 2026 Salvador (Autilius) Granados Godínez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */

package com.bugotruco;

import com.bugotruco.brains.MishiBrain;
import com.bugotruco.brains.MishiBrainFactory;
import com.bugotruco.brains.TipoCerebro;
import com.bugotruco.search.TavilySearch;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.List;

/**
 * Consola Interactiva y Panel de Control de MishiMentor v3.0.
 * Permite la gestión de auditorías e inyección de cerebros polimórficos en caliente.
 */
public class MishiConsole {
    private static final String AMARILLO = "\u001B[33m";
    private static final String RESET = "\u001B[00m";

    private final MishiOrchestrator orchestrator;
    private final Scanner scanner;
    private final MishiVault vault;
    private final TavilySearch seeker;

    // Guardamos la referencia al cerebro actual para mutarla dinámicamente
    private MishiBrain cerebroActual;
    private MishiClient mishiClient;

    public MishiConsole() {
        this.vault = new MishiVault();
        this.seeker = new TavilySearch();
        this.mishiClient = new MishiClient();
        this.scanner = new Scanner(System.in);

        System.out.println("🐾 Mishi: Ejecutando auto-diagnóstico de sistemas...");

        // 1. Verificamos la salud de la red del humano
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
            System.out.println("5. ❌ Salir");
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
                    salir = true;
                    System.out.println("\n🐾 Mishi: ¡Miau! Nos vemos mañana, Salvador.");
                    break;
                default:
                    System.out.println("\n🐾 Mishi: ¿Eh? Esa opción no existe en mi lógica.");
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
        System.out.println("           [ Versión 3.0 - Multibrain Agent ]    ");
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
            System.out.println("\n🐾 Mishi: Analizando con [" + cerebroActual.getNombreModelo() + "]... un momento.");

            String veredicto = orchestrator.realizarAuditoriaPro(rutaSeleccionada);

            System.out.println("\n" + "=".repeat(50));
            System.out.println("🚀 RESULTADO DE LA AUDITORÍA");
            System.out.println("=".repeat(50));
            System.out.println(veredicto);
            System.out.println("=".repeat(50));

            //¡EL REMATE! Corres el Mishiómetro en la consola
            MishiEvaluador.evaluarYMostrarResultados(veredicto);

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
        System.out.println("\n--- CONFIGURACIÓN DE SEGURIDAD (MISHICONFIG V3.0) ---");
        System.out.println("API Gemini:   " + (MishiConfig.getApiKeyGemini() != null ? "CONFIGURADO ✅" : "VACÍO ❌"));
        System.out.println("API OpenAI:   " + (MishiConfig.getApiKeyOpenAi() != null ? "CONFIGURADO ✅" : "VACÍO ❌"));
        System.out.println("API Tavily:   " + (MishiConfig.getApiKeyTavily() != null ? "CONFIGURADO ✅" : "VACÍO ❌"));
        System.out.println("Ollama URL:   " + MishiConfig.getUrlOllama());
        if (MishiConfig.getSystemPrompt() != null) {
            System.out.println("System Prompt: " + MishiConfig.getSystemPrompt().substring(0, Math.min(30, MishiConfig.getSystemPrompt().length())) + "...");
        }
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



}