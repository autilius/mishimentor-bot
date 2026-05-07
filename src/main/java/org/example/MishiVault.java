/*
 * Copyright 2026 Salvador (Autilius) Granados Godínez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */

package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.example.model.MishiReport;
import org.example.service.PdfService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MishiVault {

    private static final String VAULT_NAME = ".mishi_vault";
    private static final String ENTREGABLE_NAME = "Mishi_Entregables";
    private final ObjectMapper mapper;

    public MishiVault() {
        this.mapper = new ObjectMapper();
        // Esto hace que el JSON se guarde "bonito" (con sangría)
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public Path getVaultPath() {
        return Paths.get(System.getProperty("user.home"), VAULT_NAME);
    }

    public Path getEntregablePath() { return Paths.get(System.getProperty("user.home"), "Documentos", ENTREGABLE_NAME); }

    public void initVault() {
        try {
            Path ruta = getVaultPath();
            if (Files.notExists(ruta)) {
                Files.createDirectories(ruta);
            }
        } catch (IOException e) {
            System.err.println("Error al inicializar el baúl: " + e.getMessage());
        }
    }

    public void initEntregables() {
        try {
            Path ruta = getEntregablePath();
            if (Files.notExists(ruta)) {
                Files.createDirectories(ruta); // Usamos 'createDirectories' por si 'Documents' no existe (raro, pero seguro)
                System.out.println("📂 [MishiVault] Carpeta de entregables creada en Documentos.");
            }
        } catch (IOException e) {
            System.err.println("❌ Error al inicializar el baúl de entregables: " + e.getMessage());
        }
    }

    // --- EL CORAZÓN DE LA MEMORIA ---
    public void guardarNodo(MishiNode nodo) {
        // 1. Definir el nombre del archivo (usando el ID del nodo)
        initVault();
        String nombreArchivo = nodo.getId() + ".json";
        Path rutaArchivo = getVaultPath().resolve(nombreArchivo);

        try {
            // 2. Convertir el objeto MishiNode a un String JSON
            String jsonGatuno = mapper.writeValueAsString(nodo);

            // 3. Escribir el archivo (Java 11+ permite hacerlo en una línea)
            Files.writeString(rutaArchivo, jsonGatuno);

            System.out.println("🐾 Mishi: Recuerdo guardado con éxito en: " + nombreArchivo);
        } catch (IOException e) {
            System.err.println("¡Miau! Se me escapó el ovillo al guardar: " + e.getMessage());
        }
    }

    public List<MishiNode> obtenerTodosLosRecuerdos() {
        List<MishiNode> recuerdos = new ArrayList<>();

        try (Stream<Path> archivos = Files.list(getVaultPath())) {
            archivos.filter(p -> p.toString().endsWith(".json"))
                    .forEach(p -> {
                        try {
                            // Jackson hace la magia inversa: Archivo -> Objeto
                            MishiNode nodo = mapper.readValue(p.toFile(), MishiNode.class);
                            recuerdos.add(nodo);
                        } catch (IOException e) {
                            System.err.println("¡Miau! Ignorando archivo corrupto: " + p.getFileName());
                        }
                    });
        } catch (IOException e) {
            System.err.println("No pude abrir el baúl: " + e.getMessage());
        }

        // Los ordenamos por fecha (el más nuevo primero)
        recuerdos.sort((n1, n2) -> Long.compare(n2.getTimestamp(), n1.getTimestamp()));
        return recuerdos;
    }

    public List<String> obtenerListaDeReportes() {
        File carpeta = new File(getVaultPath().toUri());
        String[] archivos = carpeta.list((dir, name) -> name.endsWith(".json")); // O la extensión que uses
        return archivos != null ? Arrays.asList(archivos) : new ArrayList<>();
    }

    /**
     * EXPORTADOR COMERCIAL: Genera el entregable para el cliente.
     */
    public void exportarEntregableComercial(String rutaCompletaOriginal, String informe, String refactor) {
        // 1. Aseguramos que la carpeta exista antes de escribir
        initEntregables();

        try {
// 1. EXTRAER SOLO EL NOMBRE (La clave del éxito)
            // Convertimos la ruta completa en un objeto File para sacar solo el nombre final
            String soloNombre = new java.io.File(rutaCompletaOriginal).getName();

            // Limpiamos la extensión .java para que no se repita
            String nombreLimpio = soloNombre.replace(".java", "");

            // 2. Preparamos el nombre final del reporte
            String timestamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmm").format(new java.util.Date());
            String nombreArchivo = "Reporte_" + nombreLimpio + "_" + timestamp + ".md";

            // 3. Resolvemos la ruta (Documentos/Mishi_Entregables/Reporte_XYZ.md)
            Path archivoFinal = getEntregablePath().resolve(nombreArchivo);

            // 4. Construimos el contenido (Markdown)
            String contenido = String.format("""
            # 🛡️ INFORME DE AUDITORÍA TÉCNICA - MISHIMENTOR PRO
            **Archivo:** %s
            **Fecha:** %s
            
            ---
            ## 🔍 1. ANÁLISIS DE SEGURIDAD
            %s
            
            ---
            ## 💻 2. CÓDIGO REFACTORIZADO
            ```java
            %s
            ```
            
            ---
            *Generado por MishiMentor v2.0*
            """, rutaCompletaOriginal, timestamp, informe, refactor);

            // 4. Escribimos el archivo
            Files.writeString(archivoFinal, contenido);

            System.out.println("\n✨ [DESPACHO EXITOSO]");
            System.out.println("📍 Ubicación: " + archivoFinal.toAbsolutePath());

        } catch (IOException e) {
            System.err.println("❌ Error crítico al exportar a Documentos: " + e.getMessage());
        }
    }

    public MishiNode obtenerRecuerdoPorNombre(String nombreArchivo) {
        // 1. Construimos la ruta completa al archivo dentro del baúl
        Path rutaArchivo = getVaultPath().resolve(nombreArchivo);

        try {
            if (Files.exists(rutaArchivo)) {
                // 2. Jackson lee el archivo y lo transforma en MishiNode
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
        // 1. Aseguramos que la carpeta de entregables exista
        initEntregables();

        try {
            // 2. Mapeamos el MishiNode (memoria interna) al MishiReport (modelo comercial)
            MishiReport reporte = new MishiReport();
            reporte.setFileName(nodo.getFileName());
            reporte.setDate(nodo.getId()); // Usamos el ID como fecha de auditoría
            reporte.setRefactoredCode(nodo.getRefactoredCode());
            reporte.setSecurityFindings(nodo.getSecurityFindings());

            // 3. Preparamos el nombre del archivo y la ruta de salida
            String nombreLimpio = nodo.getFileName().replace(".java", "");
            String nombreArchivoPdf = "Reporte_" + nombreLimpio + "_" + nodo.getId() + ".pdf";
            Path rutaSalida = getEntregablePath().resolve(nombreArchivoPdf);

            // 4. Invocamos al servicio de PDF
            PdfService pdfService = new PdfService();
            pdfService.convertJsonToPdf(reporte, rutaSalida.toString());

            System.out.println("\n✨ [PDF DE GALA GENERADO]");
            System.out.println("📍 Ubicación: " + rutaSalida.toAbsolutePath());

        } catch (Exception e) {
            System.err.println("❌ Error crítico al generar el PDF: " + e.getMessage());
        }
    }

    public void generarReporteMarkdown(MishiNode nodo) {
        initEntregables();
        try {
            String nombreArchivo = "Reporte_" + nodo.getFileName().replace(".java", "") + "_" + nodo.getId() + ".md";
            Path archivoFinal = getEntregablePath().resolve(nombreArchivo);

            // Usamos los datos del NODO para construir el texto
            String contenido = String.format("""
            # 🛡️ AUDITORÍA: %s
            Fecha: %s
            
            ## 🔍 HALLAZGOS
            %s
            
            ## 💻 REFACTOR
            ```java
            %s
            ```
            """, nodo.getFileName(), nodo.getId(), nodo.getVerdict(), nodo.getRefactoredCode());

            Files.writeString(archivoFinal, contenido);
            System.out.println("✅ Markdown generado en Documentos.");
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}