/*
 * Copyright 2026 Salvador (Autilius) Granados Godínez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package com.bugotruco.mishimentor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.bugotruco.mishimentor.model.Finding;
import com.bugotruco.mishimentor.model.MishiReport;
import com.bugotruco.mishimentor.service.PdfService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class MishiVault {

    private static final String VAULT_NAME = ".mishi_vault";
    private static final String ENTREGABLE_NAME = "Mishi_Entregables";
    private static final String SISTEMA = "sistema";
    private static final String JSON = "json";
    private static final String PROMPTS = "prompts";
    private final ObjectMapper mapper;

    public MishiVault() {
        this.mapper = new ObjectMapper();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public Path getVaultPath() {
        return Paths.get(System.getProperty("user.home"), VAULT_NAME);
    }

    public Path getEntregablePath() {
        return Paths.get(System.getProperty("user.home"), "Documentos", ENTREGABLE_NAME);
    }

    public Path getSistemaPath() { return  getVaultPath().resolve(SISTEMA) ; }

    public Path getJsonPath() { return getVaultPath().resolve(JSON); }

    public Path getPromptsPath() { return getVaultPath().resolve(PROMPTS); }

    public void initVault() {
        try {
            Path ruta = getVaultPath();
            if (Files.notExists(ruta)) {
                Files.createDirectories(ruta);
            }
        } catch (IOException e) {
            System.err.println("Error al inicializar el baúl: " + e.getMessage());
        }

        try {
            Path rutaSistema = getSistemaPath();
            if (Files.notExists(rutaSistema)) {
                Files.createDirectories(rutaSistema);
            }
        } catch (IOException e) {
            System.err.println("Error al inicializar el baúl de sistema: " + e.getMessage());
        }

        try {
            Path rutaJson = getJsonPath();
            if (Files.notExists(rutaJson)) {
                Files.createDirectories(rutaJson);
            }
        } catch (IOException e) {
            System.err.println("Error al inicializar el baúl de JSON: " + e.getMessage());
        }

        try {
            Path rutaPrompts = getPromptsPath();
            if (Files.notExists(rutaPrompts)) {
                Files.createDirectories(rutaPrompts);
            }
        } catch (IOException e) {
            System.err.println("Error al inicializar el baúl de Prompts: " + e.getMessage());
        }
    }

    public void initEntregables() {
        try {
            Path ruta = getEntregablePath();
            if (Files.notExists(ruta)) {
                Files.createDirectories(ruta);
                System.out.println("📂 [MishiVault] Carpeta de entregables creada en Documentos.");
            }
        } catch (IOException e) {
            System.err.println("❌ Error al inicializar el baúl de entregables: " + e.getMessage());
        }
    }

    // --- EL CORAZÓN DE LA MEMORIA (OPTIMIZADO v3.0) ---
    public void guardarNodo(MishiNode nodo) {
        initVault();

        // 1. RESOLVER LINAJE: Buscar de forma automática el último ID de este archivo para asignarlo como padre
        try {
            List<MishiNode> historial = obtenerTodosLosRecuerdos();
            for (MishiNode recuerdoPasado : historial) {
                // El primer match que encuentre será el más reciente (gracias al ordenamiento por timestamp de obtenerTodosLosRecuerdos)
                if (recuerdoPasado.getFileName() != null && recuerdoPasado.getFileName().equals(nodo.getFileName())) {
                    nodo.setParentId(recuerdoPasado.getId());
                    System.out.println("🔗 Mishi: Detectado ancestro. Enlazando nuevo nodo al padre: " + recuerdoPasado.getId());
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("🐾 Mishi: No se pudo verificar el linaje, se guardará como nodo raíz: " + e.getMessage());
        }

        // 2. Definir el nombre del archivo final
        String nombreArchivo = nodo.getId() + ".json";
        Path rutaArchivo = getJsonPath().resolve(nombreArchivo);

        try {
            // 3. Convertir a String JSON y escribir
            String jsonGatuno = mapper.writeValueAsString(nodo);
            Files.writeString(rutaArchivo, jsonGatuno);

            System.out.println("🐾 Mishi: Recuerdo guardado con éxito en: " + nombreArchivo);
        } catch (IOException e) {
            System.err.println("¡Miau! Se me escapó el ovillo al guardar: " + e.getMessage());
        }
    }

    public List<MishiNode> obtenerTodosLosRecuerdos() {
        List<MishiNode> recuerdos = new ArrayList<>();

        try (Stream<Path> archivos = Files.list(getJsonPath())) {
            archivos.filter(p -> p.toString().endsWith(".json"))
                    .forEach(p -> {
                        try {
                            MishiNode nodo = mapper.readValue(p.toFile(), MishiNode.class);
                            recuerdos.add(nodo);
                        } catch (IOException e) {
                            System.err.println("¡Miau! Ignorando archivo corrupto: " + p.getFileName());
                        }
                    });
        } catch (IOException e) {
            System.err.println("No pude abrir el baúl: " + e.getMessage());
        }

        // Orden cronológico inverso (el más nuevo primero) para facilitar la resolución de linajes y búsquedas
        recuerdos.sort((n1, n2) -> Long.compare(n2.getTimestamp(), n1.getTimestamp()));
        return recuerdos;
    }

    public List<String> obtenerListaDeReportes() {
        File carpeta = new File(getJsonPath().toUri());
        String[] archivos = carpeta.list((dir, name) -> name.endsWith(".json"));
        return archivos != null ? Arrays.asList(archivos) : new ArrayList<>();
    }

    public MishiNode obtenerRecuerdoPorNombre(String nombreArchivo) {
        Path rutaArchivo = getJsonPath().resolve(nombreArchivo);
        try {
            if (Files.exists(rutaArchivo)) {
                return mapper.readValue(rutaArchivo.toFile(), MishiNode.class);
            } else {
                System.err.println("¡Miau! El archivo " + nombreArchivo + " no existe.");
            }
        } catch (IOException e) {
            System.err.println("Error al recuperar el recuerdo: " + e.getMessage());
        }
        return null;
    }

    /**
     * GENERADOR DE PDF DE GALA: Mapea el nodo y crea el documento comercial.
     */
    public void generarReportePdf(MishiNode nodo) {
        initEntregables();
        try {
            MishiReport reporte = new MishiReport();
            reporte.setFileName(nodo.getFileName());
            reporte.setDate(nodo.getId());
            reporte.setRefactoredCode(nodo.getRefactoredCode());
            reporte.setSecurityFindings(nodo.getSecurityFindings());

            String nombreLimpio = nodo.getFileName().replace(".java", "");
            String nombreArchivoPdf = "Reporte_" + nombreLimpio + "_" + nodo.getId() + ".pdf";
            Path rutaSalida = getEntregablePath().resolve(nombreArchivoPdf);

            PdfService pdfService = new PdfService();
            pdfService.convertJsonToPdf(reporte, rutaSalida.toString());

            System.out.println("\n✨ [PDF DE GALA GENERADO]");
            System.out.println("📍 Ubicación: " + rutaSalida.toAbsolutePath());

        } catch (Exception e) {
            System.err.println("❌ Error crítico al generar el PDF: " + e.getMessage());
        }
    }

    /**
     * GENERADOR DE MARKDOWN v3.0: Explota la lista estructurada de hallazgos.
     */
    public void generarReporteMarkdown(MishiNode nodo) {
        initEntregables();
        try {
            String nombreLimpio = nodo.getFileName().contains(File.separator)
                    ? new File(nodo.getFileName()).getName().replace(".java", "")
                    : nodo.getFileName().replace(".java", "");

            String nombreArchivo = "Reporte_" + nombreLimpio + "_" + nodo.getId() + ".md";
            Path archivoFinal = getEntregablePath().resolve(nombreArchivo);

            // Construcción elegante de los hallazgos estructurados
            StringBuilder sbFindings = new StringBuilder();
            if (nodo.getSecurityFindings() == null || nodo.getSecurityFindings().isEmpty()) {
                sbFindings.append("✅ No se detectaron vulnerabilidades críticas en esta ejecución.\n");
            } else {
                for (Finding f : nodo.getSecurityFindings()) {
                    sbFindings.append(String.format("### ⚠️ [%s] %s\n", f.getSeverity(), f.getTitle()));
                    sbFindings.append(String.format("- **Descripción:** %s\n", f.getDescription()));
                    sbFindings.append(String.format("- **Línea/Contexto:** %s\n\n", f.getLineContext()));
                }
            }

            String contenido = String.format("""
            # 🛡️ INFORME DE AUDITORÍA TÉCNICA - MISHIMENTOR PRO v3.0
            
            **Archivo Original:** %s
            **Identificador de Nodo:** %s
            **Nodo Padre (Ancetro):** %s
            
            ---
            
            ## 🔍 1. DICTAMEN DEL AUDITOR (VERDICT)
            %s
            
            ---
            
            ## 🚨 2. MATRIZ DE RIESGOS Y HALLAZGOS DETECTADOS
            %s
            
            ---
            
            ## 💻 3. PROPUESTA DE REFACTORIZACIÓN (CLEAN CODE)
            ```java
            %s
            ```
            
            ---
            *Reporte automatizado con firma inmutable de auditoría - MishiMentor 2026*
            """, nodo.getFileName(), nodo.getId(),
                    (nodo.getParentId() != null ? nodo.getParentId() : "Ninguno (Nodo Raíz)"),
                    nodo.getVerdict(), sbFindings.toString(), nodo.getRefactoredCode());

            Files.writeString(archivoFinal, contenido);
            System.out.println("✨ [MARKDOWN DE GALA GENERADO]");
            System.out.println("📍 Ubicación: " + archivoFinal.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error al exportar Markdown: " + e.getMessage());
        }
    }

    // Mantenemos este método por compatibilidad con firmas viejas de la v2.0 si las necesitas
    public void exportarEntregableComercial(String rutaCompletaOriginal, String informe, String refactor) {
        MishiNode nodoTemporal = new MishiNode(null, rutaCompletaOriginal, informe, refactor);
        generarReporteMarkdown(nodoTemporal);
    }

    public void registrarFallo(String rutaArchivo, String errorMsg) {

    }
}