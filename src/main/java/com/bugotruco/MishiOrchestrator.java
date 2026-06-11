/*
 * Copyright 2026 Salvador (Autilius) Granados Godínez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package com.bugotruco;

import com.bugotruco.brains.MishiBrain;
import com.bugotruco.model.Finding;
import com.bugotruco.search.MishiSeeker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Orquestador central de la versión 3.0.
 * Coordina el escaneo local, rastreo con Tavily, inferencia híbrida y linaje inmutable.
 */
public class MishiOrchestrator {

    private static final String VERDE = "\u001B[32m";
    private static final String RESET = "\u001B[00m";

    private  MishiBrain brain;  //Removimos el final
    private final MishiVault vault;
    private final MishiSeeker seeker;

    public MishiOrchestrator(MishiVault vault, MishiBrain brain, MishiSeeker seeker) {
        this.vault = vault;
        this.brain = brain;
        this.seeker = seeker;
    }

    /**
     * El Corazón del Flujo Pro (v3.0)
     */
    public String realizarAuditoriaPro(String rutaArchivo) {

        int intentos = 0;
        final int MAX_INTENTOS = 3;

        do {

            try {
                // 1. Escaneamos el código local
                String codigo = MishiScanner.leerArchivo(rutaArchivo);

                // 2. Extraemos librerías para buscar vulnerabilidades reales
                String dudaTecnica = "Security vulnerabilities and best practices 2026 for: " + extraerLibrerias(codigo);

                System.out.println("🐾 Mishi: Rastreando el internet con Tavily...");
                List<String> hallazgos = seeker.buscar(dudaTecnica);

                // Unimos los resultados web en un solo bloque de texto claro
                String contextoWebUnificado = String.join("\n", hallazgos);

                // 3. El Cerebro procesa todo con instrucciones de match cruzado e inyección de marcadores estructurados
                String promptAumentado = """
                [INSTRUCCIÓN DE AUDITORÍA SUPERIOR v3.0]
                Eres MishiMentor, un arquitecto de software senior y auditor de ciberseguridad. 
                Analiza el código proporcionado cruzándolo rigurosamente con la información actualizada de internet (CVEs recientes, vulnerabilidades o parches de librerías).
                
                Por cada vulnerabilidad encontrada que aplique, escribe una línea exacta con este formato:
                MISHIFINDING|ID|TITULO|GRAVEDAD|CONTEXTO_DE_LINEA|DESCRIPCION_DETALLADA
                
                Además, incluye tu propuesta de código refactorizado obligatoriamente dentro de un bloque markdown de código java (```java ... ```).
                
                === CONTEXTO WEB DE TAVILY ===
                %s
                
                === CÓDIGO A AUDITAR ===
                %s
                """.formatted(contextoWebUnificado, codigo);

                System.out.println("🧠 Mishi: Procesando veredicto con " + brain.getNombreModelo() + "...");
                String veredicto = brain.pensar(promptAumentado);

                // --- 🧠 Quirófano de Datos Estructurados v3.0 ---
                String codigoRefactorizado = extraerCodigoLimpioDeTexto(veredicto);
                List<Finding> listaDeFindingsExtraidos = extraerFindingsDeTexto(veredicto);

                // 4. Persistencia relacional automática en el Baúl
                guardarEnElBaul(rutaArchivo, veredicto, codigoRefactorizado, listaDeFindingsExtraidos);

                return veredicto;

            } catch (IOException | InterruptedException e) {
                intentos++;
                System.err.println("⚠️ Mishi: Error en sinapsis: " + e.getMessage());

                if (intentos >= MAX_INTENTOS) {
                    // FALLO CRÍTICO: Registramos en el baúl para no perder la pista
                    String errorMsg = "Fallo tras " + MAX_INTENTOS + " intentos: " + e.getMessage();
                    vault.registrarFallo(rutaArchivo, errorMsg);
                    return "¡Miau! Lo siento, me he quedado sin señal. Inténtalo más tarde o cambia de cerebro.";
                }
                // Espera exponencial: 2s, luego 4s
                try { Thread.sleep(2000L * intentos); } catch (InterruptedException ignored) {}
            }

        } while (intentos < MAX_INTENTOS);

        return "Error inesperado en el flujo de auditoría.";
    }

    /**
     * Mapea y despacha el nodo hacia el MishiVault.
     */
    private void guardarEnElBaul(String ruta, String veredicto, String codigoRefactorizado, List<Finding> findings) {
        String nombreLimpio = new File(ruta).getName();

        // Inicializa el nodo. parentId va en null porque MishiVault inspecciona el disco duro y calcula el linaje solo
        MishiNode nodo = new MishiNode(null, nombreLimpio, veredicto, codigoRefactorizado);
        nodo.setSecurityFindings(findings != null ? findings : new ArrayList<>());

        vault.guardarNodo(nodo);
        System.out.println("🐾 Mishi: Reporte estructural y árbol de linaje guardados con éxito.");
    }

    /**
     * Analizador estático de dependencias por Regex.
     */
    private String extraerLibrerias(String codigo) {
        Set<String> componentesClave = new HashSet<>();
        Pattern pattern = Pattern.compile("import\\s+(?:static\\s+)?([\\w\\.]+);");
        Matcher matcher = pattern.matcher(codigo);

        while (matcher.find()) {
            String fullImport = matcher.group(1);

            if (fullImport.startsWith("java.") || fullImport.startsWith("javax.") || fullImport.startsWith("jakarta.")) {
                continue;
            }

            if (fullImport.startsWith("org.springframework.security")) {
                componentesClave.add("Spring Security");
            } else if (fullImport.startsWith("org.springframework")) {
                componentesClave.add("Spring Framework");
            } else if (fullImport.contains("jackson")) {
                componentesClave.add("Jackson Databind");
            } else if (fullImport.contains("jwt") || fullImport.contains("auth0")) {
                componentesClave.add("JWT Auth");
            } else if (fullImport.startsWith("org.hibernate")) {
                componentesClave.add("Hibernate ORM");
            } else if (fullImport.startsWith("org.apache.logging.log4j")) {
                componentesClave.add("Log4j");
            } else {
                String[] subCapas = fullImport.split("\\.");
                if (subCapas.length >= 3) {
                    componentesClave.add(subCapas[0] + "." + subCapas[1] + "." + subCapas[2]);
                } else if (subCapas.length >= 2) {
                    componentesClave.add(subCapas[0] + "." + subCapas[1]);
                }
            }
        }

        if (componentesClave.isEmpty()) {
            return "Java secure coding standards";
        }

        return String.join(", ", componentesClave);
    }

    /**
     * Interfaz de Consola interactiva para el historial del Baúl.
     */
    public void mostrarResumenVault() {
        MishiConsole ui = new MishiConsole();
        System.out.println("\n📂 CONTENIDO DEL MISHI-VAULT");

        List<String> nombresReportes = vault.obtenerListaDeReportes();

        if (nombresReportes.isEmpty()) {
            System.out.println(" [ El baúl está vacío ]");
            return;
        }

        for (int i = 0; i < nombresReportes.size(); i++) {
            System.out.printf(" %d. 📄 %s\n", i + 1, nombresReportes.get(i));
        }

        String respuesta = ui.pedirEntrada("\nEscribe el número para leer (o 0 para volver)");

        try {
            int seleccion = Integer.parseInt(respuesta);
            if (seleccion == 0) return;

            if (seleccion > 0 && seleccion <= nombresReportes.size()) {
                String nombreArchivo = nombresReportes.get(seleccion - 1);
                MishiNode elegido = vault.obtenerRecuerdoPorNombre(nombreArchivo);

                System.out.println("\n" + VERDE + "=== REPORTE: " + nombreArchivo + " ===" + RESET);
                System.out.println(elegido.getVerdict());
                System.out.println(VERDE + "===============================================" + RESET);

                String opcionPdf = ui.pedirEntrada("¿Deseas exportar este reporte? (s/n)");
                if (opcionPdf.equalsIgnoreCase("s")) {
                    String opcion = ui.pedirEntrada("¿Qué formato deseas? \n" +
                            " 1. PDF de Gala (Para el cliente) \n" +
                            " 2. Markdown Técnico (Para GitHub/Docs) \n");
                    if (opcion.equalsIgnoreCase("1")) vault.generarReportePdf(elegido);
                    if (opcion.equalsIgnoreCase("2")) vault.generarReporteMarkdown(elegido);
                }
            } else {
                System.out.println("¡Miau! Ese número no está en la lista.");
            }
        } catch (NumberFormatException e) {
            System.out.println("¡Miau! Eso no es un número, humano.");
        }
    }

    // --- MÉTODOS DE PARSEO QUIRÚRGICO (Regex) ---

    private String extraerCodigoLimpioDeTexto(String veredicto) {
        Pattern pattern = Pattern.compile("```java\\s*(.*?)\\s*```", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(veredicto);

        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "// 🐾 Mishi: No se detectó un bloque explícito de código refactorizado.\n" + veredicto;
    }

    private List<Finding> extraerFindingsDeTexto(String veredicto) {
        List<Finding> listaFindings = new ArrayList<>();
        try {
            String[] lineas = veredicto.split("\n");
            int contador = 1;

            for (String linea : lineas) {
                if (linea.contains("MISHIFINDING|")) {
                    String[] partes = linea.replace("MISHIFINDING|", "").split("\\|", -1);

                    if (partes.length >= 5) {
                        String id = partes[0].trim().isEmpty() ? "SEC-00" + contador : partes[0].trim();
                        String titulo = partes[1].trim();
                        String gravedad = partes[2].trim().toUpperCase();
                        String contexto = partes[3].trim();
                        String descripcion = partes[4].trim();

                        Finding f = new Finding(id, titulo, descripcion, gravedad, contexto);
                        listaFindings.add(f);
                        contador++;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("🐾 Mishi: Hubo un rasguño al extraer los Findings: " + e.getMessage());
        }
        return listaFindings;
    }

    public void setCerebro(MishiBrain nuevoCerebro) {
        this.brain = nuevoCerebro; // Reemplaza el cerebro actual en el orquestador
    }

    public List<String> obtenerMotoresDisponibles() {
        List<String> disponibles = new ArrayList<>();

        if (MishiConfig.getApiKeyGemini() != null)  disponibles.add("Gemini");
        if (MishiConfig.getApiKeyOpenAi() != null)  disponibles.add("OpenAI");
        if (MishiConfig.getApiKeyDeepSeek() != null) disponibles.add("DeepSeek");
        if (MishiConfig.getApiKeyAnthropic() != null) disponibles.add("Anthropic");

        return disponibles;
    }

    public void ejecutarFlujoComercial(String rutaArchivo) {
        try {
            System.out.println("🚀 Iniciando proceso para: " + rutaArchivo);
            String codigoOriginal = MishiScanner.leerArchivo(rutaArchivo);

            // 1. Auditoría con Mishi (Para ti, con "Miaus")
            String veredictoMishi = realizarAuditoriaPro(codigoOriginal);

            // 2. Refactorización (El código corregido para el cliente)
            String codigoRefactorizado = realizarAuditoriaPro(codigoOriginal);

            // 3. Veredicto Profesional (Sin "Miaus", para el PDF del cliente)
            String veredictoPro = realizarAuditoriaPro(veredictoMishi);

            // 4. Guardar todo en el Baúl con ID Automático
            vault.exportarEntregableComercial(rutaArchivo, veredictoPro, codigoRefactorizado);

            System.out.println("✅ El flujo comercial ha finalizado. El despacho está listo.");
            //System.out.println("✅ ¡Misión cumplida! Todo guardado en el Vault.");

        } catch (Exception e) {
            System.err.println("❌ Fallo en el flujo: " + e.getMessage());
        }
    }

}