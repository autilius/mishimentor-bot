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

import com.sun.source.tree.TryTree;
import org.example.brains.MishiBrain;
import org.example.model.MishiReport;
import org.example.search.MishiSeeker;
import org.example.service.PdfService;

import javax.sound.midi.Soundbank;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MishiOrchestrator {
    private static final String VERDE = "\u001B[32m";
    private static final String RESET = "\u001B[00m";
    private final MishiBrain brain;
    private final MishiVault vault;
    private final MishiSeeker seeker;


    public MishiOrchestrator(MishiVault vault, MishiBrain brain, MishiSeeker seeker) {
        this.vault = vault;
        this.brain = brain;
        this.seeker = seeker;
    }

    /**
     * ESTE ES EL ÚNICO MÉTODO QUE NECESITAS (El Flujo Pro)
     */
    public String realizarAuditoriaPro(String rutaArchivo) {
        try {
            // 1. Escaneamos el código local
            String codigo = MishiScanner.leerArchivo(rutaArchivo);

            // 2. Extraemos librerías para buscar vulnerabilidades reales
            String dudaTecnica = "Security vulnerabilities and best practices 2026 for: " + extraerLibrerias(codigo);

            System.out.println("🐾 Mishi: Rastreando el internet con Tavily...");
            List<String> hallazgos = seeker.buscar(dudaTecnica);

            // 3. El Cerebro procesa todo
            String promptAumentado = """
                Eres MishiMentor. Analiza el siguiente código usando este contexto actualizado de internet.
                CONTEXTO WEB: %s
                CÓDIGO: %s
                """.formatted(hallazgos, codigo);

            String veredicto = brain.pensar(promptAumentado);

            // 4. ¡AQUÍ ESTÁ EL TRUCO! Guardamos en el baúl antes de terminar
            guardarEnElBaul(rutaArchivo, veredicto);

            return veredicto;

        } catch (Exception e) {
            return "¡Miau! Error en el proceso: " + e.getMessage();
        }
    }

    private void guardarEnElBaul(String ruta, String veredicto) {
        MishiNode nodo = new MishiNode();
        // Usamos solo el nombre del archivo, no toda la ruta larga
        String nombreLimpio = new File(ruta).getName();

        nodo.setFileName(nombreLimpio);
        nodo.setVerdict(veredicto);

        vault.guardarNodo(nodo);
        System.out.println("🐾 Mishi: Reporte guardado con éxito en el Vault.");
    }

    private String extraerLibrerias(String codigo) {
        StringBuilder librerias = new StringBuilder();
        Pattern pattern = Pattern.compile("import\\s+([\\w\\.]+);");
        Matcher matcher = pattern.matcher(codigo);

        while (matcher.find()) {
            String fullImport = matcher.group(1);
            if (!fullImport.startsWith("java.")) {
                librerias.append(fullImport).append(" ");
            }
        }
        return librerias.toString().trim();
    }

    public void mostrarResumenVault() {
        // 1. Asegúrate de pasar la instancia de tu consola, no dejarla en null
        MishiConsole ui = new MishiConsole();
        System.out.println("\n📂 CONTENIDO DEL MISHI-VAULT");

        List<String> nombresReportes = vault.obtenerListaDeReportes();

        if (nombresReportes.isEmpty()) {
            System.out.println("   [ El baúl está vacío ]");
            return;
        }

        // Listar reportes
        for (int i = 0; i < nombresReportes.size(); i++) {
            System.out.printf("  %d. 📄 %s\n", i + 1, nombresReportes.get(i));
        }

        String respuesta = ui.pedirEntrada("\nEscribe el número para leer (o 0 para volver)");

        try {
            int seleccion = Integer.parseInt(respuesta);

            if (seleccion == 0) return; // Volver al menú anterior

            if (seleccion > 0 && seleccion <= nombresReportes.size()) {
                // El índice real es seleccion - 1
                String nombreArchivo = nombresReportes.get(seleccion - 1);

                // Suponiendo que vault.obtenerRecuerdo devuelve un MishiNode
                MishiNode elegido = vault.obtenerRecuerdoPorNombre(nombreArchivo);

                System.out.println("\n" + VERDE + "=== REPORTE: " + nombreArchivo + " ===" + RESET);
                System.out.println(elegido.getVerdict());
                System.out.println(VERDE + "===============================================" + RESET);

                // --- AQUÍ CONECTAMOS EL PDF ---
                String opcionPdf = ui.pedirEntrada("¿Deseas exportar este reporte? (s/n)");
                if (opcionPdf.equalsIgnoreCase("s")) {
                    String opcion = ui.pedirEntrada("¿Qué formato deseas?  \n" +
                            "  1. PDF de Gala (Para el cliente) \n " +
                            " 2. Markdown Técnico (Para GitHub/Docs) \n ");
                    if (opcion.equalsIgnoreCase("1")) vault.generarReportePdf(elegido);
                    if (opcion.equalsIgnoreCase("2")) vault.generarReporteMarkdown(elegido); // El método que ya tenías casi listo
                }

            } else {
                System.out.println("¡Miau! Ese número no está en la lista.");
            }
        } catch (NumberFormatException e) {
            System.out.println("¡Miau! Eso no es un número, humano.");
        }
    }


    public void ejecutarFlujoComercial(String rutaArchivo) {
        try {
            System.out.println("🚀 Iniciando proceso para: " + rutaArchivo);
            String codigoOriginal = MishiScanner.leerArchivo(rutaArchivo);

            // 1. Auditoría con Mishi (Para ti, con "Miaus")
            String veredictoMishi = obtenerVeredictoInterno(codigoOriginal);

            // 2. Refactorización (El código corregido para el cliente)
            String codigoRefactorizado = obtenerCodigoRefactorizado(codigoOriginal);

            // 3. Veredicto Profesional (Sin "Miaus", para el PDF del cliente)
            String veredictoPro = obtenerVeredictoProfesional(veredictoMishi);

            // 4. Guardar todo en el Baúl con ID Automático
            vault.exportarEntregableComercial(rutaArchivo, veredictoPro, codigoRefactorizado);

            System.out.println("✅ El flujo comercial ha finalizado. El despacho está listo.");
            //System.out.println("✅ ¡Misión cumplida! Todo guardado en el Vault.");

        } catch (Exception e) {
            System.err.println("❌ Fallo en el flujo: " + e.getMessage());
        }
    }

    // --- MÉTODOS DE CONEXIÓN ---

    private String obtenerVeredictoInterno(String codigo) throws IOException, InterruptedException {
        String duda = "Latest security flaws and Java best practices 2026 for this code structure";
        List<String> infoWeb = seeker.buscar(duda);

        return brain.pensar("Eres MishiMentor. Audita este código usando esta info web: " + infoWeb + "\n\nCÓDIGO:\n" + codigo);
    }

    private String obtenerCodigoRefactorizado(String codigo) throws IOException, InterruptedException {
        // Usamos el prompt detallado para que el resultado sea de alta calidad
        String prompt = """
        Como experto en Clean Code y Seguridad Java, refactoriza el siguiente código 
        para corregir vulnerabilidades y mejorar la eficiencia. 
        ENTREGA SOLO EL CÓDIGO CORREGIDO, sin explicaciones largas.
        
        CÓDIGO ORIGINAL:
        %s
        """.formatted(codigo);

        return brain.pensar(prompt);
    }

    private String obtenerVeredictoProfesional(String veredictoMishi) throws IOException, InterruptedException {
        // Este método toma el veredicto del gato y lo "limpia" para el cliente
        String prompt = "Transforma este análisis técnico en un informe profesional de consultoría. " +
                "Elimina cualquier referencia a gatos o lenguaje informal. Usa un tono serio y ejecutivo:\n" + veredictoMishi;
        return brain.pensar(prompt);
    }

    private void guardarEnElBaul(String ruta, String veredicto, String refactor) {
        MishiNode nodo = new MishiNode();
        // Llamamos a tu método de ID automático
        nodo.generarIdAutomatico();

        nodo.setFileName(new File(ruta).getName());
        // Guardamos el veredicto profesional y el código corregido juntos
        nodo.setVerdict("--- INFORME TÉCNICO ---\n" + veredicto + "\n\n--- CÓDIGO SUGERIDO ---\n" + refactor);

        vault.guardarNodo(nodo);
    }

    private void prepararGeneracionPdf(MishiNode nodo, String nombreOriginal) {
        try {
            // 1. Mapeamos el 'MishiNode' a nuestro modelo 'MishiReport'
            MishiReport reporte = new MishiReport();
            reporte.setFileName(nombreOriginal);
            reporte.setDate(nodo.getId()); // O la fecha que guardes
            reporte.setRefactoredCode(nodo.getRefactoredCode()); // Asegúrate de que el nodo tenga este método
            // reporte.setSecurityFindings(nodo.getFindings()); // Si ya los tienes mapeados

            // 2. Llamamos al servicio de PDF
            PdfService pdfService = new PdfService();
            String rutaSalida = "reportes/PDF_" + nombreOriginal.replace(".json", ".pdf");

            pdfService.convertJsonToPdf(reporte, rutaSalida);

            System.out.println("✅ ¡Miau-ravilloso! Reporte generado en: " + rutaSalida);
        } catch (Exception e) {
            System.out.println("❌ Error al generar el PDF: " + e.getMessage());
        }
    }
}